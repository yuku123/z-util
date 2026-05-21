package com.zifang.util.xml.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Token 列表，支持前向遍历和回退。
 *
 * @author zifang
 */
public class TokenList {

    private final List<Token> tokens = new ArrayList<>();

    private int pos = 0;

    public void add(Token token) {
        tokens.add(token);
    }

    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    public Token peekPrevious() {
        return pos - 1 >= 0 ? tokens.get(pos - 1) : null;
    }

    public Token next() {
        return pos < tokens.size() ? tokens.get(pos++) : null;
    }

    public boolean hasMore() {
        return pos < tokens.size();
    }

    public int size() {
        return tokens.size();
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void reset() {
        pos = 0;
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }
}
