package com.zifang.util.crawler.pipeline;

/**
 * 处理器接口，定义管道阶段的处理逻辑。
 * 实现类负责处理 PipelineContext 中的数据提取和转换。
 */
public interface Processor {

    /**
     * Process the given context.
     * 
     * @param ctx the pipeline context to process
     */
    void process(PipelineContext ctx);
}
