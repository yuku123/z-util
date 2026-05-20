package com.zifang.util.http.server;

import com.zifang.util.http.base.define.RestController;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerProxyTest {

    @Test
    public void testProxyReturnsNonNull() {
        TestApi proxy = HttpServerProxy.proxy(TestApi.class);
        assertNotNull(proxy);
    }

    @Test
    public void testProxyReturnsCorrectType() {
        TestApi proxy = HttpServerProxy.proxy(TestApi.class);
        assertTrue(proxy instanceof TestApi);
    }

    @RestController("/test")
    public interface TestApi {
    }
}
