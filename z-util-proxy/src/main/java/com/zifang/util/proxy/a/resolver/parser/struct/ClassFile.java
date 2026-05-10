package com.zifang.util.proxy.a.resolver.parser.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zifang
 */
public class ClassFile {

    /**
     * 魔术
     */
    private int magic;

    private int minorVersion;

    private int majorVersion;

    private int accessFlags;

    /**
     * 常量池
     */
    public List<? super ConstantPoolItem> items;

    public List<Field> fields;

    public ClassFile() {
        items = new ArrayList<>();
        fields = new ArrayList<>();
        items.add(new ConstantPoolItem(this, 0));
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    public List<? super ConstantPoolItem> getItems() {
        return items;
    }

    public void setItems(List<? super ConstantPoolItem> items) {
        this.items = items;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassFile classFile = (ClassFile) o;
        return magic == classFile.magic &&
                minorVersion == classFile.minorVersion &&
                majorVersion == classFile.majorVersion &&
                accessFlags == classFile.accessFlags &&
                Objects.equals(items, classFile.items) &&
                Objects.equals(fields, classFile.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(magic, minorVersion, majorVersion, accessFlags, items, fields);
    }

    public void addConstantInteger(ClassFile classFile, int index, int value) {
        items.add(new ConstantInteger(classFile, index, value));
    }

    public void addConstantFloat(ClassFile classFile, int index, float value) {
        items.add(new ConstantFloat(classFile, index, value));
    }

    public void addConstantUtf8(ClassFile classFile, int index, String value) {
        items.add(new ConstantUtf8(classFile, index, value));
    }

    public void addConstantItem(ConstantPoolItem constantPoolItem) {
        items.add(constantPoolItem);
    }

    public void addConstantLong(ClassFile classFile, int index, long value) {
        items.add(new ConstantLong(classFile, index, value));
        items.add(new ConstantPoolItem(classFile, ++index));
    }

    public void addConstantDouble(ClassFile classFile, int index, double value) {
        items.add(new ConstantDouble(classFile, index, value));
        items.add(new ConstantPoolItem(classFile, ++index));
    }

    public void addConstantClass(ClassFile classFile, int index, int utf8Index) {
        items.add(new ConstantClass(classFile, index, utf8Index));
    }

    public void addConstantString(ClassFile classFile, int index, int utf8Index) {
        items.add(new ConstantString(classFile, index, utf8Index));
    }

    public void addConstantFieldref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        items.add(new ConstantFieldref(classFile, index, classIndex, nameAndTypeIndex));
    }

    public void addConstantMethodref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        items.add(new ConstantMethodref(classFile, index, classIndex, nameAndTypeIndex));
    }

    public void addConstantNameAndType(ClassFile classFile, int index, int nameIndex, int typeIndex) {
        items.add(new ConstantNameAndType(classFile, index, nameIndex, typeIndex));
    }

    public void addFiled(int accessFlag, int nameIndex, int descriptorIndex, int attributesCount) {
        fields.add(new Field(accessFlag, nameIndex, descriptorIndex, attributesCount));
    }

    public ConstantPoolItem get(int index) {
        return (ConstantPoolItem) items.get(index);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int index, Class<T> clazz) {
        Object object = items.get(index);
        if (object.getClass() == clazz) {
            return (T) object;
        }
        return null;
    }

    public String getString(int index) {
        ConstantPoolItem constantPoolItem = (ConstantPoolItem) items.get(index);
        return constantPoolItem.getValue();
    }

//	public String getClassString(int index) {
//		Object object = items.get(index);
////		System.err.println(object.getClass());
//		if (object.getClass() == ConstantClass.class) {
//			ConstantClass clazz = (ConstantClass) object;
//			return getString(clazz.getUtf8Index());
//		}
//		return null;
//	}

    public int size() {
        return items.size();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("编译器的版本号：").append(majorVersion).append(".").append(minorVersion).append("\r\n");


        result.append("常量池如下：").append("\r\n");
        for (int j = 0; j < items.size(); j++) {
            Object item = items.get(j);
            if (item.getClass() != ConstantPoolItem.class) {
                result.append(item.toString()).append("\r\n");
            }
        }


        if ((accessFlags & 0x0001) == 0x00000001) {
        }

        if ((accessFlags & 0x0010) == 0x0010) {
        }

        if ((accessFlags & 0x0020) == 0x0020) {
        }

        if ((accessFlags & 0x0020) == 0x0020) {
        }

        return result.toString();
    }

    public static void main(String[] args) {

    }

}
