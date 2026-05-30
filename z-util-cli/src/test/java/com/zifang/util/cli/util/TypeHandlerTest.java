package com.zifang.util.cli.util;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit tests for TypeHandler.
 * Tests type conversion for numbers, dates, URI, URL, File, Class, and cast methods.
 */
public class TypeHandlerTest {

    // ==================== toNumber tests ====================

    @Test
    public void testToNumberInteger() throws ParseException {
        Number result = TypeHandler.toNumber("123", Integer.class);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testToNumberIntegerNegative() throws ParseException {
        Number result = TypeHandler.toNumber("-456", Integer.class);
        assertEquals(-456, result.intValue());
    }

    @Test
    public void testToNumberIntegerHex() throws ParseException {
        Number result = TypeHandler.toNumber("0xFF", Integer.class);
        assertEquals(255, result.intValue());
    }

    @Test
    public void testToNumberIntPrimitive() throws ParseException {
        Number result = TypeHandler.toNumber("42", Integer.TYPE);
        assertEquals(42, result.intValue());
    }

    @Test
    public void testToNumberLong() throws ParseException {
        Number result = TypeHandler.toNumber("9223372036854775807", Long.class);
        assertEquals(9223372036854775807L, result.longValue());
    }

    @Test
    public void testToNumberLongNegative() throws ParseException {
        Number result = TypeHandler.toNumber("-999999999999", Long.class);
        assertEquals(-999999999999L, result.longValue());
    }

    @Test
    public void testToNumberLongPrimitive() throws ParseException {
        Number result = TypeHandler.toNumber("100", Long.TYPE);
        assertEquals(100L, result.longValue());
    }

    @Test
    public void testToNumberDouble() throws ParseException {
        Number result = TypeHandler.toNumber("3.14159", Double.class);
        assertEquals(3.14159, result.doubleValue(), 0.00001);
    }

    @Test
    public void testToNumberDoubleNegative() throws ParseException {
        Number result = TypeHandler.toNumber("-2.71828", Double.class);
        assertEquals(-2.71828, result.doubleValue(), 0.00001);
    }

    @Test
    public void testToNumberDoublePrimitive() throws ParseException {
        Number result = TypeHandler.toNumber("1.5", Double.TYPE);
        assertEquals(1.5, result.doubleValue(), 0.00001);
    }

    @Test
    public void testToNumberFloat() throws ParseException {
        Number result = TypeHandler.toNumber("2.5", Float.class);
        assertEquals(2.5f, result.floatValue(), 0.0001f);
    }

    @Test
    public void testToNumberFloatNegative() throws ParseException {
        Number result = TypeHandler.toNumber("-1.0", Float.class);
        assertEquals(-1.0f, result.floatValue(), 0.0001f);
    }

    @Test
    public void testToNumberFloatPrimitive() throws ParseException {
        Number result = TypeHandler.toNumber("0.5", Float.TYPE);
        assertEquals(0.5f, result.floatValue(), 0.0001f);
    }

    @Test
    public void testToNumberShort() throws ParseException {
        Number result = TypeHandler.toNumber("100", Short.class);
        assertEquals((short) 100, result.shortValue());
    }

    @Test
    public void testToNumberShortNegative() throws ParseException {
        Number result = TypeHandler.toNumber("-50", Short.class);
        assertEquals((short) -50, result.shortValue());
    }

    @Test
    public void testToNumberShortPrimitive() throws ParseException {
        Number result = TypeHandler.toNumber("32", Short.TYPE);
        assertEquals((short) 32, result.shortValue());
    }

    @Test
    public void testToNumberByte() throws ParseException {
        Number result = TypeHandler.toNumber("127", Byte.class);
        assertEquals((byte) 127, result.byteValue());
    }

    @Test
    public void testToNumberByteNegative() throws ParseException {
        Number result = TypeHandler.toNumber("-128", Byte.class);
        assertEquals((byte) -128, result.byteValue());
    }

    @Test
    public void testToNumberBytePrimitive() throws ParseException {
        Number result = TypeHandler.toNumber("64", Byte.TYPE);
        assertEquals((byte) 64, result.byteValue());
    }

    @Test(expected = ParseException.class)
    public void testToNumberUnsupportedType() throws ParseException {
        TypeHandler.toNumber("123", String.class);
    }

    @Test
    public void testToNumberNull() throws ParseException {
        Number result = TypeHandler.toNumber(null, Integer.class);
        assertNull(result);
    }

    // ==================== toDate tests ====================

    @Test
    public void testToDateWithPattern() throws ParseException {
        Date result = TypeHandler.toDate("2024-01-15", "yyyy-MM-dd");
        assertNotNull(result);
        assertEquals(2024 - 1900, result.getYear());
        assertEquals(0, result.getMonth());
        assertEquals(15, result.getDate());
    }

    @Test
    public void testToDateWithPatternComplex() throws ParseException {
        Date result = TypeHandler.toDate("15/01/2024 13:45:30", "dd/MM/yyyy HH:mm:ss");
        assertNotNull(result);
        assertEquals(2024 - 1900, result.getYear());
        assertEquals(0, result.getMonth());
        assertEquals(15, result.getDate());
    }

    @Test
    public void testToDateNull() throws ParseException {
        Date result = TypeHandler.toDate(null, "yyyy-MM-dd");
        assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testToDateInvalidFormat() throws ParseException {
        TypeHandler.toDate("not-a-date", "yyyy-MM-dd");
    }

    @Test
    public void testToDateDefault() throws ParseException {
        // Default format uses DateFormat.getDateInstance() which typically produces something like "Jan 15, 2024"
        // Using a more standard format for testing
        Date result = TypeHandler.toDate("Jan 15, 2024");
        assertNotNull(result);
    }

    @Test
    public void testToDateDefaultNull() throws ParseException {
        Date result = TypeHandler.toDate(null);
        assertNull(result);
    }

    // ==================== toURI tests ====================

    @Test
    public void testToURI() {
        URI result = TypeHandler.toURI("http://example.com/path?query=1");
        assertNotNull(result);
        assertEquals("http", result.getScheme());
        assertEquals("example.com", result.getHost());
        assertEquals("/path", result.getPath());
        assertEquals("query=1", result.getQuery());
    }

    @Test
    public void testToURIWithFragment() {
        URI result = TypeHandler.toURI("https://example.com/page#section");
        assertNotNull(result);
        assertEquals("section", result.getFragment());
    }

    @Test
    public void testToURINull() {
        URI result = TypeHandler.toURI(null);
        assertNull(result);
    }

    // ==================== toURL tests ====================

    @Test
    public void testToURL() throws MalformedURLException {
        URL result = TypeHandler.toURL("http://example.com:8080/path");
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
        assertEquals("example.com", result.getHost());
        assertEquals(8080, result.getPort());
        assertEquals("/path", result.getPath());
    }

    @Test
    public void testToURLHttps() throws MalformedURLException {
        URL result = TypeHandler.toURL("https://secure.example.com/api");
        assertNotNull(result);
        assertEquals("https", result.getProtocol());
    }

    @Test
    public void testToURLNull() throws MalformedURLException {
        URL result = TypeHandler.toURL(null);
        assertNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testToURLInvalid() throws MalformedURLException {
        TypeHandler.toURL("not-a-valid-url");
    }

    // ==================== toFile tests ====================

    @Test
    public void testToFile() {
        File result = TypeHandler.toFile("/tmp/test.txt");
        assertNotNull(result);
        assertEquals("test.txt", result.getName());
        assertEquals("/tmp", result.getParent());
    }

    @Test
    public void testToFileRelative() {
        File result = TypeHandler.toFile("relative/path/file.txt");
        assertNotNull(result);
        assertEquals("file.txt", result.getName());
    }

    @Test
    public void testToFileNull() {
        File result = TypeHandler.toFile(null);
        assertNull(result);
    }

    // ==================== toClass tests ====================

    @Test
    public void testToClassString() throws ClassNotFoundException {
        Class<?> result = TypeHandler.toClass("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test
    public void testToClassInteger() throws ClassNotFoundException {
        Class<?> result = TypeHandler.toClass("java.lang.Integer");
        assertEquals(Integer.class, result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testToClassNotFound() throws ClassNotFoundException {
        TypeHandler.toClass("com.nonexistent.ClassName");
    }

    @Test
    public void testToClassNull() throws ClassNotFoundException {
        Class<?> result = TypeHandler.toClass(null);
        assertNull(result);
    }

    // ==================== isNumber tests ====================

    @Test
    public void testIsNumberPositive() {
        assertTrue(TypeHandler.isNumber("123"));
    }

    @Test
    public void testIsNumberNegative() {
        assertTrue(TypeHandler.isNumber("-456"));
    }

    @Test
    public void testIsNumberDecimal() {
        assertTrue(TypeHandler.isNumber("3.14159"));
    }

    @Test
    public void testIsNumberNegativeDecimal() {
        assertTrue(TypeHandler.isNumber("-2.718"));
    }

    @Test
    public void testIsNumberFalse() {
        assertFalse(TypeHandler.isNumber("abc"));
    }

    @Test
    public void testIsNumberFalseMixed() {
        assertFalse(TypeHandler.isNumber("12abc"));
    }

    @Test
    public void testIsNumberEmpty() {
        assertFalse(TypeHandler.isNumber(""));
    }

    @Test
    public void testIsNumberNull() {
        assertFalse(TypeHandler.isNumber(null));
    }

    // ==================== cast tests ====================

    @Test
    public void testCastString() throws ParseException, MalformedURLException, ClassNotFoundException {
        String result = TypeHandler.cast("hello", String.class);
        assertEquals("hello", result);
    }

    @Test
    public void testCastBooleanTrue() throws ParseException, MalformedURLException, ClassNotFoundException {
        Boolean result = TypeHandler.cast("true", Boolean.class);
        assertTrue(result);
    }

    @Test
    public void testCastBooleanFalse() throws ParseException, MalformedURLException, ClassNotFoundException {
        Boolean result = TypeHandler.cast("false", Boolean.class);
        assertFalse(result);
    }

    @Test
    public void testCastBooleanUpperCase() throws ParseException, MalformedURLException, ClassNotFoundException {
        Boolean result = TypeHandler.cast("TRUE", Boolean.class);
        assertTrue(result);
    }

    @Test
    public void testCastInteger() throws ParseException, MalformedURLException, ClassNotFoundException {
        Integer result = TypeHandler.cast("42", Integer.class);
        assertEquals(42, result.intValue());
    }

    @Test
    public void testCastLong() throws ParseException, MalformedURLException, ClassNotFoundException {
        Long result = TypeHandler.cast("9223372036854775807", Long.class);
        assertEquals(9223372036854775807L, result.longValue());
    }

    @Test
    public void testCastDouble() throws ParseException, MalformedURLException, ClassNotFoundException {
        Double result = TypeHandler.cast("3.14", Double.class);
        assertEquals(3.14, result, 0.01);
    }

    @Test
    public void testCastFloat() throws ParseException, MalformedURLException, ClassNotFoundException {
        Float result = TypeHandler.cast("2.5", Float.class);
        assertEquals(2.5f, result, 0.01f);
    }

    @Test
    public void testCastShort() throws ParseException, MalformedURLException, ClassNotFoundException {
        Short result = TypeHandler.cast("100", Short.class);
        assertEquals((short) 100, result.shortValue());
    }

    @Test
    public void testCastByte() throws ParseException, MalformedURLException, ClassNotFoundException {
        Byte result = TypeHandler.cast("127", Byte.class);
        assertEquals((byte) 127, result.byteValue());
    }

    @Test
    public void testCastDate() throws ParseException, MalformedURLException, ClassNotFoundException {
        Date result = TypeHandler.cast("Jan 15, 2024", Date.class);
        assertNotNull(result);
    }

    @Test
    public void testCastURI() throws ParseException, MalformedURLException, ClassNotFoundException {
        URI result = TypeHandler.cast("http://example.com", URI.class);
        assertNotNull(result);
        assertEquals("http", result.getScheme());
    }

    @Test
    public void testCastURL() throws ParseException, MalformedURLException, ClassNotFoundException {
        URL result = TypeHandler.cast("http://example.com", URL.class);
        assertNotNull(result);
        assertEquals("http", result.getProtocol());
    }

    @Test
    public void testCastFile() throws ParseException, MalformedURLException, ClassNotFoundException {
        File result = TypeHandler.cast("/tmp/test.txt", File.class);
        assertNotNull(result);
        assertEquals("test.txt", result.getName());
    }

    @Test
    public void testCastClass() throws ParseException, MalformedURLException, ClassNotFoundException {
        Class<?> result = TypeHandler.cast("java.lang.String", Class.class);
        assertEquals(String.class, result);
    }

    @Test
    public void testCastNull() throws ParseException, MalformedURLException, ClassNotFoundException {
        String result = TypeHandler.cast(null, String.class);
        assertNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCastUnsupportedType() throws ParseException, MalformedURLException, ClassNotFoundException {
        TypeHandler.cast("value", Object.class);
    }
}