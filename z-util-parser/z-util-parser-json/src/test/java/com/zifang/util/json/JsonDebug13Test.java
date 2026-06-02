package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug13Test {

    @Test
    public void traceEveryChar() throws Exception {
        String parserG4 = loadG4("JsonParser.g4");
        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);

        String input = "LBrace (pair (Comma pair)*)? RBrace";
        System.out.println("INPUT: '" + input + "' len=" + input.length());
        System.out.println("INPUT chars:");
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            System.out.println("  [" + i + "] '" + c + "' U+" + String.format("%04X", (int)c));
        }

        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> els = (List<String>) splitMeth.invoke(parser, input);
        System.out.println("\nRESULT:");
        for (int i = 0; i < els.size(); i++) {
            String e = els.get(i);
            System.out.println("  [" + i + "] len=" + e.length() + " '" + e + "'");
            for (int j = 0; j < e.length(); j++) {
                System.out.println("    [" + j + "] '" + e.charAt(j) + "'");
            }
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug13Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}