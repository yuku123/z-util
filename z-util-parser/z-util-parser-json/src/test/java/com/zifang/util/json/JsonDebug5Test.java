package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug5Test {

    @Test
    public void testObjectRuleDirectly() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        // Get the rules from the parser
        java.lang.reflect.Field rulesF = parser.getClass().getDeclaredField("parserRules");
        rulesF.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, G4Rule> rules = (Map<String, G4Rule>) rulesF.get(parser);

        G4Rule objRule = rules.get("object");
        G4Rule valRule = rules.get("value");
        G4Rule pairRule = rules.get("pair");

        System.out.println("object body: '" + objRule.getBody() + "'");
        System.out.println("value body: '" + valRule.getBody() + "'");
        System.out.println("pair body: '" + pairRule.getBody() + "'");

        System.out.println("\n--- parse json ---");
        ASTNode r = parser.parse("json");
        System.out.println("json result: type=" + r.getType() + " children=" + r.getChildren().size());
        if (!r.getChildren().isEmpty()) {
            ASTNode c0 = r.getChildren().get(0);
            System.out.println("  child[0]: type=" + c0.getType() + " children=" + c0.getChildren().size());
            for (ASTNode c : c0.getChildren()) {
                System.out.println("    subchild: type=" + c.getType() + " text=" + c.getText());
            }
        }

        System.out.println("\n--- parse value ---");
        ASTNode r2 = parser.parse("value");
        System.out.println("value result: type=" + r2.getType() + " children=" + r2.getChildren().size());

        System.out.println("\n--- parse object ---");
        ASTNode r3 = parser.parse("object");
        System.out.println("object result: type=" + r3.getType() + " children=" + r3.getChildren().size());

        // Check if tokenReader is shared and exhausted
        System.out.println("\n--- remaining tokens ---");
        Token tok;
        int cnt = 0;
        while ((tok = tr.peek()) != null && cnt < 10) {
            System.out.println("  " + tok.getTokenName() + " => '" + tok.getText() + "'");
            tr.advance();
            cnt++;
        }
        if (cnt == 0) System.out.println("  (empty)");
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug5Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}