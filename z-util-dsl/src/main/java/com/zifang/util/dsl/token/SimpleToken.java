package com.zifang.util.dsl.token;

/**
 * 简单Token实现
 * Token接口的标准实现类
 */
/**
 * SimpleToken类。
 */
public class SimpleToken implements Token {

    private int type;
    private String text;
    private int line;
    private int column;
    private String tokenName;

    /**
     * 默认构造函数
     */
    /**
     * SimpleToken方法。
     */
    public SimpleToken() {
    }

    /**
     * 完整构造函数
     * @param type Token类型编码
     * @param text Token文本值
     * @param line 行号
     * @param column 列号
     * @param tokenName Token类型名称
     */
    /**
     * SimpleToken方法。
     *      * @param type int类型参数
     * @param text String类型参数
     * @param line int类型参数
     * @param column int类型参数
     * @param tokenName String类型参数
     */
    public SimpleToken(int type, String text, int line, int column, String tokenName) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.column = column;
        this.tokenName = tokenName;
    }

    @Override
    /**
     * getType方法。
     * @return int类型返回值
     */
    public int getType() {
        return type;
    }

    /**
     * 设置Token类型编码
     * @param type 类型编码
     */
    /**
     * setType方法。
     *      * @param type int类型参数
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    /**
     * getText方法。
     * @return String类型返回值
     */
    public String getText() {
        return text;
    }

    /**
     * 设置Token文本值
     * @param text Token文本
     */
    /**
     * setText方法。
     *      * @param text String类型参数
     */
    public void setText(String text) {
        this.text = text;
    }

    @Override
    /**
     * getLine方法。
     * @return int类型返回值
     */
    public int getLine() {
        return line;
    }

    /**
     * 设置行号
     * @param line 行号（从1开始）
     */
    /**
     * setLine方法。
     *      * @param line int类型参数
     */
    public void setLine(int line) {
        this.line = line;
    }

    @Override
    /**
     * getColumn方法。
     * @return int类型返回值
     */
    public int getColumn() {
        return column;
    }

    /**
     * 设置列号
     * @param column 列号（从1开始）
     */
    /**
     * setColumn方法。
     *      * @param column int类型参数
     */
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    /**
     * getTokenName方法。
     * @return String类型返回值
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * 设置Token类型名称
     * @param tokenName 类型名称
     */
    /**
     * setTokenName方法。
     *      * @param tokenName String类型参数
     */
    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", line=" + line +
                ", column=" + column +
                ", tokenName='" + tokenName + '\'' +
                '}';
    }
}