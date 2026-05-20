package com.zifang.util.json;

import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.dsl.JsonPathParser;

import java.util.List;
import java.util.Map;

/**
 * JSON 工具类，提供 JSON 序列化与反序列化、JSONPath 查询等功能。
 *
 * @author zifang
 */
public class JsonUtil {

    /**
     * 将对象序列化为 JSON 字符串。
     *
     * @param t 待序列化的对象，支持普通对象、{@link List} 和 {@link Map} 类型
     * @param <T> 对象类型
     * @return JSON 字符串，序列化失败时返回 null
     */
    public static <T> String toJson(T t) {

        if (t.getClass().isAssignableFrom(List.class)) {
            return solveList(t);
        } else if (t.getClass().isAssignableFrom(Map.class)) {
            return solveMap(t);
        } else {
            return solveObject(t);
        }
    }

    private static <T> String solveMap(T t) {
        return null;
    }

    private static <T> String solveList(T t) {
        return null;
    }

    private static <T> String solveObject(T t) {
        return null;
    }


    /**
     * 将 JSON 字符串反序列化为对象。
     *
     * @param jsonStr JSON 字符串
     * @param clazz   类型引用，用于指定反序列化的目标类型
     * @param <T>     泛型参数，反序列化的目标类型
     * @return 反序列化后的对象，若失败返回 null
     */
    public static <T> T fromJson(String jsonStr, TypeReference<?> clazz) {
        T t = null;
        return t;
    }

    /**
     * 使用 JsonPath 表达式查询 JSON 数据。
     *
     * @param json JSON 字符串
     * @param path JsonPath 表达式，例如 "$.store.book[0].title"、"$..author"
     * @return 匹配结果的列表
     */
    public static List<Object> query(String json, String path) {
        return new JsonPathParser().query(json, path);
    }
}
