package com.zifang.util.workflow.engine.runtime;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evaluates gateway conditions at runtime.
 * Supports simple EL-like expressions:
 * - ${var == value} - equality check
 * - ${var > 10} - numeric comparison
 * - ${var < 10} - numeric comparison
 * - ${var >= 10} - numeric comparison
 * - ${var <= 10} - numeric comparison
 * - ${!var} - negation
 * - ${var == "text"} - string comparison with quotes
 * - ${var != value} - not equal
 */
public class GatewayEvaluator {

    // Pattern to match expressions: ${...}
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * Evaluate a gateway condition expression against variables
     *
     * @param expression the expression to evaluate (e.g., "${approved == true}")
     * @param variables the runtime variables
     * @return true if the condition is satisfied, false otherwise
     */
    public boolean evaluate(String expression, Map<String, Object> variables) {
        if (expression == null || expression.trim().isEmpty()) {
            return true;
        }

        Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid expression format: " + expression);
        }

        String condition = matcher.group(1).trim();
        return evaluateCondition(condition, variables);
    }

    private boolean evaluateCondition(String condition, Map<String, Object> variables) {
        // Handle negation: !var
        if (condition.startsWith("!")) {
            String varName = condition.substring(1).trim();
            Object value = getVariableValue(varName, variables);
            return isFalse(value);
        }

        // Handle equality: var == value or var == "text"
        if (condition.contains("==")) {
            String[] parts = condition.split("==");
            if (parts.length == 2) {
                String varName = parts[0].trim();
                String expectedValue = parts[1].trim();
                Object actualValue = getVariableValue(varName, variables);
                return compareEquality(actualValue, expectedValue);
            }
        }

        // Handle not equal: var != value
        if (condition.contains("!=")) {
            String[] parts = condition.split("!=");
            if (parts.length == 2) {
                String varName = parts[0].trim();
                String expectedValue = parts[1].trim();
                Object actualValue = getVariableValue(varName, variables);
                return !compareEquality(actualValue, expectedValue);
            }
        }

        // Handle greater than: var > value
        if (condition.contains(">")) {
            String[] parts = condition.split(">");
            if (parts.length == 2) {
                String varName = parts[0].trim();
                String strValue = parts[1].trim();
                Object actualValue = getVariableValue(varName, variables);
                return compareNumeric(actualValue, strValue) > 0;
            }
        }

        // Handle less than: var < value
        if (condition.contains("<")) {
            // Make sure it's not <= or >=
            if (!condition.contains("<=")) {
                String[] parts = condition.split("<");
                if (parts.length == 2) {
                    String varName = parts[0].trim();
                    String strValue = parts[1].trim();
                    Object actualValue = getVariableValue(varName, variables);
                    return compareNumeric(actualValue, strValue) < 0;
                }
            }
        }

        // Handle greater than or equal: var >= value
        if (condition.contains(">=")) {
            String[] parts = condition.split(">=");
            if (parts.length == 2) {
                String varName = parts[0].trim();
                String strValue = parts[1].trim();
                Object actualValue = getVariableValue(varName, variables);
                return compareNumeric(actualValue, strValue) >= 0;
            }
        }

        // Handle less than or equal: var <= value
        if (condition.contains("<=")) {
            String[] parts = condition.split("<=");
            if (parts.length == 2) {
                String varName = parts[0].trim();
                String strValue = parts[1].trim();
                Object actualValue = getVariableValue(varName, variables);
                return compareNumeric(actualValue, strValue) <= 0;
            }
        }

        // Simple variable reference: var (truthy check)
        Object value = getVariableValue(condition, variables);
        return !isFalse(value);
    }

    private Object getVariableValue(String varName, Map<String, Object> variables) {
        if (variables == null || !variables.containsKey(varName)) {
            return null;
        }
        return variables.get(varName);
    }

    private boolean isFalse(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof Boolean) {
            return !((Boolean) value);
        }
        if (value instanceof String) {
            String str = ((String) value).toLowerCase();
            return str.isEmpty() || str.equals("false") || str.equals("null");
        }
        return false;
    }

    private boolean compareEquality(Object actualValue, String expectedValue) {
        // Handle quoted string values
        if (expectedValue.startsWith("\"") && expectedValue.endsWith("\"")) {
            String expected = expectedValue.substring(1, expectedValue.length() - 1);
            if (actualValue == null) {
                return false;
            }
            return actualValue.toString().equals(expected);
        }

        // Handle boolean values
        if (expectedValue.equalsIgnoreCase("true") || expectedValue.equalsIgnoreCase("false")) {
            boolean expected = Boolean.parseBoolean(expectedValue);
            if (actualValue instanceof Boolean) {
                return ((Boolean) actualValue) == expected;
            }
            return false;
        }

        // Handle null
        if (expectedValue.equalsIgnoreCase("null")) {
            return actualValue == null;
        }

        // Handle numeric comparison
        if (actualValue instanceof Number) {
            try {
                double actual = ((Number) actualValue).doubleValue();
                double expected = Double.parseDouble(expectedValue);
                return actual == expected;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // String comparison
        return actualValue != null && actualValue.toString().equals(expectedValue);
    }

    private int compareNumeric(Object actualValue, String strValue) {
        if (actualValue == null) {
            throw new IllegalArgumentException("Cannot compare null value numerically");
        }

        double actual;
        if (actualValue instanceof Number) {
            actual = ((Number) actualValue).doubleValue();
        } else {
            try {
                actual = Double.parseDouble(actualValue.toString());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Cannot convert value to number: " + actualValue);
            }
        }

        double expected;
        try {
            expected = Double.parseDouble(strValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse expected value as number: " + strValue);
        }

        return Double.compare(actual, expected);
    }
}