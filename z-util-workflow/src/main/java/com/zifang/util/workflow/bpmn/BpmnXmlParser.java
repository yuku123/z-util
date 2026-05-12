package com.zifang.util.workflow.bpmn;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parser for BPMN 2.0 XML format.
 * Parses BPMN XML into BpmnDiagram model using standard Java DOM API.
 */
public class BpmnXmlParser {

    private static final String BPMN_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";

    /**
     * Parse BPMN XML content into BpmnDiagram
     *
     * @param xmlContent BPMN XML string
     * @return BpmnDiagram model
     */
    public BpmnDiagram parse(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlContent.getBytes("UTF-8")));
            return parseDocument(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse BPMN XML", e);
        }
    }

    private BpmnDiagram parseDocument(Document document) {
        BpmnDiagram diagram = new BpmnDiagram();

        Element definitions = document.getDocumentElement();

        // Find process element
        NodeList processList = definitions.getElementsByTagNameNS(BPMN_NS, "process");
        if (processList.getLength() == 0) {
            // Try without namespace for compatibility
            processList = definitions.getElementsByTagName("process");
        }

        if (processList.getLength() > 0) {
            Element processElement = (Element) processList.item(0);
            diagram.setId(processElement.getAttribute("id"));
            diagram.setName(processElement.getAttribute("name"));

            parseProcessElements(processElement, diagram);
        }

        return diagram;
    }

    private void parseProcessElements(Element processElement, BpmnDiagram diagram) {
        // Parse start events
        parseNodes(processElement, "startEvent", "startEvent", diagram);

        // Parse end events
        parseNodes(processElement, "endEvent", "endEvent", diagram);

        // Parse user tasks
        parseNodes(processElement, "userTask", "userTask", diagram);

        // Parse service tasks
        parseNodes(processElement, "serviceTask", "serviceTask", diagram);

        // Parse exclusive gateways
        parseNodes(processElement, "exclusiveGateway", "exclusiveGateway", diagram);

        // Parse parallel gateways
        parseNodes(processElement, "parallelGateway", "parallelGateway", diagram);

        // Parse sequence flows
        parseSequenceFlows(processElement, diagram);
    }

    private void parseNodes(Element parentElement, String elementTag, String type, BpmnDiagram diagram) {
        NodeList nodeList = parentElement.getElementsByTagNameNS(BPMN_NS, elementTag);
        if (nodeList.getLength() == 0) {
            nodeList = parentElement.getElementsByTagName(elementTag);
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String id = element.getAttribute("id");
            String name = element.getAttribute("name");

            BpmnDiagram.BpmnNode node = new BpmnDiagram.BpmnNode(id, name, type);

            // Parse documentation if present
            NodeList docList = element.getElementsByTagNameNS(BPMN_NS, "documentation");
            if (docList.getLength() == 0) {
                docList = element.getElementsByTagName("documentation");
            }
            if (docList.getLength() > 0) {
                String docText = docList.item(0).getTextContent();
                if (docText != null && !docText.isEmpty()) {
                    node.setProperty("documentation", docText);
                }
            }

            diagram.addNode(node);
        }
    }

    private void parseSequenceFlows(Element parentElement, BpmnDiagram diagram) {
        NodeList flowList = parentElement.getElementsByTagNameNS(BPMN_NS, "sequenceFlow");
        if (flowList.getLength() == 0) {
            flowList = parentElement.getElementsByTagName("sequenceFlow");
        }

        for (int i = 0; i < flowList.getLength(); i++) {
            Element element = (Element) flowList.item(i);
            String id = element.getAttribute("id");
            String sourceRef = element.getAttribute("sourceRef");
            String targetRef = element.getAttribute("targetRef");

            BpmnDiagram.BpmnSequenceFlow flow = new BpmnDiagram.BpmnSequenceFlow(id, sourceRef, targetRef);

            // Parse condition expression
            NodeList conditionList = element.getElementsByTagNameNS(BPMN_NS, "conditionExpression");
            if (conditionList.getLength() == 0) {
                conditionList = element.getElementsByTagName("conditionExpression");
            }
            if (conditionList.getLength() > 0) {
                String condition = conditionList.item(0).getTextContent();
                if (condition != null && !condition.isEmpty()) {
                    flow.setConditionExpression(condition);
                }
            }

            diagram.addSequenceFlow(flow);
        }
    }
}