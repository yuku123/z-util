package com.zifang.util.expression.dynamic.core;

import com.zifang.util.expression.dynamic.token.Token;

import java.util.List;

/**
 * 词法分析器接口
 */
public interface Lexer {

    /**
     * 设置输入
     */
    void setInput(String input);

    /**
     * 设置输入
     */
    void setInput(char[] input);

    /**
     * 执行词法分析，返回Token列表
     */
    List<Token> tokenize();

    /**
     * 获取Token流读取器
     */
    TokenReader getTokenReader();
}