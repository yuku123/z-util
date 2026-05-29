package com.zifang.util.json.facade;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Gson 后端实现。
 */
public class GsonBackend implements JsonFacade {

    private final Gson gson;

    public GsonBackend() {
        this.gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public GsonBackend(GsonBuilder builder) {
        this.gson = builder.create();
    }

    @Override
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public String toPrettyJson(Object obj) {
        // Gson 的 toJson 返回的是单行，需要手动格式化
        String raw = gson.toJson(obj);
        return new GsonBuilder().setPrettyPrinting().create().toJson(JsonParser.parseString(raw));
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String json, TypeBinding<T> typeBinding) {
        Type type = typeBinding.getType();
        return (T) gson.fromJson(json, type);
    }

    @Override
    public JsonNode parseObject(String json) {
        JsonElement e = JsonParser.parseString(json);
        if (e == null || e.isJsonNull()) return new GsonNode(new JsonObject());
        if (!e.isJsonObject()) throw new JsonParseExceptionEx("Not a JSON object");
        return new GsonNode(e.getAsJsonObject());
    }

    @Override
    public JsonNode parseArray(String json) {
        JsonElement e = JsonParser.parseString(json);
        if (e == null || e.isJsonNull()) return new GsonNode(new JsonArray());
        if (!e.isJsonArray()) throw new JsonParseExceptionEx("Not a JSON array");
        return new GsonNode(e.getAsJsonArray());
    }

    @Override
    public String engine() {
        return "Gson";
    }

    // ==================== 内部节点封装 ====================

    private static class GsonNode implements JsonNode {
        private final JsonElement element;

        GsonNode(JsonElement element) {
            this.element = element;
        }

        @Override
        public String getString(String path) {
            return access(path).getAsString();
        }

        @Override
        public Integer getInt(String path) {
            return access(path).getAsInt();
        }

        @Override
        public Long getLong(String path) {
            return access(path).getAsLong();
        }

        @Override
        public Double getDouble(String path) {
            return access(path).getAsDouble();
        }

        @Override
        public Boolean getBoolean(String path) {
            return access(path).getAsBoolean();
        }

        @Override
        public JsonNode getNode(String path) {
            return new GsonNode(access(path));
        }

        @Override
        public boolean isObject() {
            return element.isJsonObject();
        }

        @Override
        public boolean isArray() {
            return element.isJsonArray();
        }

        @Override
        public boolean isNull() {
            return element.isJsonNull();
        }

        @Override
        public int size() {
            if (element.isJsonArray()) return element.getAsJsonArray().size();
            if (element.isJsonObject()) return element.getAsJsonObject().size();
            return 0;
        }

        @Override
        public String toJsonString() {
            return element.toString();
        }

        @Override
        public Object toJavaObject() {
            return new com.google.gson.Gson().fromJson(element, Object.class);
        }

        private JsonElement access(String path) {
            if (path == null || path.isEmpty()) return element;
            String[] parts = path.split("\\.");
            JsonElement current = element;
            for (String p : parts) {
                if (current.isJsonObject()) {
                    current = current.getAsJsonObject().get(p);
                } else if (current.isJsonArray()) {
                    try {
                        int idx = Integer.parseInt(p);
                        current = current.getAsJsonArray().get(idx);
                    } catch (Exception e) {
                        return JsonNull.INSTANCE;
                    }
                } else {
                    return JsonNull.INSTANCE;
                }
                if (current == null) return JsonNull.INSTANCE;
            }
            return current;
        }
    }

    private static class JsonParseExceptionEx extends RuntimeException {
        JsonParseExceptionEx(String msg) {
            super(msg);
        }
    }
}