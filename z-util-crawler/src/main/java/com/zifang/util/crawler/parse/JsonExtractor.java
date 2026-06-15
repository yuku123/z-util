package com.zifang.util.crawler.parse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 json-simple 进行 JSON 路径提取的 JSON 解析器。
 * <p>
 * 支持点号路径语法（如 "data.name" 或 "items[0].title"），
 * 支持方括号语法（"items[0]" 等同于 "items.0"），
 * 可提取单个值或所有匹配项，用于结构化数据的快速提取。
 *
 * @author zifang
 * @version 1.0.0
 */
public class JsonExtractor {

    private JsonExtractor() {
    }

    /**
     * 使用点号路径从 JSON 中提取单个值。
     * 路径示例："data.name"、"items[0].title"、"items[0]"
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

        String[] parts = jsonPath.split("\\.");
        Object current = root;

        for (String part : parts) {
            if (current == null) {
                return null;
            }
            // Split "items[0]" into ["items", "0"]
            String[] segments = splitBracket(part);
            for (String segment : segments) {
                if (current == null) {
                    return null;
                }
                if (current instanceof JSONObject) {
                    JSONObject obj = (JSONObject) current;
                    if (!obj.containsKey(segment)) {
                        return null;
                    }
                    current = obj.get(segment);
                } else if (current instanceof JSONArray) {
                    JSONArray array = (JSONArray) current;
                    int index = parseArrayIndex(segment);
                    if (index >= 0 && index < array.size()) {
                        current = array.get(index);
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

    /**
     * 将 "items[0]" 拆分为 ["items", "0"]，将 "name" 拆分为 ["name"]。
     */
    private static String[] splitBracket(String part) {
        int bracket = part.indexOf('[');
        if (bracket < 0) {
            return new String[]{part};
        }
        // part = "items[0][1]" → ["items", "0", "1"]
        String key = part.substring(0, bracket);
        List<String> result = new ArrayList<>();
        result.add(key);
        int pos = bracket;
        while (pos < part.length()) {
            if (part.charAt(pos) == '[') {
                int close = part.indexOf(']', pos);
                if (close > pos + 1) {
                    result.add(part.substring(pos + 1, close));
                }
                pos = close + 1;
            } else {
                pos++;
            }
        }
        return result.toArray(new String[0]);
    }

    private static int parseArrayIndex(String part) {
        try {
            return Integer.parseInt(part);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
