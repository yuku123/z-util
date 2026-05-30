package com.zifang.util.core.schedule;

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
/**
 * JobBuilder类。
 */
public class JobBuilder {

    private JobBuilder() {
        // 工具类不实例化
    }

    /**
     * 创建新的 JobBuilder，指定任务类（实现 {@link Job} 接口）。
     *
     * @param jobClass 任务类，必须实现本框架的 {@link Job} 接口
     * @return 新的 Builder 实例
     */
    /**
     * newJob方法。
     *      * @param jobClass Class?类型参数
     * @return static JobDetailBuilder类型返回值
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
    /**
     * newJob方法。
     *      * @param runnable RunnableJob类型参数
     * @return static JobDetailBuilder类型返回值
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
        private boolean requestsRecovery = false;
        private final java.util.Map<String, Object> jobData = new java.util.HashMap<>();

        JobDetailBuilder(Class<? extends Job> jobClass) {
            this.jobClass = Objects.requireNonNull(jobClass, "jobClass must not be null");
        }

        // ==================== 基本属性 ====================

        /**
         * 设置任务名称。
         */
    /**
     * withName方法。
     *      * @param name String类型参数
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder withName(String name) {
            this.name = Objects.requireNonNull(name, "job name must not be null");
            return this;
        }

        /**
         * 设置任务名称和分组。
         */
    /**
     * withIdentity方法。
     *      * @param name String类型参数
     * @param group String类型参数
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder withIdentity(String name, String group) {
            this.name = Objects.requireNonNull(name, "job name must not be null");
            this.group = Objects.requireNonNull(group, "job group must not be null");
            return this;
        }

        /**
         * 设置任务名称（使用默认分组）。
         */
    /**
     * withIdentity方法。
     *      * @param name String类型参数
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder withIdentity(String name) {
            this.name = Objects.requireNonNull(name, "job name must not be null");
            return this;
        }

        /**
         * 设置描述。
         */
    /**
     * withDescription方法。
     *      * @param description String类型参数
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        // ==================== 任务数据 ====================

        /**
         * 添加任务数据。
         */
    /**
     * usingJobData方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder usingJobData(String key, Object value) {
            jobData.put(key, value);
            return this;
        }

        /**
         * 添加多个任务数据。
         */
    /**
     * usingJobData方法。
     *      * @param data MapString,类型参数
     * @return JobDetailBuilder类型返回值
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
    /**
     * durable方法。
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder durable() {
            this.durable = true;
            return this;
        }

        /**
         * 设置为持久任务（别名）。
         */
    /**
     * storeDurably方法。
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder storeDurably() {
            return durable();
        }

        /**
         * 设置请求恢复。
         * <p>
         * 当 Scheduler 异常关闭后重启，已执行过的任务如果支持恢复，会被重新执行。
         */
    /**
     * requestRecovery方法。
     * @return JobDetailBuilder类型返回值
     */
        public JobDetailBuilder requestRecovery() {
            this.requestsRecovery = true;
            return this;
        }

        // ==================== 构建 ====================

        /**
         * 构建 JobDetail 实例。
         *
         * @return 配置完整的 JobDetail
         */
    /**
     * build方法。
     * @return JobDetail类型返回值
     */
        public JobDetail build() {
            // 始终使用 RunnableJobAdapter 作为实际 Quartz Job，
            // 用户的 Job 实现类通过 JobDataMap 传递给适配器。
            org.quartz.JobBuilder quartzBuilder = org.quartz.JobBuilder.newJob(RunnableJobAdapter.class);

            if (name != null) {
                quartzBuilder.withIdentity(name, group);
            }
            if (description != null) {
                quartzBuilder.withDescription(description);
            }
            quartzBuilder.storeDurably(durable);
            quartzBuilder.requestRecovery(requestsRecovery);

            // 将用户 Job 类存入 JobDataMap，供 RunnableJobAdapter 在执行时使用
            if (!jobClass.equals(RunnableJobAdapter.class)) {
                jobData.put("_jobClass", jobClass);
            }

            for (Map.Entry<String, Object> entry : jobData.entrySet()) {
                Object value = entry.getValue();
                if (value == null) {
                    continue;
                }
                if (value instanceof String) {
                    quartzBuilder.usingJobData(entry.getKey(), (String) value);
                } else if (value instanceof Integer) {
                    quartzBuilder.usingJobData(entry.getKey(), (Integer) value);
                } else if (value instanceof Long) {
                    quartzBuilder.usingJobData(entry.getKey(), (Long) value);
                } else if (value instanceof Boolean) {
                    quartzBuilder.usingJobData(entry.getKey(), (Boolean) value);
                } else if (value instanceof Double) {
                    quartzBuilder.usingJobData(entry.getKey(), (Double) value);
                } else if (value instanceof Float) {
                    quartzBuilder.usingJobData(entry.getKey(), (Float) value);
                } else if (value instanceof Class) {
                    // Class 类型序列化后再存储
                    quartzBuilder.usingJobData(entry.getKey(), ((Class<?>) value).getName());
                } else {
                    quartzBuilder.usingJobData(entry.getKey(), String.valueOf(value));
                }
            }

            // RunnableJobAdapter 需要持久化，以便重启后仍能通过 JobDataMap 恢复
            quartzBuilder.storeDurably(true);

            return new JobDetail(quartzBuilder.build());
        }
    }
}
