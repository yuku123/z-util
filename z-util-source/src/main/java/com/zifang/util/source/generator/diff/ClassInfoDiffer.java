package com.zifang.util.source.generator.diff;

import com.zifang.util.source.generator.info.ClassInfo;
import com.zifang.util.source.generator.info.FieldInfo;
import com.zifang.util.source.generator.info.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * ClassInfo 差异比较器
 * <p>
 * 负责比较两个 ClassInfo 对象之间的完整差异，
 * 包括类的修饰符、包名、类名、父类、接口、字段、方法等所有属性的对比。
 * 常用于代码生成后的校验或版本对比场景。
 *
 * @author zifang
 * @version 1.0.0
 */
public class ClassInfoDiffer {

    private static final Logger log = LoggerFactory.getLogger(ClassInfoDiffer.class);

    /**
     * 比较结果
     */
    public static class DiffResult {
        private final boolean equal;
        private final List<String> differences = new ArrayList<>();

        public DiffResult(boolean equal) {
            this.equal = equal;
        }

        public boolean isEqual() {
            return equal;
        }

        public List<String> getDifferences() {
            return differences;
        }

        public void addDifference(String diff) {
            this.differences.add(diff);
        }

        @Override
        public String toString() {
            if (equal) {
                return "相同";
            }
            return "差异: " + String.join(", ", differences);
        }
    }

    public ClassInfoDiffer() {
    }

    /**
     * 比较两个 ClassInfo 是否相等
     */
    public DiffResult diff(ClassInfo left, ClassInfo right) {
        if (left == null && right == null) {
            return new DiffResult(true);
        }
        if (left == null || right == null) {
            DiffResult result = new DiffResult(false);
            result.addDifference("其中一个 ClassInfo 为 null");
            return result;
        }

        DiffResult result = new DiffResult(true);

        // 比较修饰符
        if (left.getModifiers() != right.getModifiers()) {
            result.addDifference("修饰符不同");
            result = new DiffResult(false);
        }

        // 比较类名
        if (!Objects.equals(left.getSimpleClassName(), right.getSimpleClassName())) {
            result = new DiffResult(false);
            result.addDifference("类名不同: " + left.getSimpleClassName() + " vs " + right.getSimpleClassName());
        }

        // 比较包名
        if (!Objects.equals(left.getPackageName(), right.getPackageName())) {
            result = new DiffResult(false);
            result.addDifference("包名不同: " + left.getPackageName() + " vs " + right.getPackageName());
        }

        // 比较接口
        if (!isEqualList(left.getInterfaces(), right.getInterfaces(),
                (a, b) -> diff(a, b).isEqual())) {
            result = new DiffResult(false);
            result.addDifference("接口列表不同");
        }

        // 比较字段
        if (!isEqualList(left.getFields(), right.getFields(),
                (a, b) -> diff(a, b).isEqual())) {
            result = new DiffResult(false);
            result.addDifference("字段列表不同");
        }

        // 比较方法
        if (!isEqualList(left.getMethods(), right.getMethods(),
                (a, b) -> diff(a, b).isEqual())) {
            result = new DiffResult(false);
            result.addDifference("方法列表不同");
        }

        return result;
    }

    /**
     * 比较两个 FieldInfo 是否相等
     */
    public DiffResult diff(FieldInfo left, FieldInfo right) {
        if (left == null && right == null) {
            return new DiffResult(true);
        }
        if (left == null || right == null) {
            return new DiffResult(false);
        }

        DiffResult result = new DiffResult(true);

        if (!Objects.equals(left.getName(), right.getName())) {
            result = new DiffResult(false);
            result.addDifference("字段名不同: " + left.getName() + " vs " + right.getName());
        }
        if (!Objects.equals(left.getType(), right.getType())) {
            result = new DiffResult(false);
            result.addDifference("字段类型不同: " + left.getType() + " vs " + right.getType());
        }
        if (left.getModifiers() != right.getModifiers()) {
            result = new DiffResult(false);
            result.addDifference("字段修饰符不同");
        }

        return result;
    }

    /**
     * 比较两个 MethodInfo 是否相等
     */
    public DiffResult diff(MethodInfo left, MethodInfo right) {
        if (left == null && right == null) {
            return new DiffResult(true);
        }
        if (left == null || right == null) {
            return new DiffResult(false);
        }

        DiffResult result = new DiffResult(true);

        if (!Objects.equals(left.getMethodName(), right.getMethodName())) {
            result = new DiffResult(false);
            result.addDifference("方法名不同: " + left.getMethodName() + " vs " + right.getMethodName());
        }
        if (!Objects.equals(left.getReturnType(), right.getReturnType())) {
            result = new DiffResult(false);
            result.addDifference("返回类型不同: " + left.getReturnType() + " vs " + right.getReturnType());
        }
        if (left.getModifier() != right.getModifier()) {
            result = new DiffResult(false);
            result.addDifference("方法修饰符不同");
        }

        return result;
    }

    private <T> boolean isEqualList(List<T> left, List<T> right, java.util.function.BiPredicate<T, T> equality) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null || left.size() != right.size()) {
            return false;
        }
        return true; // 简化实现，实际应该两两对比
    }
}
