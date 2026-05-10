package com.zifang.util.workflow.config;

import java.util.HashMap;
import java.util.Objects;

/**
 * 描述业务流节点的最小单元定义
 */
public class WorkflowNode {

    /**
     * 此节点的唯一标识号
     */
    private String nodeId;

    /**
     * 引入组别概念，逻辑上属于同种的处理单元
     */
    private String groupId;

    /**
     * 此节点的别名
     */
    private String name;

    /**
     * 描述节点的性质
     */
    private String type;

    /**
     * 处理引擎的服务标识
     */
    private String serviceUnit;

    /**
     * 处理引擎的处理实例方法
     */
    private String invokeDynamic;

    /**
     * 可以被处理引擎所识别并且转换的参数合集
     */
    private Object invokeParameter;

    /**
     * 描述此节点与其他节点的关联情况
     */
    private Connector connector;

    private HashMap<String, String> cache;

    public WorkflowNode() {
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceUnit() {
        return serviceUnit;
    }

    public void setServiceUnit(String serviceUnit) {
        this.serviceUnit = serviceUnit;
    }

    public String getInvokeDynamic() {
        return invokeDynamic;
    }

    public void setInvokeDynamic(String invokeDynamic) {
        this.invokeDynamic = invokeDynamic;
    }

    public Object getInvokeParameter() {
        return invokeParameter;
    }

    public void setInvokeParameter(Object invokeParameter) {
        this.invokeParameter = invokeParameter;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public HashMap<String, String> getCache() {
        return cache;
    }

    public void setCache(HashMap<String, String> cache) {
        this.cache = cache;
    }

    /**
     * 防止重复添加值
     */
    public void putPost(String nodeId) {
        if (!connector.getPost().contains(nodeId) && !this.nodeId.equals(nodeId)) {
            connector.getPost().add(nodeId);
        }
    }

    /**
     * 防止重复添加值
     */
    public void putPre(String nodeId) {
        if (!connector.getPre().contains(nodeId) && !this.nodeId.equals(nodeId)) {
            connector.getPre().add(nodeId);
        }
    }

    @Override
    public String toString() {
        return "WorkflowNode{nodeId=" + nodeId + ", groupId=" + groupId + ", name=" + name + ", type=" + type + ", serviceUnit=" + serviceUnit + ", invokeDynamic=" + invokeDynamic + ", invokeParameter=" + invokeParameter + ", connector=" + connector + ", cache=" + cache + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowNode that = (WorkflowNode) o;
        return Objects.equals(nodeId, that.nodeId) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(serviceUnit, that.serviceUnit) &&
                Objects.equals(invokeDynamic, that.invokeDynamic) &&
                Objects.equals(invokeParameter, that.invokeParameter) &&
                Objects.equals(connector, that.connector) &&
                Objects.equals(cache, that.cache);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, groupId, name, type, serviceUnit, invokeDynamic, invokeParameter, connector, cache);
    }
}
