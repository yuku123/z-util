package com.zifang.util.xml.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Token 列表，支持前向遍历和回退。
 *
 * @author zifang
 */

/**
 * TokenList类。
 */
public class TokenList {

    private final List<Token> tokens = new ArrayList<>();

    private int pos = 0;

    /**
     * add方法。
     * * @param token Token类型参数
     */
    public void add(Token token) {
        tokens.add(token);
    }

    /**
     * removeLast方法。
     */
    public void removeLast() {
        if (!tokens.isEmpty()) {
            tokens.remove(tokens.size() - 1);
        }
    }

    /**
     * peek方法。
     *
     * @return Token类型返回值
     */
    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    /**
     * peekPrevious方法。
     *
     * @return Token类型返回值
     */
    public Token peekPrevious() {
        return pos - 1 >= 0 ? tokens.get(pos - 1) : null;
    }

    /**
     * next方法。
     *
     * @return Token类型返回值
     */
    public Token next() {
        return pos < tokens.size() ? tokens.get(pos++) : null;
    }

    /**
     * hasMore方法。
     *
     * @return boolean类型返回值
     */
    public boolean hasMore() {
        return pos < tokens.size();
    }

    /**
     * size方法。
     *
     * @return int类型返回值
     */
    public int size() {
        return tokens.size();
    }

    /**
     * getTokens方法。
     *
     * @return List<Token>类型返回值
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * reset方法。
     */
    public void reset() {
        pos = 0;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }
}
