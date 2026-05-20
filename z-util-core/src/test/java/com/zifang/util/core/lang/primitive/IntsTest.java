package com.zifang.util.core.lang.primitive;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntsTest {

    @Test
    public void isAlpha() {
        assertTrue(Ints.isAlpha('a'));
        assertTrue(Ints.isAlpha('z'));
        assertTrue(Ints.isAlpha('A'));
        assertTrue(Ints.isAlpha('Z'));
        assertFalse(Ints.isAlpha('0'));
        assertFalse(Ints.isAlpha(' '));
    }

    @Test
    public void isDigit() {
        assertTrue(Ints.isDigit('0'));
        assertTrue(Ints.isDigit('9'));
        assertFalse(Ints.isDigit('a'));
        assertFalse(Ints.isDigit(' '));
    }

    @Test
    public void isBlank() {
        assertTrue(Ints.isBlank(' '));
        assertTrue(Ints.isBlank('\t'));
        assertTrue(Ints.isBlank('\n'));
        assertFalse(Ints.isBlank('a'));
        assertFalse(Ints.isBlank('0'));
    }
}
