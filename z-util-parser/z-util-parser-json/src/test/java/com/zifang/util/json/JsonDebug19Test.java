package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.*;
import com.zifang.util.dsl.ast.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug19Test {

    @Test
    public void testTokenStreamForJsonObject() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        System.out.println("=== Token stream for '{\"a\":1}' ===");
        Token t;
        while ((t = tr.read()) != null) {
            System.out.println("  " + t.getTokenName() + "='" + t.getText() + "'");
        }
    }

    @Test
    public void testTraceObjectRule() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        // Trace parseRule for object
        G4Rule objectRule = null;
        java.lang.reflect.Field f = DynamicParser.class.getDeclaredField("parserRules");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, G4Rule> rules = (Map<String, G4Rule>) f.get(parser);
        objectRule = rules.get("object");
        System.out.println("object rule body: '" + objectRule.getBody() + "'");
        System.out.println("object alternatives: " + new java.util.ArrayList<>(java.util.Arrays.asList(objectRule.getBody().split("\\|"))));

        // Trace splitElements for object body
        java.lang.reflect.Method splitMeth = DynamicParser.class.getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> elements = (List<String>) splitMeth.invoke(parser, objectRule.getBody());
        System.out.println("splitElements('" + objectRule.getBody() + "') = " + elements);

        // Try parse("object") directly
        System.out.println("\n=== parse('object') on '{\"a\":1}' ===");
        try {
            ASTNode ast = parser.parse("object");
            printTree(ast, "  ");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
    }

    @Test
    public void testParsePairDirectly() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        System.out.println("=== parse('pair') on '\"a\":1}' ===");
        try {
            ASTNode ast = parser.parse("pair");
            printTree(ast, "  ");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
    }

    @Test
    public void testParseValueOnObject() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        System.out.println("=== parse('value') on '{\"a\":1}' ===");
        try {
            ASTNode ast = parser.parse("value");
            printTree(ast, "  ");
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
    }

    private void printTree(ASTNode node, String indent) {
        if (node == null) { System.out.println(indent + "null"); return; }
        System.out.println(indent + "type=" + node.getType() + " text='" + node.getText() + "' children=" + node.getChildren().size());
        for (ASTNode child : node.getChildren()) printTree(child, indent + "  ");
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug19Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}