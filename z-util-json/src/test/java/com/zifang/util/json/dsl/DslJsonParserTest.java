package com.zifang.util.json.dsl;

import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Comprehensive test suite for DslJsonParser (G4-based JSON parser)
 */
public class DslJsonParserTest {

    private DslJsonParser parser = new DslJsonParser();

    // ===== 基础类型 =====

    @Test
    public void testParseNull() {
        Object result = parser.parse("null");
        assertNull(result);
    }

    @Test
    public void testParseTrue() {
        Object result = parser.parse("true");
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testParseFalse() {
        Object result = parser.parse("false");
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testParsePositiveInt() {
        Object result = parser.parse("123");
        assertEquals(123L, result);
    }

    @Test
    public void testParseNegativeInt() {
        Object result = parser.parse("-456");
        assertEquals(-456L, result);
    }

    @Test
    public void testParsePositiveDouble() {
        Object result = parser.parse("3.14");
        assertEquals(3.14, result);
    }

    @Test
    public void testParseNegativeDouble() {
        Object result = parser.parse("-2.718");
        assertEquals(-2.718, result);
    }

    @Test
    public void testParseScientificNotation() {
        Object result = parser.parse("1.23e10");
        assertEquals(1.23e10, result);
    }

    @Test
    public void testParseScientificNotationWithPlus() {
        Object result = parser.parse("1.23e+5");
        assertEquals(1.23e+5, result);
    }

    @Test
    public void testParseScientificNotationWithMinus() {
        Object result = parser.parse("5.0e-3");
        assertEquals(5.0e-3, result);
    }

    @Test
    public void testParseString() {
        Object result = parser.parse("\"hello\"");
        assertEquals("hello", result);
    }

    @Test
    public void testParseStringWithEscapes() {
        Object result = parser.parse("\"line\\nbreak\"");
        assertEquals("line\nbreak", result);
    }

    @Test
    public void testParseStringWithQuote() {
        Object result = parser.parse("\"say \\\"hello\\\"\"");
        assertEquals("say \"hello\"", result);
    }

    // ===== 空对象/数组 =====

    @Test
    public void testParseEmptyObject() {
        Object result = parser.parse("{}");
        assertTrue(result instanceof JsonObject);
        JsonObject obj = (JsonObject) result;
        assertEquals(0, obj.getAllKeyValue().size());
    }

    @Test
    public void testParseEmptyArray() {
        Object result = parser.parse("[]");
        assertTrue(result instanceof JsonArray);
        JsonArray arr = (JsonArray) result;
        assertEquals(0, arr.size());
    }

    // ===== 基础对象 =====

    @Test
    public void testParseSimpleObject() {
        JsonObject obj = (JsonObject) parser.parse("{\"name\": \"zifang\"}");
        assertEquals("zifang", obj.get("name"));
    }

    @Test
    public void testParseObjectMultipleKeys() {
        JsonObject obj = (JsonObject) parser.parse("{\"a\": 1, \"b\": 2, \"c\": 3}");
        assertEquals(1, obj.get("a"));
        assertEquals(2, obj.get("b"));
        assertEquals(3, obj.get("c"));
    }

    @Test
    public void testParseObjectMixedTypes() {
        JsonObject obj = (JsonObject) parser.parse(
            "{\"str\": \"text\", \"num\": 42, \"bool\": true, \"nil\": null}");
        assertEquals("text", obj.get("str"));
        assertEquals(42L, obj.get("num"));
        assertEquals(Boolean.TRUE, obj.get("bool"));
        assertNull(obj.get("nil"));
    }

    @Test
    public void testParseNestedObject() {
        JsonObject obj = (JsonObject) parser.parse(
            "{\"outer\": {\"inner\": \"value\"}}");
        JsonObject inner = (JsonObject) obj.get("outer");
        assertEquals("value", inner.get("inner"));
    }

    // ===== 基础数组 =====

    @Test
    public void testParseSimpleArray() {
        JsonArray arr = (JsonArray) parser.parse("[1, 2, 3]");
        assertEquals(3, arr.size());
        assertEquals(1L, arr.get(0));
        assertEquals(2L, arr.get(1));
        assertEquals(3L, arr.get(2));
    }

    @Test
    public void testParseArrayMixedTypes() {
        JsonArray arr = (JsonArray) parser.parse("[\"text\", 42, true, null, 3.14]");
        assertEquals("text", arr.get(0));
        assertEquals(42L, arr.get(1));
        assertEquals(Boolean.TRUE, arr.get(2));
        assertNull(arr.get(3));
        assertEquals(3.14, arr.get(4));
    }

    @Test
    public void testParseNestedArray() {
        JsonArray arr = (JsonArray) parser.parse("[[1, 2], [3, 4]]");
        JsonArray inner0 = (JsonArray) arr.get(0);
        JsonArray inner1 = (JsonArray) arr.get(1);
        assertEquals(1L, inner0.get(0));
        assertEquals(4L, inner1.get(1));
    }

    // ===== 复杂结构 =====

    @Test
    public void testParseComplexStructure() {
        String json = "{"
            + "\"name\": \"zifang\","
            + "\"age\": 30,"
            + "\"active\": true,"
            + "\"address\": {\"city\": \"Beijing\", \"zip\": 100000},"
            + "\"scores\": [95, 88, 72],"
            + "\"friends\": ["
            + "    {\"name\": \"Alice\", \"score\": 90},"
            + "    {\"name\": \"Bob\", \"score\": 85}"
            + "]"
            + "}";
        JsonObject obj = (JsonObject) parser.parse(json);
        assertEquals("zifang", obj.get("name"));
        assertEquals(30L, obj.get("age"));
        assertEquals(Boolean.TRUE, obj.get("active"));

        JsonObject addr = (JsonObject) obj.get("address");
        assertEquals("Beijing", addr.get("city"));
        assertEquals(100000L, addr.get("zip"));

        JsonArray scores = (JsonArray) obj.get("scores");
        assertEquals(3, scores.size());
        assertEquals(95L, scores.get(0));

        JsonArray friends = (JsonArray) obj.get("friends");
        JsonObject bob = (JsonObject) friends.get(1);
        assertEquals("Bob", bob.get("name"));
    }

    // ===== Unicode =====

    @Test
    public void testParseUnicode() {
        JsonObject obj = (JsonObject) parser.parse("{\"chinese\": \"中文\"}");
        assertEquals("中文", obj.get("chinese"));
    }

    @Test
    public void testParseUnicodeEscaped() {
        Object result = parser.parse("\"\\u4e2d\\u6587\"");
        assertEquals("中文", result);
    }

    // ===== 空白 =====

    @Test
    public void testParseWithSpaces() {
        JsonObject obj = (JsonObject) parser.parse("{  \"a\" :   1 }");
        assertEquals(1L, obj.get("a"));
    }

    @Test
    public void testParseWithNewlines() {
        JsonObject obj = (JsonObject) parser.parse("{\n  \"a\": 1,\n  \"b\": 2\n}");
        assertEquals(1L, obj.get("a"));
        assertEquals(2L, obj.get("b"));
    }

    // ===== 数字边界 =====

    @Test
    public void testParseZero() {
        assertEquals(0L, parser.parse("0"));
    }

    // ===== 错误处理 =====

    @Test(expected = RuntimeException.class)
    public void testParseInvalidJson() {
        parser.parse("{invalid}");
    }

    @Test(expected = RuntimeException.class)
    public void testParseUnclosedObject() {
        parser.parse("{\"a\": 1");
    }

    @Test(expected = RuntimeException.class)
    public void testParseUnclosedArray() {
        parser.parse("[1, 2");
    }
}
