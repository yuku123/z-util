package com.zifang.util.crawler.cua.plan;

import com.zifang.util.crawler.cua.CuResult;
import com.zifang.util.crawler.cua.OperationRegistry;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.cua.steps.Step;
import com.zifang.util.crawler.pipeline.PipelineContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于规则的规划器，将自然语言任务描述映射为步骤序列。
 */
public class RuleBasedPlanner {

    private final OperationRegistry registry;
    private final List<Rule> rules;

    /**
     * 规则内部类，将正则表达式模式映射到步骤序列。
     */
    public static class Rule {
        private final Pattern pattern;
        private final List<String> stepSequence;

        /**
         * 构造规则。
         * @param regex 正则表达式模式（不区分大小写）
         * @param stepSequence 步骤关键字序列
         */
        public Rule(String regex, List<String> stepSequence) {
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.stepSequence = stepSequence;
        }

        /**
         * 判断任务描述是否匹配此规则。
         * @param taskDescription 任务描述
         * @return 是否匹配
         */
        public boolean matches(String taskDescription) {
            return pattern.matcher(taskDescription).find();
        }

        /**
         * 获取步骤序列。
         * @return 步骤关键字列表
         */
        public List<String> getStepSequence() {
            return stepSequence;
        }
    }

    public RuleBasedPlanner(OperationRegistry registry) {
        this.registry = registry;
        this.rules = new ArrayList<>();
        registerPredefinedRules();
    }

    /**
     * 添加自定义规则。
     * @param regex 正则表达式模式
     * @param stepSequence 步骤序列
     */
    public void addRule(String regex, List<String> stepSequence) {
        rules.add(new Rule(regex, stepSequence));
    }

    /**
     * Plan the execution by matching the task description against rules.
     * Returns a CuResult with the planned step sequence or an error if no rule matches.
     */
    public CuResult plan(String taskDescription, PipelineContext context) {
        for (Rule rule : rules) {
            if (rule.matches(taskDescription)) {
                return executePlan(rule.getStepSequence(), context);
            }
        }

        // No rule matched - return error result
        return CuResult.builder()
                .success(false)
                .errorMessage("No matching rule found for task: " + taskDescription)
                .context(context)
                .build();
    }

    /**
     * Execute a planned step sequence.
     */
    private CuResult executePlan(List<String> stepSequence, PipelineContext context) {
        List<StepResult> stepResults = new ArrayList<>();
        boolean allSuccess = true;

        for (String stepKeyword : stepSequence) {
            Step step = registry.resolve(stepKeyword);
            if (step == null) {
                stepResults.add(StepResult.builder()
                        .stepName(stepKeyword)
                        .success(false)
                        .errorMessage("Unknown step: " + stepKeyword)
                        .build());
                allSuccess = false;
                continue;
            }

            long start = System.currentTimeMillis();
            StepResult result = step.execute(context);
            result.setDurationMs(System.currentTimeMillis() - start);
            stepResults.add(result);

            if (!result.isSuccess()) {
                allSuccess = false;
            }
        }

        return CuResult.builder()
                .success(allSuccess)
                .stepResults(stepResults)
                .context(context)
                .build();
    }

    /**
     * Register predefined rules for common task patterns.
     */
    private void registerPredefinedRules() {
        addRule("click.*button", Arrays.asList("navigate_if_needed", "wait_element", "click", "screenshot_on_error"));
        addRule("fill.*form", Arrays.asList("navigate_if_needed", "wait_element", "clear", "input", "screenshot_on_error"));
        addRule("extract.*data", Arrays.asList("wait_element", "extract"));
        addRule("open.*url", Arrays.asList("navigate"));
        addRule("go.*to", Arrays.asList("navigate"));
        addRule("type.*into", Arrays.asList("input"));
        addRule("get.*text", Arrays.asList("extract"));
        addRule("save.*screenshot", Arrays.asList("screenshot"));
        addRule("take.*screenshot", Arrays.asList("screenshot"));
        addRule("switch.*window", Arrays.asList("switch"));
        addRule("switch.*tab", Arrays.asList("switch"));
        addRule("switch.*frame", Arrays.asList("switch"));
    }
}
