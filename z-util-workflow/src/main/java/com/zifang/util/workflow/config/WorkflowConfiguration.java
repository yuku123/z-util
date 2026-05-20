package com.zifang.util.workflow.config;

import java.util.List;

/**
 * 整个流程图的定义
 */
public class WorkflowConfiguration {

    /**
     * 全局性的配置，流引擎配置
     */
    private Configurations configurations;

    /**
     * 所有控制用的节点配置
     */
    private List<WorkflowNode> workflowNodeList;

    /**
     * 默认构造函数，创建一个空的流程配置
     */
    public WorkflowConfiguration() {
    }

    /**
     * 全参数构造函数，初始化流程配置
     *
     * @param configurations    全局配置信息，包含执行引擎、缓存引擎等配置
     * @param workflowNodeList  工作流节点列表，包含所有业务流程节点定义
     */
    public WorkflowConfiguration(Configurations configurations, List<WorkflowNode> workflowNodeList) {
        this.configurations = configurations;
        this.workflowNodeList = workflowNodeList;
    }

    /**
     * 获取全局配置信息
     *
     * @return 全局配置对象，包含流引擎配置、缓存引擎配置等
     */
    public Configurations getConfigurations() {
        return configurations;
    }

    /**
     * 设置全局配置信息
     *
     * @param configurations 全局配置对象，不能为null
     */
    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }

    /**
     * 获取工作流节点列表
     *
     * @return 工作流节点列表，包含所有业务流程节点定义
     */
    public List<WorkflowNode> getWorkflowNodeList() {
        return workflowNodeList;
    }

    /**
     * 设置工作流节点列表
     *
     * @param workflowNodeList 工作流节点列表，不能为null
     */
    public void setWorkflowNodeList(List<WorkflowNode> workflowNodeList) {
        this.workflowNodeList = workflowNodeList;
    }

    @Override
    public String toString() {
        return "WorkflowConfiguration{configurations=" + configurations + ", workflowNodeList=" + workflowNodeList + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowConfiguration that = (WorkflowConfiguration) o;
        if (configurations != null ? !configurations.equals(that.configurations) : that.configurations != null) return false;
        return workflowNodeList != null ? workflowNodeList.equals(that.workflowNodeList) : that.workflowNodeList == null;
    }

    @Override
    public int hashCode() {
        int result = configurations != null ? configurations.hashCode() : 0;
        result = 31 * result + (workflowNodeList != null ? workflowNodeList.hashCode() : 0);
        return result;
    }
}
