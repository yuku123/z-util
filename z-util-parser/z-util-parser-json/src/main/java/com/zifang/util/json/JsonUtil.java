package com.zifang.util.json;

import com.zifang.util.json.annotation.JsonDeserialize;
import com.zifang.util.json.annotation.JsonSerialize;
import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.exception.JsonTypeException;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import com.zifang.util.json.serializer.SerializerRegistry;
import com.zifang.util.json.serializer.ValueDeserializer;
import com.zifang.util.json.serializer.ValueSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON 工具类，提供 JSON 序列化与反序列化、JSONPath 查询等功能。
 * <p>
 * 支持注解驱动的序列化控制：
 * <ul>
 *   <li>{@code @JsonSerialize(using=KeepLongSerializer.class)} — Long 序列化为字符串，避免 JS 精度丢失</li>
 *   <li>{@code @JsonSerialize(format="yyyy-MM-dd")} — Date 字段格式化</li>
 *   <li>{@code @JsonSerialize(ignore=true)} — 序列化时忽略字段</li>
 *   <li>{@code @JsonSerialize(name="alias")} — 序列化时使用别名</li>
 * </ul>
 *
 * @author zifang
 */
public class JsonUtil {

    private static final JSONParser PARSER = new JSONParser();
    private static final SerializerRegistry REGISTRY = SerializerRegistry.getInstance();

    // ==================== Field Metadata Cache ====================

    /**
     * POJO 序列化时的字段元数据（注解信息、序列化器实例）。
     */
    private static final Map<Class<?>, List<FieldMeta>> serializeMetaCache = new ConcurrentHashMap<>();

    /**
     * POJO 反序列化时的字段元数据（注解信息、反序列化器实例）。
     */
    private static final Map<Class<?>, List<FieldMeta>> deserializeMetaCache = new ConcurrentHashMap<>();

    /**
     * 序列化时检测循环引用的 visited 集合（FEATURE008 P1 修复 2026-06-25）。
     * <p>用 {@link IdentityHashMap} 而非普通 Set，避免 POJO 自身 {@code equals/hashCode} 干扰；
     * 用 {@link ThreadLocal} 隔离并发调用，并在 {@link #toJson} 顶层清空。</p>
     * <p>命中循环引用时该字段序列化为 {@code "null"}，避免 {@link StackOverflowError}。
     * 该行为与 FastJSON 默认行为一致，比 Jackson 的"直接抛异常"更友好（不破坏业务流）。</p>
     */
    private static final ThreadLocal<Set<Object>> SERIALIZE_VISITED =
            ThreadLocal.withInitial(() -> Collections.newSetFromMap(new IdentityHashMap<>()));

    private static class FieldMeta {
        final Field field;
        final String jsonName;           // 序列化后的 JSON 属性名
        final boolean ignore;
        final ValueSerializer serializer;
        final ValueDeserializer deserializer;
        final String dateFormat;         // 注解中的 format 值

        FieldMeta(Field field,
                  String jsonName,
                  boolean ignore,
                  ValueSerializer serializer,
                  ValueDeserializer deserializer,
                  String dateFormat) {
            this.field = field;
            this.jsonName = jsonName;
            this.ignore = ignore;
            this.serializer = serializer;
            this.deserializer = deserializer;
            this.dateFormat = dateFormat;
        }
    }

    // ==================== 解析入口 ====================

    public static JsonObject parseObject(String json) {
        if (json == null || json.trim().isEmpty()) return new JsonObject();
        Object parsed = PARSER.fromJSON(json.trim());
        if (parsed instanceof JsonObject) return (JsonObject) parsed;
        throw new JsonTypeException("JSON is not an object: " + json);
    }

    public static JsonArray parseArray(String json) {
        if (json == null || json.trim().isEmpty()) return new JsonArray();
        Object parsed = PARSER.fromJSON(json.trim());
        if (parsed instanceof JsonArray) return (JsonArray) parsed;
        throw new JsonTypeException("JSON is not an array: " + json);
    }

    // ==================== 序列化 ====================

