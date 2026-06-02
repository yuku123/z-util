package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;

public class JsonDebug2Test {

    @Test
    public void traceObjectParse() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        // Trace what parseRule("object") returns
        java.lang.reflect.Field rulesField = parser.getClass().getDeclaredField("parserRules");
        rulesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.Map<String, G4Rule> rules = (java.util.Map<String, G4Rule>) rulesField.get(parser);

        System.out.println("=== object rule body ===");
        G4Rule objectRule = rules.get("object");
        System.out.println("object body: '" + objectRule.getBody() + "'");

        System.out.println("\n=== splitElements for object body ===");
        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<String> els = (java.util.List<String>) splitMeth.invoke(parser, objectRule.getBody());
        System.out.println("elements count: " + els.size());
        for (int i = 0; i < els.size(); i++) {
            System.out.println("  [" + i + "]='" + els.get(i) + "'");
        }

        System.out.println("\n=== Parse object directly ===");
        ASTNode result = parser.parse("object");
        System.out.println("result: " + result);
        System.out.println("type: " + result.getType());
        System.out.println("children: " + result.getChildren().size());

        // Now check remaining tokens
        System.out.println("\n=== Remaining tokens ===");
        Token tok;
        int count = 0;
        while ((tok = tr.peek()) != null && count < 10) {
            System.out.println("  token: " + tok.getTokenName() + " => '" + tok.getText() + "'");
            tr.advance();
            count++;
        }
        if (count == 0) System.out.println("  (no tokens)");
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug2Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}