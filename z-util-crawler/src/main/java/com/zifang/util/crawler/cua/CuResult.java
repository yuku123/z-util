package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.pipeline.PipelineContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Result object for CUA agent execution.
 */
public class CuResult {

    private boolean success;
    private List<StepResult> stepResults;
    private PipelineContext context;
    private String errorMessage;

    public CuResult() {
        this.stepResults = new ArrayList<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<StepResult> getStepResults() {
        return stepResults;
    }

    public void setStepResults(List<StepResult> stepResults) {
        this.stepResults = stepResults;
    }

    public void addStepResult(StepResult stepResult) {
        this.stepResults.add(stepResult);
    }

    public PipelineContext getContext() {
        return context;
    }

    public void setContext(PipelineContext context) {
        this.context = context;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private List<StepResult> stepResults = new ArrayList<>();
        private PipelineContext context;
        private String errorMessage;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder stepResults(List<StepResult> stepResults) {
            this.stepResults = stepResults;
            return this;
        }

        public Builder context(PipelineContext context) {
            this.context = context;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public CuResult build() {
            CuResult result = new CuResult();
            result.setSuccess(success);
            result.setStepResults(stepResults);
            result.setContext(context);
            result.setErrorMessage(errorMessage);
            return result;
        }
    }
}
