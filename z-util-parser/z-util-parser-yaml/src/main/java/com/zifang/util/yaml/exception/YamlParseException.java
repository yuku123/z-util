package com.zifang.util.yaml.exception;

/**
 * YAML 解析异常，当 YAML 字符串格式非法时抛出。
 *
 * @author zifang
 */
public class YamlParseException extends RuntimeException {

    private final int line;
    private final int column;

    public YamlParseException(String message) {
        super(message);
        this.line = -1;
        this.column = -1;
    }

    public YamlParseException(String message, Throwable cause) {
        super(message, cause);
        this.line = -1;
        this.column = -1;
    }

    public YamlParseException(String message, int line, int column) {
        super(message + " (line " + line + ", column " + column + ")");
        this.line = line;
        this.column = column;
    }

    public YamlParseException(String message, int line, int column, Throwable cause) {
        super(message + " (line " + line + ", column " + column + ")", cause);
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
