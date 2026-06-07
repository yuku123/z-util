package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

import java.io.File;

/**
 * 截图步骤，用于截取页面快照。
 * <p>
 * 任务关键字：screenshot，参数：{path, fullPage}
 * 支持普通截图和整页截图，可指定保存路径。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * ScreenshotStep类。
 */
/**
 * ScreenshotStep类。
 */
public class ScreenshotStep implements Step {

    public static final String NAME = "screenshot";
    private String path;
    private boolean fullPage = false;

    @Override
    /**
     * getName方法。
     * @return String类型返回值
     */
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
    /**
     * setParameter方法。
     *      * @param key String类型参数
     * @param value Object类型参数
     */
    public void setParameter(String key, Object value) {
        if ("path".equalsIgnoreCase(key) || "file".equalsIgnoreCase(key) || "filename".equalsIgnoreCase(key)) {
            this.path = String.valueOf(value);
        } else if ("fullPage".equalsIgnoreCase(key) || "fullpage".equalsIgnoreCase(key)) {
            this.fullPage = Boolean.parseBoolean(String.valueOf(value));
        }
    }

    @Override
    /**
     * execute方法。
     *      * @param ctx PipelineContext类型参数
     * @return StepResult类型返回值
     */
    /**
     * execute方法。
     *      * @param ctx PipelineContext类型参数
     * @return StepResult类型返回值
     */
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

    /**
     * getBrowserClient方法。
     *      * @param ctx PipelineContext类型参数
     * @return BrowserClient类型返回值
     */
    /**
     * getBrowserClient方法。
     *      * @param ctx PipelineContext类型参数
     * @return BrowserClient类型返回值
     */
    protected BrowserClient getBrowserClient(PipelineContext ctx) {
        return (BrowserClient) ctx.getParameter("browserClient");
    }
}
