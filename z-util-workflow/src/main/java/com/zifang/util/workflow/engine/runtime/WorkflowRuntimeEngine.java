package com.zifang.util.workflow.engine.runtime;

import com.zifang.util.workflow.config.Configurations;
import com.zifang.util.workflow.config.Connector;
import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.config.WorkflowConfiguration;
import com.zifang.util.workflow.config.WorkflowNode;
import com.zifang.util.workflow.engine.interfaces.AbstractEngine;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import com.zifang.util.workflow.engine.interfaces.EngineFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Runtime execution engine for workflow.
 * Manages workflow execution, node wiring, and gateway evaluation.
 */
public class WorkflowRuntimeEngine {

    private static final String STATUS_RUNNING = "RUNNING";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_FAILED = "FAILED";
    private static final String STATUS_PAUSED = "PAUSED";

    private WorkflowConfiguration configuration;
    private Map<String, ExecutableWorkflowNode> nodeMap;
    private Map<String, Object> variables;
    private ExecutionResult currentResult;
    private String processId;
    private AtomicInteger nodeIdGenerator;
    private AbstractEngine abstractEngine;

    public WorkflowRuntimeEngine() {
        this.nodeMap = new HashMap<>();
        this.variables = new HashMap<>();
        this.currentResult = new ExecutionResult();
        this.nodeIdGenerator = new AtomicInteger(0);
    }

    /**
     * Initialize the runtime engine with workflow configuration.
     * Builds node map, wires up connections, and sets up CountDownLatches.
     *
     * @param configuration the workflow configuration
     */
    public void initialize(WorkflowConfiguration configuration) {
        this.configuration = configuration;
        this.processId = "process-" + nodeIdGenerator.getAndIncrement();
        this.nodeMap.clear();
        this.variables.clear();

        // Build nodeMap from configuration.nodes
        buildNodeMap();

        // Wire up pre/post connections
        wireConnections();

        // Set countDownLatch on each node based on pre node count
        initializeCountDownLatches();

        // Initialize execution result
        initializeExecutionResult();
    }

    private void buildNodeMap() {
        if (configuration.getWorkflowNodeList() == null) {
            return;
        }

        // Initialize engine
        if (configuration.getConfigurations() != null) {
            this.abstractEngine = EngineFactory.getEngine(configuration.getConfigurations().getEngine());
        }

        for (WorkflowNode workflowNode : configuration.getWorkflowNodeList()) {
            ExecutableWorkflowNode executableNode = new ExecutableWorkflowNode(workflowNode);
            executableNode.setAbstractEngine(abstractEngine);
            executableNode.setStatus("prepared");
            nodeMap.put(workflowNode.getNodeId(), executableNode);
        }
    }

    private void wireConnections() {
        for (ExecutableWorkflowNode node : nodeMap.values()) {
            Connector connector = node.getConnector();
            if (connector == null) {
                continue;
            }

            List<String> preList = connector.getPre();
            List<String> postList = connector.getPost();

            // Wire pre nodes
            if (preList != null) {
                for (String preId : preList) {
                    ExecutableWorkflowNode preNode = nodeMap.get(preId);
                    if (preNode != null) {
                        node.putPre(preNode);
                        preNode.putPost(node);
                    }
                }
            }

            // Wire post nodes
            if (postList != null) {
                for (String postId : postList) {
                    ExecutableWorkflowNode postNode = nodeMap.get(postId);
                    if (postNode != null) {
                        node.putPost(postNode);
                    }
                }
            }
        }
    }

    private void initializeCountDownLatches() {
        for (ExecutableWorkflowNode node : nodeMap.values()) {
            CountDownLatch latch = new CountDownLatch(node.getPre().size());
            node.setCountDownLatch(latch);
        }

        // Initialize post count down latches
        for (ExecutableWorkflowNode node : nodeMap.values()) {
            for (ExecutableWorkflowNode postNode : node.getPost()) {
                node.getPostCountDownLatchList().add(postNode.getCountDownLatch());
            }
        }
    }

