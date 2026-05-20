package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 浮点数常量<br>
 * 表示Class文件中CONSTANT_Float_info类型的常量
 */
public class ConstantFloat extends ConstantPoolItem {


    /**
     * 构造方法
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     浮点数值
     */
    public ConstantFloat(ClassFile classFile, int index, float value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Float\t\t\t\t");
        result.append(value + "f");
        return result.toString();
    }

}
