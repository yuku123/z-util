package com.zifang.util.workflow.conponents;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;

import java.util.List;
import java.util.Map;

public class Task {

    private WorkFlowApplicationContext workFlowApplicationContext;

    private ExecutableWorkflowNode start;

    private List<ExecutableWorkflowNode> executableWorkNodes;

    private Map<String, ExecutableWorkflowNode> executableWorkNodeIdMap;

    public Task() {
    }

    public WorkFlowApplicationContext getWorkFlowApplicationContext() {
        return workFlowApplicationContext;
    }

    public void setWorkFlowApplicationContext(WorkFlowApplicationContext workFlowApplicationContext) {
        this.workFlowApplicationContext = workFlowApplicationContext;
    }

    public ExecutableWorkflowNode getStart() {
        return start;
    }

    public void setStart(ExecutableWorkflowNode start) {
        this.start = start;
    }

    public List<ExecutableWorkflowNode> getExecutableWorkNodes() {
        return executableWorkNodes;
    }

    public void setExecutableWorkNodes(List<ExecutableWorkflowNode> executableWorkNodes) {
        this.executableWorkNodes = executableWorkNodes;
    }

    public Map<String, ExecutableWorkflowNode> getExecutableWorkNodeIdMap() {
        return executableWorkNodeIdMap;
    }

    public void setExecutableWorkNodeIdMap(Map<String, ExecutableWorkflowNode> executableWorkNodeIdMap) {
        this.executableWorkNodeIdMap = executableWorkNodeIdMap;
    }

    public void exec() {
        start.exec();
    }
}
