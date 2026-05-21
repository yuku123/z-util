package com.zifang.util.crawler.browser;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.util.Set;

/**
 * 基于 Selenium WebDriver 的浏览器自动化客户端，为 CUA 层提供高级浏览器操作。
 * <p>
 * 该类封装了常用的 Selenium WebDriver 操作，包括页面导航、元素交互、
 * 等待条件、窗口切换、截图等功能，为上层 CUA 提供简洁的API。
 *
 * @author zifang
 * @version 1.0.0
 */
public class BrowserClient {

    private final WebDriver driver;
    private WebDriverWait wait;

    /**
     * 构造浏览器客户端，使用默认超时时间（10秒）。
     * @param driver WebDriver 实例
     */
    public BrowserClient(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    /**
     * 构造浏览器客户端，使用指定超时时间。
     * @param driver WebDriver 实例
     * @param timeoutSeconds 超时时间（秒）
     */
    public BrowserClient(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeoutSeconds);
    }

    /**
     * 导航到指定 URL。
     * @param url 目标 URL
     */
    public void get(String url) {
        driver.get(url);
    }

    /**
     * 点击由 CSS 选择器指定的元素。
     * @param cssSelector CSS 选择器
     */
    public void click(String cssSelector) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(cssSelector)));
        element.click();
    }

    /**
     * 清空由 CSS 选择器指定的输入框内容。
     * @param cssSelector CSS 选择器
     */
    public void clear(String cssSelector) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        element.clear();
    }

    /**
     * 向由 CSS 选择器指定的元素输入文本。
     * @param cssSelector CSS 选择器
     * @param text 输入文本
     */
    public void input(String cssSelector, String text) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        element.sendKeys(text);
    }

    /**
     * 获取元素的文本内容。
     * @param cssSelector CSS 选择器
     * @return 元素文本内容
     */
    public String getText(String cssSelector) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        return element.getText();
    }

    /**
     * 获取元素的属性值。
     * @param cssSelector CSS 选择器
     * @param attribute 属性名
     * @return 属性值
     */
    public String getAttr(String cssSelector, String attribute) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        return element.getAttribute(attribute);
    }

    /**
     * 等待指定时间。
     * @param milliseconds 等待时间（毫秒）
     */
    public void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 等待元素出现在 DOM 中。
     * @param cssSelector CSS 选择器
     * @param timeoutSec 超时时间（秒）
     * @return 是否在超时前找到元素
     */
    public boolean waitForElement(String cssSelector, int timeoutSec) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, (long) timeoutSec);
            customWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * 等待元素可见。
     * @param cssSelector CSS 选择器
     * @param timeoutSec 超时时间（秒）
     * @return 是否在超时前元素可见
     */
    public boolean waitForElementVisible(String cssSelector, int timeoutSec) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, (long) timeoutSec);
            customWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * 截取当前窗口截图并保存到文件。
     * @param path 保存路径，为 null 时只返回截图文件
     * @return 截图文件
     */
    public File screenshot(String path) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        if (path != null) {
            File dest = new File(path);
            screenshot.renameTo(dest);
        }
        return screenshot;
    }

    /**
     * 截取整页截图并保存到文件。
     * @param path 保存路径，为 null 时只返回截图文件
     * @return 截图文件
     */
    public File screenshotFullPage(String path) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        if (path != null) {
            File dest = new File(path);
            screenshot.renameTo(dest);
        }
        return screenshot;
    }

    /**
     * 切换到指定窗口，可以通过标题、句柄或索引匹配。
     * @param target 目标窗口标题、句柄或索引
     */
    public void switchToWindow(String target) {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(target) || handle.equals(target)) {
                return;
            }
        }
    }

    /**
     * 切换到指定 frame，可以通过名称、ID 或索引匹配。
     * @param frameIdentifier frame 名称、ID 或索引
     */
    public void switchToFrame(String frameIdentifier) {
        try {
            int index = Integer.parseInt(frameIdentifier);
            driver.switchTo().frame(index);
        } catch (NumberFormatException e) {
            driver.switchTo().frame(frameIdentifier);
        }
    }

    /**
     * 切换到默认内容（最外层 frame）。
     */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    /**
     * 获取当前页面源代码。
     * @return 页面源代码
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * 获取当前 URL。
     * @return 当前 URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * 获取页面标题。
     * @return 页面标题
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * 执行 JavaScript 脚本。
     * @param script JavaScript 脚本
     * @param args 脚本参数
     * @return 脚本执行结果
     */
    public Object executeScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * 获取底层 WebDriver 实例。
     * @return WebDriver 实例
     */
    public WebDriver getDriver() {
        return driver;
    }
}
