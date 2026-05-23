package com.zifang.util.parser.ini;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * INI Section 模型
 */
public class IniSection {

    private String name;
    private Map<String, String> values;

    public IniSection() {
        this.values = new LinkedHashMap<>();
    }

    public IniSection(String name) {
        this.name = name;
        this.values = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String get(String key) {
        return values.get(key);
    }

    public void put(String key, String value) {
        values.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name != null && !name.isEmpty()) {
            sb.append("[").append(name).append("]").append("\n");
        }
        for (Map.Entry<String, String> entry : values.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
