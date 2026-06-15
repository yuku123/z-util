package com.zifang.util.json;

import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.exception.JsonTypeException;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * JSON 工具类，提供 JSON 序列化与反序列化、JSONPath 查询等功能。
 * <p>
 * 完整 API 列表：
 * <ul>
 *   <li>{@code toJson(obj)} — 任意对象序列化为 JSON 字符串</li>
 *   <li>{@code fromJson(json, TypeRef)} — JSON 反序列化为指定类型</li>
 *   <li>{@code parseObject(json)} — JSON 字符串 → JsonObject</li>
 *   <li>{@code parseArray(json)} — JSON 字符串 → JsonArray</li>
 * </ul>
 *
 * @author zifang
 */

/**
 * JsonUtil类。
 */
public class JsonUtil {

    private static final JSONParser PARSER = new JSONParser();

    // ==================== 解析入口 ====================

    /**
     * 将 JSON 字符串解析为 JsonObject。
     */
    /**
     * parseObject方法。
     * * @param json String类型参数
     *
     * @return static JsonObject类型返回值
     */
    public static JsonObject parseObject(String json) {
        if (json == null || json.trim().isEmpty()) return new JsonObject();
        Object parsed = PARSER.fromJSON(json.trim());
        if (parsed instanceof JsonObject) return (JsonObject) parsed;
        throw new JsonTypeException("JSON is not an object: " + json);
    }

    /**
     * 将 JSON 字符串解析为 JsonArray。
     */
    /**
     * parseArray方法。
     * * @param json String类型参数
     *
     * @return static JsonArray类型返回值
     */
    public static JsonArray parseArray(String json) {
        if (json == null || json.trim().isEmpty()) return new JsonArray();
        Object parsed = PARSER.fromJSON(json.trim());
        if (parsed instanceof JsonArray) return (JsonArray) parsed;
        throw new JsonTypeException("JSON is not an array: " + json);
    }

    // ==================== 序列化 ====================

    /**
     * 将任意对象序列化为 JSON 字符串。
     */
    /**
     * toJson方法。
     * * @param t T类型参数
     *
     * @return static <T> String类型返回值
     */
    public static <T> String toJson(T t) {
        if (t == null) return "null";
        if (t instanceof JsonObject || t instanceof JsonArray) return t.toString();
        if (t instanceof String) return "\"" + escapeString((String) t) + "\"";
        if (t instanceof Number || t instanceof Boolean) return String.valueOf(t);
        if (t instanceof Date) return String.valueOf(((Date) t).getTime());
        if (t instanceof Collection) return solveList((Collection<?>) t);
        if (t instanceof Map) return solveMap((Map<?, ?>) t);
        if (t.getClass().isArray()) return solveArray(t);
        return solvePojo(t);
    }

    /**
     * 将任意对象序列化为美化 JSON 字符串（带缩进）。
     */
    /**
     * toJsonPretty方法。
     * * @param t T类型参数
     *
     * @return static <T> String类型返回值
     */
    public static <T> String toJsonPretty(T t) {
        return prettyPrint(toJson(t), 0);
    }

