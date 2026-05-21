package com.zifang.util.proxy.a.resolver2.constantpool;

import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;

/**
 * 动态调用信息
 * <p>
 * CONSTANT_InvokeDynamic_info用于表示invokedynamic指令的动态调用点。
 * 包含引导方法索引和方法名称类型索引。
 */
public class ConstantInvokeDynamicInfo extends AbstractConstantPool {

    private U2 bootstrapMethodAttrIndex;
    private U2 nameIndex;

    public ConstantInvokeDynamicInfo(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        this.bootstrapMethodAttrIndex = U2.read(inputStream);
        this.nameIndex = U2.read(inputStream);
    }

    public U2 getBootstrapMethodAttrIndex() {
        return bootstrapMethodAttrIndex;
    }

    public U2 getNameIndex() {
        return nameIndex;
    }
}
