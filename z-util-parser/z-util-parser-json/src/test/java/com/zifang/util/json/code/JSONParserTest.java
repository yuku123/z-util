package com.zifang.util.json.code;

import com.zifang.util.json.JSONParser;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JSONParser (tokenizer + recursive descent parser) 核心功能测试
 */
public class JSONParserTest {

    private JSONParser jsonParser = new JSONParser();

    // ===== 基础类型 =====

    @Test
    public void testParseNull() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": null}");
        assertNull(obj.get("k"));
    }

    @Test
    public void testParseTrue() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": true}");
        assertEquals(Boolean.TRUE, obj.get("k"));
    }

    @Test
    public void testParseFalse() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": false}");
        assertEquals(Boolean.FALSE, obj.get("k"));
    }

    @Test
    public void testParsePositiveInt() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": 123}");
        assertEquals(123, obj.get("k"));
    }

    @Test
    public void testParseNegativeInt() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": -456}");
        assertEquals(-456, obj.get("k"));
    }

    @Test
    public void testParsePositiveDouble() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": 3.14}");
        assertEquals(3.14, obj.get("k"));
    }

    @Test
    public void testParseNegativeDouble() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": -2.718}");
        assertEquals(-2.718, obj.get("k"));
    }

    @Test
    public void testParseScientificNotation() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": 1.23e10}");
        assertEquals(1.23e10, obj.get("k"));

        obj = (JsonObject) jsonParser.fromJSON("{\"k\": 1e10}");
        assertEquals(1.0e10, obj.get("k"));

        obj = (JsonObject) jsonParser.fromJSON("{\"k\": 1.23E-5}");
        assertEquals(1.23e-5, obj.get("k"));

        obj = (JsonObject) jsonParser.fromJSON("{\"k\": -1.5e3}");
        assertEquals(-1.5e3, obj.get("k"));
    }

    @Test
    public void testParseString() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"k\": \"hello\"}");
        assertEquals("hello", obj.get("k"));
    }

    // ===== 对象 =====

    @Test
    public void testParseEmptyObject() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{}");
        assertEquals(0, obj.getAllKeyValue().size());
    }

    @Test
    public void testParseSimpleObject() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"name\": \"zifang\"}");
        assertEquals("zifang", obj.get("name"));
    }

    @Test
    public void testParseNestedObject() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"outer\": {\"inner\": \"value\"}}");
        JsonObject inner = obj.getJsonObject("outer");
        assertEquals("value", inner.get("inner"));
    }

    // ===== 数组 =====

    @Test
    public void testParseEmptyArray() throws Exception {
        JsonArray arr = (JsonArray) jsonParser.fromJSON("[]");
        assertEquals(0, arr.size());
    }

    @Test
    public void testParseSimpleArray() throws Exception {
        JsonArray arr = (JsonArray) jsonParser.fromJSON("[1, 2, 3]");
        assertEquals(3, arr.size());
        assertEquals(1, arr.get(0));
        assertEquals(2, arr.get(1));
        assertEquals(3, arr.get(2));
    }

    @Test
    public void testParseArrayMixedTypes() throws Exception {
        JsonArray arr = (JsonArray) jsonParser.fromJSON("[0.1, \"a\", 123, 999, true, false, null]");
        assertEquals(0.1, arr.get(0));
        assertEquals("a", arr.get(1));
        assertEquals(123, arr.get(2));
        assertEquals(999, arr.get(3));
        assertEquals(Boolean.TRUE, arr.get(4));
        assertEquals(Boolean.FALSE, arr.get(5));
        assertNull(arr.get(6));
    }

    // ===== 复杂结构 =====

    @Test
    public void testParseComplexObject() throws Exception {
        String json = "{\"a\": 1, \"b\": \"b\", \"c\": {\"a\": 1, \"b\": null, \"d\": [0.1, \"a\", 1, 2, 123, 999.0, true, false, null]}}";
        JsonObject obj = (JsonObject) jsonParser.fromJSON(json);

        assertEquals(1, obj.get("a"));
        assertEquals("b", obj.get("b"));

        JsonObject c = obj.getJsonObject("c");
        assertEquals(1, c.get("a"));
        assertNull(c.get("b"));

        JsonArray d = c.getJsonArray("d");
        assertEquals(0.1, d.get(0));
        assertEquals("a", d.get(1));
        assertEquals(123, d.get(4));
        assertEquals(999.0, d.get(5));
        assertEquals(Boolean.TRUE, d.get(6));
        assertEquals(Boolean.FALSE, d.get(7));
        assertNull(d.get(8));
    }

    @Test
    public void testParseNestedArray() throws Exception {
        JsonArray arr = (JsonArray) jsonParser.fromJSON("[[1, 2, 3, \"中\"]]");
        JsonArray inner = arr.getJsonArray(0);
        assertEquals(1, inner.get(0));
        assertEquals(2, inner.get(1));
        assertEquals(3, inner.get(2));
        assertEquals("中", inner.get(3));
    }

    // ===== Unicode =====

    @Test
    public void testParseChineseString() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"name\": \"狄仁杰\", \"type\": \"射手\"}");
        assertEquals("狄仁杰", obj.get("name"));
        assertEquals("射手", obj.get("type"));
    }

    // ===== getJsonObject / getJsonArray 异常 =====

    @Test(expected = IllegalArgumentException.class)
    public void testGetJsonObjectMissingKey() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"a\": 1}");
        obj.getJsonObject("notexist");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJsonArrayMissingKey() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"a\": 1}");
        obj.getJsonArray("notexist");
    }

    @Test(expected = com.zifang.util.json.exception.JsonTypeException.class)
    public void testGetJsonObjectWrongType() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"a\": 1}");
        obj.getJsonObject("a");
    }

    @Test(expected = com.zifang.util.json.exception.JsonTypeException.class)
    public void testGetJsonArrayWrongType() throws Exception {
        JsonObject obj = (JsonObject) jsonParser.fromJSON("{\"a\": \"str\"}");
        obj.getJsonArray("a");
    }
}
