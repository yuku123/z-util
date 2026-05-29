package com.zifang.util.office.word;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tuple键值对泛型类的单元测试
 */
public class TupleTest {

    @Test
    public void testConstructorAndGetters() {
        Tuple<String, Integer> tuple = new Tuple<>("key1", 100);
        
        assertEquals("key1", tuple.getKey());
        assertEquals(Integer.valueOf(100), tuple.getValue());
    }

    @Test
    public void testSetters() {
        Tuple<String, String> tuple = new Tuple<>(null, null);
        
        tuple.setKey("testKey");
        tuple.setValue("testValue");
        
        assertEquals("testKey", tuple.getKey());
        assertEquals("testValue", tuple.getValue());
    }

    @Test
    public void testDifferentTypes() {
        Tuple<Integer, Boolean> tuple = new Tuple<>(42, true);
        
        assertEquals(Integer.valueOf(42), tuple.getKey());
        assertEquals(Boolean.TRUE, tuple.getValue());
    }
}