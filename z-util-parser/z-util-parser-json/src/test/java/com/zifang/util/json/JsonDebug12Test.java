package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug12Test {

    @Test
    public void directSplitElementsTest() throws Exception {
        String parserG4 = loadG4("JsonParser.g4");
        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);

        // Test with hardcoded string
        String input = "LBrace (pair (Comma pair)*)? RBrace";
        System.out.println("INPUT: '" + input + "'");

        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> els = (List<String>) splitMeth.invoke(parser, input);
        System.out.println("count: " + els.size());
        for (int i = 0; i < els.size(); i++) {
            System.out.println("  [" + i + "] = '" + els.get(i) + "'");
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug12Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}