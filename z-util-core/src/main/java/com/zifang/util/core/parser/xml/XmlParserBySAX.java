package com.zifang.util.core.parser.xml;

import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

/**
 * SAX 方式 XML 解析器示例
 */
public class XmlParserBySAX {

    /**
     * 使用 SAX 解析器解析 XML 并打印结构
     */
    public static void parse(InputStream xmlStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(xmlStream, new MyHandler());
    }

    private static class MyHandler extends DefaultHandler implements LexicalHandler {

        private StringBuilder currentText = new StringBuilder();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            System.out.println("START: " + qName);
            for (int i = 0; i < attributes.getLength(); i++) {
                System.out.println("  @" + attributes.getQName(i) + " = " + attributes.getValue(i));
            }
            currentText.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            currentText.append(new String(ch, start, length));
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String text = currentText.toString().trim();
            if (!text.isEmpty()) {
                System.out.println("  TEXT: " + text);
            }
            System.out.println("END: " + qName);
            currentText.setLength(0);
        }

        // LexicalHandler 实现（为空，SAX2 已够用）
        @Override
        public void startDTD(String name, String publicId, String systemId) {
        }

        @Override
        public void endDTD() {
        }

        @Override
        public void startEntity(String name) {
        }

        @Override
        public void endEntity(String name) {
        }

        @Override
        public void startCDATA() {
        }

        @Override
        public void endCDATA() {
        }

        @Override
        public void comment(char[] ch, int start, int length) {
        }
    }
}
