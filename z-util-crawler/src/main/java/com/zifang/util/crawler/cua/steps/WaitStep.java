package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

/**
 * 等待步骤，用于在执行过程中等待。
 * <p>
 * 任务关键字：wait，参数：{type: time|element|script, target, timeoutSec}
 * 支持固定时间等待、元素出现等待、元素可见等待和脚本执行后等待。
 *
 * @author zifang
 * @version 1.0.0
 */
public class WaitStep implements Step {

    public static final String NAME = "wait";
    private String type = "time"; // time, element, elementVisible, script
    private String target;
    private int timeoutSec = 10;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setParameter(String key, Object value) {
        if ("type".equalsIgnoreCase(key)) {
            this.type = String.valueOf(value);
        } else if ("target".equalsIgnoreCase(key) || "selector".equalsIgnoreCase(key)) {
            this.target = String.valueOf(value);
        } else if ("timeoutSec".equalsIgnoreCase(key) || "timeout".equalsIgnoreCase(key)) {
            this.timeoutSec = Integer.parseInt(String.valueOf(value));
        } else if ("ms".equalsIgnoreCase(key) || "milliseconds".equalsIgnoreCase(key)) {
            this.timeoutSec = (int) (Long.parseLong(String.valueOf(value)) / 1000);
        }
    }

    @Override
    public StepResult execute(PipelineContext ctx) {
        try {
            BrowserClient client = getBrowserClient(ctx);

            switch (type.toLowerCase()) {
                case "time":
                    long ms = timeoutSec * 1000L;
                    client.waitFor(ms);
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(true)
                            .output("Waited for " + ms + "ms")
                            .build();

                case "element":
                    boolean found = client.waitForElement(target, timeoutSec);
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(found)
                            .output(found ? "Element found: " + target : "Element not found: " + target)
                            .errorMessage(found ? null : "Element not found within timeout")
                            .build();

                case "elementvisible":
                case "visible":
                    boolean visible = client.waitForElementVisible(target, timeoutSec);
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(visible)
                            .output(visible ? "Element visible: " + target : "Element not visible: " + target)
                            .errorMessage(visible ? null : "Element not visible within timeout")
                            .build();

                case "script":
                    Object result = client.executeScript(target);
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(true)
                            .output(result)
                            .build();

                default:
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(false)
                            .errorMessage("Unknown wait type: " + type)
                            .build();
            }
        } catch (Exception e) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("Wait failed: " + e.getMessage())
                    .build();
        }
    }

    protected BrowserClient getBrowserClient(PipelineContext ctx) {
        return (BrowserClient) ctx.getParameter("browserClient");
    }
}
