package com.zifang.util.proxy.a.resolver2.attribute;

import com.zifang.util.proxy.a.resolver2.readtype.U2;
import com.zifang.util.proxy.a.resolver2.readtype.U4;

import java.io.InputStream;

/**
 * 已弃用属性
 * <p>
 * Deprecated_attribute用于标记类、接口、字段或方法已被弃用。
 * 用于编译时警告和运行时警告。
 */
public class Deprecated extends AbstractAttribute {


    public Deprecated(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }
}
