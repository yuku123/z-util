package com.zifang.util.yaml;

import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.token.Token;
import com.zifang.util.yaml.exception.YamlParseException;
import com.zifang.util.yaml.model.YamlArray;
import com.zifang.util.yaml.model.YamlMap;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 基于 G4 DSL 的 YAML 解析器。
 * 使用 DynamicLexer + DynamicParser 加载 YamlLexer.g4 + YamlParser.g4，
 * 无任何第三方依赖。
 */
public class YamlG4Parser {

    private static final String LEXER_G4 = "YamlLexer.g4";
    private static final String PARSER_G4 = "YamlParser.g4";

    /**
     * 将 YAML 字符串解析为 Java 对象（Map/List/标量）。
     */
    public Object parse(String yaml) {
        try {
            String lexerG4 = loadG4(LEXER_G4);
            String parserG4 = loadG4(PARSER_G4);

            // 调试：打印加载的规则数（不抛异常，让 DynamicParser 自己报错）
            int lexerRules = countLexerRules(lexerG4);
            int parserRules = countParserRules(parserG4);
            // 不在这里抛异常，让后续流程报错，信息更丰富

            DynamicLexer lexer = new DynamicLexer();
            lexer.loadG4(lexerG4);
            lexer.setInput(yaml);
            TokenReader tokenReader = lexer.getTokenReader();

            DynamicParser parser = new DynamicParser();
            parser.loadG4(parserG4);
            parser.setTokenReader(tokenReader);
            // 显式指定起始规则为 "yaml"，避免 hashMap 迭代顺序不确定导致
            // 第一个被遍历到的规则不一定是入口规则
            ASTNode ast = parser.parse("yaml");

            return astToJava(ast);

        } catch (YamlParseException e) {
            throw e;
        } catch (Exception e) {
            throw new YamlParseException("YAML解析失败: " + e.getMessage(), e);
        }
    }

    public YamlMap parseMap(String yaml) {
        Object result = parse(yaml);
        if (result instanceof YamlMap) return (YamlMap) result;
        throw new YamlParseException("根节点不是映射类型");
    }

    public YamlArray parseArray(String yaml) {
        Object result = parse(yaml);
        if (result instanceof YamlArray) return (YamlArray) result;
        throw new YamlParseException("根节点不是序列类型");
    }

    // 调试：计算 lexer 规则数
    private int countLexerRules(String g4) {
        int count = 0;
        boolean inLexer = false;
        for (String line : g4.split("\n")) {
            line = line.trim();
            if (line.startsWith("lexer grammar")) { inLexer = true; continue; }
            if (line.startsWith("parser grammar")) { inLexer = false; continue; }
            if (line.startsWith("options") || line.startsWith("//") || line.isEmpty()) continue;
            if (inLexer && line.contains(":")) count++;
        }
        return count;
    }

    // 调试：计算 parser 规则数
    private int countParserRules(String g4) {
        int count = 0;
        boolean inParser = false;
        for (String line : g4.split("\n")) {
            line = line.trim();
            if (line.startsWith("parser grammar")) { inParser = true; continue; }
            if (line.startsWith("lexer grammar")) { inParser = false; continue; }
            if (line.startsWith("options") || line.startsWith("//") || line.isEmpty()) continue;
            if (inParser && line.contains(":")) count++;
        }
        return count;
    }

    // ==================== G4 加载 ====================

    private String loadG4(String name) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(name)) {
            if (is == null) throw new YamlParseException("G4文件未找到: " + name);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (YamlParseException e) {
            throw e;
        } catch (Exception e) {
            throw new YamlParseException("读取G4文件失败: " + name, e);
        }
    }

    // ==================== AST → Java 对象 ====================

    private Object astToJava(ASTNode node) {
        if (node == null) {
            System.err.println("[DBG-YAML] astToJava got null node");
            return null;
        }
        String type = node.getType();
        String text = node.getText();
        List<ASTNode> children = node.getChildren();
        if (children == null) {
            System.err.println("[DBG-YAML] children is null for type=" + type);
            return null;
        }
        if (type == null) {
            System.err.println("[DBG-YAML] type is null, children=" + children.size() + " text='" + text + "'");
            return null;
        }
        if (type.equals("yaml")) {
            System.err.println("[DBG-YAML] yaml type, children=" + children.size() + " text='" + text + "'");
            for (ASTNode c : children) {
                System.err.println("  child: type=" + c.getType() + " text='" + c.getText() + "' children=" + c.getChildren().size());
            }
        }

        if (children.isEmpty() && (text == null || text.isEmpty())) return null;
        if (children.isEmpty()) return parseScalar(type, text);

        switch (type) {
            case "yaml":
            case "document":
                return !children.isEmpty() ? astToJava(children.get(0)) : null;
            case "blockMap":
            case "map":
                return astToMap(node);
            case "blockSeq":
            case "seq":
                return astToSeq(node);
            case "blockScalar":
                return text != null ? text.trim() : null;
            case "pair":
            case "mapPair":
                return !children.isEmpty() ? astToJava(children.get(0)) : null;
            default:
                if (children.size() == 1) return astToJava(children.get(0));
                if (text != null && !text.isEmpty()) return parseScalar(type, text);
                return children.stream().map(this::astToJava).toList();
        }
    }

    private YamlMap astToMap(ASTNode node) {
        YamlMap map = new YamlMap();
        for (ASTNode child : node.getChildren()) {
            Object key = extractKey(child);
            Object val = extractValue(child);
            if (key != null) map.put(String.valueOf(key), val);
        }
        return map;
    }

    private YamlArray astToSeq(ASTNode node) {
        YamlArray arr = new YamlArray();
        for (ASTNode child : node.getChildren()) arr.add(astToJava(child));
        return arr;
    }

    private Object extractKey(ASTNode pairNode) {
        for (ASTNode child : pairNode.getChildren()) {
            if (isKeyLike(child)) {
                String text = child.getText();
                return text != null ? text.trim() : astToJava(child);
            }
        }
        return null;
    }

    private Object extractValue(ASTNode pairNode) {
        for (ASTNode child : pairNode.getChildren()) {
            if (!isKeyLike(child)) return astToJava(child);
        }
        return null;
    }

    private boolean isKeyLike(ASTNode node) {
        String t = node.getType();
        if (t == null) return false;
        return t.equals("keyNode") || t.equals("blockKey") || t.equals("plainScalar")
            || t.equals("DqString") || t.equals("SqString");
    }

    private Object parseScalar(String type, String text) {
        if (text == null) return null;
        text = text.trim();
        if (text.isEmpty() || text.equals("null") || text.equals("~")) return null;
        if (text.equals("true") || text.equals("True")) return Boolean.TRUE;
        if (text.equals("false") || text.equals("False")) return Boolean.FALSE;
        try {
            if (text.contains(".") || text.contains("e") || text.contains("E")) {
                return Double.parseDouble(text);
            }
            return Long.parseLong(text);
        } catch (NumberFormatException ignored) {}
        return text;
    }
}