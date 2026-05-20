package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 常量池项基类<br>
 * 表示Class文件中常量池的基本结构
 */
public class ConstantPoolItem {

    protected ClassFile classFile;

    protected int index;

    protected String value;

    /**
     * 构造方法
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     */
    public ConstantPoolItem(ClassFile classFile, int index) {
        super();
        this.classFile = classFile;
        this.index = index;
    }

    /**
     * 获取常量的值
     *
     * @return 常量值
     */
    public String getValue() {
        return value;
    }
}
