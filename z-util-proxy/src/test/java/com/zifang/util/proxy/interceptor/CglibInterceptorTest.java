package com.zifang.util.proxy.interceptor;

import com.zifang.util.proxy.aspects.Aspect;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class CglibInterceptorTest {

    @Test
    public void testGetTarget() {
        TestTarget target = new TestTarget();
        TestAspect aspect = new TestAspect();
        CglibInterceptor interceptor = new CglibInterceptor(target, aspect);

        assertSame(target, interceptor.getTarget());
    }

    @Test
    public void testBeforeReturningTrue() {
        TestTarget target = new TestTarget();
        TestAspect aspect = new TestAspect();
        CglibInterceptor interceptor = new CglibInterceptor(target, aspect);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestTarget.class);
        enhancer.setCallback(interceptor);
        TestTarget proxy = (TestTarget) enhancer.create();

        assertEquals("Hello", proxy.sayHello());
        assertTrue(aspect.isBeforeCalled());
        assertTrue(aspect.isAfterCalled());
    }

    @Test
    public void testBeforeReturningFalse() {
        TestTarget target = new TestTarget();
        BlockingAspect aspect = new BlockingAspect();
        CglibInterceptor interceptor = new CglibInterceptor(target, aspect);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestTarget.class);
        enhancer.setCallback(interceptor);
        TestTarget proxy = (TestTarget) enhancer.create();

        proxy.sayHello();
        assertTrue(aspect.isBeforeCalled());
        assertFalse(aspect.isAfterCalled());
    }

    @Test
    public void testAfterException() {
        TestTarget target = new TestTarget();
        ExceptionAspect aspect = new ExceptionAspect();
        CglibInterceptor interceptor = new CglibInterceptor(target, aspect);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestTarget.class);
        enhancer.setCallback(interceptor);
        TestTarget proxy = (TestTarget) enhancer.create();

        try {
            proxy.throwException();
        } catch (RuntimeException ignored) {
        }
        assertTrue(aspect.isBeforeCalled());
        assertTrue(aspect.isAfterExceptionCalled());
    }

    @Test(expected = RuntimeException.class)
    public void testAfterExceptionRethrow() {
        TestTarget target = new TestTarget();
        RethrowAspect aspect = new RethrowAspect();
        CglibInterceptor interceptor = new CglibInterceptor(target, aspect);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestTarget.class);
        enhancer.setCallback(interceptor);
        TestTarget proxy = (TestTarget) enhancer.create();

        proxy.throwException();
    }

    // --- Test Inner Classes ---

    public static class TestTarget {
        public String sayHello() {
            return "Hello";
        }
        public void throwException() {
            throw new RuntimeException("test");
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
            return false;
        }

        @Override
        public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
            return true;
        }
    }
}
