package com.zifang.util.proxy.a.model.constantpool;

import com.zifang.util.proxy.a.model.readtype.U2;

import java.io.InputStream;

/**
 * 类引用信息
 * <p>
 * CONSTANT_Class_info用于表示类或接口的完全限定名。
 * name_index指向常量池中CONSTANT_Utf8_info常量的索引。
 */
public class ClassInfo extends AbstractConstantPool {
    private U2 nameIndex;

    public ClassInfo(byte tag) {
        super(tag);
    }

    public void read(InputStream inputStream) {
        this.nameIndex = U2.read(inputStream);
    }

    public U2 getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(U2 nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "nameIndex=" + nameIndex.value +
                '}';
    }
}
