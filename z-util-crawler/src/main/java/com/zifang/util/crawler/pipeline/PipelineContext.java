package com.zifang.util.crawler.pipeline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫管道执行上下文持有类，包含请求元数据、提取数据、错误信息、截图和自定义元数据。
 * <p>
 * 在管道执行过程中在各个处理器之间传递，
 * 累积 HTTP 响应、解析结果、提取数据、错误信息和截图等。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * PipelineContext类。
 */
public class PipelineContext {

    private String url;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private Map<String, Object> data;
    private String html;
    private String json;
    private Map<String, String> errors;
    private Map<String, File> screenshots;
    private Map<String, Object> metadata;

    /**
     * 构造 PipelineContext。
     */
    /**
     * PipelineContext方法。
     */
    public PipelineContext() {
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.data = new HashMap<>();
        this.errors = new HashMap<>();
        this.screenshots = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    // 请求元数据

    /**
     * 获取 URL。
     * @return 请求 URL
     */
    /**
     * getUrl方法。
     * @return String类型返回值
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置 URL。
     * @param url 请求 URL
     */
    /**
     * setUrl方法。
     *      * @param url String类型参数
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取请求头。
     * @return 请求头映射
     */
    /**
     * getHeaders方法。
     * @return Map<String, String>类型返回值
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 设置请求头。
     * @param headers 请求头映射
     */
    /**
     * setHeaders方法。
     *      * @param headers MapString,类型参数
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * 获取 Cookie。
     * @return Cookie 映射
     */
    /**
     * getCookies方法。
     * @return Map<String, String>类型返回值
     */
    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * 设置 Cookie。
     * @param cookies Cookie 映射
     */
    /**
     * setCookies方法。
     *      * @param cookies MapString,类型参数
     */
    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    // 提取的数据

    /**
     * 获取数据映射。
     * @return 数据映射
     */
    /**
     * getData方法。
     * @return Map<String, Object>类型返回值
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * 设置数据映射。
     * @param data 数据映射
     */
    /**
     * setData方法。
     *      * @param data MapString,类型参数
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * 存入数据。
     * @param key 键
     * @param value 值
     */
    /**
     * put方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    public void put(String key, Object value) {
        this.data.put(key, value);
    }

    /**
     * 存入数据（向后兼容别名）。
     * @param key 键
     * @param value 值
     */
    /**
     * putData方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    public void putData(String key, Object value) {
        this.data.put(key, value);
    }

    /**
     * 获取数据。
     * @param key 键
     * @return 值
     */
    /**
     * getData方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
    public Object getData(String key) {
        return this.data.get(key);
    }

    /**
     * 获取数据（向后兼容别名）。
     * @param key 键
     * @return 值
     */
    /**
     * get方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
    public Object get(String key) {
        return this.data.get(key);
    }

    // 参数访问（向后兼容别名）
    /**
     * 获取参数。
     * @param key 键
     * @return 值
     */
    /**
     * getParameter方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
    public Object getParameter(String key) {
        return this.data.get(key);
    }

    /**
     * 存入参数。
     * @param key 键
     * @param value 值
     */
    /**
     * putParameter方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    public void putParameter(String key, Object value) {
        this.data.put(key, value);
    }

    // 原始内容

    /**
     * 获取 HTML 内容。
     * @return HTML 字符串
     */
    /**
     * getHtml方法。
     * @return String类型返回值
     */
    public String getHtml() {
        return html;
    }

    /**
     * 设置 HTML 内容。
     * @param html HTML 字符串
     */
    /**
     * setHtml方法。
     *      * @param html String类型参数
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * 获取 JSON 内容。
     * @return JSON 字符串
     */
    /**
     * getJson方法。
     * @return String类型返回值
     */
    public String getJson() {
        return json;
    }

    /**
     * 设置 JSON 内容。
     * @param json JSON 字符串
     */
    /**
     * setJson方法。
     *      * @param json String类型参数
     */
    public void setJson(String json) {
        this.json = json;
    }

    // 错误信息

    /**
     * 获取错误映射。
     * @return 错误映射
     */
    /**
     * getErrors方法。
     * @return Map<String, String>类型返回值
     */
    public Map<String, String> getErrors() {
        return errors;
    }

    /**
     * 添加错误信息。
     * @param stage 阶段名称
     * @param message 错误信息
     */
    /**
     * addError方法。
     *      * @param stage String类型参数
     * @param message String类型参数
     */
    public void addError(String stage, String message) {
        this.errors.put(stage, message);
    }

    /**
     * 判断是否存在错误。
     * @return 是否存在错误
     */
    /**
     * hasErrors方法。
     * @return boolean类型返回值
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    // 截图

    /**
     * 获取截图映射。
     * @return 截图映射
     */
    /**
     * getScreenshots方法。
     * @return Map<String, File>类型返回值
     */
    public Map<String, File> getScreenshots() {
        return screenshots;
    }

    /**
     * 添加截图。
     * @param name 截图名称
     * @param screenshot 截图文件
     */
    /**
     * addScreenshot方法。
     *      * @param name String类型参数
     * @param screenshot File类型参数
     */
    public void addScreenshot(String name, File screenshot) {
        this.screenshots.put(name, screenshot);
    }

    // 自定义元数据

    /**
     * 获取元数据映射。
     * @return 元数据映射
     */
    /**
     * getMetadata方法。
     * @return Map<String, Object>类型返回值
     */
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * 设置元数据。
     * @param key 键
     * @param value 值
     */
    /**
     * setMetadata方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    public void setMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }

    /**
     * 获取元数据。
     * @param key 键
     * @return 值
     */
    /**
     * getMetadata方法。
     *      * @param key String类型参数
     * @return Object类型返回值
     */
    public Object getMetadata(String key) {
        return this.metadata.get(key);
    }
}
