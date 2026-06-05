package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.*;
import com.zifang.util.dsl.ast.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

public class JsonDebug20Test {

    @Test
    public void testSplitElementsInner() throws Exception {
        String input = "(pair (Comma pair)*)";
        java.lang.reflect.Method splitMeth = DynamicParser.class.getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);

        // Create a minimal parser instance to invoke the method on
        DynamicParser p = new DynamicParser();
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) splitMeth.invoke(p, input);
        System.out.println("splitElements('" + input + "') = " + result);

        // Also test the full optional body
        String input2 = "(pair (Comma pair)*)?";
        @SuppressWarnings("unchecked")
        List<String> result2 = (List<String>) splitMeth.invoke(p, input2);
        System.out.println("splitElements('" + input2 + "') = " + result2);

        // Test the full sequence
        String input3 = "LBrace (pair (Comma pair)*)? RBrace";
        @SuppressWarnings("unchecked")
        List<String> result3 = (List<String>) splitMeth.invoke(p, input3);
        System.out.println("splitElements('" + input3 + "') = " + result3);
    }

    @Test
    public void testParseSequenceDirectly() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        // Use reflection to call parseSequence
        java.lang.reflect.Method seqMeth = DynamicParser.class.getDeclaredMethod("parseSequence", String.class, ASTNode.class);
        seqMeth.setAccessible(true);

        SimpleASTNode parent = new SimpleASTNode();
        parent.setType("testParent");

        // Test: LBrace RBrace sequence
        try {
            seqMeth.invoke(parser, "LBrace RBrace", parent);
            System.out.println("parseSequence('LBrace RBrace') -> children=" + parent.getChildren().size());
            for (ASTNode c : parent.getChildren()) {
                System.out.println("  child: type=" + c.getType() + " text='" + c.getText() + "'");
            }
        } catch (Exception e) {
            System.out.println("parseSequence EXCEPTION: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }

        // Reset parent
        parent = new SimpleASTNode();
        parent.setType("testParent");

        // Test: LBrace (pair (Comma pair)*)? RBrace
        try {
            seqMeth.invoke(parser, "LBrace (pair (Comma pair)*)? RBrace", parent);
            System.out.println("\nparseSequence('LBrace (pair (Comma pair)*)? RBrace') -> children=" + parent.getChildren().size());
            for (ASTNode c : parent.getChildren()) {
                System.out.println("  child: type=" + c.getType() + " text='" + c.getText() + "' children=" + c.getChildren().size());
                for (ASTNode cc : c.getChildren()) {
                    System.out.println("    subchild: type=" + cc.getType() + " text='" + cc.getText() + "'");
                }
            }
        } catch (Exception e) {
            System.out.println("\nparseSequence EXCEPTION: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }
    }

    private static String loadG4(String name) throws Exception {
        try (java.io.InputStream is = JsonDebug20Test.class.getClassLoader().getResourceAsStream(name)) {
            return new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}