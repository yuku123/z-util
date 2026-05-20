package com.zifang.util.core.lang;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClassUtilTest {

    // --- isPrimitive ---

    @Test
    public void testIsPrimitive_WithValidPrimitiveTypes() {
        assertTrue(ClassUtil.isPrimitive("int"));
        assertTrue(ClassUtil.isPrimitive("double"));
        assertTrue(ClassUtil.isPrimitive("long"));
        assertTrue(ClassUtil.isPrimitive("short"));
        assertTrue(ClassUtil.isPrimitive("byte"));
        assertTrue(ClassUtil.isPrimitive("boolean"));
        assertTrue(ClassUtil.isPrimitive("char"));
        assertTrue(ClassUtil.isPrimitive("float"));
    }

    @Test
    public void testIsPrimitive_WithInvalidTypes() {
        assertFalse(ClassUtil.isPrimitive("String"));
        assertFalse(ClassUtil.isPrimitive("Integer"));
        assertFalse(ClassUtil.isPrimitive("java.lang.String"));
        assertFalse(ClassUtil.isPrimitive(""));
        assertFalse(ClassUtil.isPrimitive("object"));
    }

    // --- isBaseWrap ---

    @Test
    public void testIsBaseWrap_WithValidWrapperTypes() {
        assertTrue(ClassUtil.isBaseWrap("java.lang.Integer"));
        assertTrue(ClassUtil.isBaseWrap("java.lang.Double"));
        assertTrue(ClassUtil.isBaseWrap("java.lang.Float"));
        assertTrue(ClassUtil.isBaseWrap("java.lang.Long"));
        assertTrue(ClassUtil.isBaseWrap("java.lang.Short"));
        assertTrue(ClassUtil.isBaseWrap("java.lang.Byte"));
        assertTrue(ClassUtil.isBaseWrap("java.lang.Boolean"));
        assertTrue(ClassUtil.isBaseWrap("java.lang.Character"));
    }

    @Test
    public void testIsBaseWrap_WithInvalidTypes() {
        assertFalse(ClassUtil.isBaseWrap("int"));
        assertFalse(ClassUtil.isBaseWrap("String"));
        assertFalse(ClassUtil.isBaseWrap("java.lang.String"));
    }

    @Test
    public void testIsBaseWrap_WithClass() {
        assertTrue(ClassUtil.isBaseWrap(Integer.class));
        assertTrue(ClassUtil.isBaseWrap(Double.class));
        assertTrue(ClassUtil.isBaseWrap(Boolean.class));
        assertFalse(ClassUtil.isBaseWrap(String.class));
        assertFalse(ClassUtil.isBaseWrap(int.class));
    }

    // --- isBaseOrWrap ---

    @Test
    public void testIsBaseOrWrap_WithPrimitiveOrWrapperString() {
        assertTrue(ClassUtil.isBaseOrWrap("int"));
        assertTrue(ClassUtil.isBaseOrWrap("java.lang.Integer"));
        assertTrue(ClassUtil.isBaseOrWrap("double"));
        assertTrue(ClassUtil.isBaseOrWrap("java.lang.Double"));
    }

    @Test
    public void testIsBaseOrWrap_WithInvalidString() {
        assertFalse(ClassUtil.isBaseOrWrap("String"));
        assertFalse(ClassUtil.isBaseOrWrap("java.lang.String"));
    }

    @Test
    public void testIsBaseOrWrap_WithClass() {
        assertTrue(ClassUtil.isBaseOrWrap(int.class));
        assertTrue(ClassUtil.isBaseOrWrap(Integer.class));
        assertTrue(ClassUtil.isBaseOrWrap(double.class));
        assertFalse(ClassUtil.isBaseOrWrap(String.class));
    }

    @Test
    public void testIsBaseOrWrap_WithObject() {
        assertTrue(ClassUtil.isBaseOrWrap(Integer.valueOf(10)));
        assertFalse(ClassUtil.isBaseOrWrap("test"));
        assertFalse(ClassUtil.isBaseOrWrap((String) null));
    }

    // --- isBaseOrWrapOrString ---

    @Test
    public void testIsBaseOrWrapOrString() {
        assertTrue(ClassUtil.isBaseOrWrapOrString(String.class));
        assertTrue(ClassUtil.isBaseOrWrapOrString(int.class));
        assertTrue(ClassUtil.isBaseOrWrapOrString(Integer.class));
        assertFalse(ClassUtil.isBaseOrWrapOrString(Object.class));
    }

    // --- isSameClass ---

    @Test
    public void testIsSameClass_WithSameClasses() {
        assertTrue(ClassUtil.isSameClass(String.class, String.class));
        assertTrue(ClassUtil.isSameClass(Integer.class, Integer.class));
        assertTrue(ClassUtil.isSameClass(null, null));
    }

    @Test
    public void testIsSameClass_WithDifferentClasses() {
        assertFalse(ClassUtil.isSameClass(String.class, Integer.class));
        assertFalse(ClassUtil.isSameClass(String.class, null));
        assertFalse(ClassUtil.isSameClass(null, String.class));
    }

    // --- isSameNameClass ---

    @Test
    public void testIsSameNameClass_WithSameClasses() {
        assertTrue(ClassUtil.isSameNameClass(String.class, String.class));
        assertTrue(ClassUtil.isSameNameClass(Integer.class, Integer.class));
        assertTrue(ClassUtil.isSameNameClass(null, null));
    }

    @Test
    public void testIsSameNameClass_WithDifferentClasses() {
        assertFalse(ClassUtil.isSameNameClass(String.class, Integer.class));
        assertFalse(ClassUtil.isSameNameClass(String.class, null));
        assertFalse(ClassUtil.isSameNameClass(null, String.class));
    }

    // --- getShortClassName ---

    @Test
    public void testGetShortClassName_WithValidClassName() {
        assertEquals("c.z.u.c.l.ClassUtil", ClassUtil.getShortClassName("com.zifang.util.core.lang.ClassUtil"));
    }

    @Test
    public void testGetShortClassName_WithSimpleClassName() {
        assertEquals("String", ClassUtil.getShortClassName("String"));
    }

    @Test
    public void testGetShortClassName_WithNull() {
        assertNull(ClassUtil.getShortClassName(null));
    }

    // --- isOriginJdkType ---

    @Test
    public void testIsOriginJdkType() {
        assertTrue(ClassUtil.isOriginJdkType(String.class));
        assertTrue(ClassUtil.isOriginJdkType(Object.class));
        assertFalse(ClassUtil.isOriginJdkType(this.getClass()));
    }

    // --- isPrimitiveNumberType ---

    @Test
    public void testIsPrimitiveNumberType() {
        assertTrue(ClassUtil.isPrimitiveNumberType(int.class));
        assertTrue(ClassUtil.isPrimitiveNumberType(long.class));
        assertTrue(ClassUtil.isPrimitiveNumberType(short.class));
        assertTrue(ClassUtil.isPrimitiveNumberType(byte.class));
        assertFalse(ClassUtil.isPrimitiveNumberType(double.class));
        assertFalse(ClassUtil.isPrimitiveNumberType(float.class));
        assertFalse(ClassUtil.isPrimitiveNumberType(String.class));
    }

    // --- isPrimitiveFloatingPointNumberType ---

    @Test
    public void testIsPrimitiveFloatingPointNumberType() {
        assertTrue(ClassUtil.isPrimitiveFloatingPointNumberType(double.class));
        assertTrue(ClassUtil.isPrimitiveFloatingPointNumberType(float.class));
        assertFalse(ClassUtil.isPrimitiveFloatingPointNumberType(int.class));
        assertFalse(ClassUtil.isPrimitiveFloatingPointNumberType(long.class));
    }

    // --- toClass ---

    @Test
    public void testToClass_WithValidObjects() {
        Class<?>[] classes = ClassUtil.toClass("test", 123, 'a');
        assertEquals(3, classes.length);
        assertEquals(String.class, classes[0]);
        assertEquals(Integer.class, classes[1]);
        assertEquals(Character.class, classes[2]);
    }

    @Test
    public void testToClass_WithNullElements() {
        Class<?>[] classes = ClassUtil.toClass("test", null, 123);
        assertEquals(3, classes.length);
        assertEquals(String.class, classes[0]);
        assertNull(classes[1]);
        assertEquals(Integer.class, classes[2]);
    }

    @Test
    public void testToClass_WithNullArray() {
        assertNull(ClassUtil.toClass((Object[]) null));
    }

    @Test
    public void testToClass_WithEmptyArray() {
        Class<?>[] classes = ClassUtil.toClass();
        assertEquals(0, classes.length);
    }

    // --- argumentTypesToString ---

    @Test
    public void testArgumentTypesToString_WithMultipleTypes() {
        String result = ClassUtil.argumentTypesToString(new Class[]{String.class, int.class, Object.class});
        assertEquals("(java.lang.String, int, java.lang.Object)", result);
    }

    @Test
    public void testArgumentTypesToString_WithNullTypes() {
        String result = ClassUtil.argumentTypesToString(null);
        assertNotNull(result);
        assertTrue(result.contains("null") || result.equals("()"));
    }

    @Test
    public void testArgumentTypesToString_WithEmptyArray() {
        String result = ClassUtil.argumentTypesToString(new Class[]{});
        assertEquals("()", result);
    }
}
