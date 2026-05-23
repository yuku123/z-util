package com.zifang.util.json.dsl;

import com.zifang.util.json.exception.JsonParseException;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;

/**
 * JSON 解析器，将 JSON 字符串解析为 JsonObject 或 JsonArray。
 * <p>
 * 对标 Gson's {@code JsonParser}，提供静态入口方法。
 *
 * @author zifang
 */
public class JsonParser {

    /**
     * 将 JSON 字符串解析为 JsonObject。
     *
     * @param json JSON 字符串
     * @return JsonObject
     * @throws JsonParseException 如果格式非法
     */
    public static JsonObject parseString(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new JsonObject();
        }
        try {
            Object result = new com.zifang.util.json.JSONParser().fromJSON(json);
            if (result instanceof JsonObject) {
                return (JsonObject) result;
            }
            if (result instanceof JsonArray) {
                throw new JsonParseException("JSON is an array, not an object");
            }
            throw new JsonParseException("Unexpected root type: " + result.getClass().getSimpleName());
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException("Failed to parse JSON: " + e.getMessage());
        }
    }

    /**
     * 将 JSON 字符串解析为 JsonArray。
     *
     * @param json JSON 字符串
     * @return JsonArray
     * @throws JsonParseException 如果格式非法
     */
    public static JsonArray parseArray(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new JsonArray();
        }
        try {
            Object result = new com.zifang.util.json.JSONParser().fromJSON(json);
            if (result instanceof JsonArray) {
                return (JsonArray) result;
            }
            if (result instanceof JsonObject) {
                throw new JsonParseException("JSON is an object, not an array");
            }
            throw new JsonParseException("Unexpected root type: " + result.getClass().getSimpleName());
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException("Failed to parse JSON: " + e.getMessage());
        }
    }

    /**
     * 将 JSON 字符串解析为 JsonObject 或 JsonArray（自动判断）。
     *
     * @param json JSON 字符串
     * @return JsonObject 或 JsonArray
     * @throws JsonParseException 如果格式非法
     */
    public static Object parse(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new JsonObject();
        }
        try {
            return new com.zifang.util.json.JSONParser().fromJSON(json);
        } catch (JsonParseException e) {
            throw e;
        } catch (Exception e) {
            throw new JsonParseException("Failed to parse JSON: " + e.getMessage());
        }
    }
}
