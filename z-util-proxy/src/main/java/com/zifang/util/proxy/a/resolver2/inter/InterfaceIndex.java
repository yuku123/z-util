package com.zifang.util.proxy.a.resolver2.inter;


import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口索引信息
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
