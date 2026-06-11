package com.zifang.util.workflow.engine.runtime;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * 网关条件评估器。
 * 基于 Spring Expression Language (SpEL) 实现，支持完整的表达式语法：
 * <ul>
 *   <li>比较运算符: ==, !=, &lt;, &gt;, &lt;=, &gt;=</li>
 *   <li>逻辑运算符: &amp;&amp;, ||, !</li>
 *   <li>字符串匹配: 'text', "text"</li>
 *   <li>括号分组: (expr)</li>
 *   <li>方法调用: object.method()</li>
 *   <li>三目运算符: condition ? trueVal : falseVal</li>
 * </ul>
 *
 * 输入格式: ${expression}，例如:
 * <ul>
 *   <li>${level >= 1}</li>
 *   <li>${amount > 1000 && amount <= 5000}</li>
 *   <li>${status == 'approved'}</li>
 *   <li>${!isDisabled}</li>
 * </ul>
 *
 * @see WorkflowRuntimeEngine
 */
public class GatewayEvaluator {

    private final ExpressionParser expressionParser;

    /**
     * GatewayEvaluator方法。
     */
    public GatewayEvaluator() {
        this.expressionParser = new SpelExpressionParser();
    }

    /**
     * Evaluate a gateway condition expression against variables.
     *
     * @param expression the expression to evaluate (e.g., "${approved == true}" or "${amount > 1000 && amount <= 5000}")
     * @param variables  the runtime variables available in the expression context
     * @return true if the condition is satisfied, false otherwise
     * @throws IllegalArgumentException if the expression format is invalid
     */
    public boolean evaluate(String expression, Map<String, Object> variables) {
        if (expression == null || expression.trim().isEmpty()) {
            return true;
        }

        String spelExpression = toSpEL(expression);

        EvaluationContext context = createContext(variables);

        try {
            Expression exp = expressionParser.parseExpression(spelExpression);
            Object result = exp.getValue(context);

            if (result instanceof Boolean) {
                return (Boolean) result;
            }
            if (result == null) {
                return false;
            }
            return Boolean.parseBoolean(result.toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to evaluate expression: " + expression, e);
        }
    }

    /**
     * 将 ${...} 格式转换为 SpEL 表达式。
     * <p>
     * 例如: ${level >= 1} -> #level >= 1
     * <p>
     * SpEL 中 # 前缀用于引用 setVariable() 设置的变量。
     */
    private String toSpEL(String expression) {
        String trimmed = expression.trim();
        String inner;
        if (trimmed.startsWith("${") && trimmed.endsWith("}")) {
            inner = trimmed.substring(2, trimmed.length() - 1).trim();
        } else {
            inner = trimmed;
        }

        // 将变量名转换为 SpEL 的 #variableName 格式
        // 例如: "level >= 1" -> "#level >= 1"
        // 例如: "amount > 1000 && amount <= 5000" -> "#amount > 1000 && #amount <= 5000"
        return replaceVariablesWithHashPrefix(inner);
    }

    /**
     * 为表达式中的变量名添加 # 前缀。
     * 使用简单的标识符检测来避免替换字符串字面量中的内容，
     * 以及方法调用中的方法名（identifier 后紧跟 '('）。
     */
    private String replaceVariablesWithHashPrefix(String expression) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = expression.length();

        while (i < len) {
            char ch = expression.charAt(i);

            // 检测字符串字面量（单引号或双引号），跳过其内容
            if (ch == '\'' || ch == '"') {
                char quote = ch;
                result.append(ch);
                i++;
                while (i < len) {
                    char c = expression.charAt(i);
                    result.append(c);
                    if (c == quote && (i == 0 || expression.charAt(i - 1) != '\\')) {
                        i++;
                        break;
                    }
                    i++;
                }
                continue;
            }

            // 检测标识符开始（字母或下划线）
            if (Character.isLetter(ch) || ch == '_') {
                StringBuilder identifier = new StringBuilder();
                int start = i;
                while (i < len && (Character.isLetterOrDigit(expression.charAt(i)) || expression.charAt(i) == '_')) {
                    identifier.append(expression.charAt(i));
                    i++;
                }
                String ident = identifier.toString();

                // 标识符后紧跟 '(' 说明是方法调用，不加 # 前缀
                if (i < len && expression.charAt(i) == '(') {
                    result.append(ident);
                    continue;
                }

                // SpEL 关键字不需要加 #
                if (isSpelKeyword(ident)) {
                    result.append(ident);
                } else {
                    result.append('#').append(ident);
                }
                continue;
            }

            // 其他字符（操作符、括号、空格等）
            result.append(ch);
            i++;
        }

        return result.toString();
    }

    private boolean isSpelKeyword(String word) {
        return word.equals("true") || word.equals("false") || word.equals("null") || word.equals("and")
                || word.equals("or") || word.equals("not") || word.equals("div") || word.equals("mod");
    }

    /**
     * 创建 SpEL 评估上下文，将 Map 中的变量注入到上下文中。
     */
    private EvaluationContext createContext(Map<String, Object> variables) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (variables != null) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }
}
