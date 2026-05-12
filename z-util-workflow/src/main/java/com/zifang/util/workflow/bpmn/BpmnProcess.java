package com.zifang.util.workflow.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a BPMN 2.0 process containing flow nodes and sequence flows
 */
public class BpmnProcess {

    private String id;
    private String name;
    private List<BpmnFlowNode> nodes;
    private List<BpmnSequenceFlow> flows;

    public BpmnProcess() {
        this.nodes = new ArrayList<>();
        this.flows = new ArrayList<>();
    }

    public BpmnProcess(String id, String name, List<BpmnFlowNode> nodes, List<BpmnSequenceFlow> flows) {
        this.id = id;
        this.name = name;
        this.nodes = nodes != null ? nodes : new ArrayList<>();
        this.flows = flows != null ? flows : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BpmnFlowNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<BpmnFlowNode> nodes) {
        this.nodes = nodes;
    }

    public List<BpmnSequenceFlow> getFlows() {
        return flows;
    }

    public void setFlows(List<BpmnSequenceFlow> flows) {
        this.flows = flows;
    }

    @Override
    public String toString() {
        return "BpmnProcess{id='" + id + "', name='" + name + "', nodes=" + nodes + ", flows=" + flows + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BpmnProcess that = (BpmnProcess) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(nodes, that.nodes) &&
                Objects.equals(flows, that.flows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nodes, flows);
    }
}
