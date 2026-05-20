package com.zifang.util.proxy.a.resolver.parser.struct;

/**
 * 字段符号引用常量<br>
 * 表示Class文件中CONSTANT_Fieldref_info类型的常量
 */
public class ConstantFieldref extends ConstantPoolItem {

    private int classIndex;

    private int nameAndTypeIndex;

    /**
     * 构造方法
     *
     * @param classFile       ClassFile对象
     * @param index          常量池索引
     * @param classIndex      指向CONSTANT_Class_info的索引
     * @param nameAndTypeIndex 指向CONSTANT_NameAndType_info的索引
     */
    public ConstantFieldref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        super(classFile, index);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%5s", "#" + index)).append(" = Fieldref\t\t\t#");
        ConstantClass constClass = (ConstantClass) classFile.get(classIndex);
        ConstantNameAndType constNameAndType = (ConstantNameAndType) classFile.get(nameAndTypeIndex);

        result.append(classIndex + ".#" + nameAndTypeIndex).append("\t\t// ");
        result.append(classFile.getString(constClass.getUtf8Index()) + "."
                + classFile.getString(constNameAndType.getNameIndex()) + ":"
                + classFile.getString(constNameAndType.getTypeIndex()));
        return result.toString();
    }

}