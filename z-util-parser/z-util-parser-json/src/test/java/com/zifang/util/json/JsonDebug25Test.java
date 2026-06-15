package com.zifang.util.json;

import com.zifang.util.dsl.ast.SimpleASTNode;
import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import com.zifang.util.dsl.g4.model.G4Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * JsonDebug25Test类。
 */
public class JsonDebug25Test {

    private static String loadG4(String name) throws Exception {
        java.io.InputStream is = JsonDebug25Test.class.getClassLoader().getResourceAsStream(name);
        if (is == null) throw new Exception("Resource not found: " + name);
        byte[] bytes = com.zifang.util.core.io.FileUtil.readAllBytes(is);
        is.close();
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Test
    /**
     * testRuleBodyIsClean方法。
     */
    public void testRuleBodyIsClean() throws Exception {
        String parserG4 = loadG4("JsonParser.g4");
        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);

        java.lang.reflect.Field f = DynamicParser.class.getDeclaredField("parserRules");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, G4Rule> rules = (Map<String, G4Rule>) f.get(parser);

        G4Rule objectRule = rules.get("object");
        String body = objectRule.getBody();
        System.out.println("object body: '" + body + "'");
        System.out.println("object body length: " + body.length());
        System.out.println("object body bytes: " + Arrays.toString(body.getBytes(java.nio.charset.StandardCharsets.UTF_8)));

        // Check each character
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c < 32 || c > 127) {
                System.out.println("  SUSPICIOUS char at " + i + ": U+" + Integer.toHexString(Character.codePointAt(body, i)) + " (" + c + ")");
            }
        }

        // Test splitElements on the actual rule body
        java.lang.reflect.Method splitMeth = DynamicParser.class.getDeclaredMethod("splitElements", String.class);
        splitMeth.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> elements = (List<String>) splitMeth.invoke(parser, body);
        System.out.println("splitElements on rule body: " + elements);
    }

    @Test
    /**
     * testSimpleSequence方法。
     */
    public void testSimpleSequence() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("{");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        java.lang.reflect.Method seqMeth = DynamicParser.class.getDeclaredMethod("parseSequence", String.class, ASTNode.class);
        seqMeth.setAccessible(true);

        SimpleASTNode parent = new SimpleASTNode();
        parent.setType("test");

        try {
            seqMeth.invoke(parser, "LBrace RBrace", parent);
            System.out.println("parseSequence('LBrace RBrace') succeeded, children=" + parent.getChildren().size());
        } catch (Exception e) {
            System.out.println("parseSequence('LBrace RBrace') EXCEPTION: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }
    }
}