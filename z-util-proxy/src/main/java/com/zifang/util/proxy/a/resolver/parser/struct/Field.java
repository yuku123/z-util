package com.zifang.util.proxy.a.resolver.parser.struct;

import java.util.Objects;

/**
 * @author zifang
 */
public class Field {

    int accessFlag;

    int nameIndex;

    int descriptorIndex;

    int attributesCount;

    public int getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(int accessFlag) {
        this.accessFlag = accessFlag;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public void setNameIndex(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public int getDescriptorIndex() {
        return descriptorIndex;
    }

    public void setDescriptorIndex(int descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    public int getAttributesCount() {
        return attributesCount;
    }

    public void setAttributesCount(int attributesCount) {
        this.attributesCount = attributesCount;
    }

    private String getString(int nameIndex) {
        return null;
    }

    public Field(int accessFlag, int nameIndex, int descriptorIndex, int attributesCount) {
        super();
        this.accessFlag = accessFlag;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributesCount = attributesCount;
    }

}
