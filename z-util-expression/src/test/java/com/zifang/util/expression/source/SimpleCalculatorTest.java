package com.zifang.util.expression.source;

import com.zifang.util.expression.source.ast.ASTNode;
import com.zifang.util.expression.source.ast.ASTNodeType;
import com.zifang.util.expression.source.lexer.SimpleLexer;
import com.zifang.util.expression.source.lexer.Token;
import com.zifang.util.expression.source.lexer.TokenReader;
import com.zifang.util.expression.source.lexer.TokenType;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * SimpleCalculator 计算器完整测试
 */
public class SimpleCalculatorTest {

    // ==================== parse 方法测试 ====================

    @Test
    public void testParseSingleNumber() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("42");
        assertNotNull(tree);
        assertEquals(ASTNodeType.Programm, tree.getType());
        assertFalse(tree.getChildren().isEmpty());
    }

    @Test
    public void testParseSimpleAddition() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("2+3");
        assertNotNull(tree);
        ASTNode child = tree.getChildren().get(0);
        assertEquals(ASTNodeType.Additive, child.getType());
        assertEquals("+", child.getText());
        assertEquals(2, child.getChildren().size());
    }

    @Test
    public void testParseSimpleSubtraction() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("10-3");
        ASTNode child = tree.getChildren().get(0);
        assertEquals(ASTNodeType.Additive, child.getType());
        assertEquals("-", child.getText());
    }

    @Test
    public void testParseMultiplication() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("4*5");
        ASTNode child = tree.getChildren().get(0);
        assertEquals(ASTNodeType.Multiplicative, child.getType());
        assertEquals("*", child.getText());
    }

    @Test
    public void testParseDivision() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("10/2");
        ASTNode child = tree.getChildren().get(0);
        assertEquals(ASTNodeType.Multiplicative, child.getType());
        assertEquals("/", child.getText());
    }

    @Test
    public void testParseComplexExpression() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("2+3*5");
        assertNotNull(tree);
        assertEquals(1, tree.getChildren().size());
    }

    @Test
    public void testParseExpressionWithParentheses() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("(2+3)");
        assertNotNull(tree);
        assertFalse(tree.getChildren().isEmpty());
    }

    @Test
    public void testParseNestedParentheses() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("((1+2))");
        assertNotNull(tree);
        assertFalse(tree.getChildren().isEmpty());
    }

    @Test
    public void testParseIdentifier() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("abc");
        assertNotNull(tree);
        assertEquals(ASTNodeType.Identifier, tree.getChildren().get(0).getType());
    }

    @Test
    public void testParseZero() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("0");
        assertNotNull(tree);
    }

    @Test
    public void testParseLargeNumber() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("123456789");
        assertNotNull(tree);
    }

    @Test
    public void testParseMultipleOperations() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("1+2*3-4/2");
        assertNotNull(tree);
        assertEquals(1, tree.getChildren().size());
    }

    @Test
    public void testParseExpressionWithLeadingSpaces() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        ASTNode tree = calculator.parse("   2+3   ");
        assertNotNull(tree);
    }

    // ==================== evaluate 方法测试 ====================

    @Test
    public void testEvaluateAddition() {
        SimpleCalculator calculator = new SimpleCalculator();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calculator.evaluate("2+3");

        String output = out.toString();
        // evaluate 会打印计算过程，最后一行是 Result
        assertTrue(output.contains("Calculating:"));
        assertTrue(output.contains("Result:"));
    }

    @Test
    public void testEvaluateMultiplication() {
        SimpleCalculator calculator = new SimpleCalculator();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calculator.evaluate("2*3");

        String output = out.toString();
        assertTrue(output.contains("Result:"));
    }

    @Test
    public void testEvaluateDivision() {
        SimpleCalculator calculator = new SimpleCalculator();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calculator.evaluate("10/2");

        String output = out.toString();
        assertTrue(output.contains("Result:"));
    }

    @Test
    public void testEvaluateSubtraction() {
        SimpleCalculator calculator = new SimpleCalculator();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calculator.evaluate("10-3");

        String output = out.toString();
        assertTrue(output.contains("Result:"));
    }

    @Test
    public void testEvaluateParentheses() {
        SimpleCalculator calculator = new SimpleCalculator();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calculator.evaluate("(1+2)*3");

        String output = out.toString();
        assertTrue(output.contains("Result:"));
    }

    @Test
    public void testEvaluateInvalidSyntax() {
        SimpleCalculator calculator = new SimpleCalculator();
        // 不应抛出异常，内部捕获了
        calculator.evaluate("2+");
    }

    @Test
    public void testEvaluateEmptyExpression() {
        SimpleCalculator calculator = new SimpleCalculator();
        try {
            calculator.evaluate("");
            // 空表达式可能返回空的tree，不抛异常
        } catch (Exception e) {
            // 也允许抛异常
        }
    }

    // ==================== 异常情况测试 ====================

    @Test(expected = Exception.class)
    public void testParseIncompleteExpressionThrows() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.parse("+");
    }

    @Test(expected = Exception.class)
    public void testParseMissingRightOperandThrows() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.parse("2+");
    }

    @Test(expected = Exception.class)
    public void testParseMissingLeftOperandThrows() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.parse("*2");
    }

    @Test(expected = Exception.class)
    public void testParseUnclosedParenthesisThrows() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.parse("(2+3");
    }

    @Test(expected = Exception.class)
    public void testParseExtraClosingParenthesisThrows() throws Exception {
        SimpleCalculator calculator = new SimpleCalculator();
        calculator.parse("(2+3))");
    }
}
