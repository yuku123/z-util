package com.zifang.util.crawler.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML parser using Jsoup for CSS selector extraction.
 */
public class HtmlParser {

    private HtmlParser() {
    }

    /**
     * Parse HTML and return text content of first element matching CSS selector.
     */
    public static String parse(String html, String cssSelector) {
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst(cssSelector);
        return element != null ? element.text() : null;
    }

    /**
     * Parse HTML and return text content of all elements matching CSS selector.
     */
    public static List<String> parseAll(String html, String cssSelector) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(cssSelector);
        List<String> results = new ArrayList<>();
        for (Element element : elements) {
            results.add(element.text());
        }
        return results;
    }

    /**
     * Parse HTML and return attribute value of first element matching CSS selector.
     */
    public static String parseAttr(String html, String cssSelector, String attrName) {
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst(cssSelector);
        return element != null ? element.attr(attrName) : null;
    }

    /**
     * Parse HTML and return Jsoup Document for advanced operations.
     */
    public static Document parse(String html) {
        return Jsoup.parse(html);
    }
}
