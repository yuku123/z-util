package com.zifang.util.json.dsl;

import com.zifang.util.json.model.JsonObject;
import com.zifang.util.json.model.JsonArray;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 手写 JsonPath 执行器，不依赖有问题的 DynamicParser G4 引擎。
 * 支持: $.key, $.arr[0], $..key, $.arr[*], $.arr[1:3]
 */
public class JsonPathParser {

    public List<Object> query(String json, String path) {
        Object doc = new DslJsonParser().parse(json);
        List<Object> results = new ArrayList<>();
        int pos = path.charAt(0) == '$' ? 1 : 0;
        evaluate(doc, path, pos, results);
        return results;
    }

    private void evaluate(Object current, String path, int pos, List<Object> results) {
        if (current == null) return;
        if (pos >= path.length()) {
            results.add(current);
            return;
        }

        char ch = path.charAt(pos);

        if (ch == '.') {
            if (pos + 1 < path.length() && path.charAt(pos + 1) == '.') {
                // 递归下降 $..
                int next = pos + 2;
                if (next >= path.length()) {
                    collectAll(current, results);
                    return;
                }
                char nc = path.charAt(next);
                if (nc == '*') {
                    collectAll(current, results);
                    return;
                }
                if (nc == '[') {
                    evaluate(current, path, next, results);
                    return;
                }
                String name = readName(path, next);
                int end = next + name.length();
                deepScan(current, name, results);
                if (end < path.length()) {
                    evaluate(null, path, end, results);
                }
                return;
            }

            // 点访问 $.name
            // 先判断是否是 $.* 通配符（通配符在 readName 里会被当作普通名字）
            if (pos + 1 < path.length() && path.charAt(pos + 1) == '*') {
                List<Object> all = getAllChildren(current);
                for (Object item : all) {
                    results.add(item);
                }
                return;
            }
            String name = readName(path, pos + 1);
            int end = pos + 1 + name.length();
            Object child = getChild(current, name);
            if (child == NOT_FOUND) return; // key 不存在
            if (end < path.length()) {
                evaluate(child, path, end, results);
            } else {
                results.add(child);
            }
            return;
        }

        if (ch == '[') {
            int close = findMatchingBracket(path, pos);
            String inside = path.substring(pos + 1, close);
            int next = close + 1;

            if (inside.equals("*")) {
                List<Object> all = getAllChildren(current);
                for (Object item : all) {
                    if (next < path.length()) {
                        evaluate(item, path, next, results);
                    } else {
                        results.add(item);
                    }
                }
            } else if (inside.startsWith("?")) {
                // 过滤表达式 [?(@.price < 10)]
                List<Object> items = getAllChildren(current);
                for (Object item : items) {
                    if (matchesFilter(item, inside.substring(1))) {
                        if (next < path.length()) {
                            evaluate(item, path, next, results);
                        } else {
                            results.add(item);
                        }
                    }
                }
            } else if (inside.contains(":")) {
                // 切片 [1:3]
                List<Object> slice = evalSlice(current, inside);
                for (Object item : slice) {
                    if (next < path.length()) {
                        evaluate(item, path, next, results);
                    } else {
                        results.add(item);
                    }
                }
            } else {
                // 数字索引
                int idx = Integer.parseInt(inside);
                Object item = getArrayElement(current, idx);
                if (item != null) {
                    if (next < path.length()) {
                        evaluate(item, path, next, results);
                    } else {
                        results.add(item);
                    }
                }
            }
            return;
        }

        if (ch == '@') {
            if (pos + 1 < path.length() && path.charAt(pos + 1) == '[') {
                evaluate(current, path, pos + 1, results);
            }
            return;
        }
    }

