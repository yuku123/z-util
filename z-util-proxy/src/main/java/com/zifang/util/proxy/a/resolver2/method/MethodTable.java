package com.zifang.util.proxy.a.resolver2.method;

import com.zifang.util.proxy.a.resolver2.attribute.AbstractAttribute;
import com.zifang.util.proxy.a.resolver2.attribute.AttributeFactory;
import com.zifang.util.proxy.a.resolver2.constantpool.AbstractConstantPool;
import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法表
 */
public class MethodTable {
    private U2 accessFlags;
    private U2 nameIndex;
    private U2 descriptorIndex;
    private U2 attributesCount;
    private List<AbstractAttribute> attributesInfo = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param stream       输入流
     * @param poolList     常量池列表
     * @param methodCount  方法数量
     */
    public MethodTable(InputStream stream, List<AbstractConstantPool> poolList, int methodCount) {
        for (int i = 0; i < methodCount; i++) {
            parseMethod(stream, poolList);
        }
    }

    /**
     * 解析单个方法
     *
     * @param inputStream 输入流
     * @param poolList    常量池列表
     */
    public void parseMethod(InputStream inputStream, List<AbstractConstantPool> poolList) {
        accessFlags = U2.read(inputStream);
        nameIndex = U2.read(inputStream);
        descriptorIndex = U2.read(inputStream);
        attributesCount = U2.read(inputStream);
        short count = attributesCount.value;
        for (int i = 0; i < count; i++) {
            parseAttribute(inputStream, poolList);
        }
    }

    /**
     * 解析方法属性
     *
     * @param inputStream 输入流
     * @param poolList    常量池列表
     */
    public void parseAttribute(InputStream inputStream, List<AbstractConstantPool> poolList) {
        AbstractAttribute attributeTable = AttributeFactory.getAttributeTable(inputStream, poolList);
        if (attributeTable != null) {
            attributesInfo.add(attributeTable);
        }
    }

    public U2 getAccessFlags() {
        return accessFlags;
    }

    public void setAccessFlags(U2 accessFlags) {
        this.accessFlags = accessFlags;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(U2 nameIndex) {
        this.nameIndex = nameIndex;
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(U2 descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    public U2 getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(U2 attributesCount) {
        this.attributesCount = attributesCount;
    }

    public List<AbstractAttribute> getAttributesInfo() {
        return attributesInfo;
    }

    public void setAttributesInfo(List<AbstractAttribute> attributesInfo) {
        this.attributesInfo = attributesInfo;
    }
}
