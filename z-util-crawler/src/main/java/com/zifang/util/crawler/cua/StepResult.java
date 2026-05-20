package com.zifang.util.crawler.cua;

/**
 * 单个步骤执行结果对象。
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
    public StepResult() {
    }

    /**
     * 使用步骤名称和成功状态构造 StepResult。
     * @param stepName 步骤名称
     * @param success 是否成功
     */
    public StepResult(String stepName, boolean success) {
        this.stepName = stepName;
        this.success = success;
    }

    /**
     * 获取步骤名称。
     * @return 步骤名称
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * 设置步骤名称。
     * @param stepName 步骤名称
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    /**
     * 获取是否成功。
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置是否成功。
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取输出结果。
     * @return 输出结果
     */
    public Object getOutput() {
        return output;
    }

    /**
     * 设置输出结果。
     * @param output 输出结果
     */
    public void setOutput(Object output) {
        this.output = output;
    }

    /**
     * 获取错误信息。
     * @return 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 设置错误信息。
     * @param errorMessage 错误信息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 获取执行耗时（毫秒）。
     * @return 执行耗时
     */
    public long getDurationMs() {
        return durationMs;
    }

    /**
     * 设置执行耗时（毫秒）。
     * @param durationMs 执行耗时
     */
    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    /**
     * 获取 Builder 实例。
     * @return Builder 对象
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

        public Builder stepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder output(Object output) {
            this.output = output;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder durationMs(long durationMs) {
            this.durationMs = durationMs;
            return this;
        }

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
