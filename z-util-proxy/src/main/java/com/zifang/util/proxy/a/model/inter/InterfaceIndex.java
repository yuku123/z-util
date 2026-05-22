package com.zifang.util.proxy.a.model.inter;

import com.zifang.util.proxy.a.model.readtype.U2;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口索引表
 * <p>
 * 存储ClassFile中所有实现的接口，包含接口计数和接口列表。
 */
public class InterfaceIndex {
    public U2 length;
    public List<Interface> list = new ArrayList<>();

    public InterfaceIndex() {
    }

    public void addIndex(U2 index) {
        this.list.add(new Interface(index));
    }
}
