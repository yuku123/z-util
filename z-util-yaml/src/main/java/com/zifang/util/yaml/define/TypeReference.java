package com.zifang.util.yaml.define;

/**
 * 类型引用，用于在运行时保留泛型类型信息。
 * <p>
 * 类似 Gson's TypeToken，解决 Java 泛型擦除问题。
 *
 * @param <T> 目标类型
 * @author zifang
 */
public class TypeReference<T> {

    private final Class<? super T> rawType;
    private final java.lang.reflect.Type type;

    @SuppressWarnings("unchecked")
    protected TypeReference() {
        java.lang.reflect.Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType parameterized = (java.lang.reflect.ParameterizedType) superclass;
            type = parameterized.getActualTypeArguments()[0];
            rawType = (Class<? super T>) ((java.lang.reflect.ParameterizedType) type).getRawType();
        } else {
            type = Object.class;
            rawType = (Class<? super T>) Object.class;
        }
    }

    public Class<? super T> getRawType() {
        return rawType;
    }

    public java.lang.reflect.Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TypeReference{" +
                "type=" + type +
                ", rawType=" + rawType +
                '}';
    }
}
