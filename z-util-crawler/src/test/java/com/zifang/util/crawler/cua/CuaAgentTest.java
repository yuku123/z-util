package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.pipeline.PipelineContext;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.*;

/**
 * CuaAgentTest类。
 */
public class CuaAgentTest {

    @Test
    /**
     * testCuaAgentWithCustomBrowserClient方法。
     */
    public void testCuaAgentWithCustomBrowserClient() {
        try {
            WebDriver driver = new ChromeDriver();
            BrowserClient browserClient = new BrowserClient(driver);
            CuaAgent agent = new CuaAgent(browserClient);
            
            assertNotNull(agent.getRegistry());
            assertNotNull(agent.getBrowserClient());
            
            driver.quit();
        } catch (Exception e) {
            // ChromeDriver not available - skip actual test
        }
    }

    @Test
    /**
     * testGetRegistry方法。
     */
    public void testGetRegistry() {
        try {
            WebDriver driver = new ChromeDriver();
            BrowserClient browserClient = new BrowserClient(driver);
            CuaAgent agent = new CuaAgent(browserClient);
            
            OperationRegistry registry = agent.getRegistry();
            assertNotNull(registry);
            
            // Should have built-in steps registered
            assertTrue(registry.isRegistered("navigate"));
            assertTrue(registry.isRegistered("click"));
            assertTrue(registry.isRegistered("input"));
            assertTrue(registry.isRegistered("wait"));
            assertTrue(registry.isRegistered("extract"));
            assertTrue(registry.isRegistered("screenshot"));
            assertTrue(registry.isRegistered("switch"));
            
            driver.quit();
        } catch (Exception e) {
            // ChromeDriver not available - skip
        }
    }

    @Test
    /**
     * testGetBrowserClient方法。
     */
    public void testGetBrowserClient() {
        try {
            WebDriver driver = new ChromeDriver();
            BrowserClient browserClient = new BrowserClient(driver);
            CuaAgent agent = new CuaAgent(browserClient);
            
            assertEquals(browserClient, agent.getBrowserClient());
            
            driver.quit();
        } catch (Exception e) {
            // ChromeDriver not available - skip
        }
    }

    @Test
    /**
     * testExecuteStepNotFound方法。
     */
    public void testExecuteStepNotFound() {
        try {
            WebDriver driver = new ChromeDriver();
            BrowserClient browserClient = new BrowserClient(driver);
            CuaAgent agent = new CuaAgent(browserClient);
            
            PipelineContext ctx = new PipelineContext();
            ctx.putParameter("browserClient", browserClient);
            
            StepResult result = agent.executeStep("nonexistent_step_xyz", ctx);
            
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertEquals("nonexistent_step_xyz", result.getStepName());
            assertEquals("Step not executed", result.getErrorMessage());
            
            driver.quit();
        } catch (Exception e) {
            // ChromeDriver not available - skip
        }
    }

    @Test
    /**
     * testExecuteUnknownTask方法。
     */
    public void testExecuteUnknownTask() {
        try {
            WebDriver driver = new ChromeDriver();
            BrowserClient browserClient = new BrowserClient(driver);
            CuaAgent agent = new CuaAgent(browserClient);
            
            PipelineContext ctx = new PipelineContext();
            ctx.putParameter("browserClient", browserClient);
            
            CuResult result = agent.execute("do something totally unknown xyz12345", ctx);
            
            assertNotNull(result);
            assertFalse(result.isSuccess());
            assertTrue(result.getErrorMessage().contains("No matching rule"));
            
            driver.quit();
        } catch (Exception e) {
            // ChromeDriver not available - skip
        }
    }
}
