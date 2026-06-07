package com.zifang.util.xml.tokenizer;

/**
 * XML Token 类型。
 *
 * @author zifang
 */
/**
 * TokenType枚举。
 */
public enum TokenType {
    /**
     * 开始标签: &lt;tag
     */
    TAG_OPEN(1),
    /**
     * 标签闭合: &gt;
     */
    TAG_CLOSE(2),
    /**
     * 自闭合标签: /&gt;
     */
    TAG_SELF_CLOSE(4),
    /**
     * 结束标签: &lt;/tag&gt;
     */
    TAG_END(8),
    /**
     * 属性名或属性值
     */
    ATTRIBUTE(16),
    /**
     * 文本内容
     */
    TEXT(32),
    /**
     * CDATA 节: &lt;![CDATA[...]]&gt;
     */
    CDATA(64),
    /**
     * 注释: &lt;!-- ... --&gt;
     */
    COMMENT(128),
    /**
     * 处理指令: &lt;?target data?&gt;
     */
    PROCESSING_INSTRUCTION(256),
    /**
     * XML 声明: &lt;?xml version="1.0" encoding="UTF-8"?&gt;
     */
    DECLARATION(512),
    /**
     * 文档结束
     */
    END_DOCUMENT(1024);

    private final int code;

    TokenType(int code) {
        this.code = code;
    }

    /**
     * getTokenCode方法。
     * @return int类型返回值
     */
    public int getTokenCode() {
        return code;
    }
}
