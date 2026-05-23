package com.zifang.util.json.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Token列表，用于存储和管理JSON词法分析过程中产生的Token序列。
 *
 * @author zifang
 * @see Token
 * @see Tokenizer
 */
public class TokenList {

    private final List<Token> tokens;
    private int pos = 0;

    /**
     * 根据预估Token数量预分配容量。
     */
    public TokenList(int estimatedSize) {
        this.tokens = new ArrayList<>(estimatedSize);
    }

    public TokenList() {
        this.tokens = new ArrayList<>(64);
    }

    public void add(Token token) {
        tokens.add(token);
    }

    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    public Token next() {
        return tokens.get(pos++);
    }

    public boolean hasMore() {
        return pos < tokens.size();
    }

    public int size() {
        return tokens.size();
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