    public static <T> String toJson(T t) {
        if (t == null) return "null";
        // 仅在顶层调用时清空 visited；嵌套调用要保留祖先栈以正确检测循环引用。
        // (FEATURE008 P1 修复 2026-06-25 v2：原版每次都 clear() 会让 solvePojo 内部 toJson(value) 把栈清空，
        //  失去循环引用检测能力，StackOverflow 仍然会发生)
        Set<Object> visited = SERIALIZE_VISITED.get();
        boolean isTopCall = visited.isEmpty();
        if (isTopCall) {
            visited.clear();  // 防御性清空：上层调用者可能没在 finally 里清
        }
        try {
            if (t instanceof JsonObject || t instanceof JsonArray) return t.toString();
            if (t instanceof String) return "\"" + escapeString((String) t) + "\"";
            if (t instanceof Number || t instanceof Boolean) return String.valueOf(t);
            if (t instanceof Date) return String.valueOf(((Date) t).getTime());
            if (t instanceof Collection) return solveList((Collection<?>) t);
            if (t instanceof Map) return solveMap((Map<?, ?>) t);
            if (t.getClass().isArray()) return solveArray(t);
            return solvePojo(t);
        } finally {
            if (isTopCall) {
                visited.clear();  // 顶层结束清理，避免 ThreadLocal 内存泄漏 / 下次调用误判
            }
        }
    }

    public static <T> String toJsonPretty(T t) {
        return prettyPrint(toJson(t), 0);
    }

    // ==================== 反序列化 ====================

