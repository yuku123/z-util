package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import org.junit.Test;
import java.util.*;

/**
 * JsonDebug15Test类。
 */
public class JsonDebug15Test {

    @Test
    /**
     * testBasicParse方法。
     */
    public void testBasicParse() throws Exception {
        String g4 = loadG4("JsonParser.g4");
        System.out.println("G4 length: " + g4.length());
        System.out.println("G4 preview: " + g4.substring(0, Math.min(200, g4.length())));

        DynamicParser parser = new DynamicParser();
        parser.loadG4(g4);

        java.lang.reflect.Field f = DynamicParser.class.getDeclaredField("parserRules");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, com.zifang.util.dsl.g4.model.G4Rule> rules =
            (Map<String, com.zifang.util.dsl.g4.model.G4Rule>) f.get(parser);
        System.out.println("Rules count: " + rules.size());
        if (rules.isEmpty()) {
            System.out.println("WARNING: rules map is EMPTY!");
        }
        for (String k : rules.keySet()) {
            System.out.println("  " + k + " -> '" + rules.get(k).getBody() + "'");
        }

        // Now test actual parsing
        System.out.println("\n=== Testing parse ===");
        try {
            Object result = parser.parse("{\"a\":1}");
            System.out.println("parse() returned: " + result);
            System.out.println("result type: " + (result != null ? result.getClass().getName() : "null"));
        } catch (Exception e) {
            System.out.println("parse() EXCEPTION: " + e.getClass().getName() + " " + e.getMessage());
        }

        System.out.println("\n=== Testing JSONParser.fromJSON ===");
        try {
            JSONParser jsonParser = new JSONParser();
            System.out.println("Creating fromJSON with 'null'...");
            Object resultNull = jsonParser.fromJSON(null);
            System.out.println("null -> " + resultNull);
            System.out.println("Creating fromJSON with '1'...");
            Object result1 = jsonParser.fromJSON("1");
            System.out.println("1 -> " + result1);
            System.out.println("Creating fromJSON with '\"a\"'...");
            Object resultA = jsonParser.fromJSON("\"a\"");
            System.out.println("\"a\" -> " + resultA);
        } catch (Exception e) {
            System.out.println("JSONParser EXCEPTION: " + e.getClass().getName() + " " + e.getMessage());
        }
    }

    private static String loadG4(String name) throws Exception {
        java.io.InputStream is = JsonDebug15Test.class.getClassLoader().getResourceAsStream(name);
        if (is == null) throw new Exception("Resource not found: " + name);
        byte[] bytes = com.zifang.util.core.io.FileUtil.readAllBytes(is);
        is.close();
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }
}