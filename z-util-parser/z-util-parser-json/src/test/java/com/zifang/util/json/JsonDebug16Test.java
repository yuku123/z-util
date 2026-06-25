package com.zifang.util.json;

import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.core.TokenReader;
import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import com.zifang.util.dsl.token.Token;
import org.junit.Test;

/**
 * JsonDebug16Test类。
 */
public class JsonDebug16Test {

    private static String loadG4(String name) throws Exception {
        java.io.InputStream is = JsonDebug16Test.class.getClassLoader().getResourceAsStream(name);
        if (is == null) throw new Exception("Resource not found: " + name);
        byte[] bytes = readAllBytes(is);
        is.close();
        return new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Test
    /**
     * testTokenization方法。
     */
    public void testTokenization() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("1");
        TokenReader tr = lexer.getTokenReader();

        System.out.println("=== Tokens for '1' ===");
        Token t;
        while ((t = tr.read()) != null) {
            System.out.println("  tokenName='" + t.getTokenName() + "' text='" + t.getText() + "' line=" + t.getLine() + " col=" + t.getColumn());
        }

        lexer.setInput("{\"a\":1}");
        tr = lexer.getTokenReader();
        System.out.println("\n=== Tokens for '{\"a\":1}' ===");
        while ((t = tr.read()) != null) {
            System.out.println("  tokenName='" + t.getTokenName() + "' text='" + t.getText() + "'");
        }
    }

    @Test
    /**
     * testParseNumber方法。
     */
    public void testParseNumber() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("1");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        ASTNode ast = parser.parse("number");
        System.out.println("=== parse('number') on '1' ===");
        System.out.println("  type=" + ast.getType() + " text='" + ast.getText() + "' children=" + ast.getChildren().size());
        printTree(ast, "  ");
    }

    @Test
    /**
     * testParseValue方法。
     */
    public void testParseValue() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("1");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        ASTNode ast = parser.parse("value");
        System.out.println("=== parse('value') on '1' ===");
        printTree(ast, "  ");
    }

    @Test
    /**
     * testParseJson方法。
     */
    public void testParseJson() throws Exception {
        String lexerG4 = loadG4("JsonLexer.g4");
        String parserG4 = loadG4("JsonParser.g4");

        DynamicLexer lexer = new DynamicLexer();
        lexer.loadG4(lexerG4);
        lexer.setInput("1");
        TokenReader tr = lexer.getTokenReader();

        DynamicParser parser = new DynamicParser();
        parser.loadG4(parserG4);
        parser.setTokenReader(tr);

        ASTNode ast = parser.parse("json");
        System.out.println("=== parse('json') on '1' ===");
        printTree(ast, "  ");
    }

    @Test
    /**
     * testFullAstToJava方法。
     */
    public void testFullAstToJava() throws Exception {
        JSONParser jsonParser = new JSONParser();

        // Test number
        System.out.println("=== fromJSON('1') ===");
        Object r1 = jsonParser.fromJSON("1");
        System.out.println("result=" + r1 + " type=" + (r1 != null ? r1.getClass().getName() : "null"));

        // Test string
        System.out.println("\n=== fromJSON('\"a\"') ===");
        Object r2 = jsonParser.fromJSON("\"a\"");
        System.out.println("result=" + r2 + " type=" + (r2 != null ? r2.getClass().getName() : "null"));
    }

    private void printTree(ASTNode node, String indent) {
        if (node == null) {
            System.out.println(indent + "null");
            return;
        }
        System.out.println(indent + "type=" + node.getType() + " text='" + node.getText() + "' children=" + node.getChildren().size());
        for (ASTNode child : node.getChildren()) {
            printTree(child, indent + "  ");
        }
    }
}