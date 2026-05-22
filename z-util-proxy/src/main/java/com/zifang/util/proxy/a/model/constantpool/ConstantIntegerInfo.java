package com.zifang.util.proxy.a.model.constantpool;

import com.zifang.util.proxy.a.model.readtype.U4;

import java.io.InputStream;

/**
 * 整数常量
 * <p>
 * CONSTANT_Integer_info用于表示4字节的int类型常量值。
 */
public class ConstantIntegerInfo extends AbstractConstantPool {

    private U4 bytes;

    public ConstantIntegerInfo(byte tag) {
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

    @Override
    public String toString() {
        return "ConstantIntegerInfo{" +
                "value=" + bytes.value +
                '}';
    }
}
