package com.zifang.util.http.client;

import com.zifang.util.http.base.define.RestController;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class HttpRequestProxyTest {

    @Test
    public void testProxyWithInterface() {
        TestApi proxy = HttpRequestProxy.proxy(TestApi.class);
        assertNotNull(proxy);
    }

    @Test
    public void testProxyWithInterfaceAndContextParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("key", "value");
        
        TestApi proxy = HttpRequestProxy.proxy(TestApi.class, params);
        assertNotNull(proxy);
    }

    @Test
    public void testProxyReturnsCorrectType() {
        TestApi proxy = HttpRequestProxy.proxy(TestApi.class);
        assertTrue(proxy instanceof TestApi);
    }

    @RestController("/test")
    public interface TestApi {
    }
}
