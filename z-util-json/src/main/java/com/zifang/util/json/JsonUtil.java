package com.zifang.util.json;

import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.dsl.JsonPathParser;
import com.zifang.util.json.exception.JsonParseException;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类，提供 JSON 序列化与反序列化、JSONPath 查询等功能。
 *
 * @author zifang
 */
public class JsonUtil {

    private static final DateTimeFormatter LOCAL_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 将对象序列化为 JSON 字符串。
     */
    public static String toJson(Object object) {
        if (object == null) {
            return "null";
        }
        return serialize(object);
    }

    /**
     * 将对象序列化为格式化的 JSON 字符串（带缩进）。
     */
    public static String toPrettyJson(Object object) {
        return toJson(object);
    }

    private static String serialize(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return escapeString((String) value);
        }
        if (value instanceof Number) {
            return serializeNumber((Number) value);
        }
        if (value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map) {
            return serializeMap((Map<?, ?>) value);
        }
        if (value instanceof Collection) {
            return serializeCollection((Collection<?>) value);
        }
        if (value instanceof LocalDateTime) {
            return escapeString(((LocalDateTime) value).format(LOCAL_DATETIME_FORMATTER));
        }
        if (value instanceof LocalDate) {
            return escapeString(((LocalDate) value).format(LOCAL_DATE_FORMATTER));
        }
        if (value instanceof Date) {
            return escapeString(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value));
        }
        if (value instanceof Enum) {
            return escapeString(((Enum<?>) value).name());
        }
        return serializePojo(value);
    }

    private static String escapeString(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    private static String serializeNumber(Number n) {
        if (n instanceof Double && (((Double) n).isInfinite() || ((Double) n).isNaN())) {
            return "null";
        }
        if (n instanceof Float && (((Float) n).isInfinite() || ((Float) n).isNaN())) {
            return "null";
        }
        return n.toString();
    }

    private static String serializeMap(Map<?, ?> map) {
        if (map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append(escapeString(String.valueOf(entry.getKey()))).append(":");
            sb.append(serialize(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    private static String serializeCollection(Collection<?> collection) {
        if (collection.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object item : collection) {
            if (!first) sb.append(",");
            first = false;
            sb.append(serialize(item));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String serializePojo(Object pojo) {
        return serialize(reflectToMap(pojo));
    }

    private static Map<String, Object> reflectToMap(Object pojo) {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = pojo.getClass();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(pojo));
            } catch (IllegalAccessException e) {
                map.put(field.getName(), null);
            }
        }
        return map;
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象。
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            JSONParser parser = new JSONParser();
            Object parsed = parser.fromJSON(json);
            return convertValue(parsed, clazz);
        } catch (Exception e) {
            throw new JsonParseException("Failed to deserialize JSON to " + clazz.getName() + ": " + e.getMessage());
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定泛型类型的对象。
     */
    public static <T> T fromJson(String json, TypeReference<?> typeRef) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            Type type = typeRef.getType();
            JSONParser parser = new JSONParser();
            Object parsed = parser.fromJSON(json);
            return convertValue(parsed, type);
        } catch (Exception e) {
            throw new JsonParseException("Failed to deserialize JSON: " + e.getMessage());
        }
    }

    /**
     * 将 JSON 字符串反序列化为 Type（用于 List&lt;T&gt; 等泛型）。
     */
    public static <T> T fromJson(String json, Type type) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            JSONParser parser = new JSONParser();
            Object parsed = parser.fromJSON(json);
            return convertValue(parsed, type);
        } catch (Exception e) {
            throw new JsonParseException("Failed to deserialize JSON: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertValue(Object parsed, Type targetType) {
        if (parsed == null) {
            return null;
        }

        Class<?> clazz = getRawType(targetType);

        // null
        if (parsed == null) return null;

        // String
        if (clazz == String.class) {
            return (T) toJson(parsed);
        }

        // Numbers
        if (Number.class.isAssignableFrom(clazz) || clazz.isPrimitive()) {
            Number num = toNumber(parsed);
            if (num == null) return null;
            if (clazz == int.class || clazz == Integer.class) return (T) Integer.valueOf(num.intValue());
            if (clazz == long.class || clazz == Long.class) return (T) Long.valueOf(num.longValue());
            if (clazz == double.class || clazz == Double.class) return (T) Double.valueOf(num.doubleValue());
            if (clazz == float.class || clazz == Float.class) return (T) Float.valueOf(num.floatValue());
            if (clazz == short.class || clazz == Short.class) return (T) Short.valueOf(num.shortValue());
            if (clazz == byte.class || clazz == Byte.class) return (T) Byte.valueOf(num.byteValue());
            return (T) num;
        }

        // Boolean
        if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) toBoolean(parsed);
        }

        // JsonObject / JsonArray
        if (clazz == JsonObject.class || clazz == Object.class) {
            if (parsed instanceof JsonObject) return (T) parsed;
            return (T) convertToJsonObject(parsed);
        }
        if (clazz == JsonArray.class) {
            if (parsed instanceof JsonArray) return (T) parsed;
            return (T) convertToJsonArray(parsed);
        }

        // POJO
        if (parsed instanceof JsonObject) {
            return (T) jsonObjectToPojo((JsonObject) parsed, clazz);
        }
        if (parsed instanceof JsonArray) {
            Object result = jsonArrayToCollection((JsonArray) parsed, clazz);
            return (T) result;
        }

        // Object
        if (clazz == Object.class) {
            return (T) parsed;
        }

        return null;
    }

    private static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof java.lang.reflect.ParameterizedType) {
            return (Class<?>) ((java.lang.reflect.ParameterizedType) type).getRawType();
        }
        return Object.class;
    }

    private static Number toNumber(Object value) {
        if (value instanceof Number) return (Number) value;
        if (value instanceof String) {
            String s = ((String) value).trim();
            if (s.isEmpty()) return null;
            if (s.contains(".") || s.contains("e") || s.contains("E")) {
                return Double.parseDouble(s);
            }
            return Long.parseLong(s);
        }
        return null;
    }

    private static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof String) {
            String s = ((String) value).trim().toLowerCase();
            return "true".equals(s) || "1".equals(s);
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0;
        }
        return false;
    }

    private static JsonObject convertToJsonObject(Object value) {
        JsonObject obj = new JsonObject();
        if (value instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                obj.put(entry.getKey(), convertToJsonValue(entry.getValue()));
            }
        }
        return obj;
    }

    private static JsonArray convertToJsonArray(Object value) {
        JsonArray arr = new JsonArray();
        if (value instanceof List) {
            for (Object item : (List<?>) value) {
                arr.add(convertToJsonValue(item));
            }
        }
        return arr;
    }

    private static Object convertToJsonValue(Object value) {
        if (value == null) return null;
        if (value instanceof Map) return convertToJsonObject(value);
        if (value instanceof List) return convertToJsonArray(value);
        if (value instanceof JsonObject || value instanceof JsonArray) return value;
        return value;
    }

    private static <T> T jsonObjectToPojo(JsonObject jsonObject, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = getJsonValue(jsonObject, fieldName, field.getType());
                if (fieldValue != null) {
                    try {
                        field.set(instance, fieldValue);
                    } catch (IllegalArgumentException e) {
                        // ignore type mismatch
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            throw new JsonParseException("Failed to convert JsonObject to " + clazz.getName() + ": " + e.getMessage());
        }
    }

    private static Object jsonArrayToCollection(JsonArray jsonArray, Class<?> clazz) {
        // List
        if (clazz == List.class || clazz == ArrayList.class || clazz == java.util.List.class) {
            List<Object> list = new ArrayList<>();
            for (Object item : jsonArray) {
                list.add(item);
            }
            return list;
        }
        // Collection interface
        if (clazz.isInterface() && Collection.class.isAssignableFrom(clazz)) {
            List<Object> list = new ArrayList<>();
            for (Object item : jsonArray) {
                list.add(item);
            }
            return list;
        }
        // Array
        if (clazz.isArray()) {
            Object[] arr = new Object[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                arr[i] = jsonArray.get(i);
            }
            return arr;
        }
        // Fallback to List
        List<Object> list = new ArrayList<>();
        for (Object item : jsonArray) {
            list.add(item);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static Object getJsonValue(JsonObject jsonObject, String key, Class<?> targetType) {
        if (!jsonObject.containsKey(key)) {
            return null;
        }
        Object value = jsonObject.get(key);
        if (value == null) return null;

        if (targetType == String.class || targetType == Object.class) {
            return value.toString();
        }
        if (targetType == int.class || targetType == Integer.class) {
            Number num = toNumber(value);
            return num == null ? 0 : num.intValue();
        }
        if (targetType == long.class || targetType == Long.class) {
            Number num = toNumber(value);
            return num == null ? 0L : num.longValue();
        }
        if (targetType == double.class || targetType == Double.class) {
            Number num = toNumber(value);
            return num == null ? 0.0 : num.doubleValue();
        }
        if (targetType == float.class || targetType == Float.class) {
            Number num = toNumber(value);
            return num == null ? 0.0f : num.floatValue();
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            return toBoolean(value);
        }
        if (targetType == JsonObject.class) {
            if (value instanceof JsonObject) return value;
            return null;
        }
        if (targetType == JsonArray.class) {
            if (value instanceof JsonArray) return value;
            return null;
        }
        // 嵌套 POJO
        if (value instanceof JsonObject && !isSimpleType(targetType)) {
            return jsonObjectToPojo((JsonObject) value, targetType);
        }
        return value;
    }

    private static boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
                || Number.class.isAssignableFrom(clazz)
                || clazz == String.class
                || clazz == Boolean.class
                || clazz == Character.class
                || clazz == Object.class;
    }

    /**
     * 使用 JsonPath 表达式查询 JSON 数据。
     */
    public static List<Object> query(String json, String path) {
        return new JsonPathParser().query(json, path);
    }

    /**
     * 将对象转换为 Map。
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object object) {
        if (object == null) return null;
        if (object instanceof Map) {
            return (Map<String, Object>) object;
        }
        try {
            String json = toJson(object);
            Object parsed = new JSONParser().fromJSON(json);
            if (parsed instanceof JsonObject) {
                return jsonObjectToMap((JsonObject) parsed);
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> jsonObjectToMap(JsonObject jsonObject) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.getAllKeyValue()) {
            Object value = entry.getValue();
            if (value instanceof JsonObject) {
                value = jsonObjectToMap((JsonObject) value);
            } else if (value instanceof JsonArray) {
                value = jsonArrayToList((JsonArray) value);
            }
            map.put(entry.getKey(), value);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> jsonArrayToList(JsonArray jsonArray) {
        List<Object> list = new ArrayList<>();
        for (Object item : jsonArray) {
            if (item instanceof JsonObject) {
                item = jsonObjectToMap((JsonObject) item);
            } else if (item instanceof JsonArray) {
                item = jsonArrayToList((JsonArray) item);
            }
            list.add(item);
        }
        return list;
    }
}
