package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * UTF-8字符串常量<br>
 * 表示Class文件中CONSTANT_Utf8_info类型的常量
 */
public class ConstantUtf8 extends ConstantPoolItem {

    /**
     * 构造方法
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     UTF-8字符串值
     */
    public ConstantUtf8(ClassFile classFile, int index, String value) {
        super(classFile, index);
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Utf8\t\t\t\t");
        result.append(value);
        return result.toString();
    }

}