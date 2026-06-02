package com.zifang.util.json;

import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.token.Token;
import com.zifang.util.json.exception.JsonParseException;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 G4 DSL 的 JSON 解析器。
 * <p>
 * 使用 DynamicLexer + DynamicParser 加载 JsonLexer.g4 + JsonParser.g4，
 * 无任何第三方依赖。G4文件位于 src/main/resources/。
 * <p>
 * 对标 com.zifang.util.json.JSONParser，提供 fromJSON(String) 入口。
 */
public class JSONParser {

    private static final String LEXER_G4 = "JsonLexer.g4";
    private static final String PARSER_G4 = "JsonParser.g4";

    /**
     * 将 JSON 字符串解析为 Java 对象（JsonObject / JsonArray / 标量）。
     *
     * @param json JSON 字符串
     * @return JsonObject、JsonArray 或标量值（String/Number/Boolean/null）
     * @throws JsonParseException 如果格式非法
     */
    public Object fromJSON(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new JsonObject();
        }
        try {
            String lexerG4 = loadG4(LEXER_G4);
            String parserG4 = loadG4(PARSER_G4);

            DynamicLexer lexer = new DynamicLexer();
            lexer.loadG4(lexerG4);
            lexer.setInput(json);
            TokenReader tokenReader = lexer.getTokenReader();

            DynamicParser parser = new DynamicParser();
            parser.loadG4(parserG4);
            parser.setTokenReader(tokenReader);
            ASTNode ast = parser.parse("json");

            return astToJava(ast);
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException("JSON解析失败: " + e.getMessage());
        }
    }

    /**
     * 将 JSON 字符串解析为 JsonObject。
     *
     * @param json JSON 字符串
     * @return JsonObject
     * @throws JsonParseException 如果根节点不是对象
     */
    public JsonObject parseObject(String json) {
        Object result = fromJSON(json);
        if (result instanceof JsonObject) return (JsonObject) result;
        if (result instanceof JsonArray) {
            throw new JsonParseException("JSON is an array, not an object");
        }
        throw new JsonParseException("Unexpected root type: " + result.getClass().getSimpleName());
    }

    /**
     * 将 JSON 字符串解析为 JsonArray。
     *
     * @param json JSON 字符串
     * @return JsonArray
     * @throws JsonParseException 如果根节点不是数组
     */
    public JsonArray parseArray(String json) {
        Object result = fromJSON(json);
        if (result instanceof JsonArray) return (JsonArray) result;
        if (result instanceof JsonObject) {
            throw new JsonParseException("JSON is an object, not an array");
        }
        throw new JsonParseException("Unexpected root type: " + result.getClass().getSimpleName());
    }

    // ==================== G4 加载 ====================

    private String loadG4(String name) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(name)) {
            if (is == null) throw new JsonParseException("G4文件未找到: " + name);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException("读取G4文件失败: " + name + ", " + e.getMessage());
        }
    }

    // ==================== AST → Java 对象 ====================

    private Object astToJava(ASTNode node) {
        if (node == null) return null;
        String type = node.getType();
        String text = node.getText();
        List<ASTNode> children = node.getChildren();

        // 叶子节点：直接解析标量
        if (children.isEmpty()) {
            return parseScalar(type, text);
        }

        switch (type) {
            case "json":
            case "value":
                // value 层只有一个子节点
                return children.isEmpty() ? null : astToJava(children.get(0));
            case "object":
                return astToObject(node);
            case "array":
                return astToArray(node);
            default:
                // 未知节点类型，尝试单子节点或标量
                if (children.size() == 1) return astToJava(children.get(0));
                if (text != null && !text.isEmpty()) return parseScalar(type, text);
                return children.stream().map(this::astToJava).collect(Collectors.toList());
        }
    }

    private JsonObject astToObject(ASTNode node) {
        JsonObject obj = new JsonObject();
        for (ASTNode child : node.getChildren()) {
            String childType = child.getType();
            if ("pair".equals(childType)) {
                // pair 有两个子节点：key (string) 和 value
                List<ASTNode> pairChildren = child.getChildren();
                Object key = null;
                Object value = null;
                for (ASTNode pairChild : pairChildren) {
                    String pt = pairChild.getType();
                    if ("string".equals(pt)) {
                        // string 节点下有 StringLiteral terminal
                        key = extractStringValue(pairChild);
                    } else if ("value".equals(pt)) {
                        value = astToJava(pairChild);
                    }
                }
                if (key != null) {
                    obj.put(String.valueOf(key), value);
                }
            }
        }
        return obj;
    }

    private JsonArray astToArray(ASTNode node) {
        JsonArray arr = new JsonArray();
        for (ASTNode child : node.getChildren()) {
            String childType = child.getType();
            if ("value".equals(childType)) {
                arr.add(astToJava(child));
            }
        }
        return arr;
    }

    private String extractStringValue(ASTNode stringNode) {
        List<ASTNode> children = stringNode.getChildren();
        for (ASTNode child : children) {
            if ("StringLiteral".equals(child.getType())) {
                String t = child.getText();
                if (t != null && t.length() >= 2) {
                    // 去掉首尾引号
                    return t.substring(1, t.length() - 1);
                }
            }
        }
        // fallback：直接用 text
        String t = stringNode.getText();
        if (t != null && t.length() >= 2) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }

    private Object parseScalar(String type, String text) {
        if (text == null || text.trim().isEmpty()) return null;
        text = text.trim();

        switch (type) {
            case "null":
                return null;
            case "bool":
                if ("true".equals(text)) return Boolean.TRUE;
                if ("false".equals(text)) return Boolean.FALSE;
                break;
            case "number":
                if (text.contains(".") || text.contains("e") || text.contains("E")) {
                    try { return Double.parseDouble(text); } catch (NumberFormatException ignored) {}
                }
                try {
                    long v = Long.parseLong(text);
                    if (v >= Integer.MIN_VALUE && v <= Integer.MAX_VALUE) {
                        return (int) v;
                    }
                    return v;
                } catch (NumberFormatException ignored) {}
                break;
            case "string":
            case "StringLiteral":
                // 去掉引号
                if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
                    return text.substring(1, text.length() - 1);
                }
                return text;
            case "terminal":
                // 处理各种终结符
                if ("null".equals(text)) return null;
                if ("true".equals(text)) return Boolean.TRUE;
                if ("false".equals(text)) return Boolean.FALSE;
                return text;
            default:
                break;
        }
        return text;
    }
}