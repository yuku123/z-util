package com.zifang.util.source.generator.diff;

import com.zifang.util.source.generator.info.FieldInfo;

import java.util.Objects;

/**
 * FieldInfo 差异比较器
 *
 * @author zifang
 * @version 1.0.0
 */
public class FieldDiffer {

    public FieldDiffer() {
    }

    /**
     * 比较两个字段是否相等
     *
     * @return true 相等，false 不等
     */
    public boolean diff(FieldInfo left, FieldInfo right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return Objects.equals(left.getName(), right.getName())
                && Objects.equals(left.getType(), right.getType())
                && left.getModifiers() == right.getModifiers()
                && Objects.equals(left.getInitializer(), right.getInitializer());
    }
}
