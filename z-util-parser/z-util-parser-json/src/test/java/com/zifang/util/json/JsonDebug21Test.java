package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.*;
import com.zifang.util.dsl.ast.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

/**
 * JsonDebug21Test类。
 */
public class JsonDebug21Test {

    @Test
    /**
     * testFullParseJsonObject方法。
     */
    public void testFullParseJsonObject() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        // Create FRESH lexer + tokenReader + parser for each parse call
        DynamicLexer lexer1 = new DynamicLexer();
        lexer1.loadG4(lexerG4);
        lexer1.setInput("{\"a\":1}");
        TokenReader tr1 = lexer1.getTokenReader();

        DynamicParser parser1 = new DynamicParser();
        parser1.loadG4(parserG4);
        parser1.setTokenReader(tr1);

        System.out.println("=== Fresh parse('object') on '{\"a\":1}' ===");
        try {
            ASTNode ast = parser1.parse("object");
            printTree(ast, "  ");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }
    }

    @Test
    /**
     * testFullParseJsonValue方法。
     */
    public void testFullParseJsonValue() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        System.out.println("=== Fresh parse('value') on '{\"a\":1}' ===");
        try {
            ASTNode ast = parser.parse("value");
            printTree(ast, "  ");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }
    }

    @Test
    /**
     * testFullParseJson方法。
     */
    public void testFullParseJson() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        System.out.println("=== Fresh parse('json') on '{\"a\":1}' ===");
        try {
            ASTNode ast = parser.parse("json");
            printTree(ast, "  ");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }
    }

    @Test
    /**
     * testAstToJavaFull方法。
     */
    public void testAstToJavaFull() throws Exception {
        JSONParser jsonParser = new JSONParser();
        System.out.println("=== fromJSON('{\"a\":1}') ===");
        try {
            Object result = jsonParser.fromJSON("{\"a\":1}");
            System.out.println("result = " + result + " type=" + (result != null ? result.getClass().getName() : "null"));
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getClass().getName() + " " + e.getMessage());
            if (e.getCause() != null) e.getCause().printStackTrace();
        }
    }

    private void printTree(ASTNode node, String indent) {
        if (node == null) { System.out.println(indent + "null"); return; }
        System.out.println(indent + "type=" + node.getType() + " text='" + node.getText() + "' children=" + node.getChildren().size());
        for (ASTNode child : node.getChildren()) printTree(child, indent + "  ");
    }

    private static String loadG4(String name) throws Exception {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.InputStream is = JsonDebug21Test.class.getClassLoader().getResourceAsStream(name);
        if (is == null) throw new Exception("Resource not found: " + name);
        byte[] buf = new byte[4096];
        int len;
        while ((len = is.read(buf)) != -1) baos.write(buf, 0, len);
        is.close();
        return baos.toString(java.nio.charset.StandardCharsets.UTF_8.name());
    }
}