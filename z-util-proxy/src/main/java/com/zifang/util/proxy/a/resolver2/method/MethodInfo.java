package com.zifang.util.proxy.a.resolver2.method;

import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法信息
 * <p>
 * 表示ClassFile中的方法表项，包含方法访问标志、名称索引、描述符索引和属性。
 */

public class MethodInfo {

    public U2 length;

    public List<MethodTable> list = new ArrayList<MethodTable>();

    public MethodInfo(U2 length) {
        this.length = length;
    }
}
