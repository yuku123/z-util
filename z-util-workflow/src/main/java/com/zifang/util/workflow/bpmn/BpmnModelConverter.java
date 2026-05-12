package com.zifang.util.workflow.bpmn;

import com.zifang.util.workflow.config.Configurations;
import com.zifang.util.workflow.config.Connector;
import com.zifang.util.workflow.config.WorkflowConfiguration;
import com.zifang.util.workflow.config.WorkflowNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converter that transforms BpmnDiagram to WorkflowConfiguration
 */
public class BpmnModelConverter {

    private static final String ENGINE_TYPE = "spark";

    /**
     * Convert BpmnDiagram to WorkflowConfiguration
     *
     * @param diagram BPMN diagram model
     * @return WorkflowConfiguration
     */
    public WorkflowConfiguration convert(BpmnDiagram diagram) {
        WorkflowConfiguration config = new WorkflowConfiguration();

        // Set up configurations
        config.setConfigurations(createConfigurations());

        // Convert nodes
        List<WorkflowNode> workflowNodes = convertNodes(diagram);

        // Wire up connections based on sequence flows
        wireConnections(workflowNodes, diagram);

        config.setWorkflowNodeList(workflowNodes);

        return config;
    }

    private Configurations createConfigurations() {
        Configurations configurations = new Configurations();

        com.zifang.util.workflow.config.Engine engine = new com.zifang.util.workflow.config.Engine();
        engine.setType(ENGINE_TYPE);
        configurations.setEngine(engine);

        com.zifang.util.workflow.config.CacheEngine cacheEngine = new com.zifang.util.workflow.config.CacheEngine();
        cacheEngine.setEngineType("memory");
        configurations.setCacheEngine(cacheEngine);

        return configurations;
    }

    private List<WorkflowNode> convertNodes(BpmnDiagram diagram) {
        List<WorkflowNode> workflowNodes = new ArrayList<>();

        // First pass: create all workflow nodes
        for (BpmnDiagram.BpmnNode bpmnNode : diagram.getNodes()) {
            WorkflowNode workflowNode = convertNode(bpmnNode);
            workflowNodes.add(workflowNode);
        }

        return workflowNodes;
    }

    private WorkflowNode convertNode(BpmnDiagram.BpmnNode bpmnNode) {
        WorkflowNode workflowNode = new WorkflowNode();

        workflowNode.setNodeId(bpmnNode.getId());
        workflowNode.setName(bpmnNode.getName());
        workflowNode.setType(mapNodeType(bpmnNode.getType()));

        // Set up connector with empty pre/post lists (will be wired later)
        Connector connector = new Connector();
        connector.setPre(new ArrayList<>());
        connector.setPost(new ArrayList<>());
        workflowNode.setConnector(connector);

        // Set service unit based on node type
        workflowNode.setServiceUnit(getServiceUnit(bpmnNode.getType()));

        // Set invoke parameter (e.g., condition expression for gateways)
        if (bpmnNode.getProperty("conditionExpression") != null) {
            workflowNode.setInvokeParameter(bpmnNode.getProperty("conditionExpression"));
        } else if (bpmnNode.getProperty("documentation") != null) {
            workflowNode.setInvokeParameter(bpmnNode.getProperty("documentation"));
        }

        return workflowNode;
    }

    private String mapNodeType(String bpmnType) {
        if (bpmnType == null) {
            return "task";
        }

        switch (bpmnType.toLowerCase()) {
            case "startevent":
                return "startEvent";
            case "endevent":
                return "endEvent";
            case "usertask":
                return "userTask";
            case "servicetask":
                return "serviceTask";
            case "exclusivategateway":
                return "exclusiveGateway";
            case "parallelgateway":
                return "parallelGateway";
            case "task":
                return "task";
            default:
                return bpmnType.toLowerCase();
        }
    }

    private String getServiceUnit(String nodeType) {
        if (nodeType == null) {
            return "empty";
        }

        switch (nodeType.toLowerCase()) {
            case "usertask":
                return "userTaskHandler";
            case "servicetask":
                return "serviceTaskHandler";
            case "exclusivategateway":
            case "parallelgateway":
                return "gatewayHandler";
            default:
                return "empty";
        }
    }

    private void wireConnections(List<WorkflowNode> workflowNodes, BpmnDiagram diagram) {
        // Build node lookup map
        Map<String, WorkflowNode> nodeMap = new HashMap<>();
        for (WorkflowNode node : workflowNodes) {
            nodeMap.put(node.getNodeId(), node);
        }

        // Wire based on sequence flows
        for (BpmnDiagram.BpmnSequenceFlow flow : diagram.getSequenceFlows()) {
            WorkflowNode sourceNode = nodeMap.get(flow.getSourceRef());
            WorkflowNode targetNode = nodeMap.get(flow.getTargetRef());

            if (sourceNode != null && targetNode != null) {
                // Add post to source node
                if (!sourceNode.getConnector().getPost().contains(flow.getTargetRef())) {
                    sourceNode.getConnector().getPost().add(flow.getTargetRef());
                }

                // Add pre to target node
                if (!targetNode.getConnector().getPre().contains(flow.getSourceRef())) {
                    targetNode.getConnector().getPre().add(flow.getSourceRef());
                }

                // If this is a conditional flow, set the condition on the target node
                if (flow.getConditionExpression() != null && !flow.getConditionExpression().isEmpty()) {
                    targetNode.setInvokeParameter(flow.getConditionExpression());
                }
            }
        }
    }
}