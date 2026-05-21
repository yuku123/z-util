package com.zifang.util.core.time.schedule;

import com.zifang.util.core.time.schedule.listener.JobListener;
import com.zifang.util.core.time.schedule.listener.SchedulerListener;
import com.zifang.util.core.time.schedule.listener.TriggerListener;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 调度框架使用示例集。
 * <p>
 * 本文件演示了 com.zifang.util.core.time.schedule 包的核心用法。
 * 包含：简单任务、Cron 任务、有状态任务、Lambda 任务、日历排除、监听器等。
 *
 * @see SchedulerBuilder
 * @see JobBuilder
 * @see TriggerBuilder
 * @see Job
 * @see StatefulJob
 * @see RunnableJob
 * @see ScheduleCalendar
 */
public class App {

    // ==================== 示例 1：最基本的定时任务 ====================

    /**
     * 最简单的使用方式：每秒打印一条消息。
     * <p>
     * 运行方式：直接运行 main 方法
     */
    public static void example1_BasicSimpleTrigger() throws Exception {
        System.out.println("=== 示例 1：基本 SimpleTrigger ===");

        // 创建调度器
        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("example-scheduler")
                .withThreadCount(5)
                .build();

        // 定义任务
        JobDetail job = JobBuilder.newJob(BasicJob.class)
                .withName("basic-job")
                .withDescription("每 5 秒执行一次的任务")
                .usingJobData("message", "Hello from basic job!")
                .build();

        // 定义触发器：每 5 秒执行一次
        Trigger trigger = TriggerBuilder.newSimpleTrigger()
                .withName("basic-trigger")
                .forJob(job)
                .withIntervalInSeconds(5)
                .repeatForever()
                .build();

        // 调度
        scheduler.scheduleJob(job, trigger);

        // 添加监听器
        scheduler.getListenerManager().addJobListener(new JobListener() {
            public String getName() { return "my-job-listener"; }

            public void jobToBeExecuted(JobExecutionContextWrapper ctx) {
                System.out.println("  [JobListener] 任务即将执行: " + ctx.getJobName());
            }

            public void jobWasExecuted(JobExecutionContextWrapper ctx,
                                      JobExecutionException exception) {
                if (exception != null) {
                    System.err.println("  [JobListener] 任务执行失败: " + exception.getMessage());
                } else {
                    System.out.println("  [JobListener] 任务执行完成: " + ctx.getJobName());
                }
            }
        });

        // 启动
        scheduler.start();

        // 运行 30 秒后关闭
        Thread.sleep(30_000);
        scheduler.shutdown(true);
        System.out.println("示例 1 完成\n");
    }

    // ==================== 示例 2：Cron 表达式任务 ====================

