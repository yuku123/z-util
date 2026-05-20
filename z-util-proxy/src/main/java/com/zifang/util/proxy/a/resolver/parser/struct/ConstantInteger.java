package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 整数常量<br>
 * 表示Class文件中CONSTANT_Integer_info类型的常量
 */
public class ConstantInteger extends ConstantPoolItem {


    /**
     * 构造方法
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     整数值
     */
    public ConstantInteger(ClassFile classFile, int index, int value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Integer\t\t\t");
        result.append(value);
        return result.toString();
    }
}
