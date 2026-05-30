package com.zifang.util.crawler.cua.steps;

import com.zifang.util.crawler.browser.BrowserClient;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.pipeline.PipelineContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据提取步骤，用于从页面中提取数据。
 * <p>
 * 任务关键字：extract，参数：{css, attr, multiple, asJson}
 * 支持通过 CSS 选择器提取文本或属性值，可单选或多选。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * ExtractStep类。
 */
public class ExtractStep implements Step {

    public static final String NAME = "extract";
    private String css;
    private String attr;
    private boolean multiple = false;
    private boolean asJson = false;
    private String resultKey;

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
        if ("css".equalsIgnoreCase(key) || "selector".equalsIgnoreCase(key)) {
            this.css = String.valueOf(value);
        } else if ("attr".equalsIgnoreCase(key) || "attribute".equalsIgnoreCase(key)) {
            this.attr = String.valueOf(value);
        } else if ("multiple".equalsIgnoreCase(key)) {
            this.multiple = Boolean.parseBoolean(String.valueOf(value));
        } else if ("asJson".equalsIgnoreCase(key) || "json".equalsIgnoreCase(key)) {
            this.asJson = Boolean.parseBoolean(String.valueOf(value));
        } else if ("key".equalsIgnoreCase(key) || "resultKey".equalsIgnoreCase(key) || "as".equalsIgnoreCase(key)) {
            this.resultKey = String.valueOf(value);
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
                    .errorMessage("No CSS selector provided for extraction")
                    .build();
        }

        try {
            BrowserClient client = getBrowserClient(ctx);
            String pageSource = client.getPageSource();

            if (pageSource == null || pageSource.isEmpty()) {
                return StepResult.builder()
                        .stepName(NAME)
                        .success(false)
                        .errorMessage("Empty page source")
                        .build();
            }

            Document doc = Jsoup.parse(pageSource);
            Object extracted;

            if (multiple) {
                Elements elements = doc.select(selector);
                List<String> results = new ArrayList<>();
                for (Element el : elements) {
                    results.add(extractFromElement(el));
                }
                extracted = results;
            } else {
                Element element = doc.selectFirst(selector);
                if (element == null) {
                    return StepResult.builder()
                            .stepName(NAME)
                            .success(false)
                            .errorMessage("Element not found: " + selector)
                            .build();
                }
                extracted = extractFromElement(element);
            }

            // Store in context if result key provided
            if (resultKey != null && !resultKey.isEmpty()) {
                ctx.putData(resultKey, extracted);
            } else {
                ctx.putData("extracted", extracted);
            }

            return StepResult.builder()
                    .stepName(NAME)
                    .success(true)
                    .output(extracted)
                    .build();

        } catch (Exception e) {
            return StepResult.builder()
                    .stepName(NAME)
                    .success(false)
                    .errorMessage("Extraction failed: " + e.getMessage())
                    .build();
        }
    }

    private String extractFromElement(Element element) {
        if (attr != null && !attr.isEmpty()) {
            return element.attr(attr);
        }
        return element.text();
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
