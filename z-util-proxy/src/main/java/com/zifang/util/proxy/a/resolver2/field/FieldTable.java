package com.zifang.util.proxy.a.resolver2.field;

import com.zifang.util.proxy.a.resolver2.attribute.AbstractAttribute;
import com.zifang.util.proxy.a.resolver2.attribute.AttributeFactory;
import com.zifang.util.proxy.a.resolver2.constantpool.AbstractConstantPool;
import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段表
 */
public class FieldTable {
    public U2 accessFlags;
    public U2 nameIndex;
    public U2 descriptorIndex;
    public U2 attributesCount;
    public List<AbstractAttribute> attributesInfo = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param stream      输入流
     * @param poolList    常量池列表
     * @param fieldCount  字段数量
     */
    public FieldTable(InputStream stream, List<AbstractConstantPool> poolList, int fieldCount) {
        for (int i = 0; i < fieldCount; i++) {
            parseField(stream, poolList);
        }
    }

    /**
     * 解析单个字段
     *
     * @param inputStream 输入流
     * @param poolList    常量池列表
     */
    public void parseField(InputStream inputStream, List<AbstractConstantPool> poolList) {
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
     * 解析字段属性
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
}
