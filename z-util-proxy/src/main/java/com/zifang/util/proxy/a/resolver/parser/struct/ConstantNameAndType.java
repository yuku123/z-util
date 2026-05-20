package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 名称和类型常量<br>
 * 表示Class文件中CONSTANT_NameAndType_info类型的常量
 */
public class ConstantNameAndType extends ConstantPoolItem {

    private int nameIndex;

    private int typeIndex;

    /**
     * 构造方法
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param nameIndex  指向CONSTANT_Utf8_info的索引，表示名称
     * @param typeIndex 指向CONSTANT_Utf8_info的索引，表示类型描述符
     */
    public ConstantNameAndType(ClassFile classFile, int index, int nameIndex, int typeIndex) {
        super(classFile, index);
        this.nameIndex = nameIndex;
        this.typeIndex = typeIndex;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = NameAndType\t\t\t");
        result.append("#").append(nameIndex).append(":").append(typeIndex).append("\t\t// ");
        result.append(classFile.getString(nameIndex)).append(":").append(typeIndex);
        return result.toString();
    }

    /**
     * 获取名称索引
     *
     * @return 名称索引
     */
    public int getNameIndex() {
        return nameIndex;
    }

    /**
     * 获取类型索引
     *
     * @return 类型索引
     */
    public int getTypeIndex() {
        return typeIndex;
    }
}