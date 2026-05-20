package com.zifang.util.proxy.a.resolver.parser.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class文件结构模型<br>
 * 表示解析后的Java Class文件内容，包含魔数、版本号、常量池、字段等信息
 */
public class ClassFile {

    /**
     * 魔数，固定为0xCAFEBABE
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

    /**
     * 构造方法，初始化常量池和字段列表
     */
    public ClassFile() {
        items = new ArrayList<>();
        fields = new ArrayList<>();
        items.add(new ConstantPoolItem(this, 0));
    }

    /**
     * 获取魔数
     *
     * @return 魔数值
     */
    public int getMagic() {
        return magic;
    }

    /**
     * 设置魔数
     *
     * @param magic 魔数值
     */
    public void setMagic(int magic) {
        this.magic = magic;
    }

    /**
     * 获取次版本号
     *
     * @return 次版本号
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * 设置次版本号
     *
     * @param minorVersion 次版本号
     */
    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * 获取主版本号
     *
     * @return 主版本号
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * 设置主版本号
     *
     * @param majorVersion 主版本号
     */
    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    /**
     * 获取访问标志
     *
     * @return 访问标志
     */
    public int getAccessFlags() {
        return accessFlags;
    }

    /**
     * 设置访问标志
     *
     * @param accessFlags 访问标志
     */
    public void setAccessFlags(int accessFlags) {
        this.accessFlags = accessFlags;
    }

    /**
     * 获取常量池项列表
     *
     * @return 常量池项列表
     */
    public List<? super ConstantPoolItem> getItems() {
        return items;
    }

    /**
     * 设置常量池项列表
     *
     * @param items 常量池项列表
     */
    public void setItems(List<? super ConstantPoolItem> items) {
        this.items = items;
    }

    /**
     * 获取字段列表
     *
     * @return 字段列表
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * 设置字段列表
     *
     * @param fields 字段列表
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    /**
     * 添加整数常量到常量池
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     整数值
     */
    public void addConstantInteger(ClassFile classFile, int index, int value) {
        items.add(new ConstantInteger(classFile, index, value));
    }

    /**
     * 添加浮点数常量到常量池
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     浮点数值
     */
    public void addConstantFloat(ClassFile classFile, int index, float value) {
        items.add(new ConstantFloat(classFile, index, value));
    }

    /**
     * 添加UTF-8字符串常量到常量池
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     字符串值
     */
    public void addConstantUtf8(ClassFile classFile, int index, String value) {
        items.add(new ConstantUtf8(classFile, index, value));
    }

    /**
     * 添加常量池项
     *
     * @param constantPoolItem 常量池项
     */
    public void addConstantItem(ConstantPoolItem constantPoolItem) {
        items.add(constantPoolItem);
    }

    /**
     * 添加长整数常量到常量池
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     长整数值
     */
    public void addConstantLong(ClassFile classFile, int index, long value) {
        items.add(new ConstantLong(classFile, index, value));
        items.add(new ConstantPoolItem(classFile, ++index));
    }

    /**
     * 添加双精度浮点数常量到常量池
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param value     双精度浮点数值
     */
    public void addConstantDouble(ClassFile classFile, int index, double value) {
        items.add(new ConstantDouble(classFile, index, value));
        items.add(new ConstantPoolItem(classFile, ++index));
    }

    /**
     * 添加类常量到常量池
     *
     * @param classFile  ClassFile对象
     * @param index      常量池索引
     * @param utf8Index  UTF-8字符串索引
     */
    public void addConstantClass(ClassFile classFile, int index, int utf8Index) {
        items.add(new ConstantClass(classFile, index, utf8Index));
    }

    /**
     * 添加字符串常量到常量池
     *
     * @param classFile  ClassFile对象
     * @param index      常量池索引
     * @param utf8Index  UTF-8字符串索引
     */
    public void addConstantString(ClassFile classFile, int index, int utf8Index) {
        items.add(new ConstantString(classFile, index, utf8Index));
    }

    /**
     * 添加字段引用常量到常量池
     *
     * @param classFile       ClassFile对象
     * @param index           常量池索引
     * @param classIndex      类常量索引
     * @param nameAndTypeIndex 名称和类型索引
     */
    public void addConstantFieldref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        items.add(new ConstantFieldref(classFile, index, classIndex, nameAndTypeIndex));
    }

    /**
     * 添加方法引用常量到常量池
     *
     * @param classFile       ClassFile对象
     * @param index           常量池索引
     * @param classIndex      类常量索引
     * @param nameAndTypeIndex 名称和类型索引
     */
    public void addConstantMethodref(ClassFile classFile, int index, int classIndex, int nameAndTypeIndex) {
        items.add(new ConstantMethodref(classFile, index, classIndex, nameAndTypeIndex));
    }

    /**
     * 添加名称和类型常量到常量池
     *
     * @param classFile ClassFile对象
     * @param index     常量池索引
     * @param nameIndex 名称索引
     * @param typeIndex 类型索引
     */
    public void addConstantNameAndType(ClassFile classFile, int index, int nameIndex, int typeIndex) {
        items.add(new ConstantNameAndType(classFile, index, nameIndex, typeIndex));
    }

    /**
     * 添加字段到字段列表
     *
     * @param accessFlag        访问标志
     * @param nameIndex         名称索引
     * @param descriptorIndex   描述符索引
     * @param attributesCount   属性数量
     */
    public void addFiled(int accessFlag, int nameIndex, int descriptorIndex, int attributesCount) {
        fields.add(new Field(accessFlag, nameIndex, descriptorIndex, attributesCount));
    }

    /**
     * 根据索引获取常量池项
     *
     * @param index 常量池索引
     * @return 常量池项
     */
    public ConstantPoolItem get(int index) {
        return (ConstantPoolItem) items.get(index);
    }

    /**
     * 根据索引和类型获取常量池项
     *
     * @param index 常量池索引
     * @param clazz 要转换的目标类型
     * @param <T>   类型参数
     * @return 转换后的常量池项，如果类型不匹配返回null
     */
    @SuppressWarnings("unchecked")
    public <T> T get(int index, Class<T> clazz) {
        Object object = items.get(index);
        if (object.getClass() == clazz) {
            return (T) object;
        }
        return null;
    }

    /**
     * 根据索引获取常量池中的字符串值
     *
     * @param index 常量池索引
     * @return 字符串值
     */
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

    /**
     * 获取常量池大小
     *
     * @return 常量池项数量
     */
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
