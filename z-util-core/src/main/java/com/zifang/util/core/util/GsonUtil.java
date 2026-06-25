package com.zifang.util.core.util;

import com.zifang.util.json.JsonUtil;
import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.model.JsonObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @deprecated 请直接使用 {@link JsonUtil}（z-util-parser-json 提供）。
 * <p>
 * 本类保留仅为兼容旧代码，内部完全委托给自研的 z-util-parser-json。
 *
 * @author zifang
 */
@Deprecated
public class GsonUtil {

    public static <T> String objectToJsonStr(T object) {
        return JsonUtil.toJson(object);
    }

    public static <T> T jsonStrToObject(String jsonStr, Class<T> classOfT) {
        return JsonUtil.fromJson(jsonStr, classOfT);
    }

    public static <T> T jsonStrToObject(String jsonStr, Type typeReference) {
        return JsonUtil.fromJson(jsonStr, new TypeReference<T>(typeReference) {});
    }

    public static <T> T changeToSubClass(Object o, Class<T> t) {
        return jsonStrToObject(objectToJsonStr(o), t);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object o) {
        JsonObject obj = JsonUtil.parseObject(objectToJsonStr(o));
        return (Map<String, Object>) obj;
    }
}
