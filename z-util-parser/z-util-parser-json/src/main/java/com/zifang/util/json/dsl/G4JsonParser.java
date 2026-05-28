package com.zifang.util.json.dsl;

import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import com.zifang.util.dsl.token.Token;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;

import java.io.StringReader;
import java.util.List;

/**
 * 基于 z-util-dsl G4 框架的 JSON 解析器。
 * <p>
 * 加载 JsonLexer.g4 + JsonParser.g4，通过 DynamicLexer 词法分词，
 * DynamicParser 语法解析，最终遍历 AST 构造 JsonObject/JsonArray。
 * <p>
 * 解析流程：G4 → DynamicLexer → Tokens → DynamicParser → AST → JsonObject/JsonArray
 *
 * @author zifang
 */
public class G4JsonParser {

    private static final String JSON_LEXER_G4 =
            "lexer grammar JsonLexer;\n" +
            "Space:  [ \\t\\r\\n]+ -> channel(HIDDEN);\n" +
            "Null:     'null';\n" +
            "True:     'true';\n" +
            "False:    'false';\n" +
            "Number: '-'? [0-9]+ ('.' [0-9]+)? ([eE] [+-]? [0-9]+)?;\n" +
            "String: '\"' (([\\u0020-\\u0021\\u0023-\\u005b\\u005d-\\uFFFF] | '\\\\' [tnrbf\"\\\\/] | '\\\\u' [0-9a-fA-F]{4})*) '\"';\n" +
            "LCurly:  '{';\n" +
            "RCurly:  '}';\n" +
            "LBracket: '[';\n" +
            "RBracket: ']';\n" +
            "Colon:   ':';\n" +
            "Comma:   ',';\n";

    private static final String JSON_PARSER_G4 =
            "parser grammar JsonParser;\n" +
            "options { tokenVocab=JsonLexer; }\n" +
            "json: value;\n" +
            "value: object | array | String | Number | Null | True | False;\n" +
            "object: LCurly (pair (Comma pair)*)? RCurly;\n" +
            "pair: String Colon value;\n" +
            "array: LBracket (value (Comma value)*)? RBracket;\n";

    private final DynamicLexer lexer;
    private final DynamicParser parser;

    public G4JsonParser() {
        this.lexer = new DynamicLexer();
        this.lexer.loadG4(JSON_LEXER_G4);
        this.parser = new DynamicParser();
        this.parser.loadG4(JSON_PARSER_G4);
    }

    /**
     * 解析 JSON 字符串，返回 JsonObject 或 JsonArray。
     *
     * @param json JSON 字符串
     * @return JsonObject 或 JsonArray
     */
    public Object parse(String json) {
        lexer.setInput(json);
        List<Token> tokens = lexer.tokenize();
        SimpleTokenReader tokenReader = new SimpleTokenReader(tokens);
        parser.setTokenReader(tokenReader);
        com.zifang.util.dsl.core.ASTNode ast = parser.parse("json");
        return walk(ast);
    }

    private Object walk(com.zifang.util.dsl.core.ASTNode node) {
        String type = node.getType();
        switch (type) {
            case "json":
            case "value":
                if (node.getChildren().isEmpty()) return null;
                return walk(node.getChildren().get(0));
            case "object": return walkObject(node);
            case "array": return walkArray(node);
            case "pair": return walkPair(node);
            case "String": return unquoteString(node.getText());
            case "Number": return parseNumber(node.getText());
            case "Null": return null;
            case "True": return Boolean.TRUE;
            case "False": return Boolean.FALSE;
            default: return node.getText();
        }
    }

    private JsonObject walkObject(com.zifang.util.dsl.core.ASTNode node) {
        JsonObject obj = new JsonObject();
        for (com.zifang.util.dsl.core.ASTNode child : node.getChildren()) {
            String childType = child.getType();
            if (childType.equals("pair")) {
                PairResult pair = walkPair(child);
                obj.put(pair.key, pair.value);
            }
        }
        return obj;
    }

    private JsonArray walkArray(com.zifang.util.dsl.core.ASTNode node) {
        JsonArray arr = new JsonArray();
        for (com.zifang.util.dsl.core.ASTNode child : node.getChildren()) {
            String childType = child.getType();
            if (childType.equals("value")) {
                arr.add(walk(child));
            }
        }
        return arr;
    }

    private static class PairResult {
        String key;
        Object value;
    }

    private PairResult walkPair(com.zifang.util.dsl.core.ASTNode node) {
        PairResult result = new PairResult();
        List<com.zifang.util.dsl.core.ASTNode> children = node.getChildren();
        for (com.zifang.util.dsl.core.ASTNode child : children) {
            String childType = child.getType();
            if (childType.equals("String")) {
                result.key = unquoteString(child.getText());
            } else if (childType.equals("value")) {
                result.value = walk(child);
            }
        }
        return result;
    }

    private static String unquoteString(String text) {
        if (text == null || text.length() < 2) return text;
        // text is the raw token text including quotes
        if (text.startsWith("\"") && text.endsWith("\"")) {
            return decodeString(text.substring(1, text.length() - 1));
        }
        return text;
    }

    private static String decodeString(String raw) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < raw.length()) {
            char ch = raw.charAt(i);
            if (ch == '\\' && i + 1 < raw.length()) {
                char n = raw.charAt(++i);
                switch (n) {
                    case '"':  sb.append('"');  break;
                    case '\\':  sb.append('\\'); break;
                    case '/':   sb.append('/');  break;
                    case 'n':   sb.append('\n'); break;
                    case 'r':   sb.append('\r'); break;
                    case 't':   sb.append('\t'); break;
                    case 'b':   sb.append('\b'); break;
                    case 'f':   sb.append('\f'); break;
                    case 'u':
                        if (i + 4 < raw.length()) {
                            String hex = raw.substring(i + 1, i + 5);
                            sb.append((char) Integer.parseInt(hex, 16));
                            i += 5;
                        } else { sb.append(n); }
                        break;
                    default: sb.append(n); break;
                }
                i++;
            } else {
                sb.append(ch);
                i++;
            }
        }
        return sb.toString();
    }

    private static Number parseNumber(String text) {
        if (text == null) return null;
        try {
            if (text.indexOf('.') >= 0 || text.indexOf('e') >= 0 || text.indexOf('E') >= 0) {
                return Double.parseDouble(text);
            }
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return Double.parseDouble(text);
        }
    }

    /**
     * 简单的 TokenReader 实现，将 Token 列表包装为 TokenReader 接口。
     */
    private static class SimpleTokenReader implements TokenReader {
        private final List<Token> tokens;
        private int pos = 0;

        SimpleTokenReader(List<Token> tokens) {
            this.tokens = tokens;
        }

        @Override
        public Token read() {
            return pos < tokens.size() ? tokens.get(pos++) : null;
        }

        @Override
        public Token peek() {
            return pos < tokens.size() ? tokens.get(pos) : null;
        }

        @Override
        public void advance() {
            pos++;
        }

        @Override
        public Token get(int offset) {
            int idx = pos + offset;
            return idx >= 0 && idx < tokens.size() ? tokens.get(idx) : null;
        }

        @Override
        public boolean hasNext() {
            return pos < tokens.size();
        }

        @Override
        public void reset() {
            pos = 0;
        }

        @Override
        public void close() {
        }
    }
}