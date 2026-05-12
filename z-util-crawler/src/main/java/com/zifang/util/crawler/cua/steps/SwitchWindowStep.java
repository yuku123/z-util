package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

/**
 * Step for switching between windows, frames, or tabs.
 * Task: "switch", params: {type: window|frame|tab, target}
 */
public class SwitchWindowStep implements Step {

    public static final String NAME = "switch";
    private String type = "window"; // window, frame, tab
    private String target;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setParameter(String key, Object value) {
        if ("type".equalsIgnoreCase(key)) {
            this.type = String.valueOf(value);
        } else if ("target".equalsIgnoreCase(key) || "value".equalsIgnoreCase(key)) {
            this.target = String.valueOf(value);
        }
    }

    @Override
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

    protected BrowserClient getBrowserClient(PipelineContext ctx) {
        return (BrowserClient) ctx.getParameter("browserClient");
    }
}
