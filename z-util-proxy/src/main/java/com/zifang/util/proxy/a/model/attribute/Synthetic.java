package com.zifang.util.proxy.a.model.attribute;

import com.zifang.util.proxy.a.model.readtype.U2;
import com.zifang.util.proxy.a.model.readtype.U4;

import java.io.InputStream;

/**
 * 合成属性
 * <p>
 * Synthetic_attribute表示类、字段或方法是由编译器生成的，
 * 不是源码中直接存在的。
 */
public class Synthetic extends AbstractAttribute {

    public Synthetic(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }
}
