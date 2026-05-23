package com.zifang.util.yaml;

import com.zifang.util.yaml.define.TypeReference;
import com.zifang.util.yaml.exception.YamlParseException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class YamlUtilTest {

    // --- toYaml tests ---

    @Test
    public void testToYaml_Map() {
        Map<String, String> map = java.util.Collections.singletonMap("key", "value");
        String yaml = YamlUtil.toYaml(map);
        assertNotNull(yaml);
        assertTrue(yaml.contains("key"));
    }

    @Test
    public void testToYaml_Null() {
        assertEquals("null\n", YamlUtil.toYaml(null));
    }

    // --- toPrettyYaml tests ---

    @Test
    public void testToPrettyYaml_Map() {
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("name", "test");
        String yaml = YamlUtil.toPrettyYaml(map);
        assertNotNull(yaml);
        assertTrue(yaml.contains("name:"));
    }

    // --- fromYaml tests ---

    @Test
    public void testFromYaml_Class() {
        String yaml = "name: Alice\nage: 30";
        Map result = YamlUtil.fromYaml(yaml, Map.class);
        assertNotNull(result);
        assertEquals("Alice", result.get("name"));
    }

    @Test
    public void testFromYaml_Null() {
        assertNull(YamlUtil.fromYaml(null, Map.class));
    }

    @Test
    public void testFromYaml_Empty() {
        assertNull(YamlUtil.fromYaml("  \n  ", Map.class));
    }

    // --- parse tests ---

    @Test
    public void testParse_SimpleYaml() {
        Object result = YamlUtil.parse("name: test");
        assertNotNull(result);
        assertTrue(result instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) result;
        assertEquals("test", map.get("name"));
    }

    @Test
    public void testParse_Null() {
        assertNull(YamlUtil.parse(null));
    }

    // --- query tests ---

    @Test
    public void testQuery_SimpleKey() {
        String yaml = "name: zifang\nversion: 1.0";
        java.util.List<Object> results = YamlUtil.query(yaml, "$.name");
        assertEquals(1, results.size());
        assertEquals("zifang", results.get(0));
    }

    @Test
    public void testQuery_NestedKey() {
        String yaml = "config:\n  host: localhost\n  port: 8080";
        java.util.List<Object> results = YamlUtil.query(yaml, "$.config.host");
        assertEquals(1, results.size());
        assertEquals("localhost", results.get(0));
    }

    @Test
    public void testQuery_ArrayIndex() {
        String yaml = "servers:\n  - server1\n  - server2";
        java.util.List<Object> results = YamlUtil.query(yaml, "$.servers[1]");
        assertEquals(1, results.size());
        assertEquals("server2", results.get(0));
    }

    @Test
    public void testQuery_InvalidPath() {
        String yaml = "name: test";
        java.util.List<Object> results = YamlUtil.query(yaml, "$.nonexistent");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testQuery_InvalidYaml() {
        try {
            YamlUtil.query("invalid: [yaml", "$.name");
            fail("Should throw YamlParseException");
        } catch (YamlParseException e) {
            assertTrue(e.getMessage().contains("Failed to parse YAML"));
        }
    }
}
