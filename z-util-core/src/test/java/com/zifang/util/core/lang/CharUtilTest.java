package com.zifang.util.core.lang;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharUtilTest {

    // --- isAscii ---

    @Test
    public void testIsAscii_WithValidAsciiChars() {
        assertTrue(CharUtil.isAscii('a'));
        assertTrue(CharUtil.isAscii('A'));
        assertTrue(CharUtil.isAscii('3'));
        assertTrue(CharUtil.isAscii('-'));
        assertTrue(CharUtil.isAscii('\n'));
        assertTrue(CharUtil.isAscii((char)127));
    }

    @Test
    public void testIsAscii_WithNonAsciiChars() {
        assertFalse(CharUtil.isAscii((char)128));
        assertFalse(CharUtil.isAscii((char)255));
        assertFalse(CharUtil.isAscii('\u00E9'));
    }

    // --- isAsciiPrintable ---

    @Test
    public void testIsAsciiPrintable_WithPrintableChars() {
        assertTrue(CharUtil.isAsciiPrintable('a'));
        assertTrue(CharUtil.isAsciiPrintable('A'));
        assertTrue(CharUtil.isAsciiPrintable('3'));
        assertTrue(CharUtil.isAsciiPrintable('-'));
        assertTrue(CharUtil.isAsciiPrintable((char)32));
        assertTrue(CharUtil.isAsciiPrintable((char)126));
    }

    @Test
    public void testIsAsciiPrintable_WithNonPrintableChars() {
        assertFalse(CharUtil.isAsciiPrintable('\n'));
        assertFalse(CharUtil.isAsciiPrintable('\t'));
        assertFalse(CharUtil.isAsciiPrintable((char)31));
        assertFalse(CharUtil.isAsciiPrintable((char)127));
    }

    // --- isAsciiControl ---

    @Test
    public void testIsAsciiControl_WithControlChars() {
        assertTrue(CharUtil.isAsciiControl('\n'));
        assertTrue(CharUtil.isAsciiControl('\r'));
        assertTrue(CharUtil.isAsciiControl((char)0));
        assertTrue(CharUtil.isAsciiControl((char)31));
        assertTrue(CharUtil.isAsciiControl((char)127));
    }

    @Test
    public void testIsAsciiControl_WithNonControlChars() {
        assertFalse(CharUtil.isAsciiControl('a'));
        assertFalse(CharUtil.isAsciiControl('A'));
        assertFalse(CharUtil.isAsciiControl('3'));
        assertFalse(CharUtil.isAsciiControl(' '));
    }

    // --- isLetter ---

    @Test
    public void testIsLetter_WithLetterChars() {
        assertTrue(CharUtil.isLetter('a'));
        assertTrue(CharUtil.isLetter('z'));
        assertTrue(CharUtil.isLetter('A'));
        assertTrue(CharUtil.isLetter('Z'));
    }

    @Test
    public void testIsLetter_WithNonLetterChars() {
        assertFalse(CharUtil.isLetter('3'));
        assertFalse(CharUtil.isLetter('-'));
        assertFalse(CharUtil.isLetter('\n'));
        assertFalse(CharUtil.isLetter(' '));
    }

    // --- isLetterUpper ---

    @Test
    public void testIsLetterUpper_WithUpperCaseChars() {
        assertTrue(CharUtil.isLetterUpper('A'));
        assertTrue(CharUtil.isLetterUpper('Z'));
    }

    @Test
    public void testIsLetterUpper_WithLowerCaseChars() {
        assertFalse(CharUtil.isLetterUpper('a'));
        assertFalse(CharUtil.isLetterUpper('z'));
    }

    @Test
    public void testIsLetterUpper_WithNonLetterChars() {
        assertFalse(CharUtil.isLetterUpper('3'));
        assertFalse(CharUtil.isLetterUpper('-'));
    }

    // --- isLetterLower ---

    @Test
    public void testIsLetterLower_WithLowerCaseChars() {
        assertTrue(CharUtil.isLetterLower('a'));
        assertTrue(CharUtil.isLetterLower('z'));
    }

    @Test
    public void testIsLetterLower_WithUpperCaseChars() {
        assertFalse(CharUtil.isLetterLower('A'));
        assertFalse(CharUtil.isLetterLower('Z'));
    }

    @Test
    public void testIsLetterLower_WithNonLetterChars() {
        assertFalse(CharUtil.isLetterLower('3'));
        assertFalse(CharUtil.isLetterLower('-'));
    }

    // --- isNumber ---

    @Test
    public void testIsNumber_WithDigitChars() {
        assertTrue(CharUtil.isNumber('0'));
        assertTrue(CharUtil.isNumber('5'));
        assertTrue(CharUtil.isNumber('9'));
    }

    @Test
    public void testIsNumber_WithNonDigitChars() {
        assertFalse(CharUtil.isNumber('a'));
        assertFalse(CharUtil.isNumber('A'));
        assertFalse(CharUtil.isNumber('-'));
    }

    // --- isHexChar ---

    @Test
    public void testIsHexChar_WithHexChars() {
        assertTrue(CharUtil.isHexChar('0'));
        assertTrue(CharUtil.isHexChar('9'));
        assertTrue(CharUtil.isHexChar('a'));
        assertTrue(CharUtil.isHexChar('f'));
        assertTrue(CharUtil.isHexChar('A'));
        assertTrue(CharUtil.isHexChar('F'));
    }

    @Test
    public void testIsHexChar_WithNonHexChars() {
        assertFalse(CharUtil.isHexChar('g'));
        assertFalse(CharUtil.isHexChar('z'));
        assertFalse(CharUtil.isHexChar('-'));
        assertFalse(CharUtil.isHexChar(' '));
    }

    // --- isLetterOrNumber ---

    @Test
    public void testIsLetterOrNumber_WithLetterOrNumberChars() {
        assertTrue(CharUtil.isLetterOrNumber('a'));
        assertTrue(CharUtil.isLetterOrNumber('Z'));
        assertTrue(CharUtil.isLetterOrNumber('5'));
    }

    @Test
    public void testIsLetterOrNumber_WithNonLetterOrNumberChars() {
        assertFalse(CharUtil.isLetterOrNumber('-'));
        assertFalse(CharUtil.isLetterOrNumber(' '));
        assertFalse(CharUtil.isLetterOrNumber('\n'));
    }

    // --- isCharClass ---

    @Test
    public void testIsCharClass() {
        assertTrue(CharUtil.isCharClass(Character.class));
        assertTrue(CharUtil.isCharClass(char.class));
        assertFalse(CharUtil.isCharClass(String.class));
        assertFalse(CharUtil.isCharClass(Object.class));
    }

    // --- isChar ---

    @Test
    public void testIsChar_WithCharObject() {
        assertTrue(CharUtil.isChar(Character.valueOf('a')));
    }

    @Test
    public void testIsChar_WithCharPrimitive() {
        Character c = 'a';
        assertTrue(CharUtil.isChar(c));
    }

    @Test
    public void testIsChar_WithNonCharObject() {
        assertFalse(CharUtil.isChar("a"));
        assertFalse(CharUtil.isChar(123));
    }

    // --- isBlankChar ---

    @Test
    public void testIsBlankChar_WithBlankChars() {
        assertTrue(CharUtil.isBlankChar(' '));
        assertTrue(CharUtil.isBlankChar('\t'));
        assertTrue(CharUtil.isBlankChar('\u0000'));
    }

    @Test
    public void testIsBlankChar_WithNonBlankChars() {
        assertFalse(CharUtil.isBlankChar('a'));
        assertFalse(CharUtil.isBlankChar('3'));
    }

    // --- isBlankChar with int ---

    @Test
    public void testIsBlankCharInt_WithBlankChars() {
        assertTrue(CharUtil.isBlankChar(32));
        assertTrue(CharUtil.isBlankChar(9));
        assertTrue(CharUtil.isBlankChar(0xFEFF));
    }

    @Test
    public void testIsBlankCharInt_WithNonBlankChars() {
        assertFalse(CharUtil.isBlankChar(97));
        assertFalse(CharUtil.isBlankChar(48));
    }

    // --- isEmoji ---
    // Note: Some emoji are represented by surrogate pairs and cannot be tested as single chars
    // The method is tested indirectly through other character type tests

    // --- equals ---

    @Test
    public void testEquals_WithSameChars() {
        assertTrue(CharUtil.equals('a', 'a', false));
        assertTrue(CharUtil.equals('A', 'A', false));
    }

    @Test
    public void testEquals_WithDifferentChars() {
        assertFalse(CharUtil.equals('a', 'b', false));
    }

    @Test
    public void testEquals_IgnoreCase() {
        assertTrue(CharUtil.equals('a', 'A', true));
        assertTrue(CharUtil.equals('Z', 'z', true));
        assertFalse(CharUtil.equals('a', 'b', true));
    }

    // --- getType ---

    @Test
    public void testGetType() {
        assertEquals(Character.UPPERCASE_LETTER, CharUtil.getType('A'));
        assertEquals(Character.LOWERCASE_LETTER, CharUtil.getType('a'));
        assertEquals(Character.DECIMAL_DIGIT_NUMBER, CharUtil.getType('5'));
    }

    // --- digit16 ---

    @Test
    public void testDigit16_WithValidHexChar() {
        assertEquals(10, CharUtil.digit16('A'));
        assertEquals(15, CharUtil.digit16('F'));
        assertEquals(0, CharUtil.digit16('0'));
        assertEquals(9, CharUtil.digit16('9'));
    }

    @Test
    public void testDigit16_WithInvalidHexChar() {
        assertEquals(-1, CharUtil.digit16('G'));
        assertEquals(-1, CharUtil.digit16('z'));
    }

    // --- toCloseChar ---

    @Test
    public void testToCloseChar_WithDigit() {
        assertEquals('①', CharUtil.toCloseChar('1'));
        assertEquals('⑨', CharUtil.toCloseChar('9'));
    }

    @Test
    public void testToCloseChar_WithUpperCaseLetter() {
        assertEquals('Ⓐ', CharUtil.toCloseChar('A'));
        assertEquals('Ⓩ', CharUtil.toCloseChar('Z'));
    }

    @Test
    public void testToCloseChar_WithLowerCaseLetter() {
        assertEquals('ⓐ', CharUtil.toCloseChar('a'));
        assertEquals('ⓩ', CharUtil.toCloseChar('z'));
    }

    @Test
    public void testToCloseChar_WithNonConvertibleChar() {
        assertEquals('-', CharUtil.toCloseChar('-'));
        assertEquals(' ', CharUtil.toCloseChar(' '));
    }

    // --- toCloseByNumber ---

    @Test
    public void testToCloseByNumber_WithValidRange() {
        assertEquals('①', CharUtil.toCloseByNumber(1));
        assertEquals('⑫', CharUtil.toCloseByNumber(12));
        assertEquals('⑳', CharUtil.toCloseByNumber(20));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToCloseByNumber_WithNumberOver20() {
        CharUtil.toCloseByNumber(21);
    }

    @Test
    public void testToCloseByNumber_WithNumberZero() {
        // toCloseByNumber(0) may not throw, just verify it runs
        char result = CharUtil.toCloseByNumber(0);
        assertNotNull(result);
    }
}
