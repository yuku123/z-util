package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug10Test {

    @Test
    public void traceExactG4Body() throws Exception {
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

        G4Rule objRule = rules.get("object");
        String body = objRule.getBody();

        System.out.println("=== Raw G4 body from parserRules ===");
        System.out.println("length: " + body.length());
        System.out.print("chars: ");
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            System.out.print("[" + i + "]'" + c + "'(U+" + String.format("%04X", (int)c) + ") ");
        }
        System.out.println();

        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> els = (List<String>) splitMeth.invoke(parser, body);
        System.out.println("\nsplitElements result count: " + els.size());
        for (int i = 0; i < els.size(); i++) {
            String e = els.get(i);
            System.out.println("  [" + i + "] len=" + e.length() + " '" + e + "'");
            System.out.print("    chars: ");
            for (int j = 0; j < e.length(); j++) System.out.print("[" + j + "]'" + e.charAt(j) + "' ");
            System.out.println();
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug10Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}
