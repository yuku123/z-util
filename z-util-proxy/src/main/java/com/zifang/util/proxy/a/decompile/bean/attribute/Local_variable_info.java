package com.zifang.util.proxy.a.decompile.bean.attribute;

/**
 * 局部变量信息类
 * <p>
 * 描述方法的局部变量信息，包括开始PC、长度、名称索引、描述符索引和变量槽索引。
 */
public class Local_variable_info {
    private int start_pc;
    private int length;
    private int name_index;
    private int descriptor_index;
    private int index;


    public int getStart_pc() {
        return start_pc;
    }

    public void setStart_pc(int start_pc) {
        this.start_pc = start_pc;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getName_index() {
        return name_index;
    }

    public void setName_index(int name_index) {
        this.name_index = name_index;
    }

    public int getDescriptor_index() {
        return descriptor_index;
    }

    public void setDescriptor_index(int descriptor_index) {
        this.descriptor_index = descriptor_index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
