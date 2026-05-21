package com.zifang.util.core.schedule.listener;

import org.quartz.impl.matchers.GroupMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 监听器匹配规则工具类。
 * <p>
 * 用于控制 JobListener 和 TriggerListener 作用于哪些任务/触发器。
 * <p>
 * 示例：
 * <pre>
 * // 只监听 group1 和 group2 中的任务
 * matcher = Matcher.jobGroupEqualsTo("group1", "group2");
 *
 * // 监听名称以 "report-" 开头的任务
 * matcher = Matcher.jobNameStartsWith("report-");
 *
 * // 添加全局监听器
 * listenerManager.addJobListener(myListener, Matcher.anyJobs());
 * </pre>
 *
 * @see ListenerManager#addJobListener(JobListener, Matcher)
 * @see ListenerManager#addTriggerListener(TriggerListener, Matcher)
 */
public class Matcher {

    private Matcher() {
        // 工具类不实例化
    }

    // ==================== Job 匹配器 ====================

    /**
     * 匹配所有任务。
     */
    public static Matcher anyJobs() {
        return new GroupMatcherWrapper(
                GroupMatcher.anyJobGroup(),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * 匹配指定分组的所有任务。
     */
    public static Matcher jobGroupEqualsTo(String... groups) {
        return new GroupMatcherWrapper(
                GroupMatcher.jobGroupEquals(groups.length == 1 ? groups[0] : groups[0]),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * 匹配指定名称的任务。
     */
    public static Matcher jobNameEqualsTo(String name) {
        Set<GroupMatcher<org.quartz.JobKey>> matchers = new HashSet<>();
        matchers.add(GroupMatcher.jobGroupEquals(org.quartz.JobKey.DEFAULT_GROUP));
        return new GroupMatcherWrapper(
                GroupMatcher.jobGroupEquals(org.quartz.JobKey.DEFAULT_GROUP),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * 匹配名称以指定前缀开头的任务。
     */
    public static Matcher jobNameStartsWith(String prefix) {
        return new GroupMatcherWrapper(
                GroupMatcher.jobGroupEquals(org.quartz.JobKey.DEFAULT_GROUP),
                GroupMatcher.anyTriggerGroup()
        );
    }

    // ==================== Trigger 匹配器 ====================

    /**
     * 匹配所有触发器。
     */
    public static Matcher anyTriggers() {
        return new GroupMatcherWrapper(
                GroupMatcher.anyJobGroup(),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * 匹配指定分组的所有触发器。
     */
    public static Matcher triggerGroupEqualsTo(String... groups) {
        return new GroupMatcherWrapper(
                GroupMatcher.anyJobGroup(),
                GroupMatcher.triggerGroupEquals(groups.length == 1 ? groups[0] : groups[0])
        );
    }

    // ==================== 内部实现 ====================

    /**
     * 内部包装类，用于将自定义 Matcher 转换为 Quartz 的 GroupMatcher。
     */
    public static class GroupMatcherWrapper extends Matcher {

        private final org.quartz.impl.matchers.GroupMatcher<org.quartz.JobKey> jobMatcher;
        private final org.quartz.impl.matchers.GroupMatcher<org.quartz.TriggerKey> triggerMatcher;

        GroupMatcherWrapper(
                org.quartz.impl.matchers.GroupMatcher<org.quartz.JobKey> jobMatcher,
                org.quartz.impl.matchers.GroupMatcher<org.quartz.TriggerKey> triggerMatcher) {
            this.jobMatcher = jobMatcher;
            this.triggerMatcher = triggerMatcher;
        }

        org.quartz.impl.matchers.GroupMatcher<org.quartz.JobKey> toQuartzJobMatcher() {
            return jobMatcher;
        }

        org.quartz.impl.matchers.GroupMatcher<org.quartz.TriggerKey> toQuartzTriggerMatcher() {
            return triggerMatcher;
        }
    }
}
