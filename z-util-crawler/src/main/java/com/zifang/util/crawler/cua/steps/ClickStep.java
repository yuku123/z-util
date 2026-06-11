package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

/**
 * 点击步骤，用于点击页面元素。
 * <p>
 * 任务关键字：click，参数：{css, waitMs}
 * 支持点击后等待指定毫秒数，适用于点击后页面跳转场景。
 *
 * @author zifang
 * @version 1.0.0
 */
public class ClickStep implements Step {

    public static final String NAME = "click";
    private String css;
    private long waitMs = 0;

    @Override
    /**
     * getName方法。
     * @return String类型返回值
     */
    public String getName() {
        return NAME;
    }

    @Override
    /**
     * setParameter方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    public void setParameter(String key, Object value) {
        if ("css".equalsIgnoreCase(key)) {
            this.css = String.valueOf(value);
        } else if ("waitMs".equalsIgnoreCase(key) || "wait".equalsIgnoreCase(key)) {
            this.waitMs = Long.parseLong(String.valueOf(value));
        }
    }

    @Override
    /**
     * execute方法。
     *      * @param ctx PipelineContext类型参数
     * @return StepResult类型返回值
     */
    public StepResult execute(PipelineContext ctx) {
        String selector = css;
        if (selector == null) {
            selector = (String) ctx.getParameter("css");
        }

        if (selector == null || selector.isEmpty()) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("No CSS selector provided for click")
                    .build();
        }

        try {
            BrowserClient client = getBrowserClient(ctx);
            client.click(selector);

            if (waitMs > 0) {
                client.waitFor(waitMs);
            }

            return StepResult.builder()
                    .stepName(NAME)
                    .success(true)
                    .output("Clicked element: " + selector)
                    .build();
        } catch (Exception e) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("Click failed: " + e.getMessage())
                    .build();
        }
    }

    /**
     * getBrowserClient方法。
     *      * @param ctx PipelineContext类型参数
     * @return BrowserClient类型返回值
     */
    protected BrowserClient getBrowserClient(PipelineContext ctx) {
        return (BrowserClient) ctx.getParameter("browserClient");
    }
}
