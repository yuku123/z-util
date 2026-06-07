package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

import java.util.Map;

/**
 * CUA 执行管道中步骤的接口定义。
 * <p>
 * 定义步骤的基本行为，包括获取名称、设置参数和执行逻辑。
 * 所有 CUA 内置步骤（NavigateStep、ClickStep 等）都实现此接口。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * Step接口。
 */
/**
 * Step接口。
 */
public interface Step {

    /**
     * Get the name of this step.
     */
    String getName();

    /**
     * Execute this step with the given context.
     */
    StepResult execute(PipelineContext ctx);

    /**
     * Set a parameter for this step.
     */
    void setParameter(String key, Object value);

    /**
     * Set multiple parameters for this step.
     */
    default void setParameters(Map<String, Object> params) {
        if (params != null) {
            params.forEach(this::setParameter);
        }
    }
}
