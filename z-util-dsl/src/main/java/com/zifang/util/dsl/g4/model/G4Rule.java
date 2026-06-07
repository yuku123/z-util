package com.zifang.util.dsl.g4.model;

/**
 * G4文件中的一条规则
 * 表示.g4语法文件中的词法规则或语法规则
 */
/**
 * G4Rule类。
 */
/**
 * G4Rule类。
 */
public class G4Rule {
    
    /**
     * 规则类型枚举
     */
/**
 * RuleType枚举。
 */
/**
 * RuleType枚举。
 */
    public enum RuleType {
        /** 词法规则 */
        LEXER,
        /** 语法规则 */
        PARSER
    }
    
    private String name;
    private RuleType type;
    private String body;        // 原始规则体
    private boolean isFragment;
    private boolean hidden;     // 是否为HIDDEN channel

    /**
     * 默认构造函数
     */
    /**
     * G4Rule方法。
     */
    /**
     * G4Rule方法。
     */
    public G4Rule() {
    }
    
    /**
     * 完整构造函数
     * @param name 规则名称
     * @param type 规则类型（LEXER或PARSER）
     * @param body 规则体内容
     * @param isFragment 是否为fragment规则
     */
    /**
     * G4Rule方法。
     *      * @param name String类型参数
     * @param type G4Rule.RuleType类型参数
     * @param body String类型参数
     * @param isFragment boolean类型参数
     */
    /**
     * G4Rule方法。
     *      * @param name String类型参数
     * @param type G4Rule.RuleType类型参数
     * @param body String类型参数
     * @param isFragment boolean类型参数
     */
    public G4Rule(String name, G4Rule.RuleType type, String body, boolean isFragment) {
        this.name = name;
        this.type = type;
        this.body = body;
        this.isFragment = isFragment;
    }
    
    /**
     * 构造函数
     * @param name 规则名称
     * @param type 规则类型
     * @param body 规则体内容
     */
    /**
     * G4Rule方法。
     *      * @param name String类型参数
     * @param type G4Rule.RuleType类型参数
     * @param body String类型参数
     */
    /**
     * G4Rule方法。
     *      * @param name String类型参数
     * @param type G4Rule.RuleType类型参数
     * @param body String类型参数
     */
    public G4Rule(String name, G4Rule.RuleType type, String body) {
        this.name = name;
        this.type = type;
        this.body = body;
    }
    
    /**
     * 获取规则名称
     * @return 规则名称
     */
    /**
     * getName方法。
     * @return String类型返回值
     */
    /**
     * getName方法。
     * @return String类型返回值
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置规则名称
     * @param name 规则名称
     */
    /**
     * setName方法。
     *      * @param name String类型参数
     */
    /**
     * setName方法。
     *      * @param name String类型参数
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 获取规则类型
     * @return 规则类型（LEXER或PARSER）
     */
    /**
     * getType方法。
     * @return RuleType类型返回值
     */
    /**
     * getType方法。
     * @return RuleType类型返回值
     */
    public RuleType getType() {
        return type;
    }
    
    /**
     * 设置规则类型
     * @param type 规则类型
     */
    /**
     * setType方法。
     *      * @param type RuleType类型参数
     */
    /**
     * setType方法。
     *      * @param type RuleType类型参数
     */
    public void setType(RuleType type) {
        this.type = type;
    }
    
    /**
     * 获取规则体
     * @return 规则体原始字符串
     */
    /**
     * getBody方法。
     * @return String类型返回值
     */
    /**
     * getBody方法。
     * @return String类型返回值
     */
    public String getBody() {
        return body;
    }
    
    /**
     * 设置规则体
     * @param body 规则体内容
     */
    /**
     * setBody方法。
     *      * @param body String类型参数
     */
    /**
     * setBody方法。
     *      * @param body String类型参数
     */
    public void setBody(String body) {
        this.body = body;
    }
    
    /**
     * 判断是否为fragment规则
     * @return true表示是fragment规则
     */
    /**
     * isFragment方法。
     * @return boolean类型返回值
     */
    /**
     * isFragment方法。
     * @return boolean类型返回值
     */
    public boolean isFragment() {
        return isFragment;
    }
    
    /**
     * 设置是否为fragment规则
     * @param fragment true表示是fragment规则
     */
    /**
     * setFragment方法。
     *      * @param fragment boolean类型参数
     */
    /**
     * setFragment方法。
     *      * @param fragment boolean类型参数
     */
    public void setFragment(boolean fragment) {
        isFragment = fragment;
    }

    /**
     * 判断是否为HIDDEN channel
     * @return true表示隐藏在HIDDEN channel
     */
    /**
     * isHidden方法。
     * @return boolean类型返回值
     */
    /**
     * isHidden方法。
     * @return boolean类型返回值
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * 设置是否为HIDDEN channel
     * @param hidden true表示隐藏在HIDDEN channel
     */
    /**
     * setHidden方法。
     *      * @param hidden boolean类型参数
     */
    /**
     * setHidden方法。
     *      * @param hidden boolean类型参数
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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
        return "G4Rule{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", isFragment=" + isFragment +
                ", body='" + body + '\'' +
                '}';
    }
}