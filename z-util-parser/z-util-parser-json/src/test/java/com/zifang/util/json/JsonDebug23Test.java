package com.zifang.util.json;

import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.g4.DynamicParser;
import org.junit.Test;

import java.util.List;

/**
 * JsonDebug23Test类。
 */
public class JsonDebug23Test {

    private static String loadG4(String name) throws Exception {
        java.io.InputStream is = JsonDebug23Test.class.getClassLoader().getResourceAsStream(name);
        if (is == null) throw new Exception("Resource not found: " + name);
        byte[] bytes = com.zifang.util.core.io.FileUtil.readAllBytes(is);
        is.close();
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Test
    /**
     * testSplitElementsIsolated方法。
     */
    public void testSplitElementsIsolated() throws Exception {
        DynamicParser parser = new DynamicParser();

        java.lang.reflect.Method splitMeth = DynamicParser.class.getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);

        String input = "LBrace (pair (Comma pair)*)? RBrace";
        @SuppressWarnings("unchecked")
        List<String> result = (List<String>) splitMeth.invoke(parser, input);
        System.out.println("Direct call result: " + result);

        // Now compare with what parseSequence logs
        java.lang.reflect.Method seqMeth = DynamicParser.class.getDeclaredMethod("parseSequence", String.class, ASTNode.class);
        seqMeth.setAccessible(true);

        // Use a fresh parser so we can load G4 and call parseSequence
        String parserG4 = loadG4("JsonParser.g4");
        parser.loadG4(parserG4);

        SimpleASTNode parent = new SimpleASTNode();
        parent.setType("test");
        try {
            seqMeth.invoke(parser, input, parent);
        } catch (Exception e) {
            System.out.println("seqMeth.invoke EXCEPTION: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }
    }
}