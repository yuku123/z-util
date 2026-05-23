package com.zifang.util.source.generator.diff;

import com.zifang.util.source.generator.info.MethodInfo;

import java.util.Objects;

/**
 * MethodInfo 差异比较器
 *
 * @author zifang
 * @version 1.0.0
 */
public class MethodDiffer {

    public MethodDiffer() {
    }

    /**
     * 比较两个方法是否相等
     *
     * @return true 相等，false 不等
     */
    public boolean diff(MethodInfo left, MethodInfo right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        return Objects.equals(left.getMethodName(), right.getMethodName())
                && Objects.equals(left.getReturnType(), right.getReturnType())
                && left.getModifier() == right.getModifier()
                && Objects.equals(left.getMethodParameterPairs(), right.getMethodParameterPairs());
    }
}
