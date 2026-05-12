package com.zifang.util.json.dsl;

import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.token.Token;
import com.zifang.util.json.model.JsonObject;
import com.zifang.util.json.model.JsonArray;

import java.util.List;

/**
 * JSON parser using the z-util-dsl G4 engine
 */
public class DslJsonParser {

    private static final String JSON_GRAMMAR = 
        "lexer grammar JsonLexer;\n" +
        "\n" +
        "LBrace: '{';\n" +
        "RBrace: '}';\n" +
        "LBracket: '[';\n" +
        "RBracket: ']';\n" +
        "Colon: ':';\n" +
        "Comma: ',';\n" +
        "\n" +
        "StringLiteral: '\"' (~['\"\\\\] | '\\\\' .)* '\"';\n" +
        "Number: '-'? [0-9]+ ('.' [0-9]+)? ([eE] [+-]? [0-9]+)?;\n" +
        "True: 'true';\n" +
        "False: 'false';\n" +
        "Null: 'null';\n" +
        "\n" +
        "Whitespace: [ \\t\\r\\n]+ -> channel(HIDDEN);\n" +
        "\n" +
        "parser grammar JsonParser;\n" +
        "\n" +
        "options { tokenVocab=JsonLexer; }\n" +
        "\n" +
        "json: value;\n" +
        "\n" +
        "object\n" +
        "    : LBrace (pair (Comma pair)*)? RBrace\n" +
        "    ;\n" +
        "\n" +
        "pair: StringLiteral Colon value;\n" +
        "\n" +
        "array\n" +
        "    : LBracket (value (Comma value)*)? RBracket\n" +
        "    ;\n" +
        "\n" +
        "value\n" +
        "    : object\n" +
        "    | array\n" +
        "    | StringLiteral\n" +
        "    | Number\n" +
        "    | True\n" +
        "    | False\n" +
        "    | Null\n" +
        "    ;\n";

    private final DynamicLexer lexer;
    private final DynamicParser parser;

    public DslJsonParser() {
        this.lexer = new DynamicLexer();
        this.lexer.loadG4(JSON_GRAMMAR);
        this.parser = new DynamicParser();
        this.parser.loadG4(JSON_GRAMMAR);
    }

    public Object parse(String json) {
        lexer.setInput(json);
        List<Token> tokens = lexer.tokenize();
        
        TokenReader tokenReader = lexer.getTokenReader();
        parser.setTokenReader(tokenReader);
        ASTNode ast = parser.parse("json");
        
        return convertAstToJson(ast);
    }

    private Object convertAstToJson(ASTNode node) {
        String type = node.getType();
        
        if ("value".equals(type)) {
            if (!node.getChildren().isEmpty()) {
                return convertAstToJson(node.getChildren().get(0));
            }
        } else if ("object".equals(type)) {
            JsonObject obj = new JsonObject();
            for (ASTNode child : node.getChildren()) {
                String childType = child.getType();
                if ("LBrace".equals(childType) || "RBrace".equals(childType) || "Comma".equals(childType)) {
                    continue;
                }
                if ("pair".equals(childType)) {
                    List<ASTNode> pairChildren = child.getChildren();
                    String key = null;
                    Object value = null;
                    for (int i = 0; i < pairChildren.size(); i++) {
                        ASTNode pc = pairChildren.get(i);
                        String pcType = pc.getType();
                        if ("StringLiteral".equals(pcType)) {
                            key = stripQuotes(pc.getText());
                        } else if ("Colon".equals(pcType)) {
                            // skip
                        } else {
                            value = convertAstToJson(pc);
                        }
                    }
                    if (key != null) {
                        obj.put(key, value);
                    }
                } else {
                    // might be embedded value
                    Object val = convertAstToJson(child);
                    if (val instanceof JsonObject) {
                        // merge if possible
                    }
                }
            }
            return obj;
        } else if ("array".equals(type)) {
            JsonArray arr = new JsonArray();
            for (ASTNode child : node.getChildren()) {
                String childType = child.getType();
                if ("LBracket".equals(childType) || "RBracket".equals(childType) || "Comma".equals(childType)) {
                    continue;
                }
                arr.add(convertAstToJson(child));
            }
            return arr;
        } else if ("StringLiteral".equals(type)) {
            return stripQuotes(node.getText());
        } else if ("Number".equals(type)) {
            String text = node.getText();
            if (text.contains(".") || text.contains("e") || text.contains("E")) {
                return Double.parseDouble(text);
            }
            return Long.parseLong(text);
        } else if ("True".equals(type)) {
            return Boolean.TRUE;
        } else if ("False".equals(type)) {
            return Boolean.FALSE;
        } else if ("Null".equals(type)) {
            return null;
        }
        return null;
    }

    private String stripQuotes(String s) {
        if (s != null && s.length() >= 2) {
            if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
                return s.substring(1, s.length() - 1);
            }
        }
        return s;
    }
}
