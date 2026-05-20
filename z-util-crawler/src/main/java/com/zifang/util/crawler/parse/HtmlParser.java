package com.zifang.util.crawler.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 Jsoup 进行 CSS 选择器提取的 HTML 解析器。
 */
public class HtmlParser {

    private HtmlParser() {
    }

    /**
     * 解析 HTML，返回匹配 CSS 选择器的第一个元素的文本内容。
     * @param html HTML 字符串
     * @param cssSelector CSS 选择器
     * @return 元素文本内容，未找到则返回 null
     */
    public static String parse(String html, String cssSelector) {
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst(cssSelector);
        return element != null ? element.text() : null;
    }

    /**
     * 解析 HTML，返回匹配 CSS 选择器的所有元素的文本内容。
     * @param html HTML 字符串
     * @param cssSelector CSS 选择器
     * @return 文本内容列表
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
     * 解析 HTML，返回匹配 CSS 选择器的第一个元素的指定属性值。
     * @param html HTML 字符串
     * @param cssSelector CSS 选择器
     * @param attrName 属性名
     * @return 属性值，未找到则返回 null
     */
    public static String parseAttr(String html, String cssSelector, String attrName) {
        Document doc = Jsoup.parse(html);
        Element element = doc.selectFirst(cssSelector);
        return element != null ? element.attr(attrName) : null;
    }

    /**
     * 解析 HTML 并返回 Jsoup Document 对象，用于高级操作。
     * @param html HTML 字符串
     * @return Jsoup Document 对象
     */
    public static Document parse(String html) {
        return Jsoup.parse(html);
    }
}
