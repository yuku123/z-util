package com.zifang.util.proxy.a.resolver.parser.struct;

import java.util.Objects;

/**
 * 字段结构<br>
 * 表示Class文件中的field_info结构
 */
public class Field {

    int accessFlag;

    int nameIndex;

    int descriptorIndex;

    int attributesCount;

    /**
     * 获取访问标志
     *
     * @return 访问标志
     */
    public int getAccessFlag() {
        return accessFlag;
    }

    /**
     * 设置访问标志
     *
     * @param accessFlag 访问标志
     */
    public void setAccessFlag(int accessFlag) {
        this.accessFlag = accessFlag;
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
     * 设置名称索引
     *
     * @param nameIndex 名称索引
     */
    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    /**
     * 获取描述符索引
     *
     * @return 描述符索引
     */
    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    /**
     * 设置描述符索引
     *
     * @param descriptorIndex 描述符索引
     */
    public void setDescriptorIndex(int descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    /**
     * 获取属性数量
     *
     * @return 属性数量
     */
    public int getAttributesCount() {
        return attributesCount;
    }

    /**
     * 设置属性数量
     *
     * @param attributesCount 属性数量
     */
    public void setAttributesCount(int attributesCount) {
        this.attributesCount = attributesCount;
    }

    private String getString(int nameIndex) {
        return null;
    }

    /**
     * 构造方法
     *
     * @param accessFlag       访问标志
     * @param nameIndex        名称索引
     * @param descriptorIndex  描述符索引
     * @param attributesCount  属性数量
     */
    public Field(int accessFlag, int nameIndex, int descriptorIndex, int attributesCount) {
        super();
        this.accessFlag = accessFlag;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
    }

}
