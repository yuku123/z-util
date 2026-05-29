package com.zifang.util.yaml.facade;

import org.yaml.snakeyaml.Yaml;

import java.util.*;

/**
 * SnakeYAML 后端实现。
 */
public class SnakeYamlBackend implements YamlFacade {

    private final Yaml yaml;

    public SnakeYamlBackend() {
        this.yaml = new Yaml();
    }

    @Override
    public String toYaml(Object obj) {
        if (obj == null) return "null";
        return yaml.dumpAsMap(obj);
    }

    @Override
    public String toPrettyYaml(Object obj) {
        if (obj == null) return "null";
        return yaml.dumpAsMap(obj);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromYaml(String yamlStr, Class<T> clazz) {
        if (yamlStr == null || yamlStr.trim().isEmpty()) return null;
        Object result = yaml.load(yamlStr);
        return clazz.isInstance(result) ? clazz.cast(result) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromYaml(String yamlStr, YamlTypeBinding<T> binding) {
        if (yamlStr == null || yamlStr.trim().isEmpty()) return null;
        return (T) yaml.load(yamlStr);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object fromYamlFlat(String yamlStr) {
        if (yamlStr == null || yamlStr.trim().isEmpty()) return null;
        return yaml.load(yamlStr);
    }

    @Override
    public String engine() {
        return "SnakeYAML";
    }

    @Override
    public boolean isValidYaml(String yamlStr) {
        if (yamlStr == null || yamlStr.trim().isEmpty()) return false;
        try {
            yaml.load(yamlStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}