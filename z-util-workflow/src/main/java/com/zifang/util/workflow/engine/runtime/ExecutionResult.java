package com.zifang.util.workflow.engine.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 工作流执行结果。
 * 包含执行状态、已完成节点、待处理节点、运行时变量等信息。
 * 使用Builder模式构建实例。
 *
 * @see ExecutionResult#builder()
 */
public class ExecutionResult {

    /**
     * Process instance identifier
     */
    private String processId;

    /**
     * Current status: RUNNING, COMPLETED, FAILED, PAUSED
     */
    private String status;

    /**
     * List of completed node IDs
     */
    private List<String> completedNodes;

    /**
     * List of pending node IDs
     */
    private List<String> pendingNodes;

    /**
     * List of pending user task node IDs
     */
    private List<String> pendingUserTaskNodes;

    /**
     * Runtime variables
     */
    private Map<String, Object> variables;

    /**
     * Current executing node ID
     */
    private String currentNodeId;

    /**
     * Error message if execution failed
     */
    private String errorMessage;

    public ExecutionResult() {
        this.completedNodes = new ArrayList<>();
        this.pendingNodes = new ArrayList<>();
        this.pendingUserTaskNodes = new ArrayList<>();
    }

    public ExecutionResult(String processId, String status) {
        this();
        this.processId = processId;
        this.status = status;
    }

    // -------- Builder pattern --------

    /**
     * 使用Builder模式创建执行结果实例。
     *
     * @return ExecutionResultBuilder构建器
     */
    public static ExecutionResultBuilder builder() {
        return new ExecutionResultBuilder();
    }

    public static class ExecutionResultBuilder {
        private final ExecutionResult result = new ExecutionResult();

        public ExecutionResultBuilder processId(String processId) {
            result.processId = processId;
            return this;
        }

        public ExecutionResultBuilder status(String status) {
            result.status = status;
            return this;
        }

        public ExecutionResultBuilder completedNodes(List<String> completedNodes) {
            result.completedNodes = completedNodes;
            return this;
        }

        public ExecutionResultBuilder pendingNodes(List<String> pendingNodes) {
            result.pendingNodes = pendingNodes;
            return this;
        }

        public ExecutionResultBuilder pendingUserTaskNodes(List<String> pendingUserTaskNodes) {
            result.pendingUserTaskNodes = pendingUserTaskNodes;
            return this;
        }

        public ExecutionResultBuilder variables(Map<String, Object> variables) {
            result.variables = variables;
            return this;
        }

        public ExecutionResultBuilder currentNodeId(String currentNodeId) {
            result.currentNodeId = currentNodeId;
            return this;
        }

        public ExecutionResultBuilder errorMessage(String errorMessage) {
            result.errorMessage = errorMessage;
            return this;
        }

        public ExecutionResult build() {
            return result;
        }
    }

    // -------- Getters and Setters --------

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getCompletedNodes() {
        return completedNodes;
    }

    public void setCompletedNodes(List<String> completedNodes) {
        this.completedNodes = completedNodes;
    }

    public List<String> getPendingNodes() {
        return pendingNodes;
    }

    public void setPendingNodes(List<String> pendingNodes) {
        this.pendingNodes = pendingNodes;
    }

    public List<String> getPendingUserTaskNodes() {
        return pendingUserTaskNodes;
    }

    public void setPendingUserTaskNodes(List<String> pendingUserTaskNodes) {
        this.pendingUserTaskNodes = pendingUserTaskNodes;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // -------- Convenience methods --------

    public void addCompletedNode(String nodeId) {
        if (!this.completedNodes.contains(nodeId)) {
            this.completedNodes.add(nodeId);
        }
    }

    public void addPendingNode(String nodeId) {
        if (!this.pendingNodes.contains(nodeId)) {
            this.pendingNodes.add(nodeId);
        }
    }

    public void removePendingNode(String nodeId) {
        this.pendingNodes.remove(nodeId);
    }

    public void addPendingUserTaskNode(String nodeId) {
        if (!this.pendingUserTaskNodes.contains(nodeId)) {
            this.pendingUserTaskNodes.add(nodeId);
        }
    }

    public void removePendingUserTaskNode(String nodeId) {
        this.pendingUserTaskNodes.remove(nodeId);
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "processId='" + processId + '\'' +
                ", status='" + status + '\'' +
                ", completedNodes=" + completedNodes +
                ", pendingNodes=" + pendingNodes +
                ", pendingUserTaskNodes=" + pendingUserTaskNodes +
                ", variables=" + variables +
                ", currentNodeId='" + currentNodeId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExecutionResult that = (ExecutionResult) o;
        return Objects.equals(processId, that.processId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(completedNodes, that.completedNodes) &&
                Objects.equals(pendingNodes, that.pendingNodes) &&
                Objects.equals(pendingUserTaskNodes, that.pendingUserTaskNodes) &&
                Objects.equals(variables, that.variables) &&
                Objects.equals(currentNodeId, that.currentNodeId) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, status, completedNodes, pendingNodes, pendingUserTaskNodes, variables, currentNodeId, errorMessage);
    }
}