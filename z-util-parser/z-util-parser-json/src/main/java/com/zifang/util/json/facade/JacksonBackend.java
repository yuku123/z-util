package com.zifang.util.json.facade;

import com.fasterxml.jackson.databind.node.NullNode;

import java.io.IOException;

/**
 * Jackson 后端实现。
 */
public class JacksonBackend implements JsonFacade {

    private final com.fasterxml.jackson.databind.ObjectMapper mapper;

    public JacksonBackend() {
        this.mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        this.mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public JacksonBackend(com.fasterxml.jackson.databind.ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Jackson序列化失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String toPrettyJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Jackson序列化失败: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Jackson反序列化失败: " + e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String json, TypeBinding<T> typeBinding) {
        try {
            return (T) mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<T>() {
                @Override
                public java.lang.reflect.Type getType() {
                    return typeBinding.getType();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Jackson反序列化失败: " + e.getMessage(), e);
        }
    }

    @Override
    public JsonNode parseObject(String json) {
        try {
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(json);
            return new JacksonNode(node);
        } catch (IOException e) {
            throw new RuntimeException("Jackson解析失败: " + e.getMessage(), e);
        }
    }

    @Override
    public JsonNode parseArray(String json) {
        try {
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(json);
            return new JacksonNode(node);
        } catch (IOException e) {
            throw new RuntimeException("Jackson解析失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Object query(String json, String path) {
        try {
            com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(json);
            String expr = path.startsWith("$.") ? path.substring(2) : path;
            String[] steps = expr.split("\\.");
            com.fasterxml.jackson.databind.JsonNode cur = root;
            for (String step : steps) {
                if (step.contains("[")) {
                    String key = step.substring(0, step.indexOf('['));
                    int idx = Integer.parseInt(step.substring(step.indexOf('[') + 1, step.indexOf(']')));
                    cur = cur.get(key).get(idx);
                } else {
                    cur = cur.get(step);
                }
                if (cur == null) return null;
            }
            if (cur.isArray()) return mapper.convertValue(cur, Object[].class);
            if (cur.isObject()) return mapper.convertValue(cur, Object.class);
            return cur.isNull() ? null : cur.textValue();
        } catch (IOException e) {
            throw new RuntimeException("Jackson JSONPath 查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String engine() {
        return "Jackson";
    }

    // ==================== 内部节点封装 ====================

    private static class JacksonNode implements JsonNode {
        private final com.fasterxml.jackson.databind.JsonNode node;

        JacksonNode(com.fasterxml.jackson.databind.JsonNode node) {
            this.node = node;
        }

        @Override
        public String getString(String path) {
            return nav(path).asText();
        }

        @Override
        public Integer getInt(String path) {
            return Integer.valueOf(nav(path).asInt());
        }

        @Override
        public Long getLong(String path) {
            return Long.valueOf(nav(path).asLong());
        }

        @Override
        public Double getDouble(String path) {
            return Double.valueOf(nav(path).asDouble());
        }

        @Override
        public Boolean getBoolean(String path) {
            return Boolean.valueOf(nav(path).asBoolean());
        }

        @Override
        public JsonNode getNode(String path) {
            return new JacksonNode(nav(path));
        }

        @Override
        public boolean isObject() {
            return node.isObject();
        }

        @Override
        public boolean isArray() {
            return node.isArray();
        }

        @Override
        public boolean isNull() {
            return node.isNull();
        }

        @Override
        public int size() {
            return node.size();
        }

        @Override
        public String toJsonString() {
            try {
                return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(node);
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Object toJavaObject() {
            return new com.fasterxml.jackson.databind.ObjectMapper().convertValue(node, Object.class);
        }

        private com.fasterxml.jackson.databind.JsonNode nav(String path) {
            if (path == null || path.isEmpty()) return node;
            String[] parts = path.split("\\.");
            com.fasterxml.jackson.databind.JsonNode cur = node;
            for (String p : parts) {
                if (cur.isArray()) {
                    try {
                        cur = cur.get(Integer.parseInt(p));
                    } catch (Exception e) {
                        return NullNode.getInstance();
                    }
                } else {
                    cur = cur.get(p);
                }
                if (cur == null) return NullNode.getInstance();
            }
            return cur;
        }
    }
}