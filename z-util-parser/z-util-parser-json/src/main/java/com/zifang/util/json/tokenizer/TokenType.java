package com.zifang.util.json.tokenizer;

/**
 * JSON 词法分析器的Token类型枚举。
 * <p>
 * 定义了 JSON 语法中的所有基本Token类型，包括：
 * <ul>
 *   <li>结构符号：对象 { }、数组 [ ]</li>
 *   <li>分隔符：冒号 :、逗号 ,</li>
 *   <li>字面量：null、布尔值、数字、字符串</li>
 *   <li>结束标记：文档结束</li>
 * </ul>
 *
 * @author zifang
 */
public enum TokenType {
    /** 对象开始符号 {@code {} */
    BEGIN_OBJECT(1),
    /** 对象结束符号 {@code }} */
    END_OBJECT(2),
    /** 数组开始符号 {@code [} */
    BEGIN_ARRAY(4),
    /** 数组结束符号 {@code ]} */
    END_ARRAY(8),
    /** null 字面量 */
    NULL(16),
    /** 数字字面量 */
    NUMBER(32),
    /** 字符串字面量 */
    STRING(64),
    /** 布尔值字面量 */
    BOOLEAN(128),
    /** 键值分隔符冒号 {@code :} */
    SEP_COLON(256),
    /** 元素分隔符逗号 {@code ,} */
    SEP_COMMA(512),
    /** JSON 文档结束标记 */
    END_DOCUMENT(1024);

    TokenType(int code) {
        this.code = code;
    }

    private int code;

    /**
     * 获取Token类型的编码值。
     *
     * @return Token类型对应的整型编码
     */
    public int getTokenCode() {
        return code;
    }
}
