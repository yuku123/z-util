package com.zifang.util.proxy.a.resolver2.constantpool;

import com.zifang.util.proxy.a.resolver2.readtype.U1;
import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;

/**
 * 方法句柄信息
 * <p>
 * CONSTANT_MethodHandle_info用于表示方法句柄，
 * 是JVM用于实现invokedynamic指令的关键数据结构。
 */
public class ConstantMethodHandleInfo extends AbstractConstantPool {
    private U1 referenceKind;
    private U2 referenceIndex;


    public ConstantMethodHandleInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.referenceKind = U1.read(inputStream);
        this.referenceIndex = U2.read(inputStream);
    }

    public U1 getReferenceKind() {
        return referenceKind;
    }

    public U2 getReferenceIndex() {
        return referenceIndex;
    }
}
