package com.zifang.util.core.time.schedule;

/**
 * 触发器错失执行策略（Misfire Policy）。
 * <p>
 * 当调度器关闭或系统时间跳跃导致触发器错过预定执行时间时，
 * 恢复后的行为策略由本枚举定义。
 *
 * @see TriggerBuilder#withMisfirePolicy(MisfirePolicy)
 */
public enum MisfirePolicy {

    /**
     * 不使用智能策略，由触发器自己决定。
     * 对于 CronTrigger 相当于 {@code withMisfireHandlingInstructionDoNothing}；
     * 对于 SimpleTrigger 相当于 {@code withMisfireHandlingInstructionFireNow}。
     */
    SMART_POLICY(org.quartz.Trigger.MISFIRE_INSTRUCTION_SMART_POLICY),

    /**
     * 立即触发错失的次数，然后恢复正常的触发时间。
     * <p>
     * 效果：错失的触发会立即执行一次，然后按正常周期继续。
     */
    FIRE_NOW(org.quartz.Trigger.MISFIRE_INSTRUCTION_FIRE_NOW),

    /**
     * 什么都不做，直接跳到下一个正常触发时间。
     * <p>
     * 效果：错失的所有触发被忽略，只等下一个预定时间。
     */
    DO_NOTHING(org.quartz.Trigger.MISFIRE_INSTRUCTION_DO_NOTHING),

    /**
     * 将错失的触发合并为一次，立即执行。
     * <p>
     * 效果：无论错过了多少次，都只触发一次，然后恢复正常周期。
     */
    IGNORE_MISFIRE_FIRES_NOW(org.quartz.Trigger.MISFIRE_INSTRUCTION_FIRE_NOW);

    private final int instruction;

    MisfirePolicy(int instruction) {
        this.instruction = instruction;
    }

    /**
     * 转换为 Quartz 内部整数常量。
     */
    public int toQuartzInstruction() {
        return instruction;
    }
}
