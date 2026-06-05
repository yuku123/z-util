package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.*;
import com.zifang.util.dsl.ast.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug26Test {

    @Test
    public void testExactJsonDebug21Scenario() throws Exception {
        // Exactly replicate JsonDebug21Test#testFullParseJsonObject
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        System.out.println("1. Creating lexer");
        DynamicLexer lexer = new DynamicLexer();
        System.out.println("2. Loading lexer G4");
        lexer.loadG4(lexerG4);
        System.out.println("3. Setting input '{\"a\":1}'");
        lexer.setInput("{\"a\":1}");
        System.out.println("4. Getting tokenReader");
        TokenReader tr = lexer.getTokenReader();
        System.out.println("5. Printing tokens");
        Token t;
        while ((t = tr.read()) != null) {
            System.out.println("   token: " + t.getTokenName() + "='" + t.getText() + "'");
        }
        System.out.println("6. Resetting lexer input");
        lexer.setInput("{\"a\":1}");
        System.out.println("7. Getting fresh tokenReader");
        tr = lexer.getTokenReader();
        System.out.println("8. Creating parser");
        DynamicParser parser = new DynamicParser();
        System.out.println("9. Loading parser G4");
        parser.loadG4(parserG4);
        System.out.println("10. Setting tokenReader on parser");
        parser.setTokenReader(tr);
        System.out.println("11. Calling parser.parse('object')");
        try {
            ASTNode ast = parser.parse("object");
            System.out.println("SUCCESS: type=" + ast.getType() + " children=" + ast.getChildren().size());
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + (e.getCause() != null ? e.getCause().getClass().getName() + " " + e.getCause().getMessage() : e.getMessage()));
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug26Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}