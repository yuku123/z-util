package com.zifang.util.crawler.cua.plan;

import com.zifang.util.crawler.cua.CuResult;
import com.zifang.util.crawler.cua.OperationRegistry;
import com.zifang.util.crawler.cua.StepResult;
import com.zifang.util.crawler.cua.steps.Step;
import com.zifang.util.crawler.pipeline.PipelineContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule-based planner that maps natural language task descriptions to step sequences.
 */
public class RuleBasedPlanner {

    private final OperationRegistry registry;
    private final List<Rule> rules;

    /**
     * A rule mapping a pattern to a sequence of step keywords.
     */
    public static class Rule {
        private final Pattern pattern;
        private final List<String> stepSequence;

        public Rule(String regex, List<String> stepSequence) {
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.stepSequence = stepSequence;
        }

        public boolean matches(String taskDescription) {
            return pattern.matcher(taskDescription).find();
        }

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
     * Register a custom rule.
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
        addRule("click.*button", List.of("navigate_if_needed", "wait_element", "click", "screenshot_on_error"));
        addRule("fill.*form", List.of("navigate_if_needed", "wait_element", "clear", "input", "screenshot_on_error"));
        addRule("extract.*data", List.of("wait_element", "extract"));
        addRule("open.*url", List.of("navigate"));
        addRule("go.*to", List.of("navigate"));
        addRule("type.*into", List.of("input"));
        addRule("get.*text", List.of("extract"));
        addRule("save.*screenshot", List.of("screenshot"));
        addRule("take.*screenshot", List.of("screenshot"));
        addRule("switch.*window", List.of("switch"));
        addRule("switch.*tab", List.of("switch"));
        addRule("switch.*frame", List.of("switch"));
    }
}
