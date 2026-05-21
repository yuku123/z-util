package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.browser.ChromeDriverHolder;
import com.zifang.util.crawler.cua.plan.RuleBasedPlanner;
import com.zifang.util.crawler.pipeline.PipelineContext;
import org.openqa.selenium.WebDriver;

/**
 * CUA（计算机使用代理）主入口类。
 * <p>
 * 使用基于规则的规划和浏览器自动化执行自然语言任务。
 * 整合操作注册表、规则规划器和浏览器客户端，
 * 提供面向自然语言描述的高级自动化能力。
 *
 * @author zifang
 * @version 1.0.0
 */
public class CuaAgent {

    private final OperationRegistry registry;
    private final RuleBasedPlanner planner;
    private final BrowserClient browserClient;

    /**
     * 使用默认配置的 ChromeDriver 构造 CuaAgent。
     */
    public CuaAgent() {
        this.registry = new OperationRegistry();
        this.planner = new RuleBasedPlanner(registry);
        WebDriver driver = ChromeDriverHolder.getDriver();
        this.browserClient = new BrowserClient(driver);
    }

    /**
     * 使用指定的 BrowserClient 构造 CuaAgent。
     * @param browserClient 浏览器客户端实例
     */
    public CuaAgent(BrowserClient browserClient) {
        this.registry = new OperationRegistry();
        this.planner = new RuleBasedPlanner(registry);
        this.browserClient = browserClient;
    }

    /**
     * Execute a task description against the given context.
     *
     * @param taskDescription natural language description of what to do
     * @param context         pipeline context with URL, cookies, existing data
     * @return execution result with steps taken and extracted data
     */
    public CuResult execute(String taskDescription, PipelineContext context) {
        // Inject browser client into context if not present
        if (context.getParameter("browserClient") == null) {
            context.putParameter("browserClient", browserClient);
        }

        // Use planner to determine step sequence
        return planner.plan(taskDescription, context);
    }

    /**
     * Execute a single step directly.
     */
    public StepResult executeStep(String stepName, PipelineContext context) {
        return planner.plan(stepName, context).getStepResults().stream()
                .findFirst()
                .orElse(StepResult.builder()
                        .stepName(stepName)
                        .success(false)
                        .errorMessage("Step not executed")
                        .build());
    }

    /**
     * 获取操作注册表。
     * @return 操作注册表实例
     */
    public OperationRegistry getRegistry() {
        return registry;
    }

    /**
     * 获取浏览器客户端。
     * @return 浏览器客户端实例
     */
    public BrowserClient getBrowserClient() {
        return browserClient;
    }
}
