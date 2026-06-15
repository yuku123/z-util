package com.zifang.util.parser.proto;

import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

/**
 * 基于 G4 DSL 的 Proto3 解析器。
 * <p>
 * 使用 DynamicLexer + DynamicParser 加载 ProtoLexer.g4 + ProtoParser.g4，
 * 无任何第三方依赖。
 * <p>
 * 支持 proto3 子集：syntax、package、import、message（含 nested message/enum/field）、
 * enum、service、rpc（含 stream）。
 */
/**
 * ProtoG4Parser类。
 */
public class ProtoG4Parser {

    private static final String LEXER_G4 = "ProtoLexer.g4";
    private static final String PARSER_G4 = "ProtoParser.g4";

    /**
     * 解析 Proto 字符串。
     */
    /**
     * parse方法。
     *      * @param content String类型参数
     * @return ProtoDocument类型返回值
     */
    public ProtoDocument parse(String content) {
        return parse(new StringReader(content));
    }

    /**
     * 解析 Proto Reader。
     */
    /**
     * parse方法。
     *      * @param reader Reader类型参数
     * @return ProtoDocument类型返回值
     */
    public ProtoDocument parse(Reader reader) {
        try {
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[4096];
            int n;
            while ((n = reader.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
            return parseString(sb.toString());
        } catch (IOException e) {
            throw new ProtoException("读取 Proto 失败", e);
        }
    }

    private ProtoDocument parseString(String content) {
        ProtoDocument doc = new ProtoDocument();
        if (content == null || content.trim().isEmpty()) return doc;

        try {
            String lexerG4 = loadG4(LEXER_G4);
            String parserG4 = loadG4(PARSER_G4);

            DynamicLexer lexer = new DynamicLexer();
            lexer.loadG4(lexerG4);
            lexer.setInput(content);
            TokenReader tokenReader = lexer.getTokenReader();

            DynamicParser parser = new DynamicParser();
            parser.loadG4(parserG4);
            parser.setTokenReader(tokenReader);
            ASTNode ast = parser.parse("file");

            for (ASTNode top : ast.getChildren()) {
                if (!"topDecl".equals(top.getType())) continue;
                ASTNode decl = top.getChildren().isEmpty() ? null : top.getChildren().get(0);
                if (decl == null) continue;
                String type = decl.getType();
                switch (type) {
                    case "syntaxDecl":
                        doc.setSyntax(extractStringLiteral(decl));
                        break;
                    case "packageDecl":
                        doc.setPackageName(extractDottedName(decl));
                        break;
                    case "importDecl":
                        doc.getImports().add(extractStringLiteral(decl));
                        break;
                    case "messageDecl":
                        doc.getMessages().add(parseMessage(decl));
                        break;
                    case "serviceDecl":
                        doc.getServices().add(parseService(decl));
                        break;
                    // top-level enum 暂不放入 ProtoDocument（proto3 不支持顶层 enum，仅 message 内嵌）
                    default:
                        break;
                }
            }
            return doc;
        } catch (ProtoException e) {
            throw e;
        } catch (Exception e) {
            throw new ProtoException("G4 Proto解析失败: " + e.getMessage(), e);
        }
    }

    // ==================== G4 加载 ====================

    private String loadG4(String name) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(name);
            if (is == null) throw new ProtoException("G4文件未找到: " + name);
            byte[] bytes = is.readAllBytes();
            is.close();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (ProtoException e) {
            throw e;
        } catch (Exception e) {
            throw new ProtoException("读取G4文件失败: " + name, e);
        }
    }

    // ==================== AST → Proto 模型 ====================

    private ProtoMessage parseMessage(ASTNode messageDecl) {
        String name = findIdentifierText(messageDecl);
        ProtoMessage message = new ProtoMessage(name);
        ASTNode body = findChildByType(messageDecl, "messageBody");
        if (body == null) return message;
        for (ASTNode item : body.getChildren()) {
            if (!"messageBodyItem".equals(item.getType())) continue;
            ASTNode inner = item.getChildren().isEmpty() ? null : item.getChildren().get(0);
            if (inner == null) continue;
            String t = inner.getType();
            if ("fieldDecl".equals(t)) {
                message.addField(parseField(inner));
            } else if ("messageDecl".equals(t)) {
                message.getMessages().add(parseMessage(inner));
            } else if ("enumDecl".equals(t)) {
                message.getEnums().add(parseEnum(inner));
            }
        }
        return message;
    }

    private ProtoEnum parseEnum(ASTNode enumDecl) {
        String name = findIdentifierText(enumDecl);
        ProtoEnum protoEnum = new ProtoEnum(name);
        ASTNode body = findChildByType(enumDecl, "enumBody");
        if (body == null) return protoEnum;
        for (ASTNode item : body.getChildren()) {
            if (!"enumField".equals(item.getType())) continue;
            String enumName = null;
            int value = 0;
            for (ASTNode sub : item.getChildren()) {
                if ("IDENTIFIER".equals(sub.getType())) {
                    enumName = sub.getText();
                } else if ("INT_LITERAL".equals(sub.getType())) {
                    try { value = Integer.parseInt(sub.getText()); } catch (NumberFormatException ignored) {}
                }
            }
            if (enumName != null) protoEnum.addValue(enumName, value);
        }
        return protoEnum;
    }

    private ProtoService parseService(ASTNode serviceDecl) {
        String name = findIdentifierText(serviceDecl);
        ProtoService service = new ProtoService(name);
        ASTNode body = findChildByType(serviceDecl, "serviceBody");
        if (body == null) return service;
        for (ASTNode item : body.getChildren()) {
            if (!"rpcDecl".equals(item.getType())) continue;
            String rpcName = null;
            String inputType = null;
            String outputType = null;
            // 收集所有 IDENTIFIER 节点，按顺序：name, inputType, outputType
            // 但要跳过 STREAM 关键字
            java.util.List<String> idents = new java.util.ArrayList<>();
            boolean afterReturns = false;
            for (ASTNode sub : item.getChildren()) {
                String st = sub.getType();
                if ("IDENTIFIER".equals(st)) {
                    idents.add(sub.getText());
                }
            }
            // 第一个 IDENTIFIER 是 rpc name，后两个是 input/output
            if (idents.size() >= 1) rpcName = idents.get(0);
            if (idents.size() >= 2) inputType = idents.get(1);
            if (idents.size() >= 3) outputType = idents.get(2);
            if (rpcName != null) {
                service.addRpc(new ProtoRpc(rpcName, inputType, outputType));
            }
        }
        return service;
    }

    private ProtoField parseField(ASTNode fieldDecl) {
        boolean repeated = false;
        String type = null;
        String name = null;
        int tag = 0;
        for (ASTNode sub : fieldDecl.getChildren()) {
            String st = sub.getType();
            if ("REPEATED".equals(st)) repeated = true;
            else if ("OPTIONAL".equals(st) || "REQUIRED".equals(st)) { /* 修饰符，忽略 */ }
            else if ("type".equals(st)) {
                type = extractTypeText(sub);
            } else if ("IDENTIFIER".equals(st)) {
                name = sub.getText();
            } else if ("INT_LITERAL".equals(st)) {
                try { tag = Integer.parseInt(sub.getText()); } catch (NumberFormatException ignored) {}
            }
        }
        if (type == null) type = "";
        if (name == null) name = "";
        return new ProtoField(type, name, tag, repeated);
    }

    // ==================== 工具方法 ====================

    private ASTNode findChildByType(ASTNode node, String type) {
        if (node == null) return null;
        for (ASTNode c : node.getChildren()) {
            if (type.equals(c.getType())) return c;
        }
        return null;
    }

    private String findIdentifierText(ASTNode decl) {
        for (ASTNode c : decl.getChildren()) {
            if ("IDENTIFIER".equals(c.getType())) return c.getText();
        }
        return "";
    }

    private String extractStringLiteral(ASTNode decl) {
        for (ASTNode c : decl.getChildren()) {
            if ("STRING_LITERAL".equals(c.getType())) {
                String t = c.getText();
                if (t != null && t.length() >= 2) {
                    return t.substring(1, t.length() - 1);
                }
                return t;
            }
        }
        return "";
    }

    private String extractDottedName(ASTNode packageDecl) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ASTNode c : packageDecl.getChildren()) {
            if ("IDENTIFIER".equals(c.getType())) {
                if (!first) sb.append('.');
                sb.append(c.getText());
                first = false;
            }
        }
        return sb.toString();
    }

    private String extractTypeText(ASTNode typeNode) {
        StringBuilder sb = new StringBuilder();
        for (ASTNode c : typeNode.getChildren()) {
            String t = c.getType();
            if ("IDENTIFIER".equals(t) || "scalarType".equals(t)) {
                String text = c.getText();
                if (text != null && !text.isEmpty()) {
                    if (sb.length() > 0) sb.append('.');
                    sb.append(text);
                } else {
                    // scalarType 内部还有子节点
                    for (ASTNode sub : c.getChildren()) {
                        if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '.') sb.append('.');
                        sb.append(sub.getText());
                    }
                }
            } else if ("DOT".equals(t)) {
                // 跳过，G4 会处理
            }
        }
        return sb.toString();
    }
}
