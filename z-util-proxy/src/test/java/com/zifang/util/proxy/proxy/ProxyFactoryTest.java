package com.zifang.util.proxy.proxy;

import com.zifang.util.proxy.aspects.Aspect;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ProxyFactoryTest {

    @Test
    public void testCreateReturnsFactory() {
        ProxyFactory factory = ProxyFactory.create();
        assertNotNull(factory);
    }

    @Test
    public void testCreateProxyWithAspectInstance() {
        TestService target = new TestServiceImpl();
        CountingAspect aspect = new CountingAspect();
        
        TestService proxy = ProxyFactory.createProxy(target, aspect);
        assertNotNull(proxy);
        
        String result = proxy.sayHello();
        assertEquals("Hello", result);
        assertTrue(aspect.isBeforeCalled());
        assertTrue(aspect.isAfterCalled());
    }

    @Test
    public void testCreateProxyWithAspectClass() {
        TestService target = new TestServiceImpl();
        
        TestService proxy = ProxyFactory.createProxy(target, SimpleTestAspect.class);
        assertNotNull(proxy);
        
        String result = proxy.sayHello();
        assertEquals("Hello", result);
    }

    @Test
    public void testJdkProxyFactoryProxy() {
        TestService target = new TestServiceImpl();
        CountingAspect aspect = new CountingAspect();
        
        JdkProxyFactory factory = new JdkProxyFactory();
        TestService proxy = factory.proxy(target, aspect);
        assertNotNull(proxy);
        
        String result = proxy.sayHello();
        assertEquals("Hello", result);
    }

    @Test
    public void testCglibProxyFactoryProxy() {
        TestService target = new TestServiceImpl();
        CountingAspect aspect = new CountingAspect();
        
        CglibProxyFactory factory = new CglibProxyFactory();
        TestService proxy = factory.proxy(target, aspect);
        assertNotNull(proxy);
        
        String result = proxy.sayHello();
        assertEquals("Hello", result);
    }

    public interface TestService {
        String sayHello();
    }

    public static class TestServiceImpl implements TestService {
        @Override
        public String sayHello() {
            return "Hello";
        }
    }

    public static class CountingAspect implements Aspect {
        private boolean beforeCalled = false;
        private boolean afterCalled = false;

        @Override
        public boolean before(Object target, Method method, Object[] args) {
            beforeCalled = true;
            return true;
        }

        @Override
        public boolean after(Object target, Method method, Object[] args, Object returnVal) {
            afterCalled = true;
            return true;
        }

        @Override
        public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
            return true;
        }

        public boolean isBeforeCalled() { return beforeCalled; }
        public boolean isAfterCalled() { return afterCalled; }
    }

    public static class SimpleTestAspect implements Aspect {
        @Override
        public boolean before(Object target, Method method, Object[] args) {
            return true;
        }

        @Override
        public boolean after(Object target, Method method, Object[] args, Object returnVal) {
            return true;
        }

        @Override
        public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
            return true;
        }
    }
}
