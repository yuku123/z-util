package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.cua.steps.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for mapping task keywords to Step implementations.
 */
public class OperationRegistry {

    private final Map<String, Class<? extends Step>> stepMap;

    public OperationRegistry() {
        this.stepMap = new HashMap<>();
        registerBuiltInSteps();
    }

    /**
     * Register a step class for a task keyword.
     */
    public void register(String taskKeyword, Class<? extends Step> stepClass) {
        stepMap.put(taskKeyword.toLowerCase(), stepClass);
    }

    /**
     * Resolve a step class for the given task keyword.
     */
    public Step resolve(String taskKeyword) {
        Class<? extends Step> stepClass = stepMap.get(taskKeyword.toLowerCase());
        if (stepClass == null) {
            return null;
        }
        try {
            return stepClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate step: " + stepClass.getName(), e);
        }
    }

    /**
     * Check if a task keyword is registered.
     */
    public boolean isRegistered(String taskKeyword) {
        return stepMap.containsKey(taskKeyword.toLowerCase());
    }

    /**
     * Register all built-in steps.
     */
    private void registerBuiltInSteps() {
        register("navigate", NavigateStep.class);
        register("click", ClickStep.class);
        register("input", InputStep.class);
        register("wait", WaitStep.class);
        register("extract", ExtractStep.class);
        register("screenshot", ScreenshotStep.class);
        register("switch", SwitchWindowStep.class);
    }
}
