package com.zifang.util.json.tokenizer;

/**
 * JSON Token（词法单元），表示JSON语法中的最小语义单位。
 * <p>
 * 每个Token由类型和值组成，例如数字"123"对应Token(TokenType.NUMBER, "123")。
 *
 * @author zifang
 * @see TokenType
 * @see Tokenizer
 */
public class Token {

    private TokenType tokenType;

    private String value;

    /**
     * 构造一个Token。
     *
     * @param tokenType Token类型
     * @param value     Token的文本值
     */
    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    /**
     * 获取Token类型。
     *
     * @return Token类型枚举值
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * 设置Token类型。
     *
     * @param tokenType 新的Token类型
     */
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * 获取Token的值。
     *
     * @return Token的文本值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置Token的值。
     *
     * @param value 新的文本值
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value='" + value + '\'' +
                '}';
    }
}
