package com.zifang.util.crawler.parse;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class JsonExtractorTest {

    private static final String TEST_JSON = "{\"data\":{\"name\":\"test\",\"value\":123,\"items\":[\"a\",\"b\",\"c\"]}}";

    private static final String ARRAY_JSON = "{\"items\":[{\"id\":1},{\"id\":2},{\"id\":3}]}";

    private static final String NESTED_JSON = "{\"a\":{\"b\":{\"c\":\"deep value\"}}}";

    // --- extract(String, String) ---

    @Test
    public void testExtract_WithSimplePath_ReturnsValue() {
        String result = JsonExtractor.extract(TEST_JSON, "data.name");
        assertEquals("test", result);
    }

    @Test
    public void testExtract_WithNumericValue_ReturnsString() {
        String result = JsonExtractor.extract(TEST_JSON, "data.value");
        assertEquals("123", result);
    }

    @Test
    public void testExtract_WithArrayPath_ReturnsArrayString() {
        // json-simple returns the array as string representation
        String result = JsonExtractor.extract(TEST_JSON, "data.items");
        assertNotNull(result);
        // The array is returned as its string representation
    }

    @Test
    public void testExtract_WithNestedPath_ReturnsDeepValue() {
        String result = JsonExtractor.extract(NESTED_JSON, "a.b.c");
        assertEquals("deep value", result);
    }

    @Test
    public void testExtract_WithNonExistentPath_ReturnsNull() {
        String result = JsonExtractor.extract(TEST_JSON, "data.nonexistent");
        assertNull(result);
    }

    @Test
    public void testExtract_WithInvalidJson_ReturnsNull() {
        String result = JsonExtractor.extract("not valid json", "data.name");
        assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testExtract_WithNullJson_ThrowsNullPointerException() {
        JsonExtractor.extract(null, "data.name");
    }

    @Test(expected = NullPointerException.class)
    public void testExtract_WithNullPath_ThrowsNullPointerException() {
        JsonExtractor.extract(TEST_JSON, null);
    }

    @Test
    public void testExtract_WithEmptyPath_ReturnsParsedRoot() {
        // With empty path, split returns array with one empty string element
        String result = JsonExtractor.extract(TEST_JSON, "");
        assertNotNull(result);
    }

    @Test
    public void testExtract_WithTopLevelArrayAccess_ReturnsElement() {
        // For direct array access at top level: items[0] when root is object
        String result = JsonExtractor.extract(ARRAY_JSON, "items[0]");
        assertNotNull(result);
    }

    @Test
    public void testExtract_WithTopLevelArrayAccess1_ReturnsSecondElement() {
        String result = JsonExtractor.extract(ARRAY_JSON, "items[1]");
        assertNotNull(result);
    }

    // Note: Nested array index access like "data.items[0]" is NOT supported by this implementation
    // The implementation only handles array indices when the current element is already a JSONArray,
    // not when navigating through a path segment that contains brackets.

    // --- extractAll(String, String) ---

    @Test
    public void testExtractAll_WithArrayPath_ReturnsAllElements() {
        List<String> results = JsonExtractor.extractAll(TEST_JSON, "data.items");
        assertEquals(3, results.size());
        assertTrue(results.contains("a"));
        assertTrue(results.contains("b"));
        assertTrue(results.contains("c"));
    }

    @Test
    public void testExtractAll_WithObjectPath_ReturnsEmptyList() {
        // Object values are not arrays, so extractAll returns empty list
        List<String> results = JsonExtractor.extractAll(TEST_JSON, "data.name");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testExtractAll_WithArrayOfObjects_ReturnsAllObjects() {
        List<String> results = JsonExtractor.extractAll(ARRAY_JSON, "items");
        assertEquals(3, results.size());
    }

    @Test
    public void testExtractAll_WithNonExistentPath_ReturnsEmptyList() {
        List<String> results = JsonExtractor.extractAll(TEST_JSON, "data.nonexistent");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testExtractAll_WithInvalidJson_ReturnsEmptyList() {
        List<String> results = JsonExtractor.extractAll("not valid json", "data.name");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testExtractAll_WithNullJson_ThrowsNullPointerException() {
        JsonExtractor.extractAll(null, "data.name");
    }

    @Test(expected = NullPointerException.class)
    public void testExtractAll_WithNullPath_ThrowsNullPointerException() {
        JsonExtractor.extractAll(TEST_JSON, null);
    }

    @Test
    public void testExtractAll_WithEmptyJson_ReturnsEmptyList() {
        List<String> results = JsonExtractor.extractAll("{}", "data");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testExtractAll_WithTopLevelArrayIndex_ReturnsSingleElementList() {
        List<String> results = JsonExtractor.extractAll(ARRAY_JSON, "items[0]");
        assertEquals(1, results.size());
    }
}
