package com.zifang.util.workflow.engine.spark;

import com.zifang.util.workflow.config.CacheEngine;

/**
 * 缓存引擎服务。
 * <p>
 * 负责管理工作流执行过程中的数据缓存。
 * 当前为占位实现，具体逻辑待扩展。
 *
 * @see CacheEngine
 */
/**
 * CacheEngineService类。
 */
public class CacheEngineService {

    private CacheEngine cacheEngine;

    /**
     * 使用缓存引擎配置构造缓存服务。
     *
     * @param cacheEngine 缓存引擎配置
     */
    /**
     * CacheEngineService方法。
     *      * @param cacheEngine CacheEngine类型参数
     */
    public CacheEngineService(CacheEngine cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

//    public void doCache(Dataset<Row> dataset, String cacheName) {
//        dataset.createOrReplaceTempView(cacheName);
//    }
}
