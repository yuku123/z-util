package com.zifang.util.workflow.config;

import java.util.Map;

/**
 * 表明是什么类型的执行引擎
 */
public class Engine {

    /**
     * 表明是什么类型的引擎
     */
    private String type;

    /**
     * 表明在某个引擎下的种类类型
     */
    private String mode;

    /**
     * 表明这个种类类型的引擎所需要的所有参数
     */
    private Map<String, String> properties;

    public Engine() {
    }

    public Engine(String type, String mode, Map<String, String> properties) {
        this.type = type;
        this.mode = mode;
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Engine{type=" + type + ", mode=" + mode + ", properties=" + properties + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engine engine = (Engine) o;
        if (type != null ? !type.equals(engine.type) : engine.type != null) return false;
        if (mode != null ? !mode.equals(engine.mode) : engine.mode != null) return false;
        return properties != null ? properties.equals(engine.properties) : engine.properties == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
