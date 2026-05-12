package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.browser.ChromeDriverHolder;
import com.zifang.util.crawler.cua.plan.RuleBasedPlanner;
import com.zifang.util.crawler.pipeline.PipelineContext;
import org.openqa.selenium.WebDriver;

/**
 * Main CUA (Computer Use Agent) entry point.
 * Executes natural language tasks using rule-based planning and browser automation.
 */
public class CuaAgent {

    private final OperationRegistry registry;
    private final RuleBasedPlanner planner;
    private final BrowserClient browserClient;

    public CuaAgent() {
        this.registry = new OperationRegistry();
        this.planner = new RuleBasedPlanner(registry);
        WebDriver driver = ChromeDriverHolder.getDriver();
        this.browserClient = new BrowserClient(driver);
    }

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

    public OperationRegistry getRegistry() {
        return registry;
    }

    public BrowserClient getBrowserClient() {
        return browserClient;
    }
}
