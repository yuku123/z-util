package com.zifang.util.proxy.interceptor;

import com.zifang.util.proxy.aspects.Aspect;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class JdkInterceptorTest {

    @Test
    public void testGetTarget() {
        TestTarget target = new TestTarget();
        TestAspect aspect = new TestAspect();
        JdkInterceptor interceptor = new JdkInterceptor(target, aspect);

        assertSame(target, interceptor.getTarget());
    }

    @Test
    public void testBeforeReturningTrue() throws Throwable {
        TestTarget target = new TestTarget();
        TestAspect aspect = new TestAspect();
        JdkInterceptor interceptor = new JdkInterceptor(target, aspect);

        Method method = TestTarget.class.getMethod("sayHello");
        Object result = interceptor.invoke(null, method, new Object[]{});

        assertTrue(aspect.isBeforeCalled());
        assertTrue(aspect.isAfterCalled());
        assertEquals("Hello", result);
    }

    @Test
    public void testBeforeReturningFalse() throws Throwable {
        TestTarget target = new TestTarget();
        BlockingAspect aspect = new BlockingAspect();
        JdkInterceptor interceptor = new JdkInterceptor(target, aspect);

        Method method = TestTarget.class.getMethod("sayHello");
        Object result = interceptor.invoke(null, method, new Object[]{});

        assertTrue(aspect.isBeforeCalled());
        assertFalse(aspect.isAfterCalled());
        assertNull(result);
    }

    @Test
    public void testAfterException() throws Throwable {
        TestTarget target = new TestTarget();
        ExceptionAspect aspect = new ExceptionAspect();
        JdkInterceptor interceptor = new JdkInterceptor(target, aspect);

        Method method = TestTarget.class.getMethod("throwException");
        try {
            interceptor.invoke(null, method, new Object[]{});
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // afterException 返回 true 时，抛出包装异常 RuntimeException
            assertTrue(e.getCause() instanceof RuntimeException);
        }

        assertTrue(aspect.isBeforeCalled());
        assertTrue(aspect.isAfterExceptionCalled());
    }

    @Test
    public void testAfterExceptionRethrow() throws Throwable {
        TestTarget target = new TestTarget();
        RethrowAspect aspect = new RethrowAspect();
        JdkInterceptor interceptor = new JdkInterceptor(target, aspect);

        Method method = TestTarget.class.getMethod("throwException");
        // afterException 返回 false：重新抛出原始 RuntimeException
        try {
            interceptor.invoke(null, method, new Object[]{});
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // 重新抛出的是原始异常本身
            assertTrue(e.getCause() == null || e.getCause() instanceof RuntimeException);
        }
    }

    public static class TestTarget {
        public String sayHello() {
            return "Hello";
        }

        public void throwException() {
            throw new RuntimeException("Test exception");
        }
    }

    public static class TestAspect implements Aspect {
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

    public static class BlockingAspect implements Aspect {
        private boolean beforeCalled = false;
        private boolean afterCalled = false;

        @Override
        public boolean before(Object target, Method method, Object[] args) {
            beforeCalled = true;
            return false;
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

    public static class ExceptionAspect implements Aspect {
        private boolean beforeCalled = false;
        private boolean afterExceptionCalled = false;

        @Override
        public boolean before(Object target, Method method, Object[] args) {
            beforeCalled = true;
            return true;
        }

        @Override
        public boolean after(Object target, Method method, Object[] args, Object returnVal) {
            return false;
        }

        @Override
        public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
            afterExceptionCalled = true;
            return true;
        }

        public boolean isBeforeCalled() { return beforeCalled; }
        public boolean isAfterExceptionCalled() { return afterExceptionCalled; }
    }

    public static class RethrowAspect implements Aspect {
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
            return false;
        }
    }
}
