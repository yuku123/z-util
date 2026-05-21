package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

/**
 * 导航步骤，用于打开指定 URL。
 * <p>
 * 任务关键字：navigate，参数：{url}
 * 支持从参数或上下文获取目标URL，执行浏览器页面导航。
 *
 * @author zifang
 * @version 1.0.0
 */
public class NavigateStep implements Step {

    public static final String NAME = "navigate";
    private String url;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setParameter(String key, Object value) {
        if ("url".equalsIgnoreCase(key)) {
            this.url = String.valueOf(value);
        }
    }

    @Override
    public StepResult execute(PipelineContext ctx) {
        String targetUrl = url;
        if (targetUrl == null) {
            targetUrl = (String) ctx.getParameter("url");
        }
        if (targetUrl == null) {
            targetUrl = ctx.getUrl();
        }

        if (targetUrl == null || targetUrl.isEmpty()) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("No URL provided for navigation")
                    .build();
        }

        try {
            BrowserClient client = getBrowserClient(ctx);
            client.get(targetUrl);
            ctx.setUrl(targetUrl);
            return StepResult.builder()
                    .stepName(NAME)
                    .success(true)
                    .output("Navigated to: " + targetUrl)
                    .build();
        } catch (Exception e) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("Navigation failed: " + e.getMessage())
                    .build();
        }
    }

    protected BrowserClient getBrowserClient(PipelineContext ctx) {
        return (BrowserClient) ctx.getParameter("browserClient");
    }
}
