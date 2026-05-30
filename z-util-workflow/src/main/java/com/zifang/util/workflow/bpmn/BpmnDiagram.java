package com.zifang.util.workflow.bpmn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BPMN图表模型。
 * <p>
 * 包含BPMN 2.0流程定义的所有元素，包括节点和序列流。
 * 用于从XML解析后或手动构建BPMN流程模型。
 * <p>
 * 主要元素：
 * <ul>
 *   <li>nodes - BPMN节点列表（开始事件、结束事件、任务、网关等）</li>
 *   <li>sequenceFlows - 序列流列表（节点之间的连接）</li>
 *   <li>nodeMap - 节点ID到节点的快速映射</li>
 * </ul>
 *
 * @see BpmnProcess
 * @see BpmnXmlParser
 * @see BpmnModelConverter
 */
/**
 * BpmnDiagram类。
 */
public class BpmnDiagram {

    private String id;
    private String name;
    private List<BpmnNode> nodes = new ArrayList<>();
    private List<BpmnSequenceFlow> sequenceFlows = new ArrayList<>();
    private Map<String, BpmnNode> nodeMap = new HashMap<>();

    /**
     * 默认构造函数。
     */
    /**
     * BpmnDiagram方法。
     */
    public BpmnDiagram() {
    }

    /**
     * 使用指定 ID 和名称构造 BPMN 图。
     *
     * @param id   BPMN 图的唯一标识符
     * @param name BPMN 图的名称
     */
    /**
     * BpmnDiagram方法。
     *      * @param id String类型参数
     * @param name String类型参数
     */
    public BpmnDiagram(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 获取 BPMN 图的唯一标识符。
     *
     * @return BPMN 图的 ID
     */
    /**
     * getId方法。
     * @return String类型返回值
     */
    public String getId() {
        return id;
    }

    /**
     * 设置 BPMN 图的唯一标识符。
     *
     * @param id BPMN 图的 ID
     */
    /**
     * setId方法。
     *      * @param id String类型参数
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取 BPMN 图的名称。
     *
     * @return BPMN 图的名称
     */
    /**
     * getName方法。
     * @return String类型返回值
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 BPMN 图的名称。
     *
     * @param name BPMN 图的名称
     */
    /**
     * setName方法。
     *      * @param name String类型参数
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取所有 BPMN 节点的列表。
     *
     * @return BPMN 节点列表
     */
    /**
     * getNodes方法。
     * @return List<BpmnNode>类型返回值
     */
    public List<BpmnNode> getNodes() {
        return nodes;
    }

    /**
     * 设置 BPMN 节点列表。
     *
     * @param nodes BPMN 节点列表
     */
    /**
     * setNodes方法。
     *      * @param nodes ListBpmnNode类型参数
     */
    public void setNodes(List<BpmnNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * 获取所有序列流连接的列表。
     *
     * @return 序列流列表
     */
    /**
     * getSequenceFlows方法。
     * @return List<BpmnSequenceFlow>类型返回值
     */
    public List<BpmnSequenceFlow> getSequenceFlows() {
        return sequenceFlows;
    }

    /**
     * 设置序列流列表。
     *
     * @param sequenceFlows 序列流列表
     */
    /**
     * setSequenceFlows方法。
     *      * @param sequenceFlows ListBpmnSequenceFlow类型参数
     */
    public void setSequenceFlows(List<BpmnSequenceFlow> sequenceFlows) {
        this.sequenceFlows = sequenceFlows;
    }

    /**
     * 获取节点 ID 到节点的映射图。
     *
     * @return 节点映射图
     */
    /**
     * getNodeMap方法。
     * @return Map<String, BpmnNode>类型返回值
     */
    public Map<String, BpmnNode> getNodeMap() {
        return nodeMap;
    }

    /**
     * 设置节点映射图。
     *
     * @param nodeMap 节点 ID 到节点的映射图
     */
    /**
     * setNodeMap方法。
     *      * @param nodeMap MapString,类型参数
     */
    public void setNodeMap(Map<String, BpmnNode> nodeMap) {
        this.nodeMap = nodeMap;
    }

    /**
     * 向图中添加一个 BPMN 节点，同时更新节点映射。
     *
     * @param node 要添加的 BPMN 节点
     */
    /**
     * addNode方法。
     *      * @param node BpmnNode类型参数
     */
    public void addNode(BpmnNode node) {
        this.nodes.add(node);
        this.nodeMap.put(node.getId(), node);
    }

    /**
     * 向图中添加一个序列流连接。
     *
     * @param flow 要添加的序列流
     */
    /**
     * addSequenceFlow方法。
     *      * @param flow BpmnSequenceFlow类型参数
     */
    public void addSequenceFlow(BpmnSequenceFlow flow) {
        this.sequenceFlows.add(flow);
    }

    /**
     * BPMN 节点表示。
     * 表示 BPMN 2.0 中的一个流程节点，如开始事件、结束事件、任务、网关等。
     */
    public static class BpmnNode {
        private String id;
        private String name;
        private String type; // startEvent, endEvent, userTask, exclusiveGateway, parallelGateway, etc.
        private Map<String, String> properties = new HashMap<>();

        /**
         * 默认构造函数。
         */
    /**
     * BpmnNode方法。
     */
        public BpmnNode() {
        }

        /**
         * 使用指定 ID、名称和类型构造节点。
         *
         * @param id   节点唯一标识符
         * @param name 节点名称
         * @param type 节点类型，如 "startEvent"、"userTask"、"exclusiveGateway" 等
         */
    /**
     * BpmnNode方法。
     *      * @param id String类型参数
     * @param name String类型参数
     * @param type String类型参数
     */
        public BpmnNode(String id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }

        /**
         * 获取节点唯一标识符。
         *
         * @return 节点 ID
         */
    /**
     * getId方法。
     * @return String类型返回值
     */
        public String getId() {
            return id;
        }

        /**
         * 设置节点唯一标识符。
         *
         * @param id 节点 ID
         */
    /**
     * setId方法。
     *      * @param id String类型参数
     */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * 获取节点名称。
         *
         * @return 节点名称
         */
    /**
     * getName方法。
     * @return String类型返回值
     */
        public String getName() {
            return name;
        }

        /**
         * 设置节点名称。
         *
         * @param name 节点名称
         */
    /**
     * setName方法。
     *      * @param name String类型参数
     */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 获取节点类型。
         *
         * @return 节点类型，如 "startEvent"、"userTask"、"exclusiveGateway" 等
         */
    /**
     * getType方法。
     * @return String类型返回值
     */
        public String getType() {
            return type;
        }

        /**
         * 设置节点类型。
         *
         * @param type 节点类型
         */
    /**
     * setType方法。
     *      * @param type String类型参数
     */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * 获取节点的所有属性映射。
         *
         * @return 属性映射表
         */
    /**
     * getProperties方法。
     * @return Map<String, String>类型返回值
     */
        public Map<String, String> getProperties() {
            return properties;
        }

        /**
         * 设置节点的属性映射。
         *
         * @param properties 属性映射表
         */
    /**
     * setProperties方法。
     *      * @param properties MapString,类型参数
     */
        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        /**
         * 设置一个属性值。
         *
         * @param key   属性键
         * @param value 属性值
         */
    /**
     * setProperty方法。
     *      * @param key String类型参数
     * @param value String类型参数
     */
        public void setProperty(String key, String value) {
            this.properties.put(key, value);
        }

        /**
         * 获取指定键的属性值。
         *
         * @param key 属性键
         * @return 属性值，如果不存在则返回 null
         */
    /**
     * getProperty方法。
     *      * @param key String类型参数
     * @return String类型返回值
     */
        public String getProperty(String key) {
            return this.properties.get(key);
        }
    }

    /**
     * BPMN 序列流表示。
     * 表示 BPMN 2.0 中连接流程节点的有向边，包含源节点、目标节点和条件表达式等信息。
     */
    public static class BpmnSequenceFlow {
        private String id;
        private String name;
        private String sourceRef;
        private String targetRef;
        private String conditionExpression;
        private boolean isDefault;
        private Map<String, String> properties = new HashMap<>();

        /**
         * 默认构造函数。
         */
    /**
     * BpmnSequenceFlow方法。
     */
        public BpmnSequenceFlow() {
        }

        /**
         * 使用源节点和目标节点引用构造序列流。
         *
         * @param id        序列流唯一标识符
         * @param sourceRef 源节点 ID 引用
         * @param targetRef 目标节点 ID 引用
         */
    /**
     * BpmnSequenceFlow方法。
     *      * @param id String类型参数
     * @param sourceRef String类型参数
     * @param targetRef String类型参数
     */
        public BpmnSequenceFlow(String id, String sourceRef, String targetRef) {
            this.id = id;
            this.sourceRef = sourceRef;
            this.targetRef = targetRef;
        }

        /**
         * 获取序列流唯一标识符。
         *
         * @return 序列流 ID
         */
    /**
     * getId方法。
     * @return String类型返回值
     */
        public String getId() {
            return id;
        }

        /**
         * 设置序列流唯一标识符。
         *
         * @param id 序列流 ID
         */
    /**
     * setId方法。
     *      * @param id String类型参数
     */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * 获取序列流名称。
         *
         * @return 序列流名称
         */
    /**
     * getName方法。
     * @return String类型返回值
     */
        public String getName() {
            return name;
        }

        /**
         * 设置序列流名称。
         *
         * @param name 序列流名称
         */
    /**
     * setName方法。
     *      * @param name String类型参数
     */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 获取序列流源节点 ID 引用。
         *
         * @return 源节点 ID
         */
    /**
     * getSourceRef方法。
     * @return String类型返回值
     */
        public String getSourceRef() {
            return sourceRef;
        }

        /**
         * 设置序列流源节点 ID 引用。
         *
         * @param sourceRef 源节点 ID
         */
    /**
     * setSourceRef方法。
     *      * @param sourceRef String类型参数
     */
        public void setSourceRef(String sourceRef) {
            this.sourceRef = sourceRef;
        }

        /**
         * 获取序列流目标节点 ID 引用。
         *
         * @return 目标节点 ID
         */
    /**
     * getTargetRef方法。
     * @return String类型返回值
     */
        public String getTargetRef() {
            return targetRef;
        }

        /**
         * 设置序列流目标节点 ID 引用。
         *
         * @param targetRef 目标节点 ID
         */
    /**
     * setTargetRef方法。
     *      * @param targetRef String类型参数
     */
        public void setTargetRef(String targetRef) {
            this.targetRef = targetRef;
        }

        /**
         * 获取序列流条件表达式。
         * 用于网关节点的条件分支判断。
         *
         * @return 条件表达式字符串
         */
    /**
     * getConditionExpression方法。
     * @return String类型返回值
     */
        public String getConditionExpression() {
            return conditionExpression;
        }

        /**
         * 设置序列流条件表达式。
         *
         * @param conditionExpression 条件表达式
         */
    /**
     * setConditionExpression方法。
     *      * @param conditionExpression String类型参数
     */
        public void setConditionExpression(String conditionExpression) {
            this.conditionExpression = conditionExpression;
        }

        /**
         * 判断是否为默认流向。
         * 仅在排他网关中使用，当所有条件都不满足时走默认流向。
         *
         * @return 如果是默认流向返回 true
         */
    /**
     * isDefault方法。
     * @return boolean类型返回值
     */
        public boolean isDefault() {
            return isDefault;
        }

        /**
         * 设置是否为默认流向。
         *
         * @param isDefault 是否为默认流向
         */
    /**
     * setDefault方法。
     *      * @param isDefault boolean类型参数
     */
        public void setDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        /**
         * 获取序列流的所有属性映射。
         *
         * @return 属性映射表
         */
    /**
     * getProperties方法。
     * @return Map<String, String>类型返回值
     */
        public Map<String, String> getProperties() {
            return properties;
        }

        /**
         * 设置序列流的属性映射。
         *
         * @param properties 属性映射表
         */
    /**
     * setProperties方法。
     *      * @param properties MapString,类型参数
     */
        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        /**
         * 设置一个属性值。
         *
         * @param key   属性键
         * @param value 属性值
         */
    /**
     * setProperty方法。
     *      * @param key String类型参数
     * @param value String类型参数
     */
        public void setProperty(String key, String value) {
            this.properties.put(key, value);
        }

        /**
         * 获取指定键的属性值。
         *
         * @param key 属性键
         * @return 属性值，如果不存在则返回 null
         */
    /**
     * getProperty方法。
     *      * @param key String类型参数
     * @return String类型返回值
     */
        public String getProperty(String key) {
            return this.properties.get(key);
        }
    }
}