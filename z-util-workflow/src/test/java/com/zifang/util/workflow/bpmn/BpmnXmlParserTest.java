package com.zifang.util.workflow.bpmn;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * BpmnXmlParser 类测试
 */
public class BpmnXmlParserTest {

    private BpmnXmlParser parser = new BpmnXmlParser();

    @Test
    public void testParseSimpleBpmnWithStartAndEndEvents() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\" isExecutable=\"true\" name=\"Simple Process\">\n" +
                "    <startEvent id=\"StartEvent_1\" name=\"Start\"/>\n" +
                "    <endEvent id=\"EndEvent_1\" name=\"End\"/>\n" +
                "  </process>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram);
        assertEquals("Process_1", diagram.getId());
        assertEquals("Simple Process", diagram.getName());
        assertFalse(diagram.getNodes().isEmpty());
    }

    @Test
    public void testParseBpmnWithUserTask() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\">\n" +
                "    <startEvent id=\"StartEvent_1\"/>\n" +
                "    <userTask id=\"Task_1\" name=\"Review Application\"/>\n" +
                "    <endEvent id=\"EndEvent_1\"/>\n" +
                "  </process>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram);
        assertEquals(3, diagram.getNodes().size());

        boolean foundTask = false;
        for (BpmnDiagram.BpmnNode node : diagram.getNodes()) {
            if ("Task_1".equals(node.getId()) && "userTask".equals(node.getType())) {
                foundTask = true;
                assertEquals("Review Application", node.getName());
            }
        }
        assertTrue("Should find userTask node", foundTask);
    }

    @Test
    public void testParseBpmnWithExclusiveGateway() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\">\n" +
                "    <startEvent id=\"StartEvent_1\"/>\n" +
                "    <exclusiveGateway id=\"Gateway_1\" name=\"Decision Gateway\"/>\n" +
                "    <endEvent id=\"EndEvent_1\"/>\n" +
                "  </process>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram);

        boolean foundGateway = false;
        for (BpmnDiagram.BpmnNode node : diagram.getNodes()) {
            if ("Gateway_1".equals(node.getId()) && "exclusiveGateway".equals(node.getType())) {
                foundGateway = true;
                assertEquals("Decision Gateway", node.getName());
            }
        }
        assertTrue("Should find exclusiveGateway node", foundGateway);
    }

    @Test
    public void testParseBpmnWithSequenceFlow() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\">\n" +
                "    <startEvent id=\"StartEvent_1\"/>\n" +
                "    <endEvent id=\"EndEvent_1\"/>\n" +
                "    <sequenceFlow id=\"Flow_1\" sourceRef=\"StartEvent_1\" targetRef=\"EndEvent_1\"/>\n" +
                "  </process>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram);
        assertFalse("Should have sequence flows", diagram.getSequenceFlows().isEmpty());

        BpmnDiagram.BpmnSequenceFlow flow = diagram.getSequenceFlows().get(0);
        assertEquals("Flow_1", flow.getId());
        assertEquals("StartEvent_1", flow.getSourceRef());
        assertEquals("EndEvent_1", flow.getTargetRef());
    }

    @Test
    public void testParseBpmnWithConditionExpression() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\">\n" +
                "    <startEvent id=\"StartEvent_1\"/>\n" +
                "    <endEvent id=\"EndEvent_1\"/>\n" +
                "    <sequenceFlow id=\"Flow_1\" sourceRef=\"StartEvent_1\" targetRef=\"EndEvent_1\">\n" +
                "      <conditionExpression>x > 10</conditionExpression>\n" +
                "    </sequenceFlow>\n" +
                "  </process>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram);
        assertEquals(1, diagram.getSequenceFlows().size());

        BpmnDiagram.BpmnSequenceFlow flow = diagram.getSequenceFlows().get(0);
        assertEquals("x > 10", flow.getConditionExpression());
    }

    @Test
    public void testParseEmptyProcess() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\"/>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram);
        assertEquals("Process_1", diagram.getId());
        assertTrue(diagram.getNodes().isEmpty());
        assertTrue(diagram.getSequenceFlows().isEmpty());
    }

    @Test
    public void testParseWithDocumentation() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\">\n" +
                "    <startEvent id=\"StartEvent_1\">\n" +
                "      <documentation>This is the start event</documentation>\n" +
                "    </startEvent>\n" +
                "  </process>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram);
        assertEquals(1, diagram.getNodes().size());

        BpmnDiagram.BpmnNode node = diagram.getNodes().get(0);
        assertEquals("This is the start event", node.getProperty("documentation"));
    }

    @Test(expected = RuntimeException.class)
    public void testParseInvalidXml() {
        String invalidXml = "not valid xml at all";
        parser.parse(invalidXml);
    }

    @Test
    public void testBpmnDiagramNodeMap() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" " +
                "targetNamespace=\"http://bpmn.io/schema/bpmn\">\n" +
                "  <process id=\"Process_1\">\n" +
                "    <startEvent id=\"StartEvent_1\"/>\n" +
                "    <endEvent id=\"EndEvent_1\"/>\n" +
                "  </process>\n" +
                "</definitions>";

        BpmnDiagram diagram = parser.parse(xml);

        assertNotNull(diagram.getNodeMap());
        assertEquals(2, diagram.getNodeMap().size());
        assertNotNull(diagram.getNodeMap().get("StartEvent_1"));
        assertNotNull(diagram.getNodeMap().get("EndEvent_1"));
    }

    @Test
    public void testBpmnNodeProperties() {
        BpmnDiagram.BpmnNode node = new BpmnDiagram.BpmnNode();
        node.setId("node1");
        node.setName("Test Node");
        node.setType("userTask");
        node.setProperty("key1", "value1");
        node.setProperty("key2", "value2");

        assertEquals("node1", node.getId());
        assertEquals("Test Node", node.getName());
        assertEquals("userTask", node.getType());
        assertEquals("value1", node.getProperty("key1"));
        assertEquals("value2", node.getProperty("key2"));
        assertEquals(2, node.getProperties().size());
    }

    @Test
    public void testBpmnSequenceFlowProperties() {
        BpmnDiagram.BpmnSequenceFlow flow = new BpmnDiagram.BpmnSequenceFlow();
        flow.setId("flow1");
        flow.setSourceRef("node1");
        flow.setTargetRef("node2");
        flow.setConditionExpression("condition");

        assertEquals("flow1", flow.getId());
        assertEquals("node1", flow.getSourceRef());
        assertEquals("node2", flow.getTargetRef());
        assertEquals("condition", flow.getConditionExpression());
    }

    @Test
    public void testBpmnDiagramGettersAndSetters() {
        BpmnDiagram diagram = new BpmnDiagram();
        diagram.setId("diagram1");
        diagram.setName("Test Diagram");

        assertEquals("diagram1", diagram.getId());
        assertEquals("Test Diagram", diagram.getName());
    }

    @Test
    public void testBpmnDiagramConstructorWithIdAndName() {
        BpmnDiagram diagram = new BpmnDiagram("id123", "My Process");
        assertEquals("id123", diagram.getId());
        assertEquals("My Process", diagram.getName());
    }

    @Test
    public void testAddNodeAndSequenceFlow() {
        BpmnDiagram diagram = new BpmnDiagram();

        BpmnDiagram.BpmnNode node1 = new BpmnDiagram.BpmnNode("node1", "Start", "startEvent");
        BpmnDiagram.BpmnNode node2 = new BpmnDiagram.BpmnNode("node2", "End", "endEvent");
        diagram.addNode(node1);
        diagram.addNode(node2);

        assertEquals(2, diagram.getNodes().size());
        assertEquals(2, diagram.getNodeMap().size());

        BpmnDiagram.BpmnSequenceFlow flow = new BpmnDiagram.BpmnSequenceFlow("flow1", "node1", "node2");
        diagram.addSequenceFlow(flow);

        assertEquals(1, diagram.getSequenceFlows().size());
    }
}