    private String readName(String path, int start) {
        StringBuilder sb = new StringBuilder();
        int i = start;
        while (i < path.length()) {
            char c = path.charAt(i);
            if (c == '.' || c == '[' || Character.isWhitespace(c)) break;
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    private int findMatchingBracket(String s, int start) {
        int depth = 1;
        for (int i = start + 1; i < s.length(); i++) {
            if (s.charAt(i) == ']') {
                depth--;
                if (depth == 0) return i;
            } else if (s.charAt(i) == '[') {
                depth++;
            }
        }
        return s.length() - 1;
    }

    private Object getChild(Object current, String name) {
        if (current instanceof JsonObject) {
            JsonObject obj = (JsonObject) current;
            boolean exists = obj.getAllKeyValue().stream().anyMatch(e -> e.getKey().equals(name));
            if (!exists) return NOT_FOUND;
            return obj.get(name);
        }
        return null;
    }

    /** 别名：表示 key 存在但值为 null，与 key 不存在区分开 */
    private static final Object NOT_FOUND = new Object();

    private Object getArrayElement(Object current, int idx) {
        if (current instanceof JsonArray) {
            JsonArray arr = (JsonArray) current;
            if (idx >= 0 && idx < arr.size()) return arr.get(idx);
        } else if (current instanceof JsonObject) {
            JsonObject obj = (JsonObject) current;
            int i = 0;
            for (Object v : obj.getAllKeyValue().stream().map(e -> e.getValue()).collect(Collectors.toList())) {
                if (i++ == idx) return v;
            }
        }
        return null;
    }

    private List<Object> getAllChildren(Object current) {
        List<Object> result = new ArrayList<>();
        if (current instanceof JsonObject) {
            JsonObject obj = (JsonObject) current;
            for (Object v : obj.getAllKeyValue().stream().map(e -> e.getValue()).collect(Collectors.toList())) {
                result.add(v);
            }
        } else if (current instanceof JsonArray) {
            JsonArray arr = (JsonArray) current;
            for (Object item : arr) result.add(item);
        }
        return result;
    }

    private void collectAll(Object current, List<Object> results) {
        if (current instanceof JsonObject) {
            JsonObject obj = (JsonObject) current;
            for (Object v : obj.getAllKeyValue().stream().map(e -> e.getValue()).collect(Collectors.toList())) {
                results.add(v);
                collectAll(v, results);
            }
        } else if (current instanceof JsonArray) {
            JsonArray arr = (JsonArray) current;
            for (Object item : arr) {
                results.add(item);
                collectAll(item, results);
            }
        }
    }

    private void deepScan(Object current, String name, List<Object> results) {
        if (current instanceof JsonObject) {
            JsonObject obj = (JsonObject) current;
            for (Map.Entry<String, Object> e : obj.getAllKeyValue()) {
                if (e.getKey().equals(name)) results.add(e.getValue());
                deepScan(e.getValue(), name, results);
            }
        } else if (current instanceof JsonArray) {
            JsonArray arr = (JsonArray) current;
            for (Object item : arr) deepScan(item, name, results);
        }
    }

    private List<Object> evalSlice(Object current, String inside) {
        List<Object> result = new ArrayList<>();
        if (!(current instanceof JsonArray)) return result;
        JsonArray arr = (JsonArray) current;
        String[] parts = inside.split(":");
        int start = 0, end = arr.size(), step = 1;
        if (parts.length >= 1 && !parts[0].isEmpty()) start = Integer.parseInt(parts[0].trim());
        if (parts.length >= 2 && !parts[1].isEmpty()) end = Integer.parseInt(parts[1].trim());
        if (parts.length >= 3 && !parts[2].isEmpty()) step = Integer.parseInt(parts[2].trim());
        for (int i = start; i < end; i += step) {
            if (i >= 0 && i < arr.size()) result.add(arr.get(i));
        }
        return result;
    }

    private boolean matchesFilter(Object item, String filter) {
        // 解析 @.field op value
        // 例如: (@.price < 10)
        filter = filter.trim();
        if (filter.startsWith("(")) filter = filter.substring(1);
        if (filter.endsWith(")")) filter = filter.substring(0, filter.length() - 1);

        // 支持简单比较: @.field < value 或 @.field == value
        int opPos = -1;
        String op = null;
        for (String o : Arrays.asList(">=", "<=", "!=", "==", ">", "<")) {
            int p = filter.indexOf(o);
            if (p > 0) {
                opPos = p;
                op = o;
                break;
            }
        }
        if (opPos <= 0) return true;

        String left = filter.substring(0, opPos).trim();
        String right = filter.substring(opPos + op.length()).trim();

        String fieldName = left.startsWith("@.") ? left.substring(2) : left;
        Object fieldValue = getChild(item, fieldName);
        Object compareValue = parseValue(right);

        if (fieldValue == null) return false;
        if (!(fieldValue instanceof Comparable)) return false;

        @SuppressWarnings("unchecked")
        Comparable<Object> fv = (Comparable<Object>) fieldValue;
        int cmp = fv.compareTo(compareValue);

        switch (op) {
            case "==": return cmp == 0;
            case "!=": return cmp != 0;
            case ">":  return cmp > 0;
            case "<":  return cmp < 0;
            case ">=": return cmp >= 0;
            case "<=": return cmp <= 0;
            default: return false;
        }
    }

    private Object parseValue(String s) {
        s = s.trim();
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            return s.substring(1, s.length() - 1);
        }
        try {
            if (s.contains(".")) return Double.parseDouble(s);
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return s;
        }
    }
}
