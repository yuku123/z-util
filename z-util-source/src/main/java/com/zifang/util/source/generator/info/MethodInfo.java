package com.zifang.util.source.generator.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 方法信息封装类
 * <p>
 * 用于封装Java方法的完整元数据信息，
 * 包括方法修饰符、返回类型、方法名、参数列表、方法体语句等。
 * 支持方法的签名生成和相等性比较。
 *
 * @author zifang
 * @version 1.0.0
 */
public class MethodInfo {

    private int modifier;
    private String returnType;
    private String methodName;
    private List<MethodParameterPair> methodParameterPairs = new ArrayList<>();
    private List<String> statements;
    private List<AnnotationInfo> annotations = new ArrayList<>();

    public MethodInfo() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MethodInfo other = (MethodInfo) obj;
        return Objects.equals(returnType, other.returnType)
                && Objects.equals(methodName, other.methodName)
                && Objects.equals(methodParameterPairs, other.methodParameterPairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnType, methodName, methodParameterPairs);
    }

    /**
     * 获取方法签名（不考虑修饰符）
     * <p>
     * 例如：String name(String cc, Double dd);
     */
    public String signature() {
        return returnType + " " + methodName + "(" + getParameterStr() + ");";
    }

    /**
     * 获取方法的完整签名（包含修饰符）
     * <p>
     * 例如：public String name(String cc, Double dd);
     */
    public String fullSignature() {
        String modifierStr = java.lang.reflect.Modifier.toString(modifier);
        return (modifierStr.isEmpty() ? "" : modifierStr + " ")
                + returnType + " " + methodName + "(" + getParameterStr() + ");";
    }

    private String getParameterStr() {
        if (methodParameterPairs == null) {
            return "";
        }
        List<String> params = new ArrayList<>();
        for (MethodParameterPair pair : methodParameterPairs) {
            params.add(pair.toString());
        }
        return String.join(",", params);
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<MethodParameterPair> getMethodParameterPairs() {
        return methodParameterPairs;
    }

    public void setMethodParameterPairs(List<MethodParameterPair> methodParameterPairs) {
        this.methodParameterPairs = methodParameterPairs;
    }

    public List<String> getStatements() {
        return statements;
    }

    public void setStatements(List<String> statements) {
        this.statements = statements;
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationInfo> annotations) {
        this.annotations = annotations;
    }
}
