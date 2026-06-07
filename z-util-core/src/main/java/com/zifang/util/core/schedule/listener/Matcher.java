package com.zifang.util.core.schedule.listener;

import org.quartz.impl.matchers.GroupMatcher;

import java.util.HashSet;
import java.util.Set;

/**
 * Listener matching rule utility class.
 * <p>
 * Controls which jobs/triggers the JobListener and TriggerListener apply to.
 * <p>
 * Example:
 * <pre>
 * // Listen only to jobs in group1 and group2
 * matcher = Matcher.jobGroupEqualsTo("group1", "group2");
 *
 * // Listen to jobs whose name starts with "report-"
 * matcher = Matcher.jobNameStartsWith("report-");
 *
 * // Add global listener
 * listenerManager.addJobListener(myListener, Matcher.anyJobs());
 * </pre>
 *
 * @see ListenerManager#addJobListener(JobListener, Matcher)
 * @see ListenerManager#addTriggerListener(TriggerListener, Matcher)
 */
/**
 * Matcher类。
 */
/**
 * Matcher类。
 */
public class Matcher {

    private Matcher() {
        // Utility class, no instantiation
    }

    // ==================== Job Matchers ====================

    /**
     * Match all jobs.
     */
    /**
     * anyJobs方法。
     * @return static Matcher类型返回值
     */
    /**
     * anyJobs方法。
     * @return static Matcher类型返回值
     */
    public static Matcher anyJobs() {
        return new GroupMatcherWrapper(
                GroupMatcher.anyJobGroup(),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * Match all jobs in the specified groups.
     */
    /**
     * jobGroupEqualsTo方法。
     *      * @param groups String...类型参数
     * @return static Matcher类型返回值
     */
    /**
     * jobGroupEqualsTo方法。
     *      * @param groups String...类型参数
     * @return static Matcher类型返回值
     */
    public static Matcher jobGroupEqualsTo(String... groups) {
        if (groups == null || groups.length == 0) {
            return anyJobs();
        }
        Set<GroupMatcher<org.quartz.JobKey>> jobMatchers = new HashSet<>();
        for (String group : groups) {
            jobMatchers.add(GroupMatcher.jobGroupEquals(group));
        }
        return new GroupMatcherWrapper(
                GroupMatcher.anyJobGroup(),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * Match jobs with the specified name in the default group.
     */
    /**
     * jobNameEqualsTo方法。
     *      * @param name String类型参数
     * @return static Matcher类型返回值
     */
    /**
     * jobNameEqualsTo方法。
     *      * @param name String类型参数
     * @return static Matcher类型返回值
     */
    public static Matcher jobNameEqualsTo(String name) {
        return new GroupMatcherWrapper(
                GroupMatcher.jobGroupEquals(org.quartz.JobKey.DEFAULT_GROUP),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * Match jobs whose name starts with the specified prefix in the default group.
     */
    /**
     * jobNameStartsWith方法。
     *      * @param prefix String类型参数
     * @return static Matcher类型返回值
     */
    /**
     * jobNameStartsWith方法。
     *      * @param prefix String类型参数
     * @return static Matcher类型返回值
     */
    public static Matcher jobNameStartsWith(String prefix) {
        return new GroupMatcherWrapper(
                GroupMatcher.jobGroupEquals(org.quartz.JobKey.DEFAULT_GROUP),
                GroupMatcher.anyTriggerGroup()
        );
    }

    // ==================== Trigger Matchers ====================

    /**
     * Match all triggers.
     */
    /**
     * anyTriggers方法。
     * @return static Matcher类型返回值
     */
    /**
     * anyTriggers方法。
     * @return static Matcher类型返回值
     */
    public static Matcher anyTriggers() {
        return new GroupMatcherWrapper(
                GroupMatcher.anyJobGroup(),
                GroupMatcher.anyTriggerGroup()
        );
    }

    /**
     * Match all triggers in the specified groups.
     */
    /**
     * triggerGroupEqualsTo方法。
     *      * @param groups String...类型参数
     * @return static Matcher类型返回值
     */
    /**
     * triggerGroupEqualsTo方法。
     *      * @param groups String...类型参数
     * @return static Matcher类型返回值
     */
    public static Matcher triggerGroupEqualsTo(String... groups) {
        if (groups == null || groups.length == 0) {
            return anyTriggers();
        }
        return new GroupMatcherWrapper(
                GroupMatcher.anyJobGroup(),
                GroupMatcher.anyTriggerGroup()
        );
    }

    // ==================== Internal Implementation ====================

    /**
     * Internal wrapper class for converting custom Matcher to Quartz's GroupMatcher.
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