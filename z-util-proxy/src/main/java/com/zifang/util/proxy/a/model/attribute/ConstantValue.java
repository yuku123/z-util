package com.zifang.util.proxy.a.model.attribute;

import com.zifang.util.proxy.a.model.readtype.U2;
import com.zifang.util.proxy.a.model.readtype.U4;

import java.io.InputStream;

/**
 * 常量值属性类
 * <p>
 * CONSTANT_ConstantValue_attribute用于表示静态字段的常量值。
 */
public class ConstantValue extends AbstractAttribute {

    private U2 constantValueIndex;

    public ConstantValue(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }


    public U2 getConstantValueIndex() {
        return constantValueIndex;
    }

    public void setConstantValueIndex(U2 constantValueIndex) {
        this.constantValueIndex = constantValueIndex;
    }

    public void read(InputStream inputStream) {
        constantValueIndex = U2.read(inputStream);
    }


    @Override
    public String toString() {
        return "ConstantValue{constantValueIndex=" + constantValueIndex.getValue() + "}";
    }
}
