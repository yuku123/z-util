package com.zifang.util.yaml;

import com.zifang.util.yaml.define.YamlNodeType;
import com.zifang.util.yaml.dsl.YamlPathParser;
import com.zifang.util.yaml.exception.YamlParseException;
import com.zifang.util.yaml.model.YamlArray;
import com.zifang.util.yaml.model.YamlMap;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class YamlParserTest {

    private final YamlParser parser = new YamlParser();

    // --- parse(String) tests ---

    @Test
    public void testParse_NullString() {
        assertNull(parser.parse(null));
    }

    @Test
    public void testParse_EmptyString() {
        assertNull(parser.parse(""));
    }

    @Test
    public void testParse_ScalarString() {
        Object result = parser.parse("hello");
        assertEquals("hello", result);
    }

    @Test
    public void testParse_ScalarNumber() {
        Object result = parser.parse("42");
        assertEquals(42, result);
    }

    @Test
    public void testParse_ScalarFloat() {
        Object result = parser.parse("3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testParse_BooleanTrue() {
        Object result = parser.parse("true");
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testParse_BooleanFalse() {
        Object result = parser.parse("false");
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testParse_Null() {
        Object result = parser.parse("null");
        assertNull(result);
    }

    @Test
    public void testParse_SimpleMap() {
        String yaml = "name: zifang\nage: 18";
        Object result = parser.parse(yaml);
        assertTrue(result instanceof YamlMap);
        YamlMap map = (YamlMap) result;
        assertEquals("zifang", map.getString("name"));
        assertEquals(18, map.get("age"));
    }

    @Test
    public void testParse_NestedMap() {
        String yaml = "config:\n  database:\n    host: localhost\n    port: 3306";
        Object result = parser.parse(yaml);
        assertTrue(result instanceof YamlMap);
        YamlMap root = (YamlMap) result;
        YamlMap config = root.getMap("config");
        assertNotNull(config);
        YamlMap database = config.getMap("database");
        assertEquals("localhost", database.getString("host"));
        assertEquals(3306, database.get("port"));
    }

    @Test
    public void testParse_SimpleArray() {
        String yaml = "- apple\n- banana\n- cherry";
        Object result = parser.parse(yaml);
        assertTrue(result instanceof YamlArray);
        YamlArray array = (YamlArray) result;
        assertEquals(3, array.size());
        assertEquals("apple", array.get(0));
        assertEquals("banana", array.get(1));
        assertEquals("cherry", array.get(2));
    }

    @Test
    public void testParse_ArrayOfMaps() {
        String yaml = "- name: Alice\n  age: 20\n- name: Bob\n  age: 30";
        Object result = parser.parse(yaml);
        assertTrue(result instanceof YamlArray);
        YamlArray array = (YamlArray) result;
        assertEquals(2, array.size());
        YamlMap first = (YamlMap) array.get(0);
        assertEquals("Alice", first.getString("name"));
        assertEquals(20, first.get("age"));
    }

    @Test
    public void testParse_MixedStructure() {
        String yaml = "name: test\nitems:\n  - id: 1\n    value: foo\n  - id: 2\n    value: bar";
        Object result = parser.parse(yaml);
        assertTrue(result instanceof YamlMap);
        YamlMap root = (YamlMap) result;
        assertEquals("test", root.getString("name"));
        YamlArray items = root.getArray("items");
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test(expected = YamlParseException.class)
    public void testParse_InvalidYaml() {
        parser.parse("  invalid:\nyaml: [");
    }

    // --- toYaml(Object) tests ---

    @Test
    public void testToYaml_Map() {
        YamlMap map = new YamlMap();
        map.put("name", "test");
        map.put("value", 123);
        String yaml = parser.toYaml(map);
        assertNotNull(yaml);
        assertTrue(yaml.contains("name: test"));
    }

    @Test
    public void testToYaml_List() {
        YamlArray array = new YamlArray();
        array.add("a");
        array.add("b");
        String yaml = parser.toYaml(array);
        assertNotNull(yaml);
        assertTrue("YAML should contain list items: " + yaml, yaml.contains("a") && yaml.contains("b"));
    }

    @Test
    public void testToYaml_Null() {
        assertEquals("null\n", parser.toYaml(null));
    }

    // --- toPrettyYaml(Object) tests ---

    @Test
    public void testToPrettyYaml_Map() {
        YamlMap map = new YamlMap();
        map.put("name", "test");
        String yaml = parser.toPrettyYaml(map);
        assertNotNull(yaml);
        assertTrue(yaml.contains("name:"));
    }

    // --- parse(String, Class) tests ---

    @Test
    public void testParseToClass() {
        String yaml = "name: Alice\nage: 30";
        @SuppressWarnings("unchecked")
        Map<String, Object> map = parser.parse(yaml, Map.class);
        assertNotNull(map);
        assertEquals("Alice", map.get("name"));
    }

    // --- toString() of model classes ---

    @Test
    public void testYamlMapToString() {
        YamlMap map = new YamlMap();
        map.put("key", "value");
        assertTrue(map.toString().contains("key"));
    }

    @Test
    public void testYamlArrayToString() {
        YamlArray array = new YamlArray();
        array.add("item");
        assertTrue(array.toString().contains("item"));
    }

    @Test
    public void testYamlMapNodeType() {
        assertEquals(YamlNodeType.MAP, new YamlMap().getNodeType());
    }

    @Test
    public void testYamlArrayNodeType() {
        assertEquals(YamlNodeType.SEQUENCE, new YamlArray().getNodeType());
    }

    // --- YamlPathParser integration ---

    @Test
    public void testYamlPathParser_SimpleKey() {
        String yaml = "name: zifang\nage: 18";
        List<Object> results = new YamlPathParser().query(yaml, "$.name");
        assertEquals(1, results.size());
        assertEquals("zifang", results.get(0));
    }

    @Test
    public void testYamlPathParser_NestedKey() {
        String yaml = "config:\n  host: localhost";
        List<Object> results = new YamlPathParser().query(yaml, "$.config.host");
        assertEquals(1, results.size());
        assertEquals("localhost", results.get(0));
    }

    @Test
    public void testYamlPathParser_ArrayIndex() {
        String yaml = "items:\n  - apple\n  - banana";
        List<Object> results = new YamlPathParser().query(yaml, "$.items[0]");
        assertEquals(1, results.size());
        assertEquals("apple", results.get(0));
    }

    @Test
    public void testYamlPathParser_ArrayWildcard() {
        String yaml = "items:\n  - a\n  - b";
        List<Object> results = new YamlPathParser().query(yaml, "$.items[*]");
        assertEquals(2, results.size());
    }

    @Test
    public void testYamlPathParser_InvalidPath() {
        String yaml = "name: test";
        List<Object> results = new YamlPathParser().query(yaml, "$.nonexistent");
        assertTrue(results.isEmpty());
    }
}
