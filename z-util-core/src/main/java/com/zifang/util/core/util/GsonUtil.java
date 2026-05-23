package com.zifang.util.core.util;

import com.zifang.util.json.JsonUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * JSON 工具类，基于自研 json 模块实现。
 * <p>
 * 兼容 GsonUtil 的 API，原所有方法签名保持不变。
 *
 * @author zifang
 */
public class GsonUtil {

    /**
     * 将对象序列化为 JSON 字符串。
     */
    public static <T> String objectToJsonStr(T object) {
        return JsonUtil.toJson(object);
    }

    /**
     * 将 JSON 字符串反序列化为指定类型。
     */
    public static <T> T jsonStrToObject(String jsonStr, Class<T> classOfT) {
        return JsonUtil.fromJson(jsonStr, classOfT);
    }

    /**
     * 将 JSON 字符串反序列化为泛型类型。
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonStrToObject(String jsonStr, Type type) {
        return JsonUtil.fromJson(jsonStr, type);
    }

    /**
     * 将对象转换为指定子类型。
     */
    public static <T> T changeToSubClass(Object o, Class<T> t) {
        return jsonStrToObject(objectToJsonStr(o), t);
    }

    /**
     * 将对象转换为 Map。
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object o) {
        return (Map<String, Object>) JsonUtil.toMap(o);
    }
}
