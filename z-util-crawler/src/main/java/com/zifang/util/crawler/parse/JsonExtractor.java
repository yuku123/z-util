package com.zifang.util.crawler.parse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * JSON extractor using json-simple for JSON path extraction.
 */
public class JsonExtractor {

    private JsonExtractor() {
    }

    /**
     * Extract a single value from JSON using a dot-notation path.
     * Example path: "data.name" or "items[0].title"
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
     * Extract all values from JSON array at the given path.
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
            if (current instanceof JSONObject) {
                JSONObject obj = (JSONObject) current;
                if (obj.containsKey(part)) {
                    current = obj.get(part);
                } else {
                    return null;
                }
            } else if (current instanceof JSONArray) {
                JSONArray array = (JSONArray) current;
                int index = parseArrayIndex(part);
                if (index >= 0 && index < array.size()) {
                    current = array.get(index);
                } else {
                    return null;
                }
            } else {
                return null;
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
