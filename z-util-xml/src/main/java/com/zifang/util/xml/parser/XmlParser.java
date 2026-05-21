package com.zifang.util.xml.parser;

import com.zifang.util.xml.exception.XmlParseException;
import com.zifang.util.xml.model.*;
import com.zifang.util.xml.tokenizer.Token;
import com.zifang.util.xml.tokenizer.TokenList;
import com.zifang.util.xml.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML 递归下降解析器，将 Token 序列构建为 XDocument。
 * <p>
 * 解析规则:
 * <pre>
 * Document  := Prolog Content Misc*
 * Prolog    := XMLDecl? Misc*
 * XMLDecl   := '<?xml' VersionInfo ('encoding' '=' Encoded)? ('standalone' '=' Text)? '?>'
 * Content   := Element | CharData | CDSect | Comment | PI | Misc
 * Element   := EmptyElemTag | STag Content ETag
 * STag      := '<' Name (Attribute)* '>'
 * ETag      := '</' Name '>'
 * EmptyElemTag := '<' Name (Attribute)* '/>'
 * Attribute := Name '=' AttValue
 * </pre>
 *
 * @author zifang
 */
public class XmlParser {

    private static final int TAG_OPEN_BIT = 1;
    private static final int TAG_END_BIT = 2;
    private static final int TAG_SELF_CLOSE_BIT = 4;
    private static final int TAG_CLOSE_BIT = 8;

    private TokenList tokens;

    // 命名空间上下文（简化版，不做完整 NS 解析）
    private String currentDefaultNamespace;

    public XDocument parse(TokenList tokens) {
        this.tokens = tokens;
        tokens.reset();
        return parseDocument();
    }

    private XDocument parseDocument() {
        XDocument doc = new XDocument();

        while (tokens.hasMore()) {
            Token token = tokens.peek();
            if (token == null) {
                break;
            }
            TokenType type = token.getTokenType();

            if (type == TokenType.DECLARATION) {
                doc.setDeclaration(parseDeclaration());
            } else if (type == TokenType.PROCESSING_INSTRUCTION) {
                doc.addPrependNode(parseProcessingInstruction());
            } else if (type == TokenType.COMMENT) {
                doc.addPrependNode(parseComment());
            } else if (type == TokenType.TEXT) {
                // 根元素前的孤立文本（不合法但容错）
                String text = tokens.next().getValue();
                if (text.trim().length() > 0) {
                    throw new XmlParseException("Text content is not allowed before root element: " + text.trim());
                }
            } else if (type == TokenType.TAG_OPEN) {
                doc.setRoot(parseElement());
                break;
            } else {
                throw new XmlParseException("Unexpected token before root element: " + token);
            }
        }

        // 处理根元素后的剩余 token（注释、处理指令）
        while (tokens.hasMore()) {
            Token token = tokens.peek();
            if (token == null) {
                break;
            }
            TokenType type = token.getTokenType();
            if (type == TokenType.COMMENT) {
                doc.addPrependNode(parseComment());
            } else if (type == TokenType.PROCESSING_INSTRUCTION) {
                doc.addPrependNode(parseProcessingInstruction());
            } else if (type == TokenType.END_DOCUMENT) {
                tokens.next();
                break;
            } else {
                throw new XmlParseException("Unexpected token after root element: " + token);
            }
        }

        return doc;
    }

    private XDeclaration parseDeclaration() {
        Token token = tokens.next();
        String raw = token.getValue(); // e.g., "xml version=\"1.0\" encoding=\"UTF-8\""
        XDeclaration decl = new XDeclaration();

        // 解析 version
        Pattern versionPattern = Pattern.compile("version\\s*=\\s*[\"']([^\"']+)[\"']");
        Matcher m = versionPattern.matcher(raw);
        if (m.find()) {
            decl.setVersion(m.group(1));
        }

        // 解析 encoding
        Pattern encodingPattern = Pattern.compile("encoding\\s*=\\s*[\"']([^\"']+)[\"']");
        m = encodingPattern.matcher(raw);
        if (m.find()) {
            decl.setEncoding(m.group(1));
        }

        // 解析 standalone
        Pattern standalonePattern = Pattern.compile("standalone\\s*=\\s*[\"']([^\"']+)[\"']");
        m = standalonePattern.matcher(raw);
        if (m.find()) {
            decl.setStandalone(m.group(1));
        }

        return decl;
    }

