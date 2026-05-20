package com.zifang.util.crawler.parse;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HtmlParserTest {

    private static final String TEST_HTML = "<html><head><title>Test Page</title></head><body>"
            + "<h1 class='title'>Hello World</h1>"
            + "<div class='content'>"
            + "<a href='https://example.com/link1'>Link 1</a>"
            + "<a href='https://example.com/link2'>Link 2</a>"
            + "<p class='desc'>First paragraph</p>"
            + "<p class='desc'>Second paragraph</p>"
            + "</div>"
            + "<img src='https://example.com/image.png' alt='Test Image'/>"
            + "</body></html>";

    // --- parse(String, String) ---

    @Test
    public void testParse_WithValidSelector_ReturnsText() {
        String result = HtmlParser.parse(TEST_HTML, "h1.title");
        assertEquals("Hello World", result);
    }

    @Test
    public void testParse_WithNonExistentSelector_ReturnsNull() {
        String result = HtmlParser.parse(TEST_HTML, ".nonexistent");
        assertNull(result);
    }

    @Test
    public void testParse_WithTagName_ReturnsText() {
        String result = HtmlParser.parse(TEST_HTML, "p.desc");
        assertEquals("First paragraph", result);
    }

    @Test(expected = NullPointerException.class)
    public void testParse_WithNullHtml_ThrowsNullPointerException() {
        HtmlParser.parse(null, "h1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithNullSelector_ThrowsIllegalArgumentException() {
        HtmlParser.parse(TEST_HTML, null);
    }

    @Test
    public void testParse_WithEmptyHtml_ReturnsNull() {
        String result = HtmlParser.parse("", "h1");
        assertNull(result);
    }

    // --- parseAll(String, String) ---

    @Test
    public void testParseAll_WithValidSelector_ReturnsAllMatches() {
        List<String> results = HtmlParser.parseAll(TEST_HTML, "a");
        assertEquals(2, results.size());
        assertEquals("Link 1", results.get(0));
        assertEquals("Link 2", results.get(1));
    }

    @Test
    public void testParseAll_WithNonExistentSelector_ReturnsEmptyList() {
        List<String> results = HtmlParser.parseAll(TEST_HTML, ".nonexistent");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testParseAll_WithMultipleMatches_ReturnsAllTexts() {
        List<String> results = HtmlParser.parseAll(TEST_HTML, "p.desc");
        assertEquals(2, results.size());
        assertTrue(results.contains("First paragraph"));
        assertTrue(results.contains("Second paragraph"));
    }

    @Test(expected = NullPointerException.class)
    public void testParseAll_WithNullHtml_ThrowsNullPointerException() {
        HtmlParser.parseAll(null, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAll_WithNullSelector_ThrowsIllegalArgumentException() {
        HtmlParser.parseAll(TEST_HTML, null);
    }

    // --- parseAttr(String, String, String) ---

    @Test
    public void testParseAttr_WithValidSelectorAndAttr_ReturnsAttrValue() {
        String result = HtmlParser.parseAttr(TEST_HTML, "a", "href");
        assertEquals("https://example.com/link1", result);
    }

    @Test
    public void testParseAttr_WithImgTag_ReturnsSrc() {
        String result = HtmlParser.parseAttr(TEST_HTML, "img", "src");
        assertEquals("https://example.com/image.png", result);
    }

    @Test
    public void testParseAttr_WithImgTag_ReturnsAlt() {
        String result = HtmlParser.parseAttr(TEST_HTML, "img", "alt");
        assertEquals("Test Image", result);
    }

    @Test
    public void testParseAttr_WithNonExistentSelector_ReturnsNull() {
        String result = HtmlParser.parseAttr(TEST_HTML, ".nonexistent", "href");
        assertNull(result);
    }

    @Test
    public void testParseAttr_WithNonExistentAttr_ReturnsEmptyString() {
        String result = HtmlParser.parseAttr(TEST_HTML, "a", "data-id");
        assertEquals("", result);
    }

    @Test(expected = NullPointerException.class)
    public void testParseAttr_WithNullHtml_ThrowsNullPointerException() {
        HtmlParser.parseAttr(null, "a", "href");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAttr_WithNullSelector_ThrowsIllegalArgumentException() {
        HtmlParser.parseAttr(TEST_HTML, null, "href");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseAttr_WithNullAttrName_ThrowsIllegalArgumentException() {
        HtmlParser.parseAttr(TEST_HTML, "a", null);
    }

    // --- parse(String) ---

    @Test
    public void testParseHtml_ReturnsDocument() {
        Document doc = HtmlParser.parse(TEST_HTML);
        assertNotNull(doc);
        assertEquals("Test Page", doc.title());
    }

    @Test(expected = NullPointerException.class)
    public void testParseHtml_WithNull_ThrowsNullPointerException() {
        HtmlParser.parse(null);
    }

    @Test
    public void testParseHtml_ReturnsBodyText() {
        Document doc = HtmlParser.parse(TEST_HTML);
        assertNotNull(doc.body());
        assertTrue(doc.body().text().contains("Hello World"));
    }
}
