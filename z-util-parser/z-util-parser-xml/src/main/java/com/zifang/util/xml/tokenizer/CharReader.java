package com.zifang.util.xml.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * 字符流阅读器，基于 buffer 的 Reader 封装。
 * 支持 peek（预读）、next（消费）、back（回退）操作。
 *
 * @author zifang
 */

/**
 * CharReader类。
 */
public class CharReader {

    private static final int BUFFER_SIZE = 4096;

    private final Reader reader;

    private char[] buffer;

    private int pos;

    private int size;

    /**
     * CharReader方法。
     * * @param reader Reader类型参数
     */
    public CharReader(Reader reader) {
        this.reader = reader;
        this.buffer = new char[BUFFER_SIZE];
    }

    /**
     * 预读当前字符，不移动光标。
     */
    /**
     * peek方法。
     *
     * @return char类型返回值
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
    /**
     * peekPrevious方法。
     *
     * @return char类型返回值
     */
    public char peekPrevious() {
        if (pos - 1 < 0) {
            return (char) -1;
        }
        return buffer[pos - 1];
    }

    /**
     * 预读下一个字符（不移动光标），但会填充 buffer 如果需要。
     */
    /**
     * peekNext方法。
     *
     * @return char类型返回值
     */
    public char peekNext() throws IOException {
        if (pos + 1 >= size) {
            // 当前 buffer 位置不够，尝试填充
            if (pos + 1 < buffer.length && size > 0) {
                // size > 0 表示有有效数据，不需要填充
                return pos + 1 < size ? buffer[pos + 1] : (char) -1;
            }
            fillBuffer();
        }
        return pos + 1 < size ? buffer[pos + 1] : (char) -1;
    }

    /**
     * 消费并返回下一个字符，光标前移。
     */
    /**
     * next方法。
     *
     * @return char类型返回值
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
    /**
     * back方法。
     */
    public void back() {
        if (pos > 0) {
            pos--;
        }
    }

    /**
     * 是否还有更多字符。
     */
    /**
     * hasMore方法。
     *
     * @return boolean类型返回值
     */
    public boolean hasMore() throws IOException {
        if (pos < size) {
            return true;
        }
        if (size == -1) {
            return false; // EOF already reached
        }
        fillBuffer();
        return pos < size;
    }

    private void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        if (n == -1) {
            pos = 0;
            size = -1;
            return;
        }
        pos = 0;
        size = n;
    }
}