package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 方法符号引用常量<br>
 * 表示Class文件中CONSTANT_Methodref_info类型的常量
 */
public class ConstantMethodref extends ConstantPoolItem {

    private int classIndex;

    private int nameAndTypeIndex;

    /**
     * 构造方法
     *
     * @param classFile        ClassFile对象
     * @param index           常量池索引
     * @param classIndex       指向CONSTANT_Class_info的索引
     * @param nameAndTypeIndex 指向CONSTANT_NameAndType_info的索引
     */
    public ConstantMethodref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        super(classFile, index);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Methodref\t\t\t");
        result.append("#" + classIndex + ".#" + nameAndTypeIndex).append("\t\t// ");
        ConstantClass constClass = (ConstantClass) classFile.get(classIndex);
        result.append(constClass.getValue() + ".");

        ConstantNameAndType constNameAndType = (ConstantNameAndType) classFile.get(nameAndTypeIndex);
        ConstantUtf8 methodName = (ConstantUtf8) classFile.get(constNameAndType.getNameIndex());
        ConstantUtf8 methodReturnType = (ConstantUtf8) classFile.get(constNameAndType.getTypeIndex());

        result.append(methodName.getValue() + ":" + methodReturnType.getValue());

        return result.toString();
    }

}