    private void initializeExecutionResult() {
        currentResult = ExecutionResult.builder()
                .processId(processId)
                .status(STATUS_RUNNING)
                .variables(new HashMap<>(variables))
                .completedNodes(new ArrayList<>())
                .pendingNodes(new ArrayList<>(nodeMap.keySet()))
                .pendingUserTaskNodes(findUserTaskNodes())
                .build();
    }

    private List<String> findUserTaskNodes() {
        List<String> userTaskNodes = new ArrayList<>();
        for (Map.Entry<String, ExecutableWorkflowNode> entry : nodeMap.entrySet()) {
            String type = entry.getValue().getType();
            if ("userTask".equalsIgnoreCase(type)) {
                userTaskNodes.add(entry.getKey());
            }
        }
        return userTaskNodes;
    }

    /**
     * Start workflow execution from start nodes (nodes with empty pre)
     *
     * @return execution result
     */
    public ExecutionResult start() {
        List<ExecutableWorkflowNode> startNodes = findStartNodes();
        if (startNodes.isEmpty()) {
            currentResult.setStatus(STATUS_COMPLETED);
            return currentResult;
        }

        currentResult.setStatus(STATUS_RUNNING);

        // Execute start nodes
        for (ExecutableWorkflowNode startNode : startNodes) {
            executeNode(startNode);
        }

        // Check if workflow completed
        checkWorkflowCompletion();

        return currentResult;
    }

    /**
     * Resume execution at a specific node (e.g., after user completes a task)
     *
     * @param nodeId the node ID to resume at
     * @return execution result
     */
    public ExecutionResult resume(String nodeId) {
        ExecutableWorkflowNode node = nodeMap.get(nodeId);
        if (node == null) {
            currentResult.setStatus(STATUS_FAILED);
            currentResult.setErrorMessage("Node not found: " + nodeId);
            return currentResult;
        }

        // Mark node as ready to execute
        executeNode(node);

        // Check if workflow completed
        checkWorkflowCompletion();

        return currentResult;
    }

    private List<ExecutableWorkflowNode> findStartNodes() {
        List<ExecutableWorkflowNode> startNodes = new ArrayList<>();
        for (ExecutableWorkflowNode node : nodeMap.values()) {
            if (node.getPre().isEmpty()) {
                startNodes.add(node);
            }
        }
        return startNodes;
    }

    private void executeNode(ExecutableWorkflowNode node) {
        String nodeId = node.getNodeId();
        String type = node.getType();

        currentResult.setCurrentNodeId(nodeId);
        node.setStatus("started");

        // Handle gateway evaluation
        if ("exclusiveGateway".equalsIgnoreCase(type)) {
            executeGateway(node);
        } else if ("parallelGateway".equalsIgnoreCase(type)) {
            executeParallelGateway(node);
        } else {
            // Regular node execution
            executeServiceNode(node);
        }
    }

    private void executeGateway(ExecutableWorkflowNode gatewayNode) {
        // Evaluate conditions to determine the outgoing path
        List<ExecutableWorkflowNode> postNodes = gatewayNode.getPost();
        GatewayEvaluator evaluator = new GatewayEvaluator();

        String condition = (String) gatewayNode.getInvokeParameter();
        ExecutableWorkflowNode selectedNode = null;

        for (ExecutableWorkflowNode postNode : postNodes) {
            String postCondition = (String) postNode.getInvokeParameter();
            if (evaluator.evaluate(postCondition, variables)) {
                selectedNode = postNode;
                break;
            }
        }

        gatewayNode.setStatus("executed");
        currentResult.addCompletedNode(gatewayNode.getNodeId());
        currentResult.removePendingNode(gatewayNode.getNodeId());

        if (selectedNode != null) {
            executeNode(selectedNode);
        }
    }

