package com.zifang.util.zex.interview.demo2;

public class IAImplement implements IA, IB {

    private String className;
    private String method;
    private String returns;

    private String controlStr;

    public IAImplement(String controlStr) {
        this.controlStr = controlStr;
        className = controlStr.split("[$]")[0];
        method = controlStr.split("[$]")[1].split("=")[0];
        returns = controlStr.split("[$]")[1].split("=")[1];
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public String getControlStr() {
        return controlStr;
    }

    public void setControlStr(String controlStr) {
        this.controlStr = controlStr;
    }

    @Override
    public String toString() {
        return "IAImplement{className=" + className + ", method=" + method + ", returns=" + returns + ", controlStr=" + controlStr + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IAImplement that = (IAImplement) o;
        return java.util.Objects.equals(className, that.className) && java.util.Objects.equals(method, that.method) && java.util.Objects.equals(returns, that.returns) && java.util.Objects.equals(controlStr, that.controlStr);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(className, method, returns, controlStr);
    }

    @Override
    public String getName() {
        return returns;
    }

    @Override
    public String getYear() {
        return null;
    }

    @Override
    public IB getIB(String b) {
        return null;
    }

    @Override
    public String getIBName() {
        return null;
    }
}
