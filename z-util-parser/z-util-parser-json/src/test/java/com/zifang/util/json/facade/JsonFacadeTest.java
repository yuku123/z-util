package com.zifang.util.json.facade;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * JsonFacade 统一门面测试。
 * 使用 GsonBackend 验证接口契约，JacksonBackend 同理。
 */
public class JsonFacadeTest {

    private final JsonFacade gsonFacade = new GsonBackend();
    private final JsonFacade jacksonFacade = new JacksonBackend();

    // ==================== toJson / fromJson ====================

    @Test
    public void testToJsonFromJson_Roundtrip() {
        User user = new User(1L, "zifang", "zifang@example.com");
        String json = gsonFacade.toJson(user);
        assertNotNull(json);
        assertTrue(json.contains("zifang"));
        assertTrue(json.contains("zifang@example.com"));

        User restored = gsonFacade.fromJson(json, User.class);
        assertEquals(user.id, restored.id);
        assertEquals(user.name, restored.name);
        assertEquals(user.email, restored.email);
    }

    @Test
    public void testToJsonFromJson_JacksonBackend() {
        User user = new User(2L, "alice", "alice@example.com");
        String json = jacksonFacade.toJson(user);
        assertNotNull(json);

        User restored = jacksonFacade.fromJson(json, User.class);
        assertEquals(user.id, restored.id);
        assertEquals(user.name, restored.name);
    }

    @Test
    public void testToPrettyJson() {
        User user = new User(1L, "bob", "bob@example.com");
        String pretty = gsonFacade.toPrettyJson(user);
        assertNotNull(pretty);
        // pretty json 应包含换行
        assertTrue(pretty, pretty.contains("\n") || pretty.contains("\r"));
    }

    @Test
    public void testToJson_Null() {
        assertEquals("null", gsonFacade.toJson(null));
    }

    @Test
    public void testFromJson_Null() {
        User u = gsonFacade.fromJson("null", User.class);
        assertNull(u);
    }

    @Test
    public void testFromJson_List() {
        List<User> list = Arrays.asList(
                new User(1L, "a", "a@test.com"),
                new User(2L, "b", "b@test.com")
        );
        String json = gsonFacade.toJson(list);
        TypeBinding<List<User>> binding = new TypeBinding<List<User>>() {};
        List<User> restored = gsonFacade.fromJson(json, binding);
        assertEquals(2, restored.size());
        assertEquals("a", restored.get(0).name);
    }

    @Test
    public void testFromJson_Map() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "zifang");
        map.put("age", 30);
        String json = gsonFacade.toJson(map);
        Map<String, Object> restored = gsonFacade.fromJson(json,
                new TypeBinding<Map<String, Object>>() {});
        assertEquals("zifang", restored.get("name"));
        // age 可能返回 Integer 或 Double，改为比较数值
        assertEquals(30.0, ((Number) restored.get("age")).doubleValue(), 1e-10);
    }

    // ==================== parseObject / parseArray ====================

    @Test
    public void testParseObject() {
        String json = "{\"name\":\"zifang\",\"age\":30}";
        JsonNode node = gsonFacade.parseObject(json);
        assertTrue(node.isObject());
        assertFalse(node.isArray());
        assertEquals("zifang", node.getString("name"));
        assertEquals(30, (int) node.getInt("age"));
    }

    @Test
    public void testParseArray() {
        String json = "[1,2,3,4,5]";
        JsonNode node = gsonFacade.parseArray(json);
        assertTrue(node.isArray());
        assertFalse(node.isObject());
        assertEquals(5, node.size());
    }

    @Test
    public void testParseObject_Array() {
        String json = "{\"items\":[1,2,3]}";
        JsonNode node = gsonFacade.parseObject(json);
        assertTrue(node.isObject());
        assertTrue(node.getNode("items").isArray());
        assertEquals(3, node.getNode("items").size());
    }

    // ==================== JSONPath 查询 ====================

    @Test
    public void testQuery_Jackson() {
        String json = "{\"store\":{\"book\":[{\"author\":\"Author1\"},{\"author\":\"Author2\"}]}}";
        Object result = jacksonFacade.query(json, "$.store.book[0].author");
        assertEquals("Author1", result);
    }

    // ==================== isValidJson ====================

    @Test
    public void testIsValidJson() {
        assertTrue(gsonFacade.isValidJson("{\"name\":\"zifang\"}"));
        assertTrue(gsonFacade.isValidJson("[1,2,3]"));
        assertFalse(gsonFacade.isValidJson("{invalid}"));
        assertFalse(gsonFacade.isValidJson(null));
        assertFalse(gsonFacade.isValidJson(""));
    }

    // ==================== engine ====================

    @Test
    public void testEngine() {
        assertEquals("Gson", gsonFacade.engine());
        assertEquals("Jackson", jacksonFacade.engine());
    }

    // ==================== 辅助类 ====================

    public static class User {
        public Long id;
        public String name;
        public String email;

        public User() {}

        public User(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }
}