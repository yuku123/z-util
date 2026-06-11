package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.pipeline.PipelineContext;

import java.util.ArrayList;
import java.util.List;

/**
 * CUA 代理执行结果对象，包含执行状态、步骤结果列表和上下文信息。
 * <p>
 * 封装整个 CUA 任务执行的完整结果，
 * 包括所有步骤的执行详情、最终成功状态和关联的管道上下文。
 *
 * @author zifang
 * @version 1.0.0
 */
public class CuResult {

    private boolean success;
    private List<StepResult> stepResults;
    private PipelineContext context;
    private String errorMessage;

    /**
     * 构造 CuResult。
     */
    public CuResult() {
        this.stepResults = new ArrayList<>();
    }

    /**
     * 获取执行是否成功。
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置执行是否成功。
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取步骤结果列表。
     * @return 步骤结果列表
     */
    public List<StepResult> getStepResults() {
        return stepResults;
    }

    /**
     * 设置步骤结果列表。
     * @param stepResults 步骤结果列表
     */
    public void setStepResults(List<StepResult> stepResults) {
        this.stepResults = stepResults;
    }

    /**
     * 添加步骤结果。
     * @param stepResult 步骤结果
     */
    public void addStepResult(StepResult stepResult) {
        this.stepResults.add(stepResult);
    }

    /**
     * 获取上下文。
     * @return 管道上下文
     */
    public PipelineContext getContext() {
        return context;
    }

    /**
     * 设置上下文。
     * @param context 管道上下文
     */
    public void setContext(PipelineContext context) {
        this.context = context;
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
     * 获取 Builder 实例。
     * @return Builder 对象
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private List<StepResult> stepResults = new ArrayList<>();
        private PipelineContext context;
        private String errorMessage;

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
     * stepResults方法。
     *      * @param stepResults ListStepResult类型参数
     * @return Builder类型返回值
     */
        public Builder stepResults(List<StepResult> stepResults) {
            this.stepResults = stepResults;
            return this;
        }

    /**
     * context方法。
     *      * @param context PipelineContext类型参数
     * @return Builder类型返回值
     */
        public Builder context(PipelineContext context) {
            this.context = context;
            return this;
        }

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
     * build方法。
     * @return CuResult类型返回值
     */
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
