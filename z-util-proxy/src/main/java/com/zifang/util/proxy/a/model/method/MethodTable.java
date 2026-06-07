package com.zifang.util.proxy.a.model.method;

import com.zifang.util.proxy.a.model.attribute.AbstractAttribute;
import com.zifang.util.proxy.a.model.attribute.AttributeFactory;
import com.zifang.util.proxy.a.model.constantpool.AbstractConstantPool;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法表
 * <p>
 * 存储ClassFile中所有的方法信息。
 */
/**
 * MethodTable类。
 */
/**
 * MethodTable类。
 */
public class MethodTable {
    private List<MethodInfo> methods = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param stream       输入流
     * @param poolList     常量池列表
     * @param methodCount  方法数量
     */
    /**
     * MethodTable方法。
     *      * @param stream InputStream类型参数
     * @param poolList ListAbstractConstantPool类型参数
     * @param methodCount int类型参数
     */
    /**
     * MethodTable方法。
     *      * @param stream InputStream类型参数
     * @param poolList ListAbstractConstantPool类型参数
     * @param methodCount int类型参数
     */
    public MethodTable(InputStream stream, List<AbstractConstantPool> poolList, int methodCount) {
        for (int i = 0; i < methodCount; i++) {
            methods.add(parseMethod(stream, poolList));
        }
    }

    /**
     * 解析单个方法
     *
     * @param inputStream 输入流
     * @param poolList    常量池列表
     * @return 解析后的方法信息
     */
    /**
     * parseMethod方法。
     *      * @param inputStream InputStream类型参数
     * @param poolList ListAbstractConstantPool类型参数
     * @return MethodInfo类型返回值
     */
    /**
     * parseMethod方法。
     *      * @param inputStream InputStream类型参数
     * @param poolList ListAbstractConstantPool类型参数
     * @return MethodInfo类型返回值
     */
    public MethodInfo parseMethod(InputStream inputStream, List<AbstractConstantPool> poolList) {
        com.zifang.util.proxy.a.model.readtype.U2 accessFlags = 
                com.zifang.util.proxy.a.model.readtype.U2.read(inputStream);
        com.zifang.util.proxy.a.model.readtype.U2 nameIndex = 
                com.zifang.util.proxy.a.model.readtype.U2.read(inputStream);
        com.zifang.util.proxy.a.model.readtype.U2 descriptorIndex = 
                com.zifang.util.proxy.a.model.readtype.U2.read(inputStream);
        com.zifang.util.proxy.a.model.readtype.U2 attributesCount = 
                com.zifang.util.proxy.a.model.readtype.U2.read(inputStream);

        List<AbstractAttribute> attributes = new ArrayList<>();
        short count = attributesCount.value;
        for (int i = 0; i < count; i++) {
            AbstractAttribute attribute = AttributeFactory.getAttributeTable(inputStream, poolList);
            if (attribute != null) {
                attributes.add(attribute);
            }
        }

        return new MethodInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes);
    }

    /**
     * 获取所有方法
     *
     * @return 方法列表
     */
    /**
     * getMethods方法。
     * @return List<MethodInfo>类型返回值
     */
    /**
     * getMethods方法。
     * @return List<MethodInfo>类型返回值
     */
    public List<MethodInfo> getMethods() {
        return methods;
    }

    /**
     * 获取方法数量
     *
     * @return 方法数量
     */
    /**
     * getMethodCount方法。
     * @return int类型返回值
     */
    /**
     * getMethodCount方法。
     * @return int类型返回值
     */
    public int getMethodCount() {
        return methods.size();
    }

    /**
     * 根据索引获取方法
     *
     * @param index 方法索引
     * @return 方法信息
     */
    /**
     * getMethod方法。
     *      * @param index int类型参数
     * @return MethodInfo类型返回值
     */
    /**
     * getMethod方法。
     *      * @param index int类型参数
     * @return MethodInfo类型返回值
     */
    public MethodInfo getMethod(int index) {
        if (index >= 0 && index < methods.size()) {
            return methods.get(index);
        }
        return null;
    }
}