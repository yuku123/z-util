package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug8Test {

    @Test
    public void traceMatchTerminalColon() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        System.out.println("=== Tokens (before parse) ===");
        Token tok;
        int cnt = 0;
        while ((tok = tr.peek()) != null && cnt < 10) {
            System.out.println("  tokenName='" + tok.getTokenName() + "' text='" + tok.getText() + "' line=" + tok.getLine() + " col=" + tok.getColumn());
            tr.advance();
            cnt++;
        }
        if (cnt == 0) System.out.println("  (empty)");
        System.out.println("Total tokens before: " + cnt);

        // Re-create lexer and parser
        lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        tr = lexer.getTokenReader();
        parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        System.out.println("\n=== matchTerminal('Colon') ===");
        java.lang.reflect.Method mt = parser.getClass().getDeclaredMethod("matchTerminal", String.class);
        mt.setAccessible(true);
        try {
            ASTNode r = (ASTNode) mt.invoke(parser, "Colon");
            System.out.println("SUCCESS: type=" + r.getType() + " text=" + r.getText());
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) System.out.println("FAIL: " + cause.getMessage());
            else e.printStackTrace();
        }

        System.out.println("\n=== matchTerminal('LBrace') ===");
        try {
            ASTNode r = (ASTNode) mt.invoke(parser, "LBrace");
            System.out.println("SUCCESS: type=" + r.getType() + " text=" + r.getText());
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) System.out.println("FAIL: " + cause.getMessage());
            else e.printStackTrace();
        }

        System.out.println("\n=== parseRule('string') ===");
        java.lang.reflect.Method pr = parser.getClass().getDeclaredMethod("parseRule", String.class);
        pr.setAccessible(true);
        try {
            ASTNode r = (ASTNode) pr.invoke(parser, "string");
            System.out.println("SUCCESS: type=" + r.getType() + " text=" + r.getText() + " children=" + r.getChildren().size());
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) System.out.println("FAIL: " + cause.getMessage());
            else e.printStackTrace();
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug8Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}