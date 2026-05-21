package com.zifang.util.xml.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * 字符流阅读器，基于 buffer 的 Reader 封装。
 * 支持 peek（预读）、next（消费）、back（回退）操作。
 *
 * @author zifang
 */
public class CharReader {

    private static final int BUFFER_SIZE = 4096;

    private final Reader reader;

    private char[] buffer;

    private int pos;

    private int size;

    public CharReader(Reader reader) {
        this.reader = reader;
        this.buffer = new char[BUFFER_SIZE];
    }

    /**
     * 预读当前字符，不移动光标。
     */
    public char peek() throws IOException {
        if (pos >= size) {
            fillBuffer();
        }
        if (pos >= size) {
            return (char) -1;
        }
        return buffer[pos];
    }

    /**
     * 返回前一个字符。
     */
    public char peekPrevious() {
        if (pos - 1 < 0) {
            return (char) -1;
        }
        return buffer[pos - 1];
    }

    /**
     * 消费并返回下一个字符，光标前移。
     */
    public char next() throws IOException {
        if (pos >= size) {
            fillBuffer();
        }
        if (pos >= size) {
            return (char) -1;
        }
        return buffer[pos++];
    }

    /**
     * 回退一个字符。
     */
    public void back() {
        if (pos > 0) {
            pos--;
        }
    }

    /** Debug: get current position */
    public int getPos() { return pos; }
    /** Debug: get char at current position without advancing */
    public char peekPos() { return pos < size ? buffer[pos] : (char)-1; }

    /**
     * 是否还有更多字符。
     */
    public boolean hasMore() throws IOException {
        if (pos < size) {
            return true;
        }
        fillBuffer();
        return pos < size;
    }

    private void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        if (n == -1) {
            return;
        }
        pos = 0;
        size = n;
    }
}
