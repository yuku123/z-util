package com.zifang.util.dsl.token;

/**
 * 简单Token实现
 */
public class SimpleToken implements Token {

    private int type;
    private String text;
    private int line;
    private int column;
    private String tokenName;

    public SimpleToken() {
    }

    public SimpleToken(int type, String text, int line, int column, String tokenName) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.column = column;
        this.tokenName = tokenName;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    @Override
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