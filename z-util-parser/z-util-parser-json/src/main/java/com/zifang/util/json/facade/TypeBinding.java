package com.zifang.util.json.facade;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类型引用，用于在运行时捕获泛型类型信息。
 * <p>
 * 使用示例：
 * <pre>
 * TypeBinding&lt;List&lt;User&gt;&gt; binding = new TypeBinding&lt;List&lt;User&gt;&gt;() {};
 * List&lt;User&gt; users = facade.fromJson(json, binding);
 * </pre>
 */
public abstract class TypeBinding<T> {

    private final Type type;

    protected TypeBinding() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        } else {
            this.type = Object.class;
        }
    }

    public Type getType() {
        return type;
    }
}