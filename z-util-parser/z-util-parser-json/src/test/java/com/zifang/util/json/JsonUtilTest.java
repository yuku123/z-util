package com.zifang.util.json;

import com.zifang.util.json.define.TypeReference;
import com.zifang.util.json.model.JsonArray;
import com.zifang.util.json.model.JsonObject;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JsonUtilTest {

    // ==================== toJson 基本类型 ====================

    @Test
    public void testToJsonNull() {
        assertEquals("null", JsonUtil.toJson(null));
    }

    @Test
    public void testToJsonString() {
        assertEquals("\"hello\"", JsonUtil.toJson("hello"));
    }

    @Test
    public void testToJsonStringWithQuotes() {
        assertEquals("\"hello\\\"world\"", JsonUtil.toJson("hello\"world"));
    }

    @Test
    public void testToJsonStringWithNewline() {
        assertEquals("\"hello\\nworld\"", JsonUtil.toJson("hello\nworld"));
    }

    @Test
    public void testToJsonInteger() {
        assertEquals("42", JsonUtil.toJson(42));
    }

    @Test
    public void testToJsonLong() {
        assertEquals("42", JsonUtil.toJson(42L));
    }

    @Test
    public void testToJsonDouble() {
        assertEquals("3.14", JsonUtil.toJson(3.14));
    }

    @Test
    public void testToJsonBoolean() {
        assertEquals("true", JsonUtil.toJson(true));
        assertEquals("false", JsonUtil.toJson(false));
    }

    // ==================== toJson 集合 ====================

    @Test
    public void testToJsonEmptyList() {
        assertEquals("[]", JsonUtil.toJson(new ArrayList<>()));
    }

    @Test
    public void testToJsonList() {
        List<Object> list = Arrays.asList(1, "two", true);
        String json = JsonUtil.toJson(list);
        assertTrue(json.contains("1"));
        assertTrue(json.contains("\"two\""));
        assertTrue(json.contains("true"));
    }

    @Test
    public void testToJsonEmptyMap() {
        assertEquals("{}", JsonUtil.toJson(new LinkedHashMap<>()));
    }

    @Test
    public void testToJsonMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "test");
        map.put("age", 18);
        String json = JsonUtil.toJson(map);
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"test\""));
        assertTrue(json.contains("\"age\""));
        assertTrue(json.contains("18"));
    }

    // ==================== toJson POJO ====================

    public static class Person {
        public String name;
        public int age;
        public Person() {}
        public Person(String name, int age) { this.name = name; this.age = age; }
    }

    @Test
    public void testToJsonPojo() {
        Person p = new Person("Tom", 20);
        String json = JsonUtil.toJson(p);
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"Tom\""));
        assertTrue(json.contains("\"age\""));
        assertTrue(json.contains("20"));
    }

    @Test
    public void testToJsonPojoWithNull() {
        Person p = new Person(null, 0);
        String json = JsonUtil.toJson(p);
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("null"));
    }

    // ==================== parseObject / parseArray ====================

    @Test
    public void testParseObject() {
        JsonObject obj = JsonUtil.parseObject("{\"name\":\"test\",\"age\":18}");
        assertEquals("test", obj.get("name"));
        assertEquals(18L, ((Number) obj.get("age")).longValue());
    }

    @Test
    public void testParseArray() {
        JsonArray arr = JsonUtil.parseArray("[1,2,3]");
        assertEquals(3, arr.size());
        assertEquals(1L, ((Number) arr.get(0)).longValue());
    }

    @Test
    public void testParseEmptyObject() {
        JsonObject obj = JsonUtil.parseObject("{}");
        assertEquals(0, obj.size());
    }

    @Test
    public void testParseEmptyArray() {
        JsonArray arr = JsonUtil.parseArray("[]");
        assertEquals(0, arr.size());
    }

    // ==================== fromJson 基本类型 ====================

    @Test
    public void testFromJsonString() {
        String result = JsonUtil.fromJson("\"hello\"", String.class);
        assertEquals("hello", result);
    }

    @Test
    public void testFromJsonInt() {
        Integer result = JsonUtil.fromJson("42", Integer.class);
        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testFromJsonLong() {
        Long result = JsonUtil.fromJson("42", Long.class);
        assertEquals(Long.valueOf(42), result);
    }

    @Test
    public void testFromJsonDouble() {
        Double result = JsonUtil.fromJson("3.14", Double.class);
        assertEquals(3.14, result, 0.001);
    }

    @Test
    public void testFromJsonBoolean() {
        Boolean result = JsonUtil.fromJson("true", Boolean.class);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFromJsonNull() {
        String result = JsonUtil.fromJson("null", String.class);
        assertNull(result);
    }

    // ==================== fromJson 泛型 ====================

    @Test
    public void testFromJsonListTypeRef() {
        String json = "[1,2,3]";
        List<Integer> result = JsonUtil.fromJson(json, new TypeReference<List<Integer>>() {});
        assertEquals(3, result.size());
        assertEquals(1, (int) result.get(0));
        assertEquals(2, (int) result.get(1));
        assertEquals(3, (int) result.get(2));
    }

    @Test
    public void testFromJsonListOfObjects() {
        String json = "[{\"name\":\"Tom\"},{\"name\":\"Jerry\"}]";
        List<Person> result = JsonUtil.fromJson(json, new TypeReference<List<Person>>() {});
        assertEquals(2, result.size());
        assertEquals("Tom", result.get(0).name);
        assertEquals("Jerry", result.get(1).name);
    }

    @Test
    public void testFromJsonMapTypeRef() {
        String json = "{\"name\":\"test\",\"age\":18}";
        Map<String, Object> result = JsonUtil.fromJson(json, new TypeReference<Map<String, Object>>() {});
        assertEquals("test", result.get("name"));
        assertEquals(18L, ((Number) result.get("age")).longValue());
    }

    // ==================== fromJson POJO ====================

    @Test
    public void testFromJsonPojo() {
        String json = "{\"name\":\"Tom\",\"age\":20}";
        Person p = JsonUtil.fromJson(json, Person.class);
        assertEquals("Tom", p.name);
        assertEquals(20, p.age);
    }

    // ==================== roundtrip ====================

    @Test
    public void testRoundtripPojo() {
        Person original = new Person("Alice", 30);
        String json = JsonUtil.toJson(original);
        Person restored = JsonUtil.fromJson(json, Person.class);
        assertEquals("Alice", restored.name);
        assertEquals(30, restored.age);
    }

    @Test
    public void testRoundtripNestedObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("person", new Person("Bob", 25));
        map.put("active", true);
        String json = JsonUtil.toJson(map);
        JsonObject obj = JsonUtil.parseObject(json);
        JsonObject person = (JsonObject) obj.get("person");
        assertEquals("Bob", person.get("name"));
    }

    // ==================== query ====================

    @Test
    public void testQuery() {
        String json = "{\"store\":{\"book\":[{\"title\":\"Intro\"},{\"title\":\"Ref\"}]}}";
        List<Object> titles = JsonUtil.query(json, "$.store.book[*].title");
        assertFalse(titles.isEmpty());
    }

    // ==================== 边界情况 ====================

    @Test
    public void testToJsonEmptyPojo() {
        String json = JsonUtil.toJson(new Person());
        assertTrue(json.contains("\"name\""));
        assertTrue(json.contains("\"age\""));
    }

    @Test
    public void testFromJsonEmptyJsonObject() {
        String json = "{}";
        Person p = JsonUtil.fromJson(json, Person.class);
        assertNotNull(p);
    }

    @Test
    public void testUnicodeChars() {
        String json = JsonUtil.toJson("中文测试");
        assertEquals("\"中文测试\"", json);
    }

    @Test
    public void testSpecialCharsInString() {
        String json = JsonUtil.toJson("a\tb\rc\nd");
        assertTrue(json.contains("\\t"));
        assertTrue(json.contains("\\r"));
        assertTrue(json.contains("\\n"));
    }
}