package com.zifang.util.http.server;

import com.zifang.util.http.base.define.GetMapping;
import com.zifang.util.http.base.define.RestController;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerProxyFactoryTest {

    @Test
    public void testGetInterfaceClass() {
        HttpServerProxyFactory<TestApi> factory = new HttpServerProxyFactory<>(TestApi.class, new TestApiImpl());
        assertEquals(TestApi.class, factory.getInterfaceClass());
    }

    @Test
    public void testGetTarget() {
        TestApiImpl target = new TestApiImpl();
        HttpServerProxyFactory<TestApi> factory = new HttpServerProxyFactory<>(TestApi.class, target);
        assertSame(target, factory.getTarget());
    }

    @RestController("/api")
    public interface TestApi {
        @GetMapping("/test")
        String test();
    }

    public static class TestApiImpl implements TestApi {
        @Override
        public String test() {
            return "test";
        }
    }
}
