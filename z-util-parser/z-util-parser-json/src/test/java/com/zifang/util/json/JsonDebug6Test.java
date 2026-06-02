package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug6Test {

    @Test
    public void traceParseRulePair() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        java.lang.reflect.Field rulesF = parser.getClass().getDeclaredField("parserRules");
        rulesF.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, G4Rule> rules = (Map<String, G4Rule>) rulesF.get(parser);

        System.out.println("Rules in parserRules: " + rules.keySet());

        System.out.println("\n--- isRuleRef tests ---");
        System.out.println("isRuleRef('string') = " + isRuleRef(parser, "string"));
        System.out.println("isRuleRef('value') = " + isRuleRef(parser, "value"));
        System.out.println("isRuleRef('pair') = " + isRuleRef(parser, "pair"));
        System.out.println("isRuleRef('object') = " + isRuleRef(parser, "object"));
        System.out.println("isRuleRef('LBrace') = " + isRuleRef(parser, "LBrace"));

        System.out.println("\n--- Direct parseRule pair ---");
        try {
            ASTNode r = parseRule(parser, rules.get("pair"));
            System.out.println("pair result: type=" + r.getType() + " children=" + r.getChildren().size() + " text=" + r.getText());
            for (ASTNode c : r.getChildren()) {
                System.out.println("  child: type=" + c.getType() + " text=" + c.getText());
            }
        } catch (Exception e) {
            System.out.println("pair EXCEPTION: " + e.getCause().getMessage());
        }

        System.out.println("\n--- tokens remaining ---");
        Token tok;
        int cnt = 0;
        while ((tok = tr.peek()) != null && cnt < 5) {
            System.out.println("  " + tok.getTokenName() + " => '" + tok.getText() + "'");
            cnt++;
        }
        if (cnt == 0) System.out.println("  (empty)");
    }

    private static boolean isRuleRef(DynamicParser p, String name) throws Exception {
        java.lang.reflect.Method m = p.getClass().getDeclaredMethod("isRuleRef", String.class);
        m.setAccessible(true);
        return (Boolean) m.invoke(p, name);
    }

    private static ASTNode parseRule(DynamicParser p, G4Rule rule) throws Exception {
        java.lang.reflect.Method m = p.getClass().getDeclaredMethod("parseRule", G4Rule.class);
        m.setAccessible(true);
        return (ASTNode) m.invoke(p, rule);
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug6Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}