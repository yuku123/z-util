package com.zifang.util.proxy.a.resolver2.constantpool;

import com.zifang.util.proxy.a.resolver2.readtype.U8;

import java.io.InputStream;

/**
 * 长整数常量
 * <p>
 * CONSTANT_Long_info用于表示8字节的long类型常量值。
 * 注意：Long和Double类型在常量池中占用两个索引位置。
 */
public class ConstantLongInfo extends AbstractConstantPool {
    private U8 bytes;


    public ConstantLongInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.bytes = U8.read(inputStream);
    }

    public U8 getBytes() {
        return bytes;
    }
}