    private XProcessingInstruction parseProcessingInstruction() {
        Token token = tokens.next();
        String raw = token.getValue().trim();
        int spaceIdx = raw.indexOf(' ');
        String target;
        String data;
        if (spaceIdx > 0) {
            target = raw.substring(0, spaceIdx);
            data = raw.substring(spaceIdx + 1).trim();
        } else {
            target = raw;
            data = "";
        }
        return new XProcessingInstruction(target, data);
    }

    private XComment parseComment() {
        Token token = tokens.next();
        return new XComment(token.getValue());
    }

    private XElement parseElement() {
        String tagName = parseTagOpen();
        XElement element = new XElement(tagName);

        // 解析属性
        parseAttributes(element);

        // 检查是否为自闭合标签
        Token nextToken = tokens.peek();
        if (nextToken != null && nextToken.getTokenType() == TokenType.TAG_SELF_CLOSE) {
            tokens.next();
            return element;
        }

        // 消耗 TAG_CLOSE
        if (nextToken != null && nextToken.getTokenType() == TokenType.TAG_CLOSE) {
            tokens.next();
        }

        // 解析内容
        parseElementContent(element);

        return element;
    }

    private String parseTagOpen() {
        Token token = tokens.next(); // TAG_OPEN
        if (token.getTokenType() != TokenType.TAG_OPEN) {
            throw new XmlParseException("Expected TAG_OPEN, got: " + token);
        }
        return token.getValue();
    }

    private void parseAttributes(XElement element) {
        while (tokens.hasMore()) {
            Token peek = tokens.peek();
            if (peek == null) {
                break;
            }
            if (peek.getTokenType() == TokenType.ATTRIBUTE) {
                parseAttribute(element, tokens.next().getValue());
            } else if (peek.getTokenType() == TokenType.TAG_CLOSE
                    || peek.getTokenType() == TokenType.TAG_SELF_CLOSE) {
                break;
            } else {
                break;
            }
        }
    }

    private void parseAttribute(XElement element, String raw) {
        int eqIdx = raw.indexOf('=');
        if (eqIdx < 0) {
            // valueless attribute: e.g., disabled
            element.setAttribute(raw, "");
            return;
        }
        String name = raw.substring(0, eqIdx);
        String value = raw.substring(eqIdx + 1);
        element.setAttribute(name, value);
    }

    private void parseElementContent(XElement element) {
        while (tokens.hasMore()) {
            Token token = tokens.peek();
            if (token == null) {
                break;
            }
            TokenType type = token.getTokenType();

            if (type == TokenType.TAG_OPEN) {
                element.addChild(parseElement());
            } else if (type == TokenType.TAG_END) {
                // 检查标签匹配
                String endName = tokens.next().getValue();
                if (!endName.equals(element.getName())) {
                    throw new XmlParseException("Mismatched end tag: expected </" + element.getName()
                            + ">, got </" + endName + ">");
                }
                // 消耗对应的 TAG_CLOSE
                Token close = tokens.peek();
                if (close != null && close.getTokenType() == TokenType.TAG_CLOSE) {
                    tokens.next();
                }
                return;
            } else if (type == TokenType.TAG_SELF_CLOSE) {
                // Should not happen here (handled in parseElement)
                tokens.next();
                return;
            } else if (type == TokenType.TEXT) {
                String text = tokens.next().getValue();
                // 规范化空白
                if (text.trim().isEmpty()) {
                    // 保留空白文本（用于格式美化）
                    element.getChildren().add(new XText(text));
                } else {
                    element.addChild(new XText(text));
                }
            } else if (type == TokenType.CDATA) {
                element.addChild(new XCData(tokens.next().getValue()));
            } else if (type == TokenType.COMMENT) {
                element.addChild(parseComment());
            } else if (type == TokenType.PROCESSING_INSTRUCTION) {
                element.addChild(parseProcessingInstruction());
            } else if (type == TokenType.END_DOCUMENT) {
                throw new XmlParseException("Unclosed element: </" + element.getName() + ">");
            } else {
                throw new XmlParseException("Unexpected token in element content: " + token);
            }
        }
        throw new XmlParseException("Unclosed element: </" + element.getName() + ">");
    }
}
