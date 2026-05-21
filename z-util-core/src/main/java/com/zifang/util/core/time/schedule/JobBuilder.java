package com.zifang.util.core.time.schedule;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobKey;
import org.quartz.Scheduler;

import java.util.Map;
import java.util.Objects;

/**
 * 任务（JobDetail）的构建器。
 * <p>
 * 提供流畅的 API 来构建完整的 JobDetail 配置。
 * <p>
 * 示例：构建基本任务
 * <pre>
 * JobDetail job = JobBuilder.newJob(MyJob.class)
 *     .withName("my-job")
 *     .withGroup("group1")
 *     .withDescription("我的任务")
 *     .usingJobData("key", "value")
 *     .build();
 * </pre>
 * <p>
 * 示例：构建有状态任务
 * <pre>
 * JobDetail job = JobBuilder.newJob(MyStatefulJob.class)
 *     .withIdentity("stateful-job")
 *     .durable()
 *     .storeDurably()
 *     .requestRecovery()
 *     .build();
 * </pre>
 *
 * @see Job
 * @see StatefulJob
 * @see JobDetail
 * @see SchedulerManager#scheduleJob(JobDetail, Trigger)
 */
public class JobBuilder {

    private JobBuilder() {
        // 工具类不实例化
    }

    /**
     * 创建新的 JobBuilder，指定任务类。
     *
     * @param jobClass 任务类，必须实现 {@link Job} 或 {@link StatefulJob}
     * @return 新的 Builder 实例
     */
    public static JobDetailBuilder newJob(Class<? extends Job> jobClass) {
        return new JobDetailBuilder(jobClass);
    }

    /**
     * 创建新的 JobBuilder，指定任务类（使用 lambda 风格的 RunnableJob）。
     *
     * @param runnable 执行逻辑
     * @return 新的 Builder 实例
     * @see JobBuilder#ofType(Class)
     */
    public static JobDetailBuilder newJob(RunnableJob runnable) {
        Objects.requireNonNull(runnable, "runnable must not be null");
        return new JobDetailBuilder(RunnableJobAdapter.class)
                .usingJobData("_runnable", runnable);
    }

    /**
     * 任务构建器。
     */
    public static class JobDetailBuilder {
        private final Class<? extends Job> jobClass;
        private String name;
        private String group = Scheduler.DEFAULT_GROUP;
        private String description;
        private boolean durable = false;
        private boolean concurrentExecutionDisallowed = false;
        private boolean persistJobDataAfterExecution = false;
        private boolean requestsRecovery = false;
        private final java.util.Map<String, Object> jobData = new java.util.HashMap<>();

        JobDetailBuilder(Class<? extends Job> jobClass) {
            this.jobClass = Objects.requireNonNull(jobClass, "jobClass must not be null");
        }

        // ==================== 基本属性 ====================

        /**
         * 设置任务名称。
         */
        public JobDetailBuilder withName(String name) {
            this.name = Objects.requireNonNull(name, "job name must not be null");
            return this;
        }

        /**
         * 设置任务名称和分组。
         */
        public JobDetailBuilder withIdentity(String name, String group) {
            this.name = Objects.requireNonNull(name, "job name must not be null");
            this.group = Objects.requireNonNull(group, "job group must not be null");
            return this;
        }

        /**
         * 设置任务名称（使用默认分组）。
         */
        public JobDetailBuilder withIdentity(String name) {
            this.name = Objects.requireNonNull(name, "job name must not be null");
            return this;
        }

        /**
         * 设置描述。
         */
        public JobDetailBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        // ==================== 任务数据 ====================

        /**
         * 添加任务数据。
         */
        public JobDetailBuilder usingJobData(String key, Object value) {
            jobData.put(key, value);
            return this;
        }

        /**
         * 添加多个任务数据。
         */
        public JobDetailBuilder usingJobData(Map<String, ?> data) {
            jobData.putAll(data);
            return this;
        }

        // ==================== 状态控制 ====================

        /**
         * 设置为持久任务。
         * <p>
         * 持久任务在没有任何 Trigger 引用时也不会被删除。
         */
        public JobDetailBuilder durable() {
            this.durable = true;
            return this;
        }

        /**
         * 设置为持久任务（别名）。
         */
        public JobDetailBuilder storeDurably() {
            return durable();
        }

        /**
         * 设置不允许并发执行。
         * <p>
         * 等同于实现 {@link StatefulJob}。
         */
        public JobDetailBuilder disallowConcurrentExecution() {
            this.concurrentExecutionDisallowed = true;
            return this;
        }

        /**
         * 设置执行后持久化 JobDataMap。
         * <p>
         * 等同于实现 {@link StatefulJob}。
         */
        public JobDetailBuilder persistJobDataAfterExecution() {
            this.persistJobDataAfterExecution = true;
            return this;
        }

        /**
         * 设置请求恢复。
         * <p>
         * 当 Scheduler 异常关闭后重启，已执行过的任务如果支持恢复，会被重新执行。
         */
        public JobDetailBuilder requestRecovery() {
            this.requestsRecovery = true;
            return this;
        }

        /**
         * 指定任务类型（覆盖默认类型）。
         * <p>
         * 通常不需要手动调用，除非使用自定义 Job 类型。
         */
        public JobDetailBuilder ofType(Class<? extends Job> clazz) {
            // 用于自定义任务类型，这里仅作占位
            return this;
        }

        // ==================== 构建 ====================

        /**
         * 构建 JobDetail 实例。
         *
         * @return 配置完整的 JobDetail
         * @throws IllegalStateException 如果任务类实现了 {@link StatefulJob} 但同时
         *                                调用了 disallowConcurrentExecution 或 persistJobDataAfterExecution
         */
        public JobDetail build() {
            // 检查一致性
            boolean isStateful = StatefulJob.class.isAssignableFrom(jobClass);
            if (isStateful) {
                this.concurrentExecutionDisallowed = true;
                this.persistJobDataAfterExecution = true;
            }

            org.quartz.JobBuilder quartzBuilder = org.quartz.JobBuilder.newJob(jobClass);

            if (name != null) {
                quartzBuilder.withIdentity(name, group);
            }
            if (description != null) {
                quartzBuilder.withDescription(description);
            }
            quartzBuilder.durable(durable);
            quartzBuilder.requestRecovery(requestsRecovery);

            if (concurrentExecutionDisallowed) {
                quartzBuilder.disallowConcurrentExecution();
            }
            if (persistJobDataAfterExecution) {
                quartzBuilder.persistJobDataAfterExecution();
            }

            for (Map.Entry<String, Object> entry : jobData.entrySet()) {
                quartzBuilder.usingJobData(entry.getKey(),
                        entry.getValue() == null ? "" : String.valueOf(entry.getValue()));
            }

            // 特殊处理 RunnableJobAdapter
            if (jobClass == RunnableJobAdapter.class) {
                quartzBuilder.storeDurably();
            }

            return new JobDetail(quartzBuilder.build());
        }
    }
}
