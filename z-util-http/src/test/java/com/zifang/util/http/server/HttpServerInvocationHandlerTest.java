package com.zifang.util.http.server;

import com.zifang.util.http.base.define.RestController;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HttpServerInvocationHandlerTest {

    @Test
    public void testConstructorWithInterface() {
        HttpServerInvocationHandler handler = new HttpServerInvocationHandler(TestApi.class);
        assertEquals(TestApi.class, getTargetFieldValue(handler));
    }

    @Test
    public void testConstructorWithInterfaceAndContextParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        
        HttpServerInvocationHandler handler = new HttpServerInvocationHandler(TestApi.class, params);
        assertEquals(TestApi.class, getTargetFieldValue(handler));
        assertSame(params, getContextParamsFieldValue(handler));
    }

    private Object getTargetFieldValue(HttpServerInvocationHandler handler) {
        try {
            Field field = HttpServerInvocationHandler.class.getDeclaredField("target");
            field.setAccessible(true);
            return field.get(handler);
        } catch (Exception e) {
            return null;
        }
    }

    private Object getContextParamsFieldValue(HttpServerInvocationHandler handler) {
        try {
            Field field = HttpServerInvocationHandler.class.getDeclaredField("contextParams");
            field.setAccessible(true);
            return field.get(handler);
        } catch (Exception e) {
            return null;
        }
    }

    @RestController("/test")
    public interface TestApi {
    }
}
