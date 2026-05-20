package com.zifang.util.core.lang.reflect;

import com.zifang.util.core.lang.converter.IConverter;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class ClassParserTest {

    @Test
    public void test1() {
        ClassParser a = new ClassParserFactory().getInstance(A.class);

        assertEquals(1, a.getCurrentPublicField().size());
        assertEquals(1, a.getCurrentPrivateField().size());
        assertEquals(1, a.getCurrentProtectedField().size());
        assertEquals(3, a.getCurrentAllField().size());

        assertEquals(6, a.getCurrentAllMethod().size());
        assertEquals(1, a.getCurrentPrivateMethod().size());
        assertEquals(1, a.getCurrentProtectedMethod().size());
        assertEquals(3, a.getCurrentPublicMethod().size());
        assertEquals(1, a.getCurrentDefaultMethod().size());

        assertTrue(a.isNormalClass());
    }

    @Test
    public void test2() {
        ClassParser a = new ClassParserFactory().getInstance(A.class);
    }

    @Test
    public void test3() {
        ClassParser a = new ClassParserFactory().getInstance(A.class);
        ClassParser b = new ClassParserFactory().getInstance(B.class);
        ClassParser c = new ClassParserFactory().getInstance(C.class);
        ClassParser d = new ClassParserFactory().getInstance(D.class);

        Type type1 = a.getGenericType(IConverter.class);
        Type type2 = b.getGenericType(IConverter.class);
        Type type3 = c.getGenericType(IConverter.class);
        Type type4 = d.getGenericType(IConverter.class);

        assert type1 != null;
        assert type2 == null;
        assert type3 != null;
        assert type4 != null;

    }
}


class A extends AbstractA implements IA1, IA2, IConverter<Integer, Long> {
    private String a1;
    protected String a2;
    public String a3;

    public void t1() {
    }

    private void t2() {
    }

    protected void t3() {
    }

    void t4() {
    }

    @Override
    public Long to(Integer integer, Long aLong) {
        return null;
    }
}

interface IA1 {
}

interface IA2 extends IA21 {
}

interface IA21 {
}

abstract class AbstractA extends B {
}

class B {
}

class BA extends A {
    @Override
    public Long to(Integer integer, Long aLong) {
        return null;
    }
}

class C implements D {
    @Override
    public Long to(Integer integer, Long aLong) {
        return null;
    }
}

interface D extends IConverter<Integer, Long> {
}
