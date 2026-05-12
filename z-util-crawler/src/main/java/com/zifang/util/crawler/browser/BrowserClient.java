package com.zifang.util.crawler.browser;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;
import java.time.Duration;
import java.util.Set;

/**
 * Browser automation client wrapping Selenium WebDriver.
 * Provides high-level browser operations for the CUA layer.
 */
public class BrowserClient {

    private final WebDriver driver;
    private WebDriverWait wait;

    public BrowserClient(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public BrowserClient(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Navigate to a URL.
     */
    public void get(String url) {
        driver.get(url);
    }

    /**
     * Click on an element identified by CSS selector.
     */
    public void click(String cssSelector) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(cssSelector)));
        element.click();
    }

    /**
     * Clear and type text into an element identified by CSS selector.
     */
    public void clear(String cssSelector) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        element.clear();
    }

    /**
     * Input text into an element identified by CSS selector.
     */
    public void input(String cssSelector, String text) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        element.sendKeys(text);
    }

    /**
     * Get text content of an element.
     */
    public String getText(String cssSelector) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        return element.getText();
    }

    /**
     * Get attribute value of an element.
     */
    public String getAttr(String cssSelector, String attribute) {
        WebElement element = driver.findElement(By.cssSelector(cssSelector));
        return element.getAttribute(attribute);
    }

    /**
     * Wait for a specific time in milliseconds.
     */
    public void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for an element to be present.
     */
    public boolean waitForElement(String cssSelector, int timeoutSec) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
            customWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Wait for an element to be visible.
     */
    public boolean waitForElementVisible(String cssSelector, int timeoutSec) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSec));
            customWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Take a screenshot and save to file.
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
     * Take a full page screenshot.
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
     * Switch to a window by title or handle.
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
     * Switch to a frame by name, id, or index.
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
     * Switch to default content.
     */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    /**
     * Get current page source.
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * Get current URL.
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page title.
     */
    public String getTitle() {
        return driver.getTitle();
    }

    /**
     * Execute JavaScript.
     */
    public Object executeScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Get the underlying WebDriver.
     */
    public WebDriver getDriver() {
        return driver;
    }
}
