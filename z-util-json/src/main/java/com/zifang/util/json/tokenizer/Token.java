package com.zifang.util.json.tokenizer;

/**
 * JSON Token（词法单元），表示JSON语法中的最小语义单位。
 * <p>
 * 每个Token由类型和值组成，例如数字"123"对应Token(TokenType.NUMBER, "123")。
 * <p>
 * 优化：提供静态单例Token，避免简单标点符号重复创建对象。
 *
 * @author zifang
 * @see TokenType
 * @see Tokenizer
 */
public class Token {

    private final TokenType tokenType;
    private final String value;

    /**
     * 静态单例Token，避免标点符号重复创建。
     */
    public static final Token BEGIN_OBJECT  = new Token(TokenType.BEGIN_OBJECT, "{");
    public static final Token END_OBJECT    = new Token(TokenType.END_OBJECT, "}");
    public static final Token BEGIN_ARRAY   = new Token(TokenType.BEGIN_ARRAY, "[");
    public static final Token END_ARRAY     = new Token(TokenType.END_ARRAY, "]");
    public static final Token SEP_COMMA     = new Token(TokenType.SEP_COMMA, ",");
    public static final Token SEP_COLON     = new Token(TokenType.SEP_COLON, ":");
    public static final Token NULL          = new Token(TokenType.NULL, "null");
    public static final Token TRUE          = new Token(TokenType.BOOLEAN, "true");
    public static final Token FALSE         = new Token(TokenType.BOOLEAN, "false");
    public static final Token END_DOCUMENT  = new Token(TokenType.END_DOCUMENT, null);

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

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

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
