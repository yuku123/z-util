package com.zifang.util.devops.docker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * DockerClient 内部工具方法测试
 * <p>
 * 通过反射调用 private 方法验证其正确性。
 */
public class DockerClientUtilTest {

    private DockerClient client;

    @Before
    public void setUp() {
        client = new DockerClient();
    }

    // ==================== parseSize ====================

    @Test
    public void testParseSizeBytes() {
        assertEquals(0L, parseSize("0B"));
        assertEquals(100L, parseSize("100B"));
    }

    @Test
    public void testParseSizeKB() {
        assertEquals(1024L, parseSize("1KB"));
        assertEquals(1024L, parseSize("1KB"));
        assertEquals(1536L, parseSize("1.5KB"));
    }

    @Test
    public void testParseSizeMB() {
        assertEquals(5 * 1024 * 1024, parseSize("5MB"));
        assertEquals((long) (1.5 * 1024 * 1024), parseSize("1.5MB"));
    }

    @Test
    public void testParseSizeGB() {
        assertEquals(2L * 1024 * 1024 * 1024, parseSize("2GB"));
        assertEquals((long) (1.25 * 1024 * 1024 * 1024), parseSize("1.25GB"));
    }

    @Test
    public void testParseSizeNoSuffix() {
        assertEquals(0L, parseSize(""));
        assertEquals(0L, parseSize(null));
    }

    @Test
    public void testParseSizeWithSpace() {
        assertEquals(1024L, parseSize(" 1KB "));
        assertEquals(1024L, parseSize("  1KB  "));
    }

    // ==================== parsePercent ====================

    @Test
    public void testParsePercent() {
        assertEquals(25.5, parsePercent("25.5%"), 0.001);
        assertEquals(0.0, parsePercent("0%"), 0.001);
        assertEquals(100.0, parsePercent("100%"), 0.001);
    }

    @Test
    public void testParsePercentWithSpace() {
        assertEquals(50.0, parsePercent(" 50% "), 0.001);
    }

    @Test
    public void testParsePercentInvalid() {
        assertEquals(0.0, parsePercent(""), 0.001);
        assertEquals(0.0, parsePercent(null), 0.001);
        assertEquals(0.0, parsePercent("abc"), 0.001);
    }

    // ==================== getJsonString ====================

    @Test
    public void testGetJsonStringSimple() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "nginx");
        obj.addProperty("state", "running");

        assertEquals("nginx", getJsonString(obj, "name"));
        assertEquals("running", getJsonString(obj, "state"));
    }

    @Test
    public void testGetJsonStringNested() {
        JsonObject inner = new JsonObject();
        inner.addProperty("Image", "nginx:latest");

        JsonObject outer = new JsonObject();
        outer.add("Config", inner);

        assertEquals("nginx:latest", getJsonString(outer, "Config", "Image"));
    }

    @Test
    public void testGetJsonStringMissingKey() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", "nginx");

        assertNull(getJsonString(obj, "missing"));
    }

    @Test
    public void testGetJsonStringNullValue() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", (String) null);

        assertNull(getJsonString(obj, "name"));
    }

    // ==================== private method invocations via reflection ====================

    private long parseSize(String str) {
        try {
            java.lang.reflect.Method m = DockerClient.class.getDeclaredMethod("parseSize", String.class);
            m.setAccessible(true);
            return (Long) m.invoke(client, str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private double parsePercent(String str) {
        try {
            java.lang.reflect.Method m = DockerClient.class.getDeclaredMethod("parsePercent", String.class);
            m.setAccessible(true);
            return (Double) m.invoke(client, str);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonString(JsonObject obj, String... keys) {
        try {
            java.lang.reflect.Method m = DockerClient.class.getDeclaredMethod("getJsonString", JsonObject.class, String[].class);
            m.setAccessible(true);
            return (String) m.invoke(client, obj, (Object) keys);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
