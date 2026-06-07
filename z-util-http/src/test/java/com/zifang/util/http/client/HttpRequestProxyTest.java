package com.zifang.util.http.client;

import com.zifang.util.http.base.define.RestController;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * HttpRequestProxyTest类。
 */
public class HttpRequestProxyTest {

    @Test
    /**
     * testProxyWithInterface方法。
     */
    public void testProxyWithInterface() {
        TestApi proxy = HttpRequestProxy.proxy(TestApi.class);
        assertNotNull(proxy);
    }

    @Test
    /**
     * testProxyWithInterfaceAndContextParams方法。
     */
    public void testProxyWithInterfaceAndContextParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("key", "value");
        
        TestApi proxy = HttpRequestProxy.proxy(TestApi.class, params);
        assertNotNull(proxy);
    }

    @Test
    /**
     * testProxyReturnsCorrectType方法。
     */
    public void testProxyReturnsCorrectType() {
        TestApi proxy = HttpRequestProxy.proxy(TestApi.class);
        assertTrue(proxy instanceof TestApi);
    }

    @RestController("/test")
/**
 * TestApi接口。
 */
    public interface TestApi {
    }
}
