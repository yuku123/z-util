package com.zifang.util.expression.dynamic.ast;

import com.zifang.util.expression.dynamic.core.ASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 简单AST节点实现
 */
public class SimpleASTNode implements ASTNode {

    private String type;
    private String text;
    private int line;
    private int column;
    private List<ASTNode> children;
    private ASTNode parent;
    private Object token;

    public SimpleASTNode() {
        this.children = new ArrayList<>();
    }

    public SimpleASTNode(String type, String text, int line, int column) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.column = column;
        this.children = new ArrayList<>();
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
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
    public List<ASTNode> getChildren() {
        return children;
    }

    @Override
    public void addChild(ASTNode child) {
        children.add(child);
        if (child != null) {
            child.setParent(this);
        }
    }

    @Override
    public ASTNode getParent() {
        return parent;
    }

    @Override
    public void setParent(ASTNode parent) {
        this.parent = parent;
    }

    @Override
    public Object getToken() {
        return token;
    }

    @Override
    public void setToken(Object token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ASTNode{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }

    /**
     * 打印AST树（用于调试）
     */
    public void dump(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        sb.append(type);
        if (text != null && !text.isEmpty()) {
            sb.append(": ").append(text);
        }
        sb.append("\n");
        for (ASTNode child : children) {
            if (child instanceof SimpleASTNode) {
                ((SimpleASTNode) child).dump(sb, indent + 1);
            }
        }
    }
}