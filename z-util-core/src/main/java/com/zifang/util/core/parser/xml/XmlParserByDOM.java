package com.zifang.util.core.parser.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * W3C DOM 方式 XML 解析器示例
 */
public class XmlParserByDOM {

    /**
     * 解析 XML 文件并打印文档结构
     */
    public static void parse(InputStream xmlStream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlStream);
        document.getDocumentElement().normalize();
        printElement(document.getDocumentElement(), 0);
    }

    private static void printElement(Element element, int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        String prefix = sb.toString();
        System.out.println(prefix + "<" + element.getTagName() + ">");

        // 打印属性
        NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            System.out.println(prefix + "  @" + attr.getNodeName() + "=" + attr.getNodeValue());
        }

        // 打印子节点
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                printElement((Element) child, indent + 1);
            } else if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getNodeValue().trim();
                if (!text.isEmpty()) {
                    System.out.println(prefix + "  TEXT: " + text);
                }
            } else if (child.getNodeType() == Node.COMMENT_NODE) {
                System.out.println(prefix + "  <!-- -->");
            }
        }
    }
}
