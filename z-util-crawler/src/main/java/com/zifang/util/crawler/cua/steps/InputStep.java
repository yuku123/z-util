package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;

/**
 * 输入步骤，用于向输入框填写文本。
 * <p>
 * 任务关键字：input，参数：{css, text, clearFirst}
 * 支持先清空再输入，可控制是否清空输入框内容。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * InputStep类。
 */
/**
 * InputStep类。
 */
public class InputStep implements Step {

    public static final String NAME = "input";
    private String css;
    private String text;
    private boolean clearFirst = true;

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
        if ("css".equalsIgnoreCase(key)) {
            this.css = String.valueOf(value);
        } else if ("text".equalsIgnoreCase(key) || "value".equalsIgnoreCase(key)) {
            this.text = String.valueOf(value);
        } else if ("clearFirst".equalsIgnoreCase(key)) {
            this.clearFirst = Boolean.parseBoolean(String.valueOf(value));
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
