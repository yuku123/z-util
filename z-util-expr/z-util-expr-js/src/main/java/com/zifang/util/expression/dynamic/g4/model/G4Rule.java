package com.zifang.util.expression.dynamic.g4.model;

/**
 * G4文件中的一条规则
 */
public class G4Rule {

    private String name;
    private RuleType type;
    private String body;        // 原始规则体
    private boolean isFragment;
    private boolean hidden;     // 是否为HIDDEN channel
    public G4Rule() {
    }


    public G4Rule(String name, G4Rule.RuleType type, String body, boolean isFragment) {
        this.name = name;
        this.type = type;
        this.body = body;
        this.isFragment = isFragment;
    }

    public G4Rule(String name, G4Rule.RuleType type, String body) {
        this.name = name;
        this.type = type;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RuleType getType() {
        return type;
    }

    public void setType(RuleType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
        return "G4Rule{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", isFragment=" + isFragment +
                ", body='" + body + '\'' +
                '}';
    }

    public enum RuleType {
        LEXER,
        PARSER
    }
}