package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonParseDebugTest {

    @Test
    public void debugTokens() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        TokenReader tr = lexer.getTokenReader();

        System.out.println("=== Tokens ===");
        Token tok;
        int count = 0;
        while ((tok = tr.peek()) != null) {
            System.out.println("  tokenName=" + tok.getTokenName() + " text='" + tok.getText() + "' type=" + tok.getType());
            tr.advance();
            count++;
            if (count > 50) { System.out.println("Too many tokens"); break; }
        }
        if (count == 0) System.out.println("  (no tokens)");

        // Re-run parse with fresh parser
        lexer.setInput("{\"name\": \"test\"}");
        tr = lexer.getTokenReader();
        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        System.out.println("\n=== Parser rules ===");
        java.lang.reflect.Field rulesField = parser.getClass().getDeclaredField("parserRules");
        rulesField.setAccessible(true);
        java.util.Map<String, ?> map = (java.util.Map<String, ?>) rulesField.get(parser);
        System.out.println("Rule count: " + map.size());
        for (String k : map.keySet()) System.out.println("  rule: " + k);

        System.out.println("\n=== Parse result ===");
        ASTNode result = parser.parse("json");
        System.out.println("Result: " + result);
        if (result != null) {
            System.out.println("Type: " + result.getType());
            System.out.println("Text: " + result.getText());
            System.out.println("Children: " + result.getChildren().size());
            dumpAST(result, "  ");
        }
    }

    private static void dumpAST(ASTNode node, String indent) {
        System.out.println(indent + "type=" + node.getType() + " text=" + node.getText() + " children=" + node.getChildren().size());
        for (ASTNode child : node.getChildren()) {
            dumpAST(child, indent + "  ");
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonParseDebugTest.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}