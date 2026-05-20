package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 双精度浮点数常量<br>
 * 表示Class文件中CONSTANT_Double_info类型的常量
 */
public class ConstantDouble extends ConstantPoolItem {

    /**
     * 构造方法
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     双精度浮点数值
     */
    public ConstantDouble(ClassFile classFile, int index, double value) {
        super(classFile, index);
        this.value = String.valueOf(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Double\t\t\t\t");
        result.append(value).append("d");
        return result.toString();
    }

}