package com.zifang.util.crawler.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * ChromeDriver 单例持有类，支持懒加载和自动下载。
 * <p>
 * 使用 WebDriverManager 自动管理 ChromeDriver 版本，
 * 确保与本地 Chrome 浏览器版本匹配。支持 headless 模式运行。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * ChromeDriverHolder类。
 */
public class ChromeDriverHolder {

    private static WebDriver driver;

    private ChromeDriverHolder() {
    }

    /**
     * Get the singleton WebDriver instance.
     * Automatically downloads matching ChromeDriver if needed.
     */
    /**
     * getDriver方法。
     * @return static synchronized WebDriver类型返回值
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
    /**
     * quit方法。
     * @return static synchronized void类型返回值
     */
    public static synchronized void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
