package com.zifang.util.dsl.g4.model;

/**
 * Token定义（用于动态词法分析器）
 */
public class TokenDefinition {
    
    private String name;
    private String pattern;      // 正则表达式
    private int precedence;      // 优先级
    private boolean isFragment;
    private boolean hidden;      // 是否为HIDDEN channel

    
    public TokenDefinition() {
    }
    
    public TokenDefinition(String name, String pattern, int precedence) {
        this.name = name;
        this.pattern = pattern;
        this.precedence = precedence;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
    public int getPrecedence() {
        return precedence;
    }
    
    public void setPrecedence(int precedence) {
        this.precedence = precedence;
    }
    
    public boolean isFragment() {
        return isFragment;
    }
    
    public void setFragment(boolean fragment) {
        isFragment = fragment;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "TokenDefinition{" +
                "name='" + name + '\'' +
                ", pattern='" + pattern + '\'' +
                ", precedence=" + precedence +
                ", isFragment=" + isFragment +
                ", hidden=" + hidden +
                '}';
    }
}