package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

/**
 * Step for inputting text into an element.
 * Task: "input", params: {css, text, clearFirst}
 */
public class InputStep implements Step {

    public static final String NAME = "input";
    private String css;
    private String text;
    private boolean clearFirst = true;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setParameter(String key, Object value) {
        if ("css".equalsIgnoreCase(key)) {
            this.css = String.valueOf(value);
        } else if ("text".equalsIgnoreCase(key) || "value".equalsIgnoreCase(key)) {
            this.text = String.valueOf(value);
        } else if ("clearFirst".equalsIgnoreCase(key)) {
            this.clearFirst = Boolean.parseBoolean(String.valueOf(value));
        }
    }

    @Override
    public StepResult execute(PipelineContext ctx) {
        String selector = css;
        String inputText = text;

        if (selector == null) {
            selector = (String) ctx.getParameter("css");
        }
        if (inputText == null) {
            inputText = (String) ctx.getParameter("text");
        }

        if (selector == null || selector.isEmpty()) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("No CSS selector provided for input")
                    .build();
        }

        if (inputText == null) {
            inputText = "";
        }

        try {
            BrowserClient client = getBrowserClient(ctx);

            if (clearFirst) {
                client.clear(selector);
            }
            client.input(selector, inputText);

            return StepResult.builder()
                    .stepName(NAME)
                    .success(true)
                    .output("Input text into: " + selector)
                    .build();
        } catch (Exception e) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("Input failed: " + e.getMessage())
                    .build();
        }
    }

    protected BrowserClient getBrowserClient(PipelineContext ctx) {
        return (BrowserClient) ctx.getParameter("browserClient");
    }
}
