package com.zifang.util.workflow.bpmn;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for BPMN flow nodes (startEvent, endEvent, userTask, serviceTask, etc.)
 */
public class BpmnFlowNode {

    private String id;
    private String name;
    private String type;
    private Map<String, Object> properties;

    public BpmnFlowNode() {
        this.properties = new HashMap<>();
    }

    public BpmnFlowNode(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.properties = new HashMap<>();
    }

    public BpmnFlowNode(String id, String name, String type, Map<String, Object> properties) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.properties = properties != null ? properties : new HashMap<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    @Override
    public String toString() {
        return "BpmnFlowNode{id='" + id + "', name='" + name + "', type='" + type + "', properties=" + properties + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BpmnFlowNode that = (BpmnFlowNode) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, properties);
    }
}
