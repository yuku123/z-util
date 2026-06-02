package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug7Test {

    @Test
    public void traceParseRulePairStepByStep() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> els = (List<String>) splitMeth.invoke(parser, "string Colon value");
        System.out.println("splitElements('string Colon value'):");
        for (int i = 0; i < els.size(); i++) System.out.println("  [" + i + "] = '" + els.get(i) + "'");

        System.out.println("\n--- trace parseRule string ---");
        try {
            ASTNode r = parseRuleDirect(parser, "string");
            System.out.println("string result: type=" + r.getType() + " text=" + r.getText() + " children=" + r.getChildren().size());
        } catch (Exception e) { System.out.println("string EXCEPTION: " + e.getCause()); }

        System.out.println("\n--- trace matchTerminal Colon ---");
        java.lang.reflect.Method mt = parser.getClass().getDeclaredMethod("matchTerminal", String.class);
        mt.setAccessible(true);
        try {
            ASTNode r = (ASTNode) mt.invoke(parser, "Colon");
            System.out.println("Colon result: type=" + r.getType() + " text=" + r.getText());
        } catch (Exception e) { System.out.println("Colon EXCEPTION: " + e.getCause()); }

        System.out.println("\n--- trace parseRule value ---");
        try {
            ASTNode r = parseRuleDirect(parser, "value");
            System.out.println("value result: type=" + r.getType() + " text=" + r.getText() + " children=" + r.getChildren().size());
        } catch (Exception e) { System.out.println("value EXCEPTION: " + e.getCause()); }

        System.out.println("\n--- now try parseRule pair ---");
        try {
            ASTNode r = parseRuleDirect(parser, "pair");
            System.out.println("pair result: type=" + r.getType() + " text=" + r.getText() + " children=" + r.getChildren().size());
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) System.out.println("pair EXCEPTION: " + cause.getMessage());
            else e.printStackTrace();
        }

        System.out.println("\n--- tokens remaining after pair ---");
        Token tok;
        int cnt = 0;
        while ((tok = tr.peek()) != null && cnt < 5) {
            System.out.println("  " + tok.getTokenName() + " => '" + tok.getText() + "'");
            tr.advance();
            cnt++;
        }
        if (cnt == 0) System.out.println("  (empty)");
    }

    private static ASTNode parseRuleDirect(DynamicParser p, String name) throws Exception {
        java.lang.reflect.Method m = p.getClass().getDeclaredMethod("parseRule", String.class);
        m.setAccessible(true);
        return (ASTNode) m.invoke(p, name);
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug7Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}