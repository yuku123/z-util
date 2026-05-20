package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 长整数常量<br>
 * 表示Class文件中CONSTANT_Long_info类型的常量
 */
public class ConstantLong extends ConstantPoolItem {


    /**
     * 构造方法
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     长整数值
     */
    public ConstantLong(ClassFile classFile, int index, long value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Long\t\t\t\t");
        result.append(value).append("l");
        return result.toString();
    }
}
