package com.zifang.util.json.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Token列表，用于存储和管理JSON词法分析过程中产生的Token序列。
 * <p>
 * 提供Token的顺序访问能力，支持前向读取和回退操作。
 *
 * @author zifang
 * @see Token
 * @see Tokenizer
 */
public class TokenList {

    private List<Token> tokens = new ArrayList<>(64);

    private int pos = 0;

    public TokenList() {
        this.tokens = new ArrayList<>(64);
    }

    public TokenList(int initialCapacity) {
        this.tokens = new ArrayList<>(initialCapacity);
    }

    /**
     * 添加一个Token到列表末尾。
     */
    public void add(Token token) {
        tokens.add(token);
    }

    /**
     * 查看当前Token（不移动位置）。
     */
    public Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    /**
     * 查看前一个Token。
     */
    public Token peekPrevious() {
        return pos - 1 < 0 ? null : tokens.get(pos - 2);
    }

    /**
     * 获取下一个Token并移动位置。
     */
    public Token next() {
        return tokens.get(pos++);
    }

    /**
     * 判断是否还有更多Token。
     */
    public boolean hasMore() {
        return pos < tokens.size();
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }
}
