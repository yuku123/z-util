package com.zifang.util.workflow.bpmn;

import java.util.Objects;

/**
 * Represents a BPMN 2.0 sequence flow connecting flow nodes
 */
public class BpmnSequenceFlow {

    private String id;
    private String sourceRef;
    private String targetRef;
    private String name;
    private String conditionExpression;
    private boolean isDefault;

    public BpmnSequenceFlow() {
    }

    public BpmnSequenceFlow(String id, String sourceRef, String targetRef) {
        this.id = id;
        this.sourceRef = sourceRef;
        this.targetRef = targetRef;
    }

    public BpmnSequenceFlow(String id, String sourceRef, String targetRef, String name, String conditionExpression, boolean isDefault) {
        this.id = id;
        this.sourceRef = sourceRef;
        this.targetRef = targetRef;
        this.name = name;
        this.conditionExpression = conditionExpression;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getTargetRef() {
        return targetRef;
    }

    public void setTargetRef(String targetRef) {
        this.targetRef = targetRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "BpmnSequenceFlow{id='" + id + "', sourceRef='" + sourceRef + "', targetRef='" + targetRef + 
               "', name='" + name + "', conditionExpression='" + conditionExpression + "', isDefault=" + isDefault + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BpmnSequenceFlow that = (BpmnSequenceFlow) o;
        return isDefault == that.isDefault &&
                Objects.equals(id, that.id) &&
                Objects.equals(sourceRef, that.sourceRef) &&
                Objects.equals(targetRef, that.targetRef) &&
                Objects.equals(name, that.name) &&
                Objects.equals(conditionExpression, that.conditionExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceRef, targetRef, name, conditionExpression, isDefault);
    }
}
