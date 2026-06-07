package com.zifang.util.monitor.thread.executor;

import com.zifang.util.monitor.thread.alarm.AlarmPolicy;
import com.zifang.util.monitor.thread.alarm.AlarmService;
import com.zifang.util.monitor.thread.utility.TimeUtil;

/**
 * 线程池配置。
 *
 * @author zifang
 */
/**
 * ThreadPoolConfigUnit类。
 */
/**
 * ThreadPoolConfigUnit类。
 */
public class ThreadPoolConfigUnit {

    /**
     * 线程池名称。
     */
    private String poolName;

    /**
     * 线程池大小。
     */
    private int poolSize;

    /**
     * 任务类型。
     */
    private Class taskType;

    /**
     * 计算类型，包括CPU密集型和IO密集型。
     */
    private byte computeType;

    /**
     * 线程超时阀值，单位是毫秒。当当前毫秒时间戳 - 线程启动毫秒时间戳 >= threadOvertimeThreshhold时，认为此线程已超时。
     * 监控管理器会在最多monitorInterval的时间之后，设置该工作线程的中断标志位。
     */
    private long threadOvertimeThreshhold = 3 * TimeUtil.MINUTES_SECONDS * TimeUtil.SECOND_MILLISECONDS;

    /**
     * 告警策略，决定何时将监控组件标记为告警状态。
     */
    private AlarmPolicy alarmPolicy;

    /**
     * 告警服务，对告警状态的监控组件进行告警。
     */
    private AlarmService alarmService;

    /**
     * 监控间隔，默认为10秒。
     */
    private long monitorInterval = 10 * TimeUtil.SECOND_MILLISECONDS;

    /**
     * 获取线程池名称。
     *
     * @return 线程池名称
     */
    /**
     * getPoolName方法。
     * @return String类型返回值
     */
    /**
     * getPoolName方法。
     * @return String类型返回值
     */
    public String getPoolName() {
        return poolName;
    }

    /**
     * 设置线程池名称。
     *
     * @param poolName 线程池名称
     */
    /**
     * setPoolName方法。
     *      * @param poolName String类型参数
     */
    /**
     * setPoolName方法。
     *      * @param poolName String类型参数
     */
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    /**
     * 获取线程池大小。
     *
     * @return 线程池大小
     */
    /**
     * getPoolSize方法。
     * @return int类型返回值
     */
    /**
     * getPoolSize方法。
     * @return int类型返回值
     */
    public int getPoolSize() {
        return poolSize;
    }

    /**
     * 设置线程池大小。
     *
     * @param poolSize 线程池大小
     */
    /**
     * setPoolSize方法。
     *      * @param poolSize int类型参数
     */
    /**
     * setPoolSize方法。
     *      * @param poolSize int类型参数
     */
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    /**
     * 获取计算类型。
     *
     * @return 计算类型（CPU密集型或IO密集型）
     */
    /**
     * getComputeType方法。
     * @return byte类型返回值
     */
    /**
     * getComputeType方法。
     * @return byte类型返回值
     */
    public byte getComputeType() {
        return computeType;
    }

    /**
     * 设置计算类型。
     *
     * @param computeType 计算类型（CPU密集型或IO密集型）
     */
    /**
     * setComputeType方法。
     *      * @param computeType byte类型参数
     */
    /**
     * setComputeType方法。
     *      * @param computeType byte类型参数
     */
    public void setComputeType(byte computeType) {
        this.computeType = computeType;
    }

    /**
     * 获取任务类型。
     *
     * @return 任务类型Class对象
     */
    /**
     * getTaskType方法。
     * @return Class类型返回值
     */
    /**
     * getTaskType方法。
     * @return Class类型返回值
     */
    public Class getTaskType() {
        return taskType;
    }

