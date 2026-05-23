package com.zifang.util.expression.dynamic.token;

/**
 * Token接口
 */
public interface Token {

    int getType();

    String getText();

    int getLine();

    int getColumn();

    String getTokenName();
}