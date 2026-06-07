package com.zifang.util.workflow.config;

import java.util.Map;

/**
 * 执行引擎配置类。
 * 用于配置工作流引擎的类型、模式和属性信息。
 *
 * @see Configurations
 */
/**
 * Engine类。
 */
/**
 * Engine类。
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

    /**
     * 默认构造函数
     */
    /**
     * Engine方法。
     */
    /**
     * Engine方法。
     */
    public Engine() {
    }

    /**
     * 全参数构造函数
     *
     * @param type       引擎类型，如spark、flink等
     * @param mode       引擎模式，表示在某个引擎下的种类类型
     * @param properties 引擎属性参数Map，配置引擎所需的所有参数
     */
    /**
     * Engine方法。
     *      * @param type String类型参数
     * @param mode String类型参数
     * @param properties MapString,类型参数
     */
    /**
     * Engine方法。
     *      * @param type String类型参数
     * @param mode String类型参数
     * @param properties MapString,类型参数
     */
    public Engine(String type, String mode, Map<String, String> properties) {
        this.type = type;
        this.mode = mode;
        this.properties = properties;
    }

    /**
     * 获取引擎类型
     *
     * @return 引擎类型，如spark、flink等
     */
    /**
     * getType方法。
     * @return String类型返回值
     */
    /**
     * getType方法。
     * @return String类型返回值
     */
    public String getType() {
        return type;
    }

    /**
     * 设置引擎类型
     *
     * @param type 引擎类型，如spark、flink等
     */
    /**
     * setType方法。
     *      * @param type String类型参数
     */
    /**
     * setType方法。
     *      * @param type String类型参数
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取引擎模式
     *
     * @return 引擎模式，表示在某个引擎下的种类类型
     */
    /**
     * getMode方法。
     * @return String类型返回值
     */
    /**
     * getMode方法。
     * @return String类型返回值
     */
    public String getMode() {
        return mode;
    }

    /**
     * 设置引擎模式
     *
     * @param mode 引擎模式，表示在某个引擎下的种类类型
     */
    /**
     * setMode方法。
     *      * @param mode String类型参数
     */
    /**
     * setMode方法。
     *      * @param mode String类型参数
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * 获取引擎属性参数
     *
     * @return 引擎属性参数Map
     */
    /**
     * getProperties方法。
     * @return Map<String, String>类型返回值
     */
    /**
     * getProperties方法。
     * @return Map<String, String>类型返回值
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * 设置引擎属性参数
     *
     * @param properties 引擎属性参数Map
     */
    /**
     * setProperties方法。
     *      * @param properties MapString,类型参数
     */
    /**
     * setProperties方法。
     *      * @param properties MapString,类型参数
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "Engine{type=" + type + ", mode=" + mode + ", properties=" + properties + "}";
    }

    @Override
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engine engine = (Engine) o;
        if (type != null ? !type.equals(engine.type) : engine.type != null) return false;
        if (mode != null ? !mode.equals(engine.mode) : engine.mode != null) return false;
        return properties != null ? properties.equals(engine.properties) : engine.properties == null;
    }

    @Override
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }
}
