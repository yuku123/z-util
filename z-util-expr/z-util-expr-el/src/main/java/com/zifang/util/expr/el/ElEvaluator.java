package com.zifang.util.expr.el;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * EL (Expression Language) evaluator compatible with JSP EL subset.
 * Uses Spring Expression Language (SpEL) as the underlying implementation.
 *
 * <p>Supported features:</p>
 * <ul>
 *   <li>Arithmetic: + - * / %</li>
 *   <li>Comparison: == != > < >= <=</li>
 *   <li>Logical: && || !</li>
 *   <li>Ternary conditional: ? :</li>
 *   <li>Property access: bean.prop</li>
 *   <li>Map/List access: map['key'] / list[0]</li>
 *   <li>Method call: obj.method()</li>
 *   <li>Empty operator: empty</li>
 * </ul>
 */
public class ElEvaluator {

    private final ExpressionParser parser;
    private final Map<String, Object> variableMap;
    private StandardEvaluationContext evaluationContext;

    public ElEvaluator() {
        this.parser = new SpelExpressionParser();
        this.evaluationContext = new StandardEvaluationContext();
        this.variableMap = new HashMap<>();
    }

    /**
     * Evaluate an EL expression and return the result.
     *
     * @param expression the EL expression to evaluate
     * @return the result of the expression evaluation
     * @throws ElException if the expression evaluation fails
     */
    public Object eval(String expression) {
        try {
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(evaluationContext);
        } catch (Exception e) {
            throw new ElException("Failed to evaluate EL expression: " + expression, e);
        }
    }

    /**
     * Evaluate an EL expression with custom root object.
     *
     * @param expression the EL expression to evaluate
     * @param rootObject the root object to use for evaluation
     * @return the result of the expression evaluation
     * @throws ElException if the expression evaluation fails
     */
    public Object eval(String expression, Object rootObject) {
        try {
            StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
            // Add stored variables to the new context
            for (Map.Entry<String, Object> entry : variableMap.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(context);
        } catch (Exception e) {
            throw new ElException("Failed to evaluate EL expression: " + expression, e);
        }
    }

    /**
     * Evaluate an EL expression with a set of variables.
     *
     * @param expression the EL expression to evaluate
     * @param variables  the variables to set in the evaluation context
     * @return the result of the expression evaluation
     * @throws ElException if the expression evaluation fails
     */
    public Object eval(String expression, Map<String, Object> variables) {
        try {
            StandardEvaluationContext context = new StandardEvaluationContext();
            if (variables != null) {
                for (Map.Entry<String, Object> entry : variables.entrySet()) {
                    context.setVariable(entry.getKey(), entry.getValue());
                }
            }
            Expression exp = parser.parseExpression(expression);
            return exp.getValue(context);
        } catch (Exception e) {
            throw new ElException("Failed to evaluate EL expression: " + expression, e);
        }
    }

    /**
     * Set a variable in the default evaluation context.
     *
     * @param name  the variable name
     * @param value the variable value
     */
    public void setVariable(String name, Object value) {
        variableMap.put(name, value);
        evaluationContext.setVariable(name, value);
    }

    /**
     * Get a variable from the internal variable map.
     *
     * @param name the variable name
     * @return the variable value, or null if not found
     */
    public Object getVariable(String name) {
        return variableMap.get(name);
    }

    /**
     * Clear all variables from the default evaluation context.
     */
    public void clearVariables() {
        variableMap.clear();
        this.evaluationContext = new StandardEvaluationContext();
    }

    /**
     * Get all variables from the internal variable map.
     *
     * @return a map of all set variables
     */
    public Map<String, Object> getVariables() {
        return new HashMap<>(variableMap);
    }

    /**
     * Set multiple variables in the default evaluation context.
     *
     * @param variables a map of variable names to values
     */
    public void setVariables(Map<String, Object> variables) {
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                setVariable(entry.getKey(), entry.getValue());
            }
        }
    }
}
