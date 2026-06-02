package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug14Test {

    @Test
    public void traceSplitElementsLogic() throws Exception {
        String parserG4 = loadG4("JsonParser.g4");
        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);

        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);

        String input = "LBrace (pair (Comma pair)*)? RBrace";
        System.out.println("INPUT: '" + input + "' LEN=" + input.length());

        // Print every character
        for (int i = 0; i < input.length(); i++) {
            System.out.println("  [" + i + "] '" + input.charAt(i) + "'");
        }

        @SuppressWarnings("unchecked")
        List<String> els = (List<String>) splitMeth.invoke(parser, input);
        System.out.println("\nRESULT count=" + els.size());
        for (int i = 0; i < els.size(); i++) {
            String e = els.get(i);
            System.out.println("  [" + i + "] len=" + e.length() + " '" + e + "'");
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug14Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}