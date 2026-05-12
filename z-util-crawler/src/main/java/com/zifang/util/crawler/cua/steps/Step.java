package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

import java.util.Map;

/**
 * Interface for a step in the CUA execution pipeline.
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
