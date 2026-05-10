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

    public WorkflowConfiguration() {
    }

    public WorkflowConfiguration(Configurations configurations, List<WorkflowNode> workflowNodeList) {
        this.configurations = configurations;
        this.workflowNodeList = workflowNodeList;
    }

    public Configurations getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }

    public List<WorkflowNode> getWorkflowNodeList() {
        return workflowNodeList;
    }

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
