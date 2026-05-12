package com.zifang.util.crawler.pipeline;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Context holder for crawler pipeline execution.
 * Contains request metadata, extracted data, errors, screenshots, and custom metadata.
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

    public PipelineContext() {
        this.headers = new HashMap<>();
        this.cookies = new HashMap<>();
        this.data = new HashMap<>();
        this.errors = new HashMap<>();
        this.screenshots = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    // Request metadata

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    // Extracted data

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void put(String key, Object value) {
        this.data.put(key, value);
    }

    // Aliases for backward compatibility
    public void putData(String key, Object value) {
        this.data.put(key, value);
    }

    public Object getData(String key) {
        return this.data.get(key);
    }

    public Object get(String key) {
        return this.data.get(key);
    }

    // Parameter access (alias for backward compatibility)
    public Object getParameter(String key) {
        return this.data.get(key);
    }

    public void putParameter(String key, Object value) {
        this.data.put(key, value);
    }

    // Raw content

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    // Errors

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String stage, String message) {
        this.errors.put(stage, message);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    // Screenshots

    public Map<String, File> getScreenshots() {
        return screenshots;
    }

    public void addScreenshot(String name, File screenshot) {
        this.screenshots.put(name, screenshot);
    }

    // Custom metadata

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(String key, Object value) {
        this.metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return this.metadata.get(key);
    }
}
