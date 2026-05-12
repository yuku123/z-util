package com.zifang.util.workflow.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BPMN diagram model containing definitions
 */
public class BpmnDiagram {

    private String id;
    private String name;
    private List<BpmnNode> nodes = new ArrayList<>();
    private List<BpmnSequenceFlow> sequenceFlows = new ArrayList<>();
    private Map<String, BpmnNode> nodeMap = new HashMap<>();

    public BpmnDiagram() {
    }

    public BpmnDiagram(String id, String name) {
        this.id = id;
        this.name = name;
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

    public List<BpmnNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<BpmnNode> nodes) {
        this.nodes = nodes;
    }

    public List<BpmnSequenceFlow> getSequenceFlows() {
        return sequenceFlows;
    }

    public void setSequenceFlows(List<BpmnSequenceFlow> sequenceFlows) {
        this.sequenceFlows = sequenceFlows;
    }

    public Map<String, BpmnNode> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<String, BpmnNode> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public void addNode(BpmnNode node) {
        this.nodes.add(node);
        this.nodeMap.put(node.getId(), node);
    }

    public void addSequenceFlow(BpmnSequenceFlow flow) {
        this.sequenceFlows.add(flow);
    }

    /**
     * BPMN node representation
     */
    public static class BpmnNode {
        private String id;
        private String name;
        private String type; // startEvent, endEvent, userTask, exclusiveGateway, parallelGateway, etc.
        private Map<String, String> properties = new HashMap<>();

        public BpmnNode() {
        }

        public BpmnNode(String id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
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

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public void setProperty(String key, String value) {
            this.properties.put(key, value);
        }

        public String getProperty(String key) {
            return this.properties.get(key);
        }
    }

    /**
     * BPMN sequence flow (connection between nodes)
     */
    public static class BpmnSequenceFlow {
        private String id;
        private String sourceRef;
        private String targetRef;
        private String conditionExpression;

        public BpmnSequenceFlow() {
        }

        public BpmnSequenceFlow(String id, String sourceRef, String targetRef) {
            this.id = id;
            this.sourceRef = sourceRef;
            this.targetRef = targetRef;
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

        public String getConditionExpression() {
            return conditionExpression;
        }

        public void setConditionExpression(String conditionExpression) {
            this.conditionExpression = conditionExpression;
        }
    }
}