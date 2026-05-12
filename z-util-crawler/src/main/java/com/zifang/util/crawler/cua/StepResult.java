package com.zifang.util.crawler.cua;

/**
 * Result object for a single step execution.
 */
public class StepResult {

    private String stepName;
    private boolean success;
    private Object output;
    private String errorMessage;
    private long durationMs;

    public StepResult() {
    }

    public StepResult(String stepName, boolean success) {
        this.stepName = stepName;
        this.success = success;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

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
