package com.zifang.util.workflow.config;

import java.util.Map;

/**
 * 全局配置信息
 */
public class Configurations {

    /**
     * 唯一的标识出配置id , 这个值与工作流上下文的id绑定
     */
    private Integer workflowConfigurationId;

    /**
     * 全局配置的执行引擎配置
     */
    private Engine engine;

    /**
     * 配置的作为每步缓存的执行引擎
     */
    private CacheEngine cacheEngine;

    /**
     * 为每个操作者提供专属的参数
     */
    private Map<String, String> personalEnvironment;

    /**
     * 运行时参数设置
     */
    private Map<String, String> runtimeParameter;

    public Configurations() {
    }

    public Integer getWorkflowConfigurationId() {
        return workflowConfigurationId;
    }

    public void setWorkflowConfigurationId(Integer workflowConfigurationId) {
        this.workflowConfigurationId = workflowConfigurationId;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public CacheEngine getCacheEngine() {
        return cacheEngine;
    }

    public void setCacheEngine(CacheEngine cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

    public Map<String, String> getPersonalEnvironment() {
        return personalEnvironment;
    }

    public void setPersonalEnvironment(Map<String, String> personalEnvironment) {
        this.personalEnvironment = personalEnvironment;
    }

    public Map<String, String> getRuntimeParameter() {
        return runtimeParameter;
    }

    public void setRuntimeParameter(Map<String, String> runtimeParameter) {
        this.runtimeParameter = runtimeParameter;
    }

    @Override
    public String toString() {
        return "Configurations{workflowConfigurationId=" + workflowConfigurationId + ", engine=" + engine + ", cacheEngine=" + cacheEngine + ", personalEnvironment=" + personalEnvironment + ", runtimeParameter=" + runtimeParameter + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configurations that = (Configurations) o;
        if (workflowConfigurationId != null ? !workflowConfigurationId.equals(that.workflowConfigurationId) : that.workflowConfigurationId != null) return false;
        if (engine != null ? !engine.equals(that.engine) : that.engine != null) return false;
        if (cacheEngine != null ? !cacheEngine.equals(that.cacheEngine) : that.cacheEngine != null) return false;
        if (personalEnvironment != null ? !personalEnvironment.equals(that.personalEnvironment) : that.personalEnvironment != null) return false;
        return runtimeParameter != null ? runtimeParameter.equals(that.runtimeParameter) : that.runtimeParameter == null;
    }

    @Override
    public int hashCode() {
        int result = workflowConfigurationId != null ? workflowConfigurationId.hashCode() : 0;
        result = 31 * result + (engine != null ? engine.hashCode() : 0);
        result = 31 * result + (cacheEngine != null ? cacheEngine.hashCode() : 0);
        result = 31 * result + (personalEnvironment != null ? personalEnvironment.hashCode() : 0);
        result = 31 * result + (runtimeParameter != null ? runtimeParameter.hashCode() : 0);
        return result;
    }
}
