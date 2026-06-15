package com.zifang.util.xml.tokenizer;

/**
 * XML Token，tokenizer 输出基本单元。
 *
 * @author zifang
 */

/**
 * Token类。
 */
public class Token {

    private TokenType tokenType;

    private String value;

    /**
     * Token方法。
     * * @param tokenType TokenType类型参数
     *
     * @param value String类型参数
     */
    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    /**
     * getTokenType方法。
     *
     * @return TokenType类型返回值
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * getValue方法。
     *
     * @return String类型返回值
     */
    public String getValue() {
        return value;
    }

    /**
     * setValue方法。
     * * @param value String类型参数
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value='" + value + '\'' +
                '}';
    }
}
