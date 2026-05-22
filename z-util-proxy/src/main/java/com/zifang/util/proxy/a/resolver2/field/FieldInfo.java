package com.zifang.util.proxy.a.resolver2.field;

import com.zifang.util.proxy.a.resolver2.attribute.AbstractAttribute;
import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.util.List;

/**
 * 字段信息
 * <p>
 * 表示ClassFile中的字段表项，包含字段访问标志、名称索引、描述符索引和属性。
 */
public class FieldInfo {
    private U2 accessFlags;
    private U2 nameIndex;
    private U2 descriptorIndex;
    private U2 attributesCount;
    private List<AbstractAttribute> attributes;

    public FieldInfo(U2 accessFlags, U2 nameIndex, U2 descriptorIndex, 
                     U2 attributesCount, List<AbstractAttribute> attributes) {
        this.accessFlags = accessFlags;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
        this.attributes = attributes;
    }

    public U2 getAccessFlags() {
        return accessFlags;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    public U2 getAttributesCount() {
        return attributesCount;
    }

    public List<AbstractAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "accessFlags=" + (accessFlags != null ? accessFlags.value : 0) +
                ", nameIndex=" + (nameIndex != null ? nameIndex.value : 0) +
                ", descriptorIndex=" + (descriptorIndex != null ? descriptorIndex.value : 0) +
                ", attributesCount=" + (attributesCount != null ? attributesCount.value : 0) +
                '}';
    }
}