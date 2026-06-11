package com.zifang.util.core.schedule;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;

import java.util.*;

/**
 * 任务描述（JobDetail）的封装类。
 * <p>
 * 持有任务的完整描述信息：任务标识、任务类、JobDataMap、并发控制等。
 * 通过 {@link JobBuilder} 构建。
 *
 * @see JobBuilder
 * @see SchedulerManager#scheduleJob(JobDetail, Trigger)
 */
public class JobDetail {

    private final org.quartz.JobDetail delegate;

    /**
     * 内部构造器，从 Quartz JobDetail 构建。
     */
    public JobDetail(org.quartz.JobDetail delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
    }

    /**
     * 创建 JobDetail 实例。
     *
     * @return 新的 JobDetail 实例
     * @deprecated use {@link JobBuilder#newJob(Class)}
     */
    @Deprecated
    /**
     * create方法。
     * @return static JobDetail类型返回值
     */
    public static JobDetail create() {
        return new JobDetail(new JobDetailImpl());
    }

    // ==================== 基本属性 ====================

    /**
     * 获取任务名称。
     */
    public String getName() {
        return delegate.getKey().getName();
    }

    /**
     * 获取任务所属分组。
     */
    public String getGroup() {
        return delegate.getKey().getGroup();
    }

    /**
     * 获取任务键。
     */
    public JobKey getKey() {
        return delegate.getKey();
    }

    /**
     * 获取任务类。
     */
    @SuppressWarnings("unchecked")
    /**
     * getJobClass方法。
     * @return Class<? extends org.quartz.Job>类型返回值
     */
    public Class<? extends org.quartz.Job> getJobClass() {
        return delegate.getJobClass();
    }

    /**
     * 获取任务描述。
     */
    public String getDescription() {
        return delegate.getDescription();
    }

    /**
     * 获取 JobDataMap（任务级别数据）。
     * <p>
     * 可以用来在任务执行时传递参数。
     *
     * @return 可修改的 JobDataMap
     */
    public Map<String, Object> getJobDataMap() {
        Map<String, Object> result = new HashMap<>();
        delegate.getJobDataMap().forEach(result::put);
        return Collections.unmodifiableMap(result);
    }

    /**
     * 获取可写的 JobDataMap 视图。
     */
    public org.quartz.JobDataMap getRawJobDataMap() {
        return delegate.getJobDataMap();
    }

    // ==================== 状态属性 ====================

    /**
     * 是否持久化。
     * 默认 true。
     */
    public boolean isDurable() {
        return delegate.isDurable();
    }

    /**
     * 是否不允许并发执行。
     *
     * @see StatefulJob
     */
    public boolean isConcurrentExectionDisallowed() {
        return delegate.isConcurrentExectionDisallowed();
    }

    /**
     * 是否在完成执行后持久化 JobDataMap。
     *
     * @see StatefulJob
     */
    public boolean isPersistJobDataAfterExecution() {
        return delegate.isPersistJobDataAfterExecution();
    }

    /**
     * 是否请求恢复。
     * 在 Scheduler 重新启动后，标记为请求恢复的任务会被重新执行。
     */
    public boolean requestsRecovery() {
        return delegate.requestsRecovery();
    }

    // ==================== 操作 ====================

    /**
     * 获取底层 Quartz {@link JobDetail} 对象。
     * <p>
     * 谨慎使用，仅在需要直接操作 Quartz API 时调用。
     */
    public org.quartz.JobDetail getDelegate() {
        return delegate;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "JobDetail{" +
                "key=" + getKey() +
                ", description=" + getDescription() +
                ", durable=" + isDurable() +
                '}';
    }

    // ==================== 内部 Builder 支持 ====================

    /**
     * 内部 Builder，委托给 quartz.JobDetailImpl。
     */
    static class Builder {
        private final JobDetailImpl delegate = new JobDetailImpl();

        Builder withIdentity(String name, String group) {
            delegate.setName(name);
            delegate.setGroup(group);
            return this;
        }

        Builder withIdentity(String name) {
            delegate.setName(name);
            return this;
        }

        Builder ofClass(Class<? extends org.quartz.Job> jobClass) {
            delegate.setJobClass(jobClass);
            return this;
        }

        Builder withDescription(String description) {
            delegate.setDescription(description);
            return this;
        }

        Builder usingJobData(String key, Object value) {
            delegate.getJobDataMap().put(key, value);
            return this;
        }

        Builder usingJobData(Map<String, ?> data) {
            data.forEach(delegate.getJobDataMap()::put);
            return this;
        }

        Builder durable(boolean durable) {
            delegate.setDurability(durable);
            return this;
        }

        Builder requestRecovery(boolean requestRecovery) {
            delegate.setRequestsRecovery(requestRecovery);
            return this;
        }

        org.quartz.JobDetail build() {
            return delegate;
        }
    }
}
