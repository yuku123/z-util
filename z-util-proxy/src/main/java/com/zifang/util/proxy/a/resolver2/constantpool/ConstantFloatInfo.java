package com.zifang.util.proxy.a.resolver2.constantpool;

import com.zifang.util.proxy.a.resolver2.readtype.U4;

import java.io.InputStream;

/**
 * 浮点数常量
 * <p>
 * CONSTANT_Float_info用于表示4字节的float类型常量值。
 */
public class ConstantFloatInfo extends AbstractConstantPool {
    private U4 bytes;


    public ConstantFloatInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.bytes = U4.read(inputStream);
    }

    public U4 getBytes() {
        return bytes;
    }

    public void setBytes(U4 bytes) {
        this.bytes = bytes;
    }
}
