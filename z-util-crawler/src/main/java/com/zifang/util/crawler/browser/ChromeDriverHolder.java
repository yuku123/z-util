package com.zifang.util.crawler.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Singleton holder for ChromeDriver with lazy initialization and auto-download support.
 */
public class ChromeDriverHolder {

    private static WebDriver driver;

    private ChromeDriverHolder() {
    }

    /**
     * Get the singleton WebDriver instance.
     * Automatically downloads matching ChromeDriver if needed.
     */
    public static synchronized WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            driver = new ChromeDriver(options);
        }
        return driver;
    }

    /**
     * Close and quit the driver.
     */
    public static synchronized void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
