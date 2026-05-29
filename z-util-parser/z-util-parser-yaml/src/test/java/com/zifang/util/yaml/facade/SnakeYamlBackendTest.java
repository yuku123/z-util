package com.zifang.util.yaml.facade;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * SnakeYamlBackend 测试。
 */
public class SnakeYamlBackendTest {

    private final SnakeYamlBackend backend = new SnakeYamlBackend();

    // ==================== toYaml / fromYaml ====================

    @Test
    public void testToYamlFromYaml_Roundtrip() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", "zifang");
        data.put("age", 30);
        data.put("active", true);
        String yaml = backend.toYaml(data);
        assertNotNull(yaml);
        assertTrue(yaml.contains("name: zifang"));

        Map<?, ?> restored = backend.fromYaml(yaml, Map.class);
        assertEquals("zifang", restored.get("name"));
        assertEquals(30, restored.get("age"));
    }

    @Test
    public void testToYaml_Null() {
        assertEquals("null", backend.toYaml(null));
    }

    @Test
    public void testFromYaml_NullOrEmpty() {
        assertNull(backend.fromYaml(null, Map.class));
        assertNull(backend.fromYaml("", Map.class));
        assertNull(backend.fromYaml("   ", Map.class));
    }

    @Test
    public void testFromYaml_List() {
        String yaml = "- name: alice\n  age: 25\n- name: bob\n  age: 30";
        YamlTypeBinding<List<Map<String, Object>>> binding = new YamlTypeBinding<List<Map<String, Object>>>() {};
        List<Map<String, Object>> list = backend.fromYaml(yaml, binding);
        assertEquals(2, list.size());
        assertEquals("alice", list.get(0).get("name"));
    }

    @Test
    public void testToPrettyYaml() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", "zifang");
        data.put("items", Arrays.asList(1, 2, 3));
        String pretty = backend.toPrettyYaml(data);
        assertNotNull(pretty);
        // block style 应有换行
        assertTrue(pretty.contains("\n"));
    }

    // ==================== engine ====================

    @Test
    public void testEngine() {
        assertEquals("SnakeYAML", backend.engine());
    }

    // ==================== isValidYaml ====================

    @Test
    public void testIsValidYaml() {
        assertTrue(backend.isValidYaml("name: test\nage: 30"));
        assertTrue(backend.isValidYaml("- a\n- b"));
        assertFalse(backend.isValidYaml("{invalid"));
        assertFalse(backend.isValidYaml(null));
        assertFalse(backend.isValidYaml(""));
    }
}