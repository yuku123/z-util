package com.zifang.util.json.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * 字符缓冲读取器，提供字符流的顺序读取和回退能力。
 * <p>
 * 使用缓冲机制提高读取效率，默认缓冲区大小为1024字符。
 *
 * @author zifang
 */
public class CharReader {

    private static final int BUFFER_SIZE = 1024;

    private final Reader reader;

    private char[] buffer;

    private int pos;

    private int size;

    /**
     * 构造一个字符缓冲读取器。
     *
     * @param reader 底层的字符输入流
     */
    public CharReader(Reader reader) {
        this.reader = reader;
        buffer = new char[BUFFER_SIZE];
    }

    /**
     * 查看当前字符（不移动位置）。
     *
     * @return 当前字符，如果已到缓冲区末尾则返回-1
     */
    public char peek() {
        if (pos - 1 >= size) {
            return (char) -1;
        }

        return buffer[Math.max(0, pos - 1)];
    }

    /**
     * 读取下一个字符并移动位置。
     *
     * @return 下一个字符，如果已到末尾则返回-1
     * @throws IOException 如果读取过程中发生I/O错误
     */
    public char next() throws IOException {
        if (!hasMore()) {
            return (char) -1;
        }

        return buffer[pos++];
    }

    /**
     * 将位置回退一个字符。
     */
    public void back() {
        pos = Math.max(0, --pos);
    }

    /**
     * 判断是否还有更多字符可读。
     *
     * @return 如果还有字符返回true
     * @throws IOException 如果读取过程中发生I/O错误
     */
    public boolean hasMore() throws IOException {
        if (pos < size) {
            return true;
        }

        fillBuffer();
        return pos < size;
    }

    /**
     * 填充缓冲区。
     *
     * @throws IOException 如果读取过程中发生I/O错误
     */
    void fillBuffer() throws IOException {
        int n = reader.read(buffer);
        if (n == -1) {
            return;
        }

        pos = 0;
        size = n;
    }
}
