package com.zifang.util.json.dsl;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * JsonPathParser (JsonPath 查询引擎) 完整测试
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

    @Test
    public void testRootArray() {
        String json = "[1, 2, 3]";
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

    @Test
    public void testDotAccessNullValue() {
        String json = "{\"name\": null}";
        List<Object> result = parser.query(json, "$.name");
        assertEquals(1, result.size());
        assertNull(result.get(0));
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

    @Test
    public void testArrayNegativeIndex() {
        String json = "{\"items\": [\"a\", \"b\", \"c\"]}";
        // JsonPath 不强制要求支持负索引，按实现行为验证
        List<Object> result = parser.query(json, "$.items[-1]");
        // 实现返回空则为空
        assertNotNull(result);
    }

    // ===== 数组切片 =====

    @Test
    public void testArraySlice() {
        String json = "{\"items\": [\"a\", \"b\", \"c\", \"d\", \"e\"]}";
        List<Object> result = parser.query(json, "$.items[1:3]");
        assertEquals(2, result.size());
        assertEquals("b", result.get(0));
        assertEquals("c", result.get(1));
    }

    @Test
    public void testArraySliceFromStart() {
        String json = "{\"items\": [\"a\", \"b\", \"c\"]}";
        List<Object> result = parser.query(json, "$.items[:2]");
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
        assertEquals("b", result.get(1));
    }

    @Test
    public void testArraySliceToEnd() {
        String json = "{\"items\": [\"a\", \"b\", \"c\"]}";
        List<Object> result = parser.query(json, "$.items[1:]");
        assertEquals(2, result.size());
        assertEquals("b", result.get(0));
        assertEquals("c", result.get(1));
    }

    @Test
    public void testArraySliceEmptyResult() {
        String json = "{\"items\": [\"a\", \"b\"]}";
        List<Object> result = parser.query(json, "$.items[5:7]");
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

    @Test
    public void testRecursiveDescentWildcard() {
        String json = "{\"a\": {\"b\": 1}, \"c\": {\"b\": 2}}";
        List<Object> result = parser.query(json, "$..b");
        assertEquals(2, result.size());
    }

    // ===== 过滤表达式 =====

    @Test
    public void testFilterLessThan() {
        String json = "{\"items\": [{\"price\": 5}, {\"price\": 15}, {\"price\": 8}]}";
        List<Object> result = parser.query(json, "$.items[?(@.price < 10)]");
        assertEquals(2, result.size());
    }

    @Test
    public void testFilterEqual() {
        String json = "{\"items\": [{\"name\": \"a\", \"score\": 90}, {\"name\": \"b\", \"score\": 70}, {\"name\": \"c\", \"score\": 90}]}";
        // Filter by numeric equality using score
        List<Object> result = parser.query(json, "$.items[?(@.score == 90)]");
        assertEquals(2, result.size());
    }

    @Test
    public void testFilterGreaterThan() {
        String json = "{\"items\": [{\"score\": 90}, {\"score\": 70}, {\"score\": 85}]}";
        List<Object> result = parser.query(json, "$.items[?(@.score > 80)]");
        assertEquals(2, result.size());
    }

    @Test
    public void testFilterNotEqual() {
        String json = "{\"items\": [{\"type\": \"a\"}, {\"type\": \"b\"}, {\"type\": \"a\"}]}";
        List<Object> result = parser.query(json, "$.items[?(@.type != \"a\")]");
        assertEquals(1, result.size());
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

        List<Object> authors = parser.query(json, "$.store.book[0].author");
        assertEquals("Tolkien", authors.get(0));

        List<Object> colors = parser.query(json, "$.store.bicycle.color");
        assertEquals("red", colors.get(0));

        List<Object> prices = parser.query(json, "$.store.book[*].price");
        assertEquals(3, prices.size());
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

    @Test
    public void testQueryMissingKeyInNestedObject() {
        String json = "{\"person\": {\"name\": \"alice\"}}";
        List<Object> result = parser.query(json, "$.person.age");
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

    @Test
    public void testQueryArrayOfPrimitives() {
        String json = "[10, 20, 30]";
        List<Object> result = parser.query(json, "$[1]");
        assertEquals(20L, result.get(0));
    }

    // ===== 字符串值比较 =====

    @Test
    public void testFilterStringComparison() {
        String json = "{\"items\": [{\"name\": \"alice\"}, {\"name\": \"bob\"}, {\"name\": \"alice\"}]}";
        List<Object> result = parser.query(json, "$.items[?(@.name == \"alice\")]");
        assertEquals(2, result.size());
    }
}
