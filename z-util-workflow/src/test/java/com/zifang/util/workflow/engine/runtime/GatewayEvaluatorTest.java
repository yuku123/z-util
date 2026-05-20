package com.zifang.util.workflow.engine.runtime;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * GatewayEvaluator 表达式评估器测试。
 * 测试各种条件表达式类型。
 * 
 * 注意：当前实现支持的表达式格式：
 * - ${var == value} - 相等比较
 * - ${var > value} - 大于比较
 * - ${var < value} - 小于比较
 * - ${var != value} - 不等于
 * - ${!var} - 取反
 * - ${var} - 单变量（真值）
 */
public class GatewayEvaluatorTest {

    private GatewayEvaluator evaluator;

    @Before
    public void setUp() {
        evaluator = new GatewayEvaluator();
    }

    // ===== 基础表达式测试 =====

    @Test
    public void testEvaluateSpEL_BooleanTrue() {
        String expression = "${approved == true}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);

        boolean result = evaluator.evaluate(expression, variables);

        assertTrue(result);
    }

    @Test
    public void testEvaluateSpEL_BooleanFalse() {
        String expression = "${approved == true}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", false);

        boolean result = evaluator.evaluate(expression, variables);

        assertFalse(result);
    }

    @Test
    public void testEvaluateSpEL_NumericGreaterThan() {
        String expression = "${amount > 1000}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("amount", 500);
        assertFalse(evaluator.evaluate(expression, variables));

        variables.put("amount", 1001);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("amount", 1000);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluateSpEL_LessThan() {
        String expression = "${temperature < 0}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("temperature", -1);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("temperature", 0);
        assertFalse(evaluator.evaluate(expression, variables));

        variables.put("temperature", 1);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluateSpEL_NotEqual() {
        String expression = "${type != cancelled}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("type", "active");
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("type", "cancelled");
        assertFalse(evaluator.evaluate(expression, variables));
    }

    // ===== 单变量测试 =====

    @Test
    public void testEvaluate_SingleVariableTrue() {
        String expression = "${isActive}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("isActive", true);

        assertTrue(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_SingleVariableFalse() {
        String expression = "${isActive}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("isActive", false);

        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_Negation() {
        String expression = "${!isDisabled}";
        Map<String, Object> variables = new HashMap<>();

        variables.put("isDisabled", false);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("isDisabled", true);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    // ===== 边界条件测试 =====

    @Test
    public void testEvaluateWithNullVariable() {
        String expression = "${field != null}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("field", null);

        assertFalse(evaluator.evaluate(expression, variables));

        variables.put("field", "value");
        assertTrue(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluateWithMissingVariable() {
        String expression = "${unknownField == true}";
        Map<String, Object> variables = new HashMap<>();

        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluateWithZeroValue() {
        String expression = "${count > 0}";
        Map<String, Object> variables = new HashMap<>();

        variables.put("count", 0);
        assertFalse(evaluator.evaluate(expression, variables));

        variables.put("count", 1);
        assertTrue(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluateWithNegativeNumber() {
        String expression = "${balance < 0}";
        Map<String, Object> variables = new HashMap<>();

        variables.put("balance", -100);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("balance", 0);
        assertFalse(evaluator.evaluate(expression, variables));

        variables.put("balance", 100);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluateWithDecimalNumber() {
        String expression = "${price > 9.99}";
        Map<String, Object> variables = new HashMap<>();

        variables.put("price", 9.99);
        assertFalse(evaluator.evaluate(expression, variables));

        variables.put("price", 10.00);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("price", 10.001);
        assertTrue(evaluator.evaluate(expression, variables));
    }

    // ===== 特殊表达式格式测试 =====

    @Test
    public void testEvaluate_NullExpression_ReturnsTrue() {
        Map<String, Object> variables = new HashMap<>();

        assertTrue(evaluator.evaluate(null, variables));
    }

    @Test
    public void testEvaluate_EmptyExpression_ReturnsTrue() {
        Map<String, Object> variables = new HashMap<>();

        assertTrue(evaluator.evaluate("", variables));
    }

    // ===== 错误处理测试 =====

    @Test
    public void testEvaluate_InvalidExpressionFormat_NoBraces() {
        // 无大括号的表达式格式无效
        String expression = "no braces expression";
        Map<String, Object> variables = new HashMap<>();

        try {
            evaluator.evaluate(expression, variables);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // ===== 性能测试 =====

    @Test
    public void testEvaluate_ManyIterations() {
        String expression = "${count > 50}";
        Map<String, Object> variables = new HashMap<>();
        variables.put("count", 75);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            evaluator.evaluate(expression, variables);
        }
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < 1000);
    }

    // ===== 业务场景测试 =====

    @Test
    public void testEvaluate_LevelBasedApproval() {
        String expression = "${level > 0}";
        Map<String, Object> variables = new HashMap<>();

        variables.put("level", 1);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("level", 0);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_AmountRangeBasedRouting() {
        String expression = "${amount < 1001}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("amount", 500);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("amount", 1001);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_CreditScore() {
        String expression = "${creditScore > 749}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("creditScore", 800);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("creditScore", 749);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_InventoryCheck() {
        String expression = "${stock > 0}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("stock", 100);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("stock", 0);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_TimeWindow() {
        String expression = "${hour < 18}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("hour", 12);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("hour", 19);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_VIPCheck() {
        String expression = "${isVIP == true}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("isVIP", true);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("isVIP", false);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_PurchaseAmount() {
        String expression = "${purchaseAmount > 999}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("purchaseAmount", 1500);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("purchaseAmount", 500);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_AgeCheck() {
        String expression = "${age > 17}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("age", 25);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("age", 17);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_AccountBalance() {
        String expression = "${balance > 99}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("balance", 500);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("balance", 50);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_StatusCheck() {
        String expression = "${status == approved}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("status", "approved");
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("status", "pending");
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_TemperatureFreezing() {
        String expression = "${temperature < 0}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("temperature", -5);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("temperature", 0);
        assertFalse(evaluator.evaluate(expression, variables));
    }

    @Test
    public void testEvaluate_DiscountEligibility() {
        String expression = "${spending > 100}";

        Map<String, Object> variables = new HashMap<>();

        variables.put("spending", 150);
        assertTrue(evaluator.evaluate(expression, variables));

        variables.put("spending", 99);
        assertFalse(evaluator.evaluate(expression, variables));
    }
}
