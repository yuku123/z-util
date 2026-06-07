package com.zifang.util.crawler.browser;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * WebDriver.executeScript() 的简单封装。
 * <p>
 * 提供同步和异步 JavaScript 执行功能，
 * 简化在 Selenium 测试中对 JavaScript 的调用。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * JsEvaluator类。
 */
/**
 * JsEvaluator类。
 */
public class JsEvaluator {

    private final JavascriptExecutor executor;

    /**
     * 构造 JsEvaluator。
     * @param driver WebDriver 实例
     */
    /**
     * JsEvaluator方法。
     *      * @param driver WebDriver类型参数
     */
    /**
     * JsEvaluator方法。
     *      * @param driver WebDriver类型参数
     */
    public JsEvaluator(WebDriver driver) {
        this.executor = (JavascriptExecutor) driver;
    }

    /**
     * 执行同步 JavaScript。
     * @param script JavaScript 脚本
     * @return 脚本执行结果
     */
    /**
     * evaluate方法。
     *      * @param script String类型参数
     * @return Object类型返回值
     */
    /**
     * evaluate方法。
     *      * @param script String类型参数
     * @return Object类型返回值
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
    /**
     * evaluate方法。
     *      * @param script String类型参数
     * @param args Object...类型参数
     * @return Object类型返回值
     */
    /**
     * evaluate方法。
     *      * @param script String类型参数
     * @param args Object...类型参数
     * @return Object类型返回值
     */
    public Object evaluate(String script, Object... args) {
        return executor.executeScript(script, args);
    }

    /**
     * 执行异步 JavaScript。
     * @param script JavaScript 脚本
     * @return 脚本执行结果
     */
    /**
     * evaluateAsync方法。
     *      * @param script String类型参数
     * @return Object类型返回值
     */
    /**
     * evaluateAsync方法。
     *      * @param script String类型参数
     * @return Object类型返回值
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
    /**
     * evaluateAsync方法。
     *      * @param script String类型参数
     * @param args Object...类型参数
     * @return Object类型返回值
     */
    /**
     * evaluateAsync方法。
     *      * @param script String类型参数
     * @param args Object...类型参数
     * @return Object类型返回值
     */
    public Object evaluateAsync(String script, Object... args) {
        return executor.executeAsyncScript(script, args);
    }
}
