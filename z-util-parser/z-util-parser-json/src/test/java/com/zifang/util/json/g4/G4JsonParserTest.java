package com.zifang.util.json.g4;

import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * G4JsonParser 功能测试
 * 对标 com.zifang.util.json.code.JSONParserTest
 */
public class G4JsonParserTest {

    private G4JsonParser parser = new G4JsonParser();

    // ===== 基础类型 =====

    @Test
    public void testParseNull() {
        JsonObject obj = parser.parseObject("{\"k\": null}");
        assertNull(obj.get("k"));
    }

    @Test
    public void testParseTrue() {
        JsonObject obj = parser.parseObject("{\"k\": true}");
        assertEquals(Boolean.TRUE, obj.get("k"));
    }

    @Test
    public void testParseFalse() {
        JsonObject obj = parser.parseObject("{\"k\": false}");
        assertEquals(Boolean.FALSE, obj.get("k"));
    }

    @Test
    public void testParsePositiveInt() {
        JsonObject obj = parser.parseObject("{\"k\": 123}");
        assertEquals(123, obj.get("k"));
    }

    @Test
    public void testParseNegativeInt() {
        JsonObject obj = parser.parseObject("{\"k\": -456}");
        assertEquals(-456, obj.get("k"));
    }

    @Test
    public void testParsePositiveDouble() {
        JsonObject obj = parser.parseObject("{\"k\": 3.14}");
        assertEquals(3.14, obj.get("k"));
    }

    @Test
    public void testParseNegativeDouble() {
        JsonObject obj = parser.parseObject("{\"k\": -0.001}");
        assertEquals(-0.001, obj.get("k"));
    }

    @Test
    public void testParseScientific() {
        JsonObject obj = parser.parseObject("{\"k\": 1.23e5}");
        assertEquals(1.23e5, obj.get("k"));
    }

    @Test
    public void testParseString() {
        JsonObject obj = parser.parseObject("{\"k\": \"hello\"}");
        assertEquals("hello", obj.get("k"));
    }

    @Test
    public void testParseEmptyString() {
        JsonObject obj = parser.parseObject("{\"k\": \"\"}");
        assertEquals("", obj.get("k"));
    }

    // ===== 对象 =====

    @Test
    public void testParseEmptyObject() {
        JsonObject obj = parser.parseObject("{}");
        assertTrue(obj.isEmpty());
    }

    @Test
    public void testParseSimpleObject() {
        JsonObject obj = parser.parseObject("{\"name\": \"test\", \"age\": 20}");
        assertEquals("test", obj.get("name"));
        assertEquals(20, obj.get("age"));
    }

    @Test
    public void testParseNestedObject() {
        JsonObject obj = parser.parseObject("{\"outer\": {\"inner\": 123}}");
        JsonObject outer = obj.getJsonObject("outer");
        assertEquals(123, outer.get("inner"));
    }

    @Test
    public void testParseObjectWithVariousValues() {
        JsonObject obj = parser.parseObject("{\"n\": null, \"b\": true, \"s\": \"str\", \"i\": 42}");
        assertNull(obj.get("n"));
        assertEquals(Boolean.TRUE, obj.get("b"));
        assertEquals("str", obj.get("s"));
        assertEquals(42, obj.get("i"));
    }

    // ===== 数组 =====

    @Test
    public void testParseEmptyArray() {
        JsonArray arr = parser.parseArray("[]");
        assertTrue(arr.isEmpty());
    }

    @Test
    public void testParseSimpleArray() {
        JsonArray arr = parser.parseArray("[1, 2, 3]");
        assertEquals(3, arr.size());
        assertEquals(1, arr.get(0));
        assertEquals(2, arr.get(1));
        assertEquals(3, arr.get(2));
    }

    @Test
    public void testParseMixedArray() {
        JsonArray arr = parser.parseArray("[\"a\", 1, true, null]");
        assertEquals("a", arr.get(0));
        assertEquals(1, arr.get(1));
        assertEquals(Boolean.TRUE, arr.get(2));
        assertNull(arr.get(3));
    }

    @Test
    public void testParseNestedArray() {
        JsonArray arr = parser.parseArray("[[1, 2], [3, 4]]");
        assertEquals(2, arr.size());
        JsonArray inner0 = arr.getJsonArray(0);
        assertEquals(1, inner0.get(0));
    }

    // ===== 复杂结构 =====

    @Test
    public void testParseComplexObject() {
        JsonObject obj = parser.parseObject("{\"name\": \"test\", \"nums\": [1,2,3], \"enabled\": true}");
        assertEquals("test", obj.get("name"));
        assertEquals(Boolean.TRUE, obj.get("enabled"));
        JsonArray nums = obj.getJsonArray("nums");
        assertEquals(3, nums.size());
    }

    @Test
    public void testParseObjectInArray() {
        JsonArray arr = parser.parseArray("[{\"k\": 1}, {\"k\": 2}]");
        assertEquals(2, arr.size());
        JsonObject o0 = arr.getJsonObject(0);
        assertEquals(1, o0.get("k"));
    }

    @Test
    public void testParseNullValue() {
        Object result = parser.fromJSON("null");
        assertNull(result);
    }

    @Test
    public void testParseFromJSON() {
        Object result = parser.fromJSON("{\"x\": 1}");
        assertTrue(result instanceof JsonObject);
    }

    // ===== 错误处理 =====

    @Test(expected = com.zifang.util.json.exception.JsonParseException.class)
    public void testParseInvalidJson() {
        parser.fromJSON("{invalid}");
    }

    @Test(expected = com.zifang.util.json.exception.JsonParseException.class)
    public void testParseArrayAsObject() {
        parser.parseObject("[1, 2, 3]");
    }

    @Test(expected = com.zifang.util.json.exception.JsonParseException.class)
    public void testParseObjectAsArray() {
        parser.parseArray("{\"k\": 1}");
    }
}