package com.zifang.util.json.tokenizer;

import com.zifang.util.json.exception.JsonParseException;

/**
 * 字符缓冲读取器，直接操作 char[] 数组。
 * <p>
 * 对于 JSON，一次性加载全部内容到 char[]，通过整数索引访问，
 * 消除 Reader 缓冲区的边界检查开销，显著提升解析性能。
 * <p>
 * 支持回退（pushBack）一个字符，支持 EOF 保护。
 *
 * @author zifang
 */
public class CharReader {

    private final char[] buffer;
    private final int len;
    private int pos = 0;
    private int pushBackPos = -1;
    /** 标记是否已到达 EOF，防止 EOF 后 back() 导致数组越界 */
    private boolean eof = false;

    /**
     * 从 String 构建读取器（全量加载，无 IO）。
     */
    public CharReader(String str) {
        this.buffer = str.toCharArray();
        this.len = buffer.length;
    }

    /**
     * 从 char[] 构建读取器（全量加载，无 IO）。
     */
    public CharReader(char[] chars) {
        this.buffer = chars;
        this.len = chars.length;
    }

    /**
     * 从 Reader 一次性加载全部内容。
     */
    public static CharReader fromReader(java.io.Reader reader) throws java.io.IOException {
        java.io.BufferedReader br = reader instanceof java.io.BufferedReader
                ? (java.io.BufferedReader) reader
                : new java.io.BufferedReader(reader, 8192);
        StringBuilder sb = new StringBuilder();
        char[] chunk = new char[8192];
        int n;
        while ((n = br.read(chunk)) != -1) {
            sb.append(chunk, 0, n);
        }
        return new CharReader(sb.toString());
    }

    /**
     * 读取下一个字符并移动位置。
     */
    public char next() {
        if (pushBackPos >= 0) {
            int saved = pushBackPos;
            pushBackPos = -1;
            return buffer[saved];
        }
        if (pos >= len) {
            eof = true;
            return (char) -1;
        }
        return buffer[pos++];
    }

    /**
     * 将位置回退一个字符（最多一层嵌套回退）。
     */
    public void back() {
        if (eof) {
            return;
        }
        if (pushBackPos >= 0) {
            // 已有回退，恢复原位
            pos = pushBackPos;
            pushBackPos = -1;
        } else if (pos > 0) {
            pushBackPos = pos;
            pos--;
        }
    }

    /**
     * 查看当前字符（不移动位置）。
     */
    public char peek() {
        if (pushBackPos >= 0) {
            return buffer[pushBackPos];
        }
        if (pos >= len) {
            return (char) -1;
        }
        return buffer[pos];
    }

    /**
     * 判断是否还有更多字符可读。
     */
    public boolean hasMore() {
        if (pushBackPos >= 0) {
            return true;
        }
        return pos < len;
    }

    public int position() {
        return pos;
    }

    public int remaining() {
        return len - pos;
    }
}
