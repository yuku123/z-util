package com.zifang.util.crawler.cua;

/**
 * 单个步骤执行结果对象。
 * <p>
 * 记录步骤执行的名称、成败状态、输出结果、错误信息和执行耗时，
 * 支持 Builder 模式构建，用于 CUA 执行过程中的步骤结果传递。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * StepResult类。
 */
/**
 * StepResult类。
 */
public class StepResult {

    private String stepName;
    private boolean success;
    private Object output;
    private String errorMessage;
    private long durationMs;

    /**
     * 构造 StepResult。
     */
    /**
     * StepResult方法。
     */
    /**
     * StepResult方法。
     */
    public StepResult() {
    }

    /**
     * 使用步骤名称和成功状态构造 StepResult。
     * @param stepName 步骤名称
     * @param success 是否成功
     */
    /**
     * StepResult方法。
     *      * @param stepName String类型参数
     * @param success boolean类型参数
     */
    /**
     * StepResult方法。
     *      * @param stepName String类型参数
     * @param success boolean类型参数
     */
    public StepResult(String stepName, boolean success) {
        this.stepName = stepName;
        this.success = success;
    }

    /**
     * 获取步骤名称。
     * @return 步骤名称
     */
    /**
     * getStepName方法。
     * @return String类型返回值
     */
    /**
     * getStepName方法。
     * @return String类型返回值
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * 设置步骤名称。
     * @param stepName 步骤名称
     */
    /**
     * setStepName方法。
     *      * @param stepName String类型参数
     */
    /**
     * setStepName方法。
     *      * @param stepName String类型参数
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    /**
     * 获取是否成功。
     * @return 是否成功
     */
    /**
     * isSuccess方法。
     * @return boolean类型返回值
     */
    /**
     * isSuccess方法。
     * @return boolean类型返回值
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置是否成功。
     * @param success 是否成功
     */
    /**
     * setSuccess方法。
     *      * @param success boolean类型参数
     */
    /**
     * setSuccess方法。
     *      * @param success boolean类型参数
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取输出结果。
     * @return 输出结果
     */
    /**
     * getOutput方法。
     * @return Object类型返回值
     */
    /**
     * getOutput方法。
     * @return Object类型返回值
     */
    public Object getOutput() {
        return output;
    }

    /**
     * 设置输出结果。
     * @param output 输出结果
     */
    /**
     * setOutput方法。
     *      * @param output Object类型参数
     */
    /**
     * setOutput方法。
     *      * @param output Object类型参数
     */
    public void setOutput(Object output) {
        this.output = output;
    }

    /**
     * 获取错误信息。
     * @return 错误信息
     */
    /**
     * getErrorMessage方法。
     * @return String类型返回值
     */
    /**
     * getErrorMessage方法。
     * @return String类型返回值
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误信息。
     * @param errorMessage 错误信息
     */
    /**
     * setErrorMessage方法。
     *      * @param errorMessage String类型参数
     */
    /**
     * setErrorMessage方法。
     *      * @param errorMessage String类型参数
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 获取执行耗时（毫秒）。
     * @return 执行耗时
     */
    /**
     * getDurationMs方法。
     * @return long类型返回值
     */
    /**
     * getDurationMs方法。
     * @return long类型返回值
     */
    public long getDurationMs() {
        return durationMs;
    }

    /**
     * 设置执行耗时（毫秒）。
     * @param durationMs 执行耗时
     */
    /**
     * setDurationMs方法。
     *      * @param durationMs long类型参数
     */
    /**
     * setDurationMs方法。
     *      * @param durationMs long类型参数
     */
    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    /**
     * 获取 Builder 实例。
     * @return Builder 对象
     */
    /**
     * builder方法。
     * @return static Builder类型返回值
     */
    /**
     * builder方法。
     * @return static Builder类型返回值
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String stepName;
        private boolean success;
        private Object output;
        private String errorMessage;
        private long durationMs;

    /**
     * stepName方法。
     *      * @param stepName String类型参数
     * @return Builder类型返回值
     */
    /**
     * stepName方法。
     *      * @param stepName String类型参数
     * @return Builder类型返回值
     */
        public Builder stepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

    /**
     * success方法。
     *      * @param success boolean类型参数
     * @return Builder类型返回值
     */
    /**
     * success方法。
     *      * @param success boolean类型参数
     * @return Builder类型返回值
     */
        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

    /**
     * output方法。
     *      * @param output Object类型参数
     * @return Builder类型返回值
     */
    /**
     * output方法。
     *      * @param output Object类型参数
     * @return Builder类型返回值
     */
        public Builder output(Object output) {
            this.output = output;
            return this;
        }

    /**
     * errorMessage方法。
     *      * @param errorMessage String类型参数
     * @return Builder类型返回值
     */
    /**
     * errorMessage方法。
     *      * @param errorMessage String类型参数
     * @return Builder类型返回值
     */
        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

    /**
     * durationMs方法。
     *      * @param durationMs long类型参数
     * @return Builder类型返回值
     */
    /**
     * durationMs方法。
     *      * @param durationMs long类型参数
     * @return Builder类型返回值
     */
        public Builder durationMs(long durationMs) {
            this.durationMs = durationMs;
            return this;
        }

    /**
     * build方法。
     * @return StepResult类型返回值
     */
    /**
     * build方法。
     * @return StepResult类型返回值
     */
        public StepResult build() {
            StepResult result = new StepResult();
            result.setStepName(stepName);
            result.setSuccess(success);
            result.setOutput(output);
            result.setErrorMessage(errorMessage);
            result.setDurationMs(durationMs);
            return result;
        }
    }
}
