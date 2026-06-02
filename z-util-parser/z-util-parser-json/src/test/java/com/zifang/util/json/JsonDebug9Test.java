package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.G4Rule;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug9Test {

    @Test
    public void testSplitElementsParenBug() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"name\": \"test\"}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        java.lang.reflect.Method splitMeth = parser.getClass().getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> els = (List<String>) splitMeth.invoke(parser, "(pair (Comma pair)*)?");
        System.out.println("splitElements('(pair (Comma pair)*)?'):");
        System.out.println("  count: " + els.size());
        for (int i = 0; i < els.size(); i++) System.out.println("  [" + i + "] = '" + els.get(i) + "'");

        // If we get here, let's try parseSequence on the whole object body
        System.out.println("\n--- parseSequence on object body ---");
        java.lang.reflect.Method parseSeq = parser.getClass().getDeclaredMethod("parseSequence", String.class, ASTNode.class);
        parseSeq.setAccessible(true);
        SimpleASTNode dummyParent = new SimpleASTNode();
        dummyParent.setType("parent");
        try {
            parseSeq.invoke(parser, "LBrace (pair (Comma pair)*)? RBrace", dummyParent);
            System.out.println("parseSequence success, children: " + dummyParent.getChildren().size());
            for (ASTNode c : dummyParent.getChildren()) {
                System.out.println("  child: type=" + c.getType() + " text=" + c.getText());
            }
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause != null) System.out.println("parseSequence FAIL: " + cause.getMessage());
            else e.printStackTrace();
        }

        System.out.println("\n--- remaining tokens after sequence ---");
        Token tok;
        int cnt = 0;
        while ((tok = tr.peek()) != null && cnt < 10) {
            System.out.println("  " + tok.getTokenName() + " => '" + tok.getText() + "'");
            cnt++;
        }
        if (cnt == 0) System.out.println("  (empty)");
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug9Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}