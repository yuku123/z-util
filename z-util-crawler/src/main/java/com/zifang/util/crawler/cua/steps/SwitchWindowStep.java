package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

/**
 * 切换步骤，用于在窗口、frame 或标签页之间切换。
 * <p>
 * 任务关键字：switch，参数：{type: window|frame|tab, target}
 * 支持在浏览器多窗口、多标签页和 iframe 之间切换上下文。
 *
 * @author zifang
 * @version 1.0.0
 */
public class SwitchWindowStep implements Step {

    public static final String NAME = "switch";
    private String type = "window"; // window, frame, tab
    private String target;

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
        if ("type".equalsIgnoreCase(key)) {
            this.type = String.valueOf(value);
        } else if ("target".equalsIgnoreCase(key) || "value".equalsIgnoreCase(key)) {
            this.target = String.valueOf(value);
        }
    }

    @Override
    /**
     * execute方法。
     *      * @param ctx PipelineContext类型参数
     * @return StepResult类型返回值
     */
    public StepResult execute(PipelineContext ctx) {
        if (target == null) {
            target = (String) ctx.getParameter("target");
        }

        try {
            BrowserClient client = getBrowserClient(ctx);

            switch (type.toLowerCase()) {
                case "window":
                case "tab":
                    if (target == null || target.isEmpty()) {
                        return StepResult.builder()
                                .stepName(NAME)
                                .success(false)
                                .errorMessage("No target provided for window/tab switch")
                                .build();
                    }
                    client.switchToWindow(target);
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(true)
                            .output("Switched to window/tab: " + target)
                            .build();

                case "frame":
                    if (target == null || target.isEmpty()) {
                        client.switchToDefaultContent();
                        return StepResult.builder()
                                .stepName(NAME)
                                .success(true)
                                .output("Switched to default content")
                                .build();
                    }
                    client.switchToFrame(target);
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(true)
                            .output("Switched to frame: " + target)
                            .build();

                default:
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(false)
                            .errorMessage("Unknown switch type: " + type)
                            .build();
            }

        } catch (Exception e) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("Switch failed: " + e.getMessage())
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