    /**
     * 使用 cron 表达式定义复杂的调度规则。
     * <p>
     * 示例：工作日上午 9:00、12:00、18:00 各执行一次
     */
    public static void example2_CronTrigger() throws Exception {
        System.out.println("=== 示例 2：CronTrigger ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("cron-scheduler")
                .build();

        JobDetail job = JobBuilder.newJob(CronJob.class)
                .withName("cron-job")
                .usingJobData("task", "cron-demo")
                .build();

        // Cron 表达式：每天上午 9:00 执行
        Trigger trigger = TriggerBuilder.newCronTrigger()
                .withName("cron-trigger")
                .forJob(job)
                .withCronExpression("0 0 9 * * ?")
                .withMisfirePolicy(MisfirePolicy.DO_NOTHING)
                .inTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        System.out.println("  CronTrigger 已调度，将在每天 09:00 执行");
        System.out.println("  下次触发时间: " + trigger.getNextFireTime());

        Thread.sleep(5000);
        scheduler.shutdown(false);
        System.out.println("示例 2 完成\n");
    }

    // ==================== 示例 3：Lambda 风格任务 ====================

    /**
     * 使用 {@link RunnableJob} 接口，配合 Lambda 表达式或 Method Reference，
     * 无需创建独立的 Job 类。
     */
    public static void example3_LambdaJob() throws Exception {
        System.out.println("=== 示例 3：Lambda 风格任务 ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("lambda-scheduler")
                .build();

        // 使用 lambda 定义任务
        JobDetail job = JobBuilder.newJob((RunnableJob) ctx -> {
            String msg = ctx.get("msg", "default");
            LocalDateTime now = ctx.getFireTimeAsLocalDateTime();
            System.out.println("  [Lambda] 执行时间: " + now + ", 消息: " + msg);
        })
                .withName("lambda-job")
                .usingJobData("msg", "Lambda 任务测试消息")
                .build();

        // 每 3 秒执行一次
        Trigger trigger = TriggerBuilder.newSimpleTrigger()
                .withName("lambda-trigger")
                .forJob(job)
                .withIntervalInSeconds(3)
                .repeatForever()
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        Thread.sleep(15_000);
        scheduler.shutdown(false);
        System.out.println("示例 3 完成\n");
    }

    // ==================== 示例 4：有状态任务（不允许并发） ====================

    /**
     * 有状态任务 {@link StatefulJob} 保证同一任务实例不会并发执行。
     * 适合处理需要独占资源的任务。
     */
    public static void example4_StatefulJob() throws Exception {
        System.out.println("=== 示例 4：有状态任务 ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("stateful-scheduler")
                .build();

        JobDetail job = JobBuilder.newJob(StatefulDemoJob.class)
                .withName("stateful-job")
                .disallowConcurrentExecution()
                .persistJobDataAfterExecution()
                .build();

        // 高频触发，测试并发控制
        Trigger trigger = TriggerBuilder.newSimpleTrigger()
                .withName("stateful-trigger")
                .forJob(job)
                .withIntervalInSeconds(1)
                .repeatForever()
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        Thread.sleep(10_000);
        scheduler.shutdown(false);
        System.out.println("示例 4 完成\n");
    }

    // ==================== 示例 5：日历排除（节假日不执行） ====================

    /**
     * 使用 {@link ScheduleCalendar} 排除特定日期。
     * 适合实现节假日不执行任务的功能。
     */
    public static void example5_CalendarExclusion() throws Exception {
        System.out.println("=== 示例 5：日历排除 ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("calendar-scheduler")
                .build();

        // 创建节假日日历
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(LocalDate.of(2026, 1, 1));   // 元旦
        holidays.add(LocalDate.of(2026, 5, 1));   // 劳动节
        holidays.add(LocalDate.of(2026, 10, 1));  // 国庆节

        ScheduleCalendar holidayCalendar = ScheduleCalendar.excludeDates(holidays);

        // 添加到调度器
        scheduler.addCalendar("holidays", holidayCalendar);

        JobDetail job = JobBuilder.newJob(BasicJob.class)
                .withName("calendar-job")
                .usingJobData("message", "排除节假日")
                .build();

        // 每天 9:00 执行，但排除节假日
        Trigger trigger = TriggerBuilder.newCronTrigger()
                .withName("calendar-trigger")
                .forJob(job)
                .withCronExpression("0 0 9 * * ?")
                .modifiedByCalendar("holidays")  // 关联节假日日历
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        System.out.println("  日历已添加，排除日期: " + holidays);
        System.out.println("  下次触发时间: " + trigger.getNextFireTime());

        Thread.sleep(5000);
        scheduler.shutdown(false);
        System.out.println("示例 5 完成\n");
    }

    // ==================== 示例 6：DailyTimeIntervalTrigger（每日时间段） ====================

    /**
     * 在每天的指定时间段内，按固定间隔执行任务。
     * 适合实现"工作时间每 N 分钟执行一次"的场景。
     */
    public static void example6_DailyTimeIntervalTrigger() throws Exception {
        System.out.println("=== 示例 6：DailyTimeIntervalTrigger ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("daily-scheduler")
                .build();

        JobDetail job = JobBuilder.newJob(BasicJob.class)
                .withName("daily-job")
                .usingJobData("message", "工作时间内每 30 秒执行")
                .build();

        // 工作日（周一到周五）上午 9:00 - 18:00，每 30 秒执行一次
        Trigger trigger = TriggerBuilder.newDailyTimeIntervalTrigger()
                .withName("daily-trigger")
                .forJob(job)
                .onWeekdays()
                .startingDailyAt(LocalTime.of(9, 0))
                .endingDailyAt(LocalTime.of(18, 0))
                .withIntervalInSeconds(30)
                .withMisfirePolicy(MisfirePolicy.IGNORE_MISFIRE_FIRES_NOW)
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        System.out.println("  DailyTimeIntervalTrigger 已调度");
        System.out.println("  下次触发时间: " + trigger.getNextFireTime());

        Thread.sleep(5000);
        scheduler.shutdown(false);
        System.out.println("示例 6 完成\n");
    }

    // ==================== 示例 7：监听器完整示例 ====================

    /**
     * 演示三种监听器的完整使用：
     * {@link JobListener}、{@link TriggerListener}、{@link SchedulerListener}
     */
    public static void example7_AllListeners() throws Exception {
        System.out.println("=== 示例 7：三种监听器 ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("listener-scheduler")
                .build();

        // 添加 Job 监听器
        scheduler.getListenerManager().addJobListener(new JobListener() {
            public String getName() { return "logging-job-listener"; }

            public void jobToBeExecuted(JobExecutionContextWrapper ctx) {
                System.out.println("  [JobListener] >>> jobToBeExecuted: " + ctx.getJobName());
            }

            public void jobWasExecuted(JobExecutionContextWrapper ctx,
                                      JobExecutionException exception) {
                System.out.println("  [JobListener] >>> jobWasExecuted: " + ctx.getJobName()
                        + ", result=" + ctx.getResult()
                        + ", error=" + (exception != null ? exception.getMessage() : "none"));
            }

            public void jobExecutionVetoed(JobExecutionContextWrapper ctx) {
                System.out.println("  [JobListener] >>> jobExecutionVetoed: " + ctx.getJobName());
            }
        });

        // 添加 Trigger 监听器
        scheduler.getListenerManager().addTriggerListener(new TriggerListener() {
            public String getName() { return "logging-trigger-listener"; }

            public void triggerFired(Trigger trigger,
                                   JobExecutionContextWrapper ctx) {
                System.out.println("  [TriggerListener] >>> triggerFired: " + trigger.getKey().getName());
            }

            public void triggerComplete(Trigger trigger,
                                      JobExecutionContextWrapper ctx,
                                      int instructionCode) {
                System.out.println("  [TriggerListener] >>> triggerComplete: " + trigger.getKey().getName());
            }

            public void triggerMisfired(Trigger trigger) {
                System.err.println("  [TriggerListener] >>> triggerMisfired: " + trigger.getKey().getName());
            }
        });

        // 添加 Scheduler 监听器
        scheduler.getListenerManager().addSchedulerListener(new SchedulerListener() {
            public void jobScheduled(Trigger trigger) {
                System.out.println("  [SchedulerListener] >>> jobScheduled: " + trigger.getKey());
            }

            public void jobDeleted(JobKey jobKey) {
                System.out.println("  [SchedulerListener] >>> jobDeleted: " + jobKey);
            }

            public void schedulerError(String msg, org.quartz.SchedulerException cause) {
                System.err.println("  [SchedulerListener] >>> schedulerError: " + msg);
            }

            public void schedulerShutdown() {
                System.out.println("  [SchedulerListener] >>> schedulerShutdown");
            }
        });

        JobDetail job = JobBuilder.newJob(BasicJob.class)
                .withName("listener-job")
                .usingJobData("message", "listener demo")
                .build();

        Trigger trigger = TriggerBuilder.newSimpleTrigger()
                .withName("listener-trigger")
                .forJob(job)
                .withIntervalInSeconds(2)
                .withRepeatCount(3)
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        Thread.sleep(12_000);
        scheduler.shutdown(false);
        System.out.println("示例 7 完成\n");
    }

    // ==================== 示例 8：暂停/恢复/立即触发 ====================

    /**
     * 演示动态控制任务：暂停、恢复、立即触发一次。
     */
    public static void example8_PauseResumeTrigger() throws Exception {
        System.out.println("=== 示例 8：暂停/恢复/立即触发 ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("control-scheduler")
                .build();

        JobDetail job = JobBuilder.newJob(BasicJob.class)
                .withName("control-job")
                .usingJobData("message", "controlled job")
                .build();

        Trigger trigger = TriggerBuilder.newSimpleTrigger()
                .withName("control-trigger")
                .forJob(job)
                .withIntervalInSeconds(1)
                .repeatForever()
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        System.out.println("  任务已调度，触发器: " + trigger.getName());
        System.out.println("  下次触发: " + trigger.getNextFireTime());

        Thread.sleep(5000);

        // 暂停触发器
        System.out.println("  >> 暂停触发器...");
        scheduler.pauseTrigger(trigger.getName());
        Thread.sleep(3000);

        // 恢复触发器
        System.out.println("  >> 恢复触发器...");
        scheduler.resumeTrigger(trigger.getName());
        Thread.sleep(3000);

        // 立即触发一次
        System.out.println("  >> 立即触发一次...");
        scheduler.triggerJob(job.getKey());

        Thread.sleep(3000);
        scheduler.shutdown(false);
        System.out.println("示例 8 完成\n");
    }

    // ==================== 示例 9：CalendarIntervalTrigger（每月/每年） ====================

    /**
     * 使用 CalendarIntervalTrigger 按月或按年间隔执行。
     * 自动处理月份天数变化和夏令时。
     */
    public static void example9_CalendarIntervalTrigger() throws Exception {
        System.out.println("=== 示例 9：CalendarIntervalTrigger ===");

        SchedulerManager scheduler = SchedulerBuilder.newScheduler()
                .withName("interval-scheduler")
                .build();

        JobDetail job = JobBuilder.newJob(BasicJob.class)
                .withName("interval-job")
                .usingJobData("message", "monthly job")
                .build();

        // 每月 1 日凌晨 0:00 执行
        Trigger trigger = TriggerBuilder.newCalendarIntervalTrigger()
                .withName("interval-trigger")
                .forJob(job)
                .withIntervalInMonths(1)
                .withMisfirePolicy(MisfirePolicy.FIRE_NOW)
                .build();

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        System.out.println("  CalendarIntervalTrigger 已调度（每月执行）");
        System.out.println("  下次触发时间: " + trigger.getNextFireTime());

        Thread.sleep(3000);
        scheduler.shutdown(false);
        System.out.println("示例 9 完成\n");
    }

    // ==================== main ====================

    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  Schedule Framework Demo                ║");
        System.out.println("║  com.zifang.util.core.time.schedule     ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // 运行所有示例
        example1_BasicSimpleTrigger();

        example2_CronTrigger();

        example3_LambdaJob();

        example4_StatefulJob();

        example5_CalendarExclusion();

        example6_DailyTimeIntervalTrigger();

        example7_AllListeners();

        example8_PauseResumeTrigger();

        example9_CalendarIntervalTrigger();

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  All examples completed!                ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ==================== 示例使用的 Job 实现 ====================

    /**
     * 最基本的 Job 实现。
     */
    public static class BasicJob implements Job {
        @Override
        public void execute(JobExecutionContext ctx) throws JobExecutionException {
            String message = ctx.getMergedJobDataMap().getString("message");
            Date fireTime = ctx.getFireTime();
            System.out.println("  [BasicJob] fireTime=" + fireTime + ", msg=" + message);
        }
    }

    /**
     * Cron 任务示例。
     */
    public static class CronJob implements Job {
        @Override
        public void execute(JobExecutionContext ctx) throws JobExecutionException {
            String task = ctx.getMergedJobDataMap().getString("task");
            System.out.println("  [CronJob] 执行任务: " + task + ", 时间: " + ctx.getFireTime());
        }
    }

    /**
     * 有状态任务示例。
     * 每次执行会将计数器加 1 并保存。
     */
    public static class StatefulDemoJob implements Job, StatefulJob {
        private int counter = 0;

        @Override
        public void execute(JobExecutionContext ctx) throws JobExecutionException {
            counter = ctx.getInt("counter");

            System.out.println("  [StatefulJob] 第 " + counter + " 次执行，线程: " + Thread.currentThread().getName());
            ctx.getJobDetail().getJobDataMap().put("counter", ++counter);

            // 模拟耗时操作
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
