package com.zifang.util.core.schedule;

/**
 * 调度器操作异常。
 * <p>
 * 所有调度相关操作（如调度任务、暂停触发器等）抛出的通用异常。
 *
 * @see SchedulerManager
 */
public class SchedulerRuntimeException extends RuntimeException {

    /**
     * SchedulerRuntimeException方法。
     * * @param message String类型参数
     */
    public SchedulerRuntimeException(String message) {
        super(message);
    }

    /**
     * SchedulerRuntimeException方法。
     * * @param message String类型参数
     *
     * @param cause Throwable类型参数
     */
    public SchedulerRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * SchedulerRuntimeException方法。
     * * @param cause Throwable类型参数
     */
    public SchedulerRuntimeException(Throwable cause) {
        super(cause);
    }
}
