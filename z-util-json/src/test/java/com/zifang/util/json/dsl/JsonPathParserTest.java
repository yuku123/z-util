package com.zifang.util.json.dsl;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Comprehensive test suite for JsonPathParser
 */
public class JsonPathParserTest {

    private JsonPathParser parser = new JsonPathParser();
    private DslJsonParser jsonParser = new DslJsonParser();

    private Object parse(String json) {
        return jsonParser.parse(json);
    }

    // ===== Root =====

    @Test
    public void testRootObject() {
        String json = "{\"name\": \"zifang\", \"age\": 30}";
        List<Object> result = parser.query(json, "$");
        assertFalse(result.isEmpty());
    }

    // ===== 属性访问 =====

    @Test
    public void testDotAccess() {
        String json = "{\"name\": \"zifang\", \"age\": 30}";
        List<Object> result = parser.query(json, "$.name");
        assertEquals("zifang", result.get(0));
    }

    @Test
    public void testDotAccessNested() {
        String json = "{\"person\": {\"name\": \"zifang\"}}";
        List<Object> result = parser.query(json, "$.person.name");
        assertEquals("zifang", result.get(0));
    }

    @Test
    public void testDotAccessMultipleLevels() {
        String json = "{\"a\": {\"b\": {\"c\": {\"d\": \"value\"}}}}";
        List<Object> result = parser.query(json, "$.a.b.c.d");
        assertEquals("value", result.get(0));
    }

    @Test
    public void testDotAccessMissingKey() {
        String json = "{\"name\": \"zifang\"}";
        List<Object> result = parser.query(json, "$.age");
        assertTrue(result.isEmpty());
    }

    // ===== 数组索引 =====

    @Test
    public void testArrayIndex() {
        String json = "{\"items\": [\"a\", \"b\", \"c\"]}";
        List<Object> result = parser.query(json, "$.items[0]");
        assertEquals("a", result.get(0));
    }

    @Test
    public void testArrayIndexSecond() {
        String json = "{\"items\": [\"a\", \"b\", \"c\"]}";
        List<Object> result = parser.query(json, "$.items[1]");
        assertEquals("b", result.get(0));
    }

    @Test
    public void testArrayIndexLast() {
        String json = "{\"items\": [\"a\", \"b\", \"c\"]}";
        List<Object> result = parser.query(json, "$.items[2]");
        assertEquals("c", result.get(0));
    }

    @Test
    public void testArrayIndexOutOfBounds() {
        String json = "{\"items\": [\"a\", \"b\"]}";
        List<Object> result = parser.query(json, "$.items[10]");
        assertTrue(result.isEmpty());
    }

    // ===== 通配符 =====

    @Test
    public void testWildcardArray() {
        String json = "{\"items\": [{\"name\": \"a\"}, {\"name\": \"b\"}]}";
        List<Object> result = parser.query(json, "$.items[*]");
        assertEquals(2, result.size());
    }

    @Test
    public void testWildcardObject() {
        String json = "{\"a\": 1, \"b\": 2, \"c\": 3}";
        List<Object> result = parser.query(json, "$.*");
        assertTrue(result.size() >= 3);
    }

    // ===== 递归下降 =====

    @Test
    public void testRecursiveDescent() {
        String json = "{\"level1\": {\"level2\": {\"value\": \"deep\"}}}";
        List<Object> result = parser.query(json, "$..value");
        assertEquals("deep", result.get(0));
    }

    @Test
    public void testRecursiveDescentArray() {
        String json = "{\"list\": [{\"value\": 1}, {\"value\": 2}]}";
        List<Object> result = parser.query(json, "$..value");
        assertTrue(result.size() >= 2);
    }

    // ===== 复杂结构 =====

    @Test
    public void testStoreBookExample() {
        String json = "{"
            + "\"store\": {"
            + "    \"book\": ["
            + "        {\"category\": \"fiction\", \"author\": \"Tolkien\", \"price\": 8.99},"
            + "        {\"category\": \"fiction\", \"author\": \"Austen\", \"price\": 12.99},"
            + "        {\"category\": \"programming\", \"author\": \"Apress\", \"price\": 10.99}"
            + "    ],"
            + "    \"bicycle\": {\"color\": \"red\", \"price\": 399}"
            + "}"
            + "}";

        // $.store.book[0].author
        List<Object> authors = parser.query(json, "$.store.book[0].author");
        assertEquals("Tolkien", authors.get(0));

        // $.store.bicycle.color
        List<Object> colors = parser.query(json, "$.store.bicycle.color");
        assertEquals("red", colors.get(0));
    }

    // ===== 空值处理 =====

    @Test
    public void testQueryNullValue() {
        String json = "{\"name\": null}";
        List<Object> result = parser.query(json, "$.name");
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    public void testQueryNullObject() {
        String json = "{\"person\": null}";
        List<Object> result = parser.query(json, "$.person.name");
        assertTrue(result.isEmpty());
    }

    // ===== 边界 =====

    @Test
    public void testQueryEmptyJson() {
        String json = "{}";
        List<Object> result = parser.query(json, "$.name");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testQueryEmptyArray() {
        String json = "{\"items\": []}";
        List<Object> result = parser.query(json, "$.items[0]");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testQueryOnArray() {
        String json = "[1, 2, 3]";
        List<Object> result = parser.query(json, "$[0]");
        assertEquals(1L, result.get(0));
    }

    @Test
    public void testQueryOnArraySecond() {
        String json = "[{\"name\": \"a\"}, {\"name\": \"b\"}]";
        List<Object> result = parser.query(json, "$[1].name");
        assertEquals("b", result.get(0));
    }
}
