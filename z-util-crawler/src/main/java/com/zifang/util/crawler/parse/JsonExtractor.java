package com.zifang.util.crawler.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自研 JSON 路径提取器。
 * <p>
 * 支持点号路径语法（如 "data.name" 或 "items[0].title"），
 * 支持方括号语法（"items[0]" 等同于 "items.0"），
 * 可提取单个值或所有匹配项。
 */
public class JsonExtractor {

    private JsonExtractor() {}

    public static String extract(String jsonString, String jsonPath) {
        try {
            Object value = navigatePath(jsonString, jsonPath);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> extractAll(String jsonString, String jsonPath) {
        List<String> results = new ArrayList<>();
        try {
            Object value = navigatePath(jsonString, jsonPath);
            if (value instanceof List) {
                for (Object item : (List<?>) value) {
                    results.add(item == null ? null : item.toString());
                }
            }
        } catch (Exception e) {
        }
        return results;
    }

    private static Object navigatePath(String jsonString, String jsonPath) {
        Object root = SimpleJsonParser.parse(jsonString);
        if (root == null) return null;
        String[] parts = jsonPath.split("\\.");
        Object current = root;
        for (String part : parts) {
            if (current == null) return null;
            for (String segment : splitBracket(part)) {
                if (current == null) return null;
                if (current instanceof Map) {
                    Map<?, ?> obj = (Map<?, ?>) current;
                    if (!obj.containsKey(segment)) return null;
                    current = obj.get(segment);
                } else if (current instanceof List) {
                    List<?> array = (List<?>) current;
                    int index = parseArrayIndex(segment);
                    if (index >= 0 && index < array.size()) current = array.get(index);
                    else return null;
                } else {
                    return null;
                }
            }
        }
        return current;
    }

    private static String[] splitBracket(String part) {
        int bracket = part.indexOf('[');
        if (bracket < 0) return new String[]{part};
        String key = part.substring(0, bracket);
        List<String> result = new ArrayList<>();
        result.add(key);
        int pos = bracket;
        while (pos < part.length()) {
            if (part.charAt(pos) == '[') {
                int close = part.indexOf(']', pos);
                if (close > pos + 1) result.add(part.substring(pos + 1, close));
                pos = close + 1;
            } else {
                pos++;
            }
        }
        return result.toArray(new String[0]);
    }

    private static int parseArrayIndex(String part) {
        try { return Integer.parseInt(part); } catch (NumberFormatException e) { return -1; }
    }

    static final class SimpleJsonParser {
        private final String src;
        private int pos;

        static Object parse(String src) {
            if (src == null) return null;
            SimpleJsonParser p = new SimpleJsonParser(src);
            p.skipWs();
            Object v = p.parseValue();
            p.skipWs();
            if (p.pos < p.src.length()) {
                throw new RuntimeException("Unexpected char at " + p.pos);
            }
            return v;
        }

        private SimpleJsonParser(String src) { this.src = src; }

        private void skipWs() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) pos++;
        }

        private Object parseValue() {
            skipWs();
            if (pos >= src.length()) throw new RuntimeException("Unexpected end");
            char c = src.charAt(pos);
            if (c == '{') return parseObject();
            if (c == '[') return parseArray();
            if (c == '"') return parseString();
            if (c == 't' || c == 'f') return parseBoolean();
            if (c == 'n') return parseNull();
            return parseNumber();
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new java.util.LinkedHashMap<>();
            pos++;
            skipWs();
            if (pos < src.length() && src.charAt(pos) == '}') { pos++; return map; }
            while (true) {
                skipWs();
                if (src.charAt(pos) != '"') throw new RuntimeException("Expected string at " + pos);
                String key = parseString();
                skipWs();
                if (src.charAt(pos) != ':') throw new RuntimeException("Expected ':' at " + pos);
                pos++;
                Object value = parseValue();
                map.put(key, value);
                skipWs();
                if (pos < src.length() && src.charAt(pos) == ',') { pos++; continue; }
                if (pos < src.length() && src.charAt(pos) == '}') { pos++; return map; }
                throw new RuntimeException("Expected ',' or '}' at " + pos);
            }
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            pos++;
            skipWs();
            if (pos < src.length() && src.charAt(pos) == ']') { pos++; return list; }
            while (true) {
                list.add(parseValue());
                skipWs();
                if (pos < src.length() && src.charAt(pos) == ',') { pos++; continue; }
                if (pos < src.length() && src.charAt(pos) == ']') { pos++; return list; }
                throw new RuntimeException("Expected ',' or ']' at " + pos);
            }
        }

        private String parseString() {
            pos++;
            StringBuilder sb = new StringBuilder();
            while (pos < src.length() && src.charAt(pos) != '"') {
                char c = src.charAt(pos);
                if (c == '\\' && pos + 1 < src.length()) {
                    char next = src.charAt(pos + 1);
                    switch (next) {
                        case '"': sb.append('"'); break;
                        case '\\': sb.append('\\'); break;
                        case '/': sb.append('/'); break;
                        case 'b': sb.append('\b'); break;
                        case 'f': sb.append('\f'); break;
                        case 'n': sb.append('\n'); break;
                        case 'r': sb.append('\r'); break;
                        case 't': sb.append('\t'); break;
                        case 'u':
                            if (pos + 5 >= src.length()) throw new RuntimeException("Bad unicode escape");
                            sb.append((char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16));
                            pos += 4;
                            break;
                        default: sb.append(next);
                    }
                    pos += 2;
                } else {
                    sb.append(c);
                    pos++;
                }
            }
            if (pos >= src.length()) throw new RuntimeException("Unterminated string");
            pos++;
            return sb.toString();
        }

        private Boolean parseBoolean() {
            if (src.startsWith("true", pos)) { pos += 4; return Boolean.TRUE; }
            if (src.startsWith("false", pos)) { pos += 5; return Boolean.FALSE; }
            throw new RuntimeException("Expected boolean at " + pos);
        }

        private Object parseNull() {
            if (src.startsWith("null", pos)) { pos += 4; return null; }
            throw new RuntimeException("Expected null at " + pos);
        }

        private Object parseNumber() {
            int start = pos;
            if (pos < src.length() && (src.charAt(pos) == '-' || src.charAt(pos) == '+')) pos++;
            while (pos < src.length() && "0123456789.eE+-".indexOf(src.charAt(pos)) >= 0) pos++;
            String num = src.substring(start, pos);
            if (num.contains(".") || num.contains("e") || num.contains("E")) return Double.valueOf(num);
            try { return Long.valueOf(num); } catch (NumberFormatException e) { return Double.valueOf(num); }
        }
    }
}
