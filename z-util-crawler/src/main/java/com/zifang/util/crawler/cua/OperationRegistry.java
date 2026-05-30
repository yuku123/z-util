package com.zifang.util.crawler.cua;

import com.zifang.util.crawler.cua.steps.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作注册表，负责将任务关键字映射到 Step 实现类。
 * <p>
 * 维护步骤关键字到具体 Step 实现类的映射关系，
 * 支持内置步骤自动注册和用户自定义步骤扩展。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * OperationRegistry类。
 */
public class OperationRegistry {

    private final Map<String, Class<? extends Step>> stepMap;

    /**
     * 构造操作注册表。
     */
    /**
     * OperationRegistry方法。
     */
    public OperationRegistry() {
        this.stepMap = new HashMap<>();
        registerBuiltInSteps();
    }

    /**
     * 注册任务关键字对应的步骤类。
     * @param taskKeyword 任务关键字（如 "navigate", "click" 等）
     * @param stepClass 步骤实现类
     */
    /**
     * register方法。
     *      * @param taskKeyword String类型参数
     * @param stepClass Class?类型参数
     */
    public void register(String taskKeyword, Class<? extends Step> stepClass) {
        stepMap.put(taskKeyword.toLowerCase(), stepClass);
    }

    /**
     * 解析任务关键字对应的步骤实例。
     * @param taskKeyword 任务关键字
     * @return 步骤实例，如果未注册则返回 null
     * @throws RuntimeException 如果步骤类无法实例化
     */
    /**
     * resolve方法。
     *      * @param taskKeyword String类型参数
     * @return Step类型返回值
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
     * 检查任务关键字是否已注册。
     * @param taskKeyword 任务关键字
     * @return 是否已注册
     */
    /**
     * isRegistered方法。
     *      * @param taskKeyword String类型参数
     * @return boolean类型返回值
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
