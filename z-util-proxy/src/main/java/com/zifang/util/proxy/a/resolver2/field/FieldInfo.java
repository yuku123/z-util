package com.zifang.util.proxy.a.resolver2.field;

import com.zifang.util.proxy.a.resolver2.readtype.U2;

/**
 * 字段信息
 * <p>
 * 表示ClassFile中的字段表项，包含字段访问标志、名称索引、描述符索引和属性。
 */
import java.util.ArrayList;
import java.util.List;
public class FieldInfo {
    public U2 length;
    public List<FieldTable> list = new ArrayList();

    public FieldInfo(U2 length) {
        this.length = length;
    }
}
