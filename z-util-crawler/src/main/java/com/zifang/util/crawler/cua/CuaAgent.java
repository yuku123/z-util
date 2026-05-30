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
/**
 * CuaAgent类。
 */
public class CuaAgent {

    private final OperationRegistry registry;
    private final RuleBasedPlanner planner;
    private final BrowserClient browserClient;

    /**
     * 使用默认配置的 ChromeDriver 构造 CuaAgent。
     */
    /**
     * CuaAgent方法。
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
    /**
     * CuaAgent方法。
     *      * @param browserClient BrowserClient类型参数
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
    /**
     * execute方法。
     *      * @param taskDescription String类型参数
     * @param context PipelineContext类型参数
     * @return CuResult类型返回值
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
    /**
     * executeStep方法。
     *      * @param stepName String类型参数
     * @param context PipelineContext类型参数
     * @return StepResult类型返回值
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
    /**
     * getRegistry方法。
     * @return OperationRegistry类型返回值
     */
    public OperationRegistry getRegistry() {
        return registry;
    }

    /**
     * 获取浏览器客户端。
     * @return 浏览器客户端实例
     */
    /**
     * getBrowserClient方法。
     * @return BrowserClient类型返回值
     */
    public BrowserClient getBrowserClient() {
        return browserClient;
    }
}
