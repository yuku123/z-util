package com.zifang.util.compile.sorce2.generator.info;

/**
 * 方法参数信息封装类
 * <p>
 * 用于封装Java方法的参数（形参）的类型和名称信息。
 * 是MethodInfo中参数列表的基本组成单元。
 *
 * @author zifang
 * @version 1.0.0
 */
public class MethodParameterPair {

    /**
     * 参数类型全限定名
     */
    private String paramType;

    /**
     * 参数名称
     */
    private String paramName;


    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String toString() {
        return paramType + " " + paramName;
    }
}