    /**
     * 设置任务类型。
     *
     * @param taskType 任务类型Class对象
     */
    /**
     * setTaskType方法。
     *      * @param taskType Class类型参数
     */
    /**
     * setTaskType方法。
     *      * @param taskType Class类型参数
     */
    public void setTaskType(Class taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取线程超时阈值。
     *
     * @return 线程超时阈值（毫秒）
     */
    /**
     * getThreadOvertimeThreshhold方法。
     * @return long类型返回值
     */
    /**
     * getThreadOvertimeThreshhold方法。
     * @return long类型返回值
     */
    public long getThreadOvertimeThreshhold() {
        return threadOvertimeThreshhold;
    }

    /**
     * 设置线程超时阈值。
     *
     * @param threadOvertimeThreshhold 线程超时阈值（毫秒）
     */
    /**
     * setThreadOvertimeThreshhold方法。
     *      * @param threadOvertimeThreshhold long类型参数
     */
    /**
     * setThreadOvertimeThreshhold方法。
     *      * @param threadOvertimeThreshhold long类型参数
     */
    public void setThreadOvertimeThreshhold(long threadOvertimeThreshhold) {
        this.threadOvertimeThreshhold = threadOvertimeThreshhold;
    }

    /**
     * 获取告警策略。
     *
     * @return 告警策略对象
     */
    /**
     * getAlarmPolicy方法。
     * @return AlarmPolicy类型返回值
     */
    /**
     * getAlarmPolicy方法。
     * @return AlarmPolicy类型返回值
     */
    public AlarmPolicy getAlarmPolicy() {
        return alarmPolicy;
    }

    /**
     * 获取告警服务。
     *
     * @return 告警服务对象
     */
    /**
     * getAlarmService方法。
     * @return AlarmService类型返回值
     */
    /**
     * getAlarmService方法。
     * @return AlarmService类型返回值
     */
    public AlarmService getAlarmService() {
        return alarmService;
    }

    /**
     * 设置告警策略。
     *
     * @param alarmPolicy 待设置告警策略。
     */
    /**
     * setAlarmPolicy方法。
     *      * @param alarmPolicy AlarmPolicy类型参数
     */
    /**
     * setAlarmPolicy方法。
     *      * @param alarmPolicy AlarmPolicy类型参数
     */
    public void setAlarmPolicy(AlarmPolicy alarmPolicy) {
        this.alarmPolicy = alarmPolicy;
    }

    /**
     * 设置告警服务。
     *
     * @param alarmService 待设置告警服务。
     */
    /**
     * setAlarmService方法。
     *      * @param alarmService AlarmService类型参数
     */
    /**
     * setAlarmService方法。
     *      * @param alarmService AlarmService类型参数
     */
    public void setAlarmService(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    /**
     * 获取监控间隔时间。
     *
     * @return 监控间隔时间（毫秒）
     */
    /**
     * getMonitorInterval方法。
     * @return long类型返回值
     */
    /**
     * getMonitorInterval方法。
     * @return long类型返回值
     */
    public long getMonitorInterval() {
        return monitorInterval;
    }

    /**
     * 设置监控间隔时间。
     *
     * @param monitorInterval 监控间隔时间（毫秒）
     */
    /**
     * setMonitorInterval方法。
     *      * @param monitorInterval long类型参数
     */
    /**
     * setMonitorInterval方法。
     *      * @param monitorInterval long类型参数
     */
    public void setMonitorInterval(long monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    /**
     * 返回配置单元的字符串表示。
     *
     * @return 包含所有配置字段的字符串表示
     */
    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "ThreadPoolConfigUnit{" +
                "poolName='" + poolName + '\'' +
                ", poolSize=" + poolSize +
                ", taskType=" + taskType +
                ", computeType=" + computeType +
                ", threadOvertimeThreshhold=" + threadOvertimeThreshhold +
                ", alarmPolicy=" + alarmPolicy +
                ", alarmService=" + alarmService +
                ", monitorInterval=" + monitorInterval +
                '}';
    }
}
