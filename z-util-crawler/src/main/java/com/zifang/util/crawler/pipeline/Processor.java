package com.zifang.util.crawler.pipeline;

/**
 * 处理器接口，定义管道阶段的处理逻辑。
 * <p>
 * 实现类负责处理 PipelineContext 中的数据提取和转换，
 * 每个处理器可以在数据通过管道时对其进行修改或增强。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * Processor接口。
 */
public interface Processor {

    /**
     * Process the given context.
     * 
     * @param ctx the pipeline context to process
     */
    void process(PipelineContext ctx);
}