    public static <T> T fromJson(String jsonStr, TypeReference<T> typeRef) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) return null;
        Object parsed = PARSER.fromJSON(jsonStr.trim());
        return convertValue(parsed, typeRef.getType());
    }

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
        // 循环引用检测：当前 obj 已在祖先栈中，序列化为 null 避免 StackOverflow
        Set<Object> visited = SERIALIZE_VISITED.get();
        if (!visited.add(obj)) {
            return "null";
        }
        try {
        List<FieldMeta> metas = getSerializeMetas(obj.getClass());
        StringBuilder sb = new StringBuilder("{");
        int i = 0;
        for (FieldMeta meta : metas) {
            if (meta.ignore) continue;
            meta.field.setAccessible(true);
            Object value;
            try {
                value = meta.field.get(obj);
            } catch (Exception e) {
                value = null;
            }
            if (i > 0) sb.append(",");
            sb.append("\"").append(escapeString(meta.jsonName)).append("\":");
            if (value == null) {
                sb.append("null");
            } else if (meta.serializer != null) {
                sb.append(serializeWithSerializer(value, meta.serializer, meta.dateFormat));
            } else {
                sb.append(toJson(value));
            }
            i++;
        }
        sb.append("}");
        return sb.toString();
        } finally {
            // 退出该 POJO 的递归层级，从 visited 中移除（让兄弟节点/复用节点能正常序列化）
            visited.remove(obj);
        }
    }

    private static String serializeWithSerializer(Object value, ValueSerializer serializer, String format) {
        try {
            return serializer.serialize(value, format);
        } catch (Exception e) {
            throw new RuntimeException("Serializer error: " + serializer.getClass().getSimpleName(), e);
        }
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
                    Map<Object, Object> map = new LinkedHashMap<>();
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
            List<Object> list = new ArrayList<>(arr.size());
            for (Object item : arr) {
                list.add(item);
            }
            return (T) list;
        }

        return (T) parsed;
    }

    private static <T> T deserializePojo(JsonObject obj, Class<T> clazz) {
        try {
            // 枚举类型特殊处理：用 "name" 字段 + Enum.valueOf（FEATURE008 P1 修复 v4 2026-06-25）
            // 修复原因：枚举没有无参构造器，clazz.getDeclaredConstructor() 会抛 NoSuchMethodException
            // 同时枚举序列化时输出 {"name":"GET","ordinal":0,"hash":0}，反序列化应提取 name
            if (clazz.isEnum()) {
                Object nameVal = obj.get("name");
                if (nameVal == null) {
                    // 兼容旧的"枚举值字符串"格式（直接传 "GET"）：JsonObject 不暴露 values()，
                    // 这里保持严格模式，要求 JSON 中必须含 "name" 字段。
                    throw new RuntimeException("Enum name not found in JSON: " + obj);
                }
                @SuppressWarnings({"unchecked", "rawtypes"})
                T enumValue = (T) Enum.valueOf((Class<Enum>) clazz.asSubclass(Enum.class), nameVal.toString());
                return enumValue;
            }

            T instance = clazz.getDeclaredConstructor().newInstance();
            List<FieldMeta> metas = getDeserializeMetas(clazz);
            for (FieldMeta meta : metas) {
                // 优先用注解 name 查找字段（支持别名）
                Object rawValue = obj.get(meta.jsonName);
                if (rawValue == null) continue;
                // 关键：用 field.getGenericType() 而非 getType() — 这样 List<InputParam> 能保留泛型
                // (FEATURE008 P1 修复 v5 2026-06-25)
                java.lang.reflect.Type targetType = meta.field.getGenericType();
                Object converted;
                if (meta.deserializer != null) {
                    converted = deserializeWithDeserializer(rawValue, meta.deserializer, meta.dateFormat, meta.field.getType());
                } else {
                    converted = convertValue(rawValue, targetType);
                }
                meta.field.setAccessible(true);
                meta.field.set(instance, converted);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("deserializePojo failed: " + clazz.getName(), e);
        }
    }

    private static Object deserializeWithDeserializer(Object rawValue, ValueDeserializer deserializer,
                                                     String format, Class<?> targetType) {
        try {
            String strVal;
            if (rawValue instanceof String) {
                strVal = (String) rawValue;
            } else {
                strVal = String.valueOf(rawValue);
            }
            return deserializer.deserialize(strVal, targetType, format);
        } catch (Exception e) {
            throw new RuntimeException("Deserializer error: " + deserializer.getClass().getSimpleName(), e);
        }
    }

    // ==================== Field Metadata ====================

    private static List<FieldMeta> getSerializeMetas(Class<?> clazz) {
        List<FieldMeta> cached = serializeMetaCache.get(clazz);
        if (cached != null) return cached;

        List<FieldMeta> metas = new ArrayList<>();
        for (Field field : getAllFields(clazz)) {
            JsonSerialize ann = field.getAnnotation(JsonSerialize.class);
            String jsonName;
            boolean ignore;
            ValueSerializer serializer = null;
            String dateFormat = "";

            if (ann != null) {
                jsonName = ann.name().isEmpty() ? field.getName() : ann.name();
                ignore = ann.ignore();
                if (!ann.using().equals(JsonSerialize.NONE.class)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends ValueSerializer> serClass = (Class<? extends ValueSerializer>) ann.using();
                    serializer = REGISTRY.getSerializer(serClass);
                }
                dateFormat = ann.format();
            } else {
                jsonName = field.getName();
                ignore = false;
            }

            metas.add(new FieldMeta(field, jsonName, ignore, serializer, null, dateFormat));
        }

        serializeMetaCache.put(clazz, metas);
        return metas;
    }

    private static List<FieldMeta> getDeserializeMetas(Class<?> clazz) {
        List<FieldMeta> cached = deserializeMetaCache.get(clazz);
        if (cached != null) return cached;

        List<FieldMeta> metas = new ArrayList<>();
        for (Field field : getAllFields(clazz)) {
            JsonDeserialize ann = field.getAnnotation(JsonDeserialize.class);
            String jsonName;
            ValueDeserializer deserializer = null;
            String dateFormat = "";

            if (ann != null) {
                jsonName = ann.name().isEmpty() ? field.getName() : ann.name();
                if (!ann.using().equals(JsonDeserialize.NONE.class)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends ValueDeserializer> deserClass = (Class<? extends ValueDeserializer>) ann.using();
                    deserializer = REGISTRY.getDeserializer(deserClass);
                }
                dateFormat = ann.format();
            } else {
                jsonName = field.getName();
            }

            metas.add(new FieldMeta(field, jsonName, false, null, deserializer, dateFormat));
        }

        deserializeMetaCache.put(clazz, metas);
        return metas;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Field f : current.getDeclaredFields()) {
                // 跳过 static 字段（FEATURE008 P1 修复 v3 2026-06-25）：
                //   - 枚举类的 $VALUES / $ENTRIES 静态数组会触发指数级膨胀
                //     （def.toJson() 从 200 字节爆到 197 MB）
                //   - 普通类的 static 字段也不属于"实例状态"，不应序列化
                // 同时跳过 synthetic 字段（compiler 生成的 this$0 / 桥接方法等）
                if (Modifier.isStatic(f.getModifiers())) continue;
                if (f.isSynthetic()) continue;
                fields.add(f);
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    // ==================== 工具方法 ====================

    static String escapeString(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                default:   sb.append(ch); break;
            }
        }
        return sb.toString();
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

    private static int toInteger(Object v) {
        if (v == null) return 0;
        if (v instanceof Number) return ((Number) v).intValue();
        try { return Integer.parseInt(String.valueOf(v)); }
        catch (NumberFormatException e) { return 0; }
    }

    private static long toLong(Object v) {
        if (v == null) return 0L;
        if (v instanceof Number) return ((Number) v).longValue();
        try { return Long.parseLong(String.valueOf(v)); }
        catch (NumberFormatException e) { return 0L; }
    }

    private static double toDouble(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try { return Double.parseDouble(String.valueOf(v)); }
        catch (NumberFormatException e) { return 0.0; }
    }

    private static boolean toBoolean(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean) return (Boolean) v;
        return Boolean.parseBoolean(String.valueOf(v));
    }
}
