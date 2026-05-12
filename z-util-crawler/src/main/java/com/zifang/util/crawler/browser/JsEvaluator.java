package com.zifang.util.crawler.browser;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Simple wrapper for WebDriver.executeScript()
 */
public class JsEvaluator {

    private final JavascriptExecutor executor;

    public JsEvaluator(WebDriver driver) {
        this.executor = (JavascriptExecutor) driver;
    }

    /**
     * Execute synchronous JavaScript
     */
    public Object evaluate(String script) {
        return executor.executeScript(script);
    }

    /**
     * Execute synchronous JavaScript with arguments
     */
    public Object evaluate(String script, Object... args) {
        return executor.executeScript(script, args);
    }

    /**
     * Execute asynchronous JavaScript
     */
    public Object evaluateAsync(String script) {
        return executor.executeAsyncScript(script);
    }

    /**
     * Execute asynchronous JavaScript with arguments
     */
    public Object evaluateAsync(String script, Object... args) {
        return executor.executeAsyncScript(script, args);
    }
}
