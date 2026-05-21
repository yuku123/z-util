package com.zifang.util.proxy.a.resolver2.constantpool;

import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;

/**
 * 方法类型信息
 * <p>
 * CONSTANT_MethodType_info用于表示方法类型的符号引用。
 * descriptor_index指向CONSTANT_Utf8_info常量的索引。
 */
public class ConstantMethodTypeInfo extends AbstractConstantPool {

    private U2 descriptorIndex;


    public ConstantMethodTypeInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.descriptorIndex = U2.read(inputStream);
    }

    public U2 getDescriptorIndex() {
        return descriptorIndex;
    }
}
