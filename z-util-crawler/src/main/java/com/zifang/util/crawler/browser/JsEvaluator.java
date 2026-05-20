package com.zifang.util.crawler.browser;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * WebDriver.executeScript() 的简单封装。
 */
public class JsEvaluator {

    private final JavascriptExecutor executor;

    /**
     * 构造 JsEvaluator。
     * @param driver WebDriver 实例
     */
    public JsEvaluator(WebDriver driver) {
        this.executor = (JavascriptExecutor) driver;
    }

    /**
     * 执行同步 JavaScript。
     * @param script JavaScript 脚本
     * @return 脚本执行结果
     */
    public Object evaluate(String script) {
        return executor.executeScript(script);
    }

    /**
     * 执行同步 JavaScript（带参数）。
     * @param script JavaScript 脚本
     * @param args 脚本参数
     * @return 脚本执行结果
     */
    public Object evaluate(String script, Object... args) {
        return executor.executeScript(script, args);
    }

    /**
     * 执行异步 JavaScript。
     * @param script JavaScript 脚本
     * @return 脚本执行结果
     */
    public Object evaluateAsync(String script) {
        return executor.executeAsyncScript(script);
    }

    /**
     * 执行异步 JavaScript（带参数）。
     * @param script JavaScript 脚本
     * @param args 脚本参数
     * @return 脚本执行结果
     */
    public Object evaluateAsync(String script, Object... args) {
        return executor.executeAsyncScript(script, args);
    }
}