    private static String prettyPrint(String json, int indent) {
        if (json == null || json.isEmpty()) return json;
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        for (int i = 0; i < json.length(); i++) {
            char ch = json.charAt(i);
            if (ch == '{' || ch == '[') {
                sb.append(ch).append('\n');
                depth++;
                sb.append(indent(depth));
            } else if (ch == '}' || ch == ']') {
                sb.append('\n');
                depth--;
                sb.append(indent(depth)).append(ch);
            } else if (ch == ',') {
                sb.append(ch).append('\n');
                sb.append(indent(depth));
            } else if (ch == ':') {
                sb.append(ch).append(' ');
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static String indent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) sb.append("  ");
        return sb.toString();
    }

    // ==================== 反序列化 ====================

    /**
     * 将 JSON 字符串反序列化为目标类型（支持泛型）。
     */
    @SuppressWarnings("unchecked")
    /**
     * fromJson方法。
     *      * @param jsonStr String类型参数
     * @param typeRef TypeReferenceT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T fromJson(String jsonStr, TypeReference<T> typeRef) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) return null;
        Object parsed = PARSER.fromJSON(jsonStr.trim());
        return (T) convertValue(parsed, typeRef.getType());
    }

    /**
     * 将 JSON 字符串反序列化为指定 Class。
     */
    /**
     * fromJson方法。
     * * @param jsonStr String类型参数
     *
     * @param clazz ClassT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) return null;
        Object parsed = PARSER.fromJSON(jsonStr.trim());
        return convertValue(parsed, clazz);
    }

    // ==================== 序列化内部实现 ====================

    private static String solveList(Collection<?> list) {
        if (list == null) return "null";
        StringBuilder sb = new StringBuilder("[");
        int i = 0, size = list.size();
        for (Object item : list) {
            sb.append(toJson(item));
            if (++i < size) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String solveMap(Map<?, ?> map) {
        if (map == null) return "null";
        StringBuilder sb = new StringBuilder("{");
        int i = 0, size = map.size();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append("\"").append(escapeString(String.valueOf(entry.getKey()))).append("\":");
            sb.append(toJson(entry.getValue()));
            if (++i < size) sb.append(",");
        }
        sb.append("}");
        return sb.toString();
    }

    private static String solveArray(Object arr) {
        int len = java.lang.reflect.Array.getLength(arr);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < len; i++) {
            sb.append(toJson(java.lang.reflect.Array.get(arr, i)));
            if (i < len - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String solvePojo(Object obj) {
        JsonObject jsonObject = new JsonObject();
        for (Field field : getAllFields(obj.getClass())) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) continue;
            field.setAccessible(true);
            try {
                jsonObject.put(field.getName(), field.get(obj));
            } catch (Exception e) {
                jsonObject.put(field.getName(), null);
            }
        }
        return jsonObject.toString();
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    // ==================== 反序列化内部实现 ====================

    @SuppressWarnings("unchecked")
    private static <T> T convertValue(Object parsed, java.lang.reflect.Type targetType) {
        if (parsed == null) return null;

        if (targetType == JsonObject.class) {
            if (parsed instanceof JsonObject) return (T) parsed;
            throw new JsonTypeException("Cannot convert to JsonObject: " + parsed.getClass());
        }
        if (targetType == JsonArray.class) {
            if (parsed instanceof JsonArray) return (T) parsed;
            throw new JsonTypeException("Cannot convert to JsonArray: " + parsed.getClass());
        }
        if (targetType == String.class) {
            return (T) (parsed instanceof String ? (String) parsed : parsed.toString());
        }
        if (targetType == Integer.class || targetType == int.class) {
            return (T) Integer.valueOf(toInteger(parsed));
        }
        if (targetType == Long.class || targetType == long.class) {
            return (T) Long.valueOf(toLong(parsed));
        }
        if (targetType == Double.class || targetType == double.class) {
            return (T) Double.valueOf(toDouble(parsed));
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            return (T) Boolean.valueOf(toBoolean(parsed));
        }

        // List<T>
        if (targetType instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) targetType;
            if (pt.getRawType() == List.class || pt.getRawType() == Collection.class) {
                if (parsed instanceof JsonArray) {
                    JsonArray arr = (JsonArray) parsed;
                    java.lang.reflect.Type elemType = pt.getActualTypeArguments()[0];
                    List<Object> list = new ArrayList<>(arr.size());
                    for (int i = 0; i < arr.size(); i++) {
                        list.add(convertValue(arr.get(i), elemType));
                    }
                    return (T) list;
                }
            }
            if (pt.getRawType() == Map.class) {
                if (parsed instanceof JsonObject) {
                    JsonObject obj = (JsonObject) parsed;
                    java.lang.reflect.Type keyType = pt.getActualTypeArguments()[0];
                    java.lang.reflect.Type valType = pt.getActualTypeArguments()[1];
                    Map<Object, Object> map = new LinkedHashMap<Object, Object>();
                    for (Map.Entry<String, Object> e : obj.getAllKeyValue()) {
                        map.put(convertValue(e.getKey(), keyType), convertValue(e.getValue(), valType));
                    }
                    return (T) map;
                }
            }
        }

        // 数组
        if (targetType instanceof Class && ((Class<?>) targetType).isArray()) {
            if (parsed instanceof JsonArray) {
                JsonArray arr = (JsonArray) parsed;
                Class<?> compType = ((Class<?>) targetType).getComponentType();
                Object array = java.lang.reflect.Array.newInstance(compType, arr.size());
                for (int i = 0; i < arr.size(); i++) {
                    java.lang.reflect.Array.set(array, i, convertValue(arr.get(i), compType));
                }
                return (T) array;
            }
        }

        // POJO
        if (parsed instanceof JsonObject) {
            return deserializePojo((JsonObject) parsed, (Class<T>) targetType);
        }
        if (parsed instanceof JsonArray && targetType == List.class) {
            JsonArray arr = (JsonArray) parsed;
            List<Object> list = new ArrayList<Object>(arr.size());
            for (Object item : arr) {
                list.add(item);
            }
            return (T) list;
        }

        return (T) parsed;
    }

    private static <T> T deserializePojo(JsonObject obj, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Map<String, Field> fieldMap = buildFieldMap(clazz);
            for (Map.Entry<String, Object> e : obj.getAllKeyValue()) {
                Field field = fieldMap.get(e.getKey());
                if (field == null) continue;
                field.setAccessible(true);
                Object converted = convertValue(e.getValue(), field.getType());
                field.set(instance, converted);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("deserializePojo failed: " + clazz.getName(), e);
        }
    }

    private static Map<String, Field> buildFieldMap(Class<?> clazz) {
        Map<String, Field> map = new HashMap<>();
        for (Field f : getAllFields(clazz)) {
            if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
                map.put(f.getName(), f);
            }
        }
        return map;
    }

    // ==================== 工具方法 ====================

    static String escapeString(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                default:
                    sb.append(ch);
                    break;
            }
        }
        return sb.toString();
    }

    private static int toInteger(Object v) {
        if (v == null) return 0;
        if (v instanceof Number) return ((Number) v).intValue();
        try {
            return Integer.parseInt(String.valueOf(v));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static long toLong(Object v) {
        if (v == null) return 0L;
        if (v instanceof Number) return ((Number) v).longValue();
        try {
            return Long.parseLong(String.valueOf(v));
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private static double toDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try {
            return Double.parseDouble(String.valueOf(v));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static boolean toBoolean(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean) return (Boolean) v;
        return Boolean.parseBoolean(String.valueOf(v));
    }
}