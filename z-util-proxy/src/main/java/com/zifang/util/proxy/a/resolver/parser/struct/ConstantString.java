package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 字符串常量<br>
 * 表示Class文件中CONSTANT_String_info类型的常量
 */
public class ConstantString extends ConstantPoolItem {
    private int utf8Index;

    /**
     * 构造方法
     *
     * @param classFile  ClassFile对象
     * @param index      常量池索引
     * @param utf8Index  UTF-8字符串索引，指向字符串内容
     */
    public ConstantString(ClassFile classFile, int index, int utf8Index) {
        super(classFile, index);
        this.utf8Index = utf8Index;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = String\t\t\t\t");
        result.append("#").append(utf8Index).append("\t\t// ");
        result.append(classFile.getString(utf8Index));
        return result.toString();
    }
}
