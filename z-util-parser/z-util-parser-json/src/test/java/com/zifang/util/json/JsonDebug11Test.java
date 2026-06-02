package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug11Test {

    @Test
    public void directSplitElements() throws Exception {
        String parserG4 = loadG4("JsonParser.g4");
        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);

        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> els = (List<String>) splitMeth.invoke(parser, "LBrace (pair (Comma pair)*)? RBrace");
        System.out.println("splitElements('LBrace (pair (Comma pair)*)? RBrace'):");
        System.out.println("  count: " + els.size());
        for (int i = 0; i < els.size(); i++) System.out.println("  [" + i + "] = '" + els.get(i) + "'");

        // Also check the substring
        String body = "LBrace (pair (Comma pair)*)? RBrace";
        String inner = body.substring(1, body.length() - 1);
        System.out.println("\nmanual substring(1, len-1) = '" + inner + "' len=" + inner.length());
        @SuppressWarnings("unchecked")
        List<String> els2 = (List<String>) splitMeth.invoke(parser, inner);
        System.out.println("splitElements on that:");
        for (int i = 0; i < els2.size(); i++) System.out.println("  [" + i + "] = '" + els2.get(i) + "'");

        // Now test from actual G4 body
        java.lang.reflect.Field rulesF = parser.getClass().getDeclaredField("parserRules");
        rulesF.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, G4Rule> rules = (Map<String, G4Rule>) rulesF.get(parser);
        String actualBody = rules.get("object").getBody();
        System.out.println("\nactual body from parserRules: len=" + actualBody.length() + " '" + actualBody + "'");
        @SuppressWarnings("unchecked")
        List<String> els3 = (List<String>) splitMeth.invoke(parser, actualBody);
        System.out.println("splitElements on actual body:");
        for (int i = 0; i < els3.size(); i++) System.out.println("  [" + i + "] = '" + els3.get(i) + "'");
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug11Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}