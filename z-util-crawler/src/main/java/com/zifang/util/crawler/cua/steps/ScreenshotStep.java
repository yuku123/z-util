package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

import java.io.File;

/**
 * Step for taking screenshots.
 * Task: "screenshot", params: {path, fullPage}
 */
public class ScreenshotStep implements Step {

    public static final String NAME = "screenshot";
    private String path;
    private boolean fullPage = false;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setParameter(String key, Object value) {
        if ("path".equalsIgnoreCase(key) || "file".equalsIgnoreCase(key) || "filename".equalsIgnoreCase(key)) {
            this.path = String.valueOf(value);
        } else if ("fullPage".equalsIgnoreCase(key) || "fullpage".equalsIgnoreCase(key)) {
            this.fullPage = Boolean.parseBoolean(String.valueOf(value));
        }
    }

    @Override
    public StepResult execute(PipelineContext ctx) {
        String screenshotPath = path;
        if (screenshotPath == null) {
            screenshotPath = (String) ctx.getParameter("path");
        }

        if (screenshotPath == null || screenshotPath.isEmpty()) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("No path provided for screenshot")
                    .build();
        }

        try {
            BrowserClient client = getBrowserClient(ctx);
            File screenshot;

            if (fullPage) {
                screenshot = client.screenshotFullPage(screenshotPath);
            } else {
                screenshot = client.screenshot(screenshotPath);
            }

            return StepResult.builder()
                    .stepName(NAME)
                    .success(true)
                    .output(screenshot.getAbsolutePath())
                    .build();

        } catch (Exception e) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("Screenshot failed: " + e.getMessage())
                    .build();
        }
    }

    protected BrowserClient getBrowserClient(PipelineContext ctx) {
        return (BrowserClient) ctx.getParameter("browserClient");
    }
}
