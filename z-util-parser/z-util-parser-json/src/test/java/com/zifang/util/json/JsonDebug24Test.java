package com.zifang.util.json;

import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import org.junit.Test;

import java.util.List;

/**
 * JsonDebug24Test类。
 */
public class JsonDebug24Test {

    private static String loadG4(String name) throws Exception {
        java.io.InputStream is = JsonDebug24Test.class.getClassLoader().getResourceAsStream(name);
        if (is == null) throw new Exception("Resource not found: " + name);
        byte[] bytes = readAllBytes(is);
        is.close();
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Test
    /**
     * testDirect方法。
     */
    public void testDirect() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        // Call splitElements directly on the object rule body
        java.lang.reflect.Method splitMeth = DynamicParser.class.getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);

        String objectBody = "LBrace (pair (Comma pair)*)? RBrace";
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) splitMeth.invoke(parser, objectBody);
        System.out.println("Direct splitElements result: " + result);
    }

    @Test
    /**
     * testDirectParseSequence方法。
     */
    public void testDirectParseSequence() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        java.lang.reflect.Method seqMeth = DynamicParser.class.getDeclaredMethod("parseSequence", String.class, ASTNode.class);
        seqMeth.setAccessible(true);

        SimpleASTNode parent = new SimpleASTNode();
        parent.setType("test");

        try {
            seqMeth.invoke(parser, "LBrace (pair (Comma pair)*)? RBrace", parent);
            System.out.println("parseSequence succeeded, children=" + parent.getChildren().size());
            for (ASTNode c : parent.getChildren()) {
                System.out.println("  child: type=" + c.getType() + " text='" + c.getText() + "'");
            }
        } catch (Exception e) {
            System.out.println("parseSequence EXCEPTION: " + (e.getCause() != null ? e.getCause().getClass().getName() + " " + e.getCause().getMessage() : e.getMessage()));
        }
    }
}