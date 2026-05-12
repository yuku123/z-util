package com.zifang.util.crawler.pipeline;

/**
 * Processor interface for pipeline stages.
 * Implementations process the PipelineContext to extract/transform data.
 */
public interface Processor {

    /**
     * Process the given context.
     * 
     * @param ctx the pipeline context to process
     */
    void process(PipelineContext ctx);
}
