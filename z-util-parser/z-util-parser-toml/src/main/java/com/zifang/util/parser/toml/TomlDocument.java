package com.zifang.util.parser.toml;

import java.util.*;

/**
 * TOML 文档数据模型
 */
public class TomlDocument {

    private Map<String, TomlTable> tables;
    private List<TomlTable> arrayOfTables;

    public TomlDocument() {
        this.tables = new LinkedHashMap<>();
        this.arrayOfTables = new ArrayList<>();
    }

    /**
     * 获取所有顶级表
     */
    public Map<String, TomlTable> getTables() {
        return tables;
    }

    /**
     * 获取数组 of tables 列表
     */
    public List<TomlTable> getArrayOfTables() {
        return arrayOfTables;
    }

    /**
     * 添加表
     */
    public void addTable(String name, TomlTable table) {
        tables.put(name, table);
    }

    /**
     * 添加数组 of tables
     */
    public void addArrayOfTables(TomlTable table) {
        arrayOfTables.add(table);
    }

    /**
     * 根据路径获取表（支持 dotted path 如 "server.host"）
     */
    public TomlTable getTable(String path) {
        String[] parts = path.split("\\.");
        if (parts.length == 1) {
            return tables.get(path);
        }

        TomlTable current = tables.get(parts[0]);
        for (int i = 1; i < parts.length && current != null; i++) {
            current = current.getSubTable(parts[i]);
        }
        return current;
    }

    /**
     * 获取顶层表（用于数组 of tables 展开）
     */
    public TomlTable getRootTable() {
        return tables.get("");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, TomlTable> entry : tables.entrySet()) {
            if (!entry.getKey().isEmpty()) {
                sb.append("[").append(entry.getKey()).append("]\n");
            }
            sb.append(entry.getValue().toString());
        }
        for (TomlTable table : arrayOfTables) {
            sb.append("[[").append(table.getName()).append("]]\n");
            sb.append(table.toString());
        }
        return sb.toString();
    }

    /**
     * TOML 表数据模型
     */
    public static class TomlTable {
        private String name;
        private Map<String, Object> values;
        private Map<String, TomlTable> subTables;
        private List<TomlTable> subTableArrays;
        private TomlTable parent;

        public TomlTable() {
            this(null);
        }

        public TomlTable(String name) {
            this.name = name;
            this.values = new LinkedHashMap<>();
            this.subTables = new LinkedHashMap<>();
            this.subTableArrays = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public Map<String, Object> getValues() {
            return values;
        }

        public Map<String, TomlTable> getSubTables() {
            return subTables;
        }

        public List<TomlTable> getSubTableArrays() {
            return subTableArrays;
        }

        public TomlTable getParent() {
            return parent;
        }

        public void setParent(TomlTable parent) {
            this.parent = parent;
        }

        public void put(String key, Object value) {
            values.put(key, value);
        }

        public Object get(String key) {
            return values.get(key);
        }

        public String getString(String key) {
            Object value = values.get(key);
            return value != null ? value.toString() : null;
        }

        public Integer getInteger(String key) {
            Object value = values.get(key);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return null;
        }

        public Long getLong(String key) {
            Object value = values.get(key);
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return null;
        }

        public Double getDouble(String key) {
            Object value = values.get(key);
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return null;
        }

        public Boolean getBoolean(String key) {
            Object value = values.get(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            if (value instanceof String) {
                String s = ((String) value).toLowerCase();
                if ("true".equals(s)) return true;
                if ("false".equals(s)) return false;
            }
            return null;
        }

        public List<?> getList(String key) {
            Object value = values.get(key);
            if (value instanceof List) {
                return (List<?>) value;
            }
            return null;
        }

        public TomlTable getSubTable(String name) {
            return subTables.get(name);
        }

        public void addSubTable(String name, TomlTable table) {
            table.setParent(this);
            subTables.put(name, table);
        }

        public void addSubTableArray(TomlTable table) {
            table.setParent(this);
            subTableArrays.add(table);
        }

        /**
         * 获取完整路径
         */
        public String getPath() {
            if (parent == null || parent.name == null || parent.name.isEmpty()) {
                return name != null ? name : "";
            }
            return parent.getPath() + "." + name;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
            }
            for (TomlTable sub : subTables.values()) {
                sb.append("[").append(sub.getPath()).append("]\n");
                sb.append(sub.toString());
            }
            for (TomlTable arr : subTableArrays) {
                sb.append("[[").append(arr.getPath()).append("]]\n");
                sb.append(arr.toString());
            }
            return sb.toString();
        }
    }
}
