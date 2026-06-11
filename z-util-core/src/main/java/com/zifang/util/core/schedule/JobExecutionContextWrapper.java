package com.zifang.util.core.schedule;

import org.quartz.JobExecutionContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * {@link org.quartz.JobExecutionContext} 的封装类，提供更友好的 API。
 * <p>
 * 用于 {@link RunnableJob} 的 execute 方法中。
 *
 * @see RunnableJob
 * @see Job
 */
public class JobExecutionContextWrapper {

    private final JobExecutionContext delegate;

    /**
     * 从 Quartz JobExecutionContext 构造包装器。
     */
    public JobExecutionContextWrapper(JobExecutionContext delegate) {
        this.delegate = delegate;
    }

    /**
     * 获取触发时间。
     */
    public Date getFireTime() {
        return delegate.getFireTime();
    }

    /**
     * 获取触发时间（LocalDateTime 形式）。
     */
    public LocalDateTime getFireTimeAsLocalDateTime() {
        Date d = delegate.getFireTime();
        if (d == null) return null;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取任务的 Key。
     */
    public org.quartz.JobKey getJobKey() {
        return delegate.getJobDetail().getKey();
    }

    /**
     * 获取任务的名称。
     */
    public String getJobName() {
        return delegate.getJobDetail().getKey().getName();
    }

    /**
     * 获取任务的分组。
     */
    public String getJobGroup() {
        return delegate.getJobDetail().getKey().getGroup();
    }

    /**
     * 获取触发器的 Key。
     */
    public org.quartz.TriggerKey getTriggerKey() {
        return delegate.getTrigger().getKey();
    }

    /**
     * 获取触发器名称。
     */
    public String getTriggerName() {
        return delegate.getTrigger().getKey().getName();
    }

    /**
     * 获取触发器分组。
     */
    public String getTriggerGroup() {
        return delegate.getTrigger().getKey().getGroup();
    }

    /**
     * 获取合并后的 JobDataMap（包含 Trigger 和 JobDetail 中的数据，JobDetail 优先）。
     */
    public Map<String, Object> getMergedJobDataMap() {
        return delegate.getMergedJobDataMap().getWrappedMap();
    }

    /**
     * 从合并后的 JobDataMap 获取值。
     */
    @SuppressWarnings("unchecked")
    /**
     * get方法。
     *      * @param key String类型参数
     * @return <T> T类型返回值
     */
    public <T> T get(String key) {
        return (T) delegate.getMergedJobDataMap().get(key);
    }

    /**
     * 从合并后的 JobDataMap 获取值，提供默认值。
     */
    @SuppressWarnings("unchecked")
    /**
     * get方法。
     *      * @param key String类型参数
     * @param defaultValue T类型参数
     * @return <T> T类型返回值
     */
    public <T> T get(String key, T defaultValue) {
        Object val = delegate.getMergedJobDataMap().get(key);
        return val != null ? (T) val : defaultValue;
    }

    /**
     * 获取 String 类型的值。
     */
    public String getString(String key) {
        Object val = delegate.getMergedJobDataMap().get(key);
        return val != null ? String.valueOf(val) : null;
    }

    /**
     * 获取 Integer 类型的值。
     */
    public Integer getInt(String key) {
        Object val = delegate.getMergedJobDataMap().get(key);
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Number) return ((Number) val).intValue();
        if (val != null) {
            try {
                return Integer.parseInt(String.valueOf(val));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取 Long 类型的值。
     */
    public Long getLong(String key) {
        Object val = delegate.getMergedJobDataMap().get(key);
        if (val instanceof Long) return (Long) val;
        if (val instanceof Number) return ((Number) val).longValue();
        if (val != null) {
            try {
                return Long.parseLong(String.valueOf(val));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取 Boolean 类型的值。
     */
    public Boolean getBoolean(String key) {
        Object val = delegate.getMergedJobDataMap().get(key);
        if (val instanceof Boolean) return (Boolean) val;
        if (val != null) return Boolean.parseBoolean(String.valueOf(val));
        return null;
    }

    /**
     * 获取原始 Quartz JobExecutionContext。
     * <p>
     * 谨慎使用。
     */
    public JobExecutionContext getDelegate() {
        return delegate;
    }

    /**
     * 获取调度器实例。
     */
    public org.quartz.Scheduler getScheduler() {
        return delegate.getScheduler();
    }

    /**
     * 获取结果对象（由任务设置，供监听器使用）。
     */
    public Object getResult() {
        return delegate.getResult();
    }

    /**
     * 设置结果对象。
     */
    public void setResult(Object result) {
        delegate.setResult(result);
    }

    /**
     * 获取恢复触发时间（如果任务被标记为请求恢复）。
     */
    public Date getScheduledFireTime() {
        return delegate.getScheduledFireTime();
    }

    /**
     * 获取调度时间（毫秒）。
     */
    public long getScheduledFireTimeAsLong() {
        Date d = delegate.getScheduledFireTime();
        return d != null ? d.getTime() : 0;
    }

    /**
     * 获取触发计数。
     */
    public int getRefireCount() {
        return delegate.getRefireCount();
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "JobExecutionContextWrapper{" +
                "job=" + getJobName() +
                ", trigger=" + getTriggerName() +
                ", fireTime=" + getFireTime() +
                '}';
    }
}
