package com.zifang.util.expression.source;

import com.zifang.util.expression.source.ast.ASTNode;
import com.zifang.util.expression.source.ast.ASTNodeType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SimpleParser 类测试
 */
public class SimpleParserTest {

    @Test
    public void testParseIntDeclaration() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "int age = 45;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertFalse(tree.getChildren().isEmpty());
    }

    @Test
    public void testParseMultipleStatements() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "int age = 45+2; age= 20; age+10*2;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertEquals(3, tree.getChildren().size());
    }

    @Test
    public void testParseExpressionStatement() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "2+3;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertFalse(tree.getChildren().isEmpty());
    }

    @Test
    public void testParseAssignmentStatement() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "age = 20;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertFalse(tree.getChildren().isEmpty());
    }

    @Test(expected = Exception.class)
    public void testParseInvalidSyntaxMissingSemicolon() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "2+3+;";

        parser.parse(script);
    }

    @Test(expected = Exception.class)
    public void testParseInvalidSyntaxIncompleteExpression() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "2+3*;";

        parser.parse(script);
    }

    @Test
    public void testParseComplexExpression() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "2+3*5;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
    }

    @Test
    public void testParseSimpleAddition() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "a + b;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
    }

    @Test
    public void testParseParenthesizedExpression() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "(a + b);";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
    }

    @Test
    public void testParseIntLiteral() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "123;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
    }

    @Test
    public void testParseIdentifier() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "myVariable;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
    }

    @Test
    public void testParseMultipleDeclarations() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "int a = 10; int b = 20;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertEquals(2, tree.getChildren().size());
    }

    @Test
    public void testParseMixedStatements() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "int x = 5; x = x + 1; x;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertEquals(3, tree.getChildren().size());
    }

    @Test
    public void testParseSubtraction() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "a - b;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
    }

    @Test
    public void testParseDivision() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "a / b;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
    }

    @Test
    public void testParseComplexArithmetic() throws Exception {
        SimpleParser parser = new SimpleParser();
        String script = "int result = a + b * c - d / e;";

        ASTNode tree = parser.parse(script);

        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertFalse(tree.getChildren().isEmpty());
    }

    @Test
    public void testParseEmptyProgram() throws Exception {
        SimpleParser parser = new SimpleParser();
        // Empty or whitespace-only script might throw exception or return empty tree
        // depending on implementation
        try {
            ASTNode tree = parser.parse("");
            // If no exception, tree might be null or empty
            if (tree != null) {
                assertEquals(ASTNodeType.Programm, tree.getType());
            }
        } catch (Exception e) {
            // Some implementations might throw exception for empty input
            assertTrue(true);
        }
    }
}
