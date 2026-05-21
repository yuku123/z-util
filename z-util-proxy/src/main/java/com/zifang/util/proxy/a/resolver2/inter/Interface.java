package com.zifang.util.proxy.a.resolver2.inter;

import com.zifang.util.proxy.a.resolver2.readtype.U2;

/**
 * 接口信息
 * <p>
 * 表示ClassFile中实现的单个接口的引用。
 */
public class Interface {
    public U2 index;

    public Interface(U2 index) {
        this.index = index;
    }
}
