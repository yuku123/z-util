package com.zifang.util.crawler.parse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 Jsoup 进行 CSS 选择器提取的 HTML 解析器。
 * <p>
 * 提供静态方法解析 HTML 字符串，支持通过 CSS 选择器提取文本内容、
 * 属性值，以及返回 Jsoup Document 对象进行更复杂的操作。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * HtmlParser类。
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
    /**
     * parse方法。
     *      * @param html String类型参数
     * @param cssSelector String类型参数
     * @return static String类型返回值
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
    /**
     * parseAll方法。
     *      * @param html String类型参数
     * @param cssSelector String类型参数
     * @return static List<String>类型返回值
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
    /**
     * parseAttr方法。
     *      * @param html String类型参数
     * @param cssSelector String类型参数
     * @param attrName String类型参数
     * @return static String类型返回值
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
    /**
     * parse方法。
     *      * @param html String类型参数
     * @return static Document类型返回值
     */
    public static Document parse(String html) {
        return Jsoup.parse(html);
    }
}
