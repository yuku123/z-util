package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 类符号引用常量<br>
 * 表示Class文件中CONSTANT_Class_info类型的常量，用于引用类或接口
 */
public class ConstantClass extends ConstantPoolItem {

    private int utf8Index;

    /**
     * 构造方法
     *
     * @param classFile  ClassFile对象
     * @param index      常量池索引
     * @param utf8Index  UTF-8字符串索引，指向类或接口的全限定名
     */
    public ConstantClass(ClassFile classFile, int index, int utf8Index) {
        super(classFile, index);
        this.utf8Index = utf8Index;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Class\t\t\t\t");
        result.append("#").append(index).append("\t\t// ");
        result.append(classFile.getString(utf8Index));
        return result.toString();
    }

    /**
     * 获取UTF-8字符串索引
     *
     * @return UTF-8字符串索引
     */
    public int getUtf8Index() {
        return utf8Index;
    }

    @Override
    public String getValue() {
        return classFile.getString(utf8Index);
    }
}
