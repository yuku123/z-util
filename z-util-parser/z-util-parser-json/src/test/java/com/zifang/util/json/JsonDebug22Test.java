package com.zifang.util.json;

import com.zifang.util.dsl.g4.*;
import com.zifang.util.dsl.g4.model.*;
import com.zifang.util.dsl.ast.*;
import com.zifang.util.dsl.core.*;
import com.zifang.util.dsl.token.*;
import org.junit.Test;
import java.util.*;

/**
 * JsonDebug22Test类。
 */
public class JsonDebug22Test {

    @Test
    /**
     * testSplitElementsInContext方法。
     */
    public void testSplitElementsInContext() throws Exception {
        // Call splitElements via reflection INSIDE parseSequence context
        // and compare to direct call
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{\"a\":1}");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        // Call splitElements directly via reflection
        java.lang.reflect.Method splitMeth = DynamicParser.class.getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);

        String objectBody = "LBrace (pair (Comma pair)*)? RBrace";
        @SuppressWarnings("unchecked")
        List<String> directResult = (List<String>) splitMeth.invoke(parser, objectBody);
        System.out.println("Direct splitElements('" + objectBody + "') = " + directResult);

        // Now trace what parseSequence does
        java.lang.reflect.Method seqMeth = DynamicParser.class.getDeclaredMethod("parseSequence", String.class, ASTNode.class);
        seqMeth.setAccessible(true);

        SimpleASTNode parent = new SimpleASTNode();
        parent.setType("test");
        try {
            seqMeth.invoke(parser, objectBody, parent);
        } catch (Exception e) {
            // Ignore, we just want to see the PSEQ IN log
        }
    }

    @Test
    /**
     * testSameInput方法。
     */
    public void testSameInput() throws Exception {
        // Verify that splitElements gives same result whether called directly or inside parseSequence
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);

        java.lang.reflect.Method splitMeth = DynamicParser.class.getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);

        String input = "LBrace (pair (Comma pair)*)? RBrace";
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) splitMeth.invoke(parser, input);
        System.out.println("splitElements result: " + result);
    }

    private static String loadG4(String name) throws Exception {
        java.io.InputStream is = JsonDebug22Test.class.getClassLoader().getResourceAsStream(name);
        if (is == null) throw new Exception("Resource not found: " + name);
        byte[] bytes = com.zifang.util.core.io.FileUtil.readAllBytes(is);
        is.close();
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }
}