package com.zifang.util.workflow.engine.java;

import com.zifang.util.workflow.engine.interfaces.AbstractEngine;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;

import java.util.Map;

/**
 * Java执行引擎实现类。
 * 负责在Java环境下执行工作流节点，提供纯Java的计算能力。
 * 当前为占位实现，具体功能待扩展。
 *
 * @see AbstractEngine
 */
public class JavaEngine extends AbstractEngine {

    /**
     * 获取已注册的引擎服务映射表。
     *
     * @return 服务单元名称到服务类类型的映射
     */
    @Override
    public Map<String, Class<? extends AbstractEngineService>> getRegisteredEngineServiceMap() {
        return null;
    }

    /**
     * 根据服务单元名称获取对应的引擎服务实例。
     *
     * @param serviceUnit 服务单元名称
     * @return 引擎服务实例
     */
    @Override
    public AbstractEngineService getRegisteredEngineService(String serviceUnit) {
        return null;
    }

    /**
     * 注册引擎服务。
     *
     * @param name         服务单元名称
     * @param engineService 服务类类型
     */
    @Override
    public void register(String name, Class<? extends AbstractEngineService> engineService) {

    }

    /**
     * 引擎初始化方法。
     */
    @Override
    public void doInitial() {

    }
}
