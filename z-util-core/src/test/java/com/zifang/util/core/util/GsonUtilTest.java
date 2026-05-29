package com.zifang.util.core.util;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GsonUtilTest {

    @Test
    public void testObjectToJsonStr_WithString() {
        String result = GsonUtil.objectToJsonStr("hello");
        assertEquals("\"hello\"", result);
    }

    @Test
    public void testObjectToJsonStr_WithInteger() {
        String result = GsonUtil.objectToJsonStr(123);
        assertEquals("123", result);
    }

    @Test
    public void testObjectToJsonStr_WithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "test");
        map.put("value", 123);
        String result = GsonUtil.objectToJsonStr(map);
        assertTrue(result.contains("\"name\""));
        assertTrue(result.contains("\"test\""));
        assertTrue(result.contains("\"value\""));
        assertTrue(result.contains("123"));
    }

    @Test
    public void testObjectToJsonStr_WithList() {
        List<String> list = Arrays.asList("a", "b", "c");
        String result = GsonUtil.objectToJsonStr(list);
        assertTrue(result.contains("\"a\""));
        assertTrue(result.contains("\"b\""));
        assertTrue(result.contains("\"c\""));
    }

    @Test
    public void testObjectToJsonStr_WithNull() {
        String result = GsonUtil.objectToJsonStr(null);
        assertEquals("null", result);
    }

    @Test
    public void testJsonStrToObject_WithString() {
        String json = "\"hello\"";
        String result = GsonUtil.jsonStrToObject(json, String.class);
        assertEquals("hello", result);
    }

    @Test
    public void testJsonStrToObject_WithInteger() {
        String json = "123";
        Integer result = GsonUtil.jsonStrToObject(json, Integer.class);
        assertEquals(Integer.valueOf(123), result);
    }

    @Test
    public void testJsonStrToObject_WithMap() {
        String json = "{\"name\":\"test\",\"value\":123}";
        Map result = GsonUtil.jsonStrToObject(json, Map.class);
        assertEquals("test", result.get("name"));
        assertEquals(123, ((Number) result.get("value")).intValue());
    }

    @Test
    public void testJsonStrToObject_WithTypeReference() {
        String json = "[1,2,3]";
        Type type = new TypeToken<List<Integer>>() {}.getType();
        List<Integer> result = GsonUtil.jsonStrToObject(json, type);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    public void testJsonStrToObject_WithLocalDateTime() {
        String json = "\"2024-01-15T10:30:00\"";
        LocalDateTime result = GsonUtil.jsonStrToObject(json, LocalDateTime.class);
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(15, result.getDayOfMonth());
        assertEquals(10, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void testChangeToSubClass() {
        Parent parent = new Parent("test", 100);
        Child child = GsonUtil.changeToSubClass(parent, Child.class);
        assertNotNull(child);
        assertEquals("test", child.name);
        assertEquals(100, child.value);
    }

    @Test
    public void testToMap() {
        TestBean bean = new TestBean("hello", 123);
        Map<String, Object> result = GsonUtil.toMap(bean);
        assertNotNull(result);
        assertEquals("hello", result.get("name"));
        assertEquals(123, ((Number) result.get("value")).intValue());
    }

    @Test
    public void testRoundTrip() {
        TestBean original = new TestBean("roundTrip", 999);
        String json = GsonUtil.objectToJsonStr(original);
        TestBean restored = GsonUtil.jsonStrToObject(json, TestBean.class);
        assertNotNull(restored);
        assertEquals(original.name, restored.name);
        assertEquals(original.value, restored.value);
    }

    // Helper classes
    private static class Parent {
        public String name;
        public int value;
        public Parent(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    private static class Child extends Parent {
        public Child(String name, int value) {
            super(name, value);
        }
    }

    private static class TestBean {
        public String name;
        public int value;
        public TestBean(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}