    private void executeParallelGateway(ExecutableWorkflowNode gatewayNode) {
        // For parallel gateway, activate all outgoing nodes
        List<ExecutableWorkflowNode> postNodes = gatewayNode.getPost();

        gatewayNode.setStatus("executed");
        currentResult.addCompletedNode(gatewayNode.getNodeId());
        currentResult.removePendingNode(gatewayNode.getNodeId());

        // Notify all post nodes
        for (ExecutableWorkflowNode postNode : postNodes) {
            postNode.getCountDownLatch().countDown();
            executeNode(postNode);
        }
    }

    private void executeServiceNode(ExecutableWorkflowNode node) {
        String nodeId = node.getNodeId();
        String serviceUnit = node.getServiceUnit();

        try {
            // Initialize engine service if not already done
            if (node.getAbstractEngineService() == null && abstractEngine != null) {
                AbstractEngineService engineService = abstractEngine.getRegisteredEngineService(serviceUnit);
                node.setAbstractEngineService(engineService);
            }

            // Execute the service
            if (node.getAbstractEngineService() != null) {
                node.getAbstractEngineService().setInvokeParameter(node.getInvokeParameter());
                node.getAbstractEngineService().exec(node);
            }

            node.setStatus("executed");
            currentResult.addCompletedNode(nodeId);
            currentResult.removePendingNode(nodeId);

            // Handle user task nodes specially
            if ("userTask".equalsIgnoreCase(node.getType())) {
                currentResult.addPendingUserTaskNode(nodeId);
                currentResult.setStatus(STATUS_PAUSED);
                return;
            }

            // Notify post nodes
            notifyPostNodes(node);

        } catch (Exception e) {
            node.setStatus("failed");
            currentResult.setStatus(STATUS_FAILED);
            currentResult.setErrorMessage("Node execution failed: " + nodeId + " - " + e.getMessage());
        }
    }

    private void notifyPostNodes(ExecutableWorkflowNode node) {
        List<ExecutableWorkflowNode> postNodes = node.getPost();

        for (ExecutableWorkflowNode postNode : postNodes) {
            // Decrement the post node's countdown latch
            CountDownLatch latch = postNode.getCountDownLatch();
            if (latch != null && latch.getCount() > 0) {
                latch.countDown();
            }

            // If all pre nodes are done, execute this post node
            if (postNode.getCountDownLatch().getCount() == 0) {
                // Check if node is not already executed
                if (!"executed".equals(postNode.getStatus())) {
                    executeNode(postNode);
                }
            }
        }
    }

    private void checkWorkflowCompletion() {
        boolean allCompleted = true;
        for (ExecutableWorkflowNode node : nodeMap.values()) {
            if (!"executed".equals(node.getStatus())) {
                allCompleted = false;
                break;
            }
        }

        if (allCompleted) {
            currentResult.setStatus(STATUS_COMPLETED);
            currentResult.setCurrentNodeId(null);
        }
    }

    /**
     * Get current execution result
     *
     * @return current execution result
     */
    public ExecutionResult getCurrentResult() {
        return currentResult;
    }

    /**
     * Get list of pending user tasks
     *
     * @return list of pending user task node IDs
     */
    public List<String> getPendingUserTasks() {
        return currentResult.getPendingUserTaskNodes();
    }

    /**
     * Set a runtime variable
     *
     * @param key   variable key
     * @param value variable value
     */
    public void setVariable(String key, Object value) {
        variables.put(key, value);
        if (currentResult.getVariables() != null) {
            currentResult.getVariables().put(key, value);
        }
    }

    /**
     * Get a runtime variable
     *
     * @param key variable key
     * @return variable value
     */
    public Object getVariable(String key) {
        return variables.get(key);
    }

    /**
     * Get all runtime variables
     *
     * @return variables map
     */
    public Map<String, Object> getVariables() {
        return new HashMap<>(variables);
    }

    // -------- Getters and Setters --------

    public WorkflowConfiguration getConfiguration() {
        return configuration;
    }

    public Map<String, ExecutableWorkflowNode> getNodeMap() {
        return nodeMap;
    }

    public String getProcessId() {
        return processId;
    }
}