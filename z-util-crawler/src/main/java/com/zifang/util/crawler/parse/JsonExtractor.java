package com.zifang.util.crawler.parse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON 路径提取器。
 * <p>
 * 支持点号路径语法（如 "data.name" 或 "items[0].title"），
 * 可提取单个值或所有匹配项。
 *
 * @author zifang
 */
public class JsonExtractor {

    private JsonExtractor() {
    }

    /**
     * 使用点号路径从 JSON 中提取单个值。
     *
     * @param jsonString JSON 字符串
     * @param jsonPath   点号路径
     * @return 提取的值，未找到或解析失败返回 null
     */
    public static String extract(String jsonString, String jsonPath) {
        try {
            Object value = navigatePath(jsonString, jsonPath);
            return value != null ? value.toString() : null;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 从 JSON 中提取指定路径处的所有值。
     *
     * @param jsonString JSON 字符串
     * @param jsonPath   点号路径
     * @return 提取的值列表
     */
    public static List<String> extractAll(String jsonString, String jsonPath) {
        List<String> results = new ArrayList<>();
        try {
            Object value = navigatePath(jsonString, jsonPath);
            if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (Object item : array) {
                    results.add(item.toString());
                }
            }
        } catch (ParseException e) {
            // return empty list
        }
        return results;
    }

    private static Object navigatePath(String jsonString, String jsonPath) throws ParseException {
        JSONParser parser = new JSONParser();
        Object root = parser.parse(jsonString);

        if (jsonPath == null) {
            throw new NullPointerException("jsonPath is null");
        }
        if (jsonPath.trim().isEmpty()) {
            return root;
        }

        String[] parts = jsonPath.split("\\.");
        Object current = root;

        for (String part : parts) {
            if (current == null) {
                return null;
            }
            if (part.isEmpty()) {
                continue;
            }

            // 处理 part 中的数组索引，如 "items[0]" → 先取 "items" 再取 [0]
            int bracketIdx = part.indexOf('[');
            String key = (bracketIdx >= 0) ? part.substring(0, bracketIdx) : part;
            String bracket = (bracketIdx >= 0) ? part.substring(bracketIdx) : null;

            // 先按键访问（如果 key 非空）
            if (!key.isEmpty()) {
                if (current instanceof JSONObject) {
                    JSONObject obj = (JSONObject) current;
                    if (!obj.containsKey(key)) {
                        return null;
                    }
                    current = obj.get(key);
                } else {
                    return null;
                }
            }

            // 再处理数组索引
            if (bracket != null) {
                if (current instanceof JSONArray) {
                    int index = parseArrayIndex(bracket);
                    if (index >= 0 && index < ((JSONArray) current).size()) {
                        current = ((JSONArray) current).get(index);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }

        return current;
    }

    private static int parseArrayIndex(String part) {
        int start = part.indexOf('[');
        int end = part.indexOf(']');
        if (start >= 0 && end > start) {
            try {
                return Integer.parseInt(part.substring(start + 1, end));
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
}
