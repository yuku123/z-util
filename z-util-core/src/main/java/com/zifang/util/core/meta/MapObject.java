package com.zifang.util.core.meta;

import java.util.HashMap;
import java.util.Optional;

/**
 * 泛型 Map 对象，提供便捷的类型转换取值方法。
 * <p>
 * 继承自 HashMap<String, Object>，内部存储 Object 类型值，
 * 提供类型安全的 getString、getLong、getInteger 等方法，
 * 避免手动类型转换和 NullPointerException。
 *
 * @author zifang
 * @see HashMap
 */
public class MapObject extends HashMap<String, Object> {

    private static final long serialVersionUID = 5070417008655524317L;

    public String getString(String key) {
        return Optional.ofNullable(get(key)).map(Object::toString).orElse(null);
    }

    public Long getLong(String key) {
        return Optional.ofNullable(get(key)).map(value -> Long.parseLong(value.toString()))
                .orElse(null);
    }

    public Integer getInteger(String key) {
        return Optional.ofNullable(get(key)).map(value -> Integer.parseInt(value.toString()))
                .orElse(null);
    }

    public Short getShort(String key) {
        return Optional.ofNullable(get(key)).map(value -> Short.parseShort(value.toString()))
                .orElse(null);
    }

    public Byte getByte(String key) {
        return Optional.ofNullable(get(key)).map(value -> Byte.parseByte(value.toString()))
                .orElse(null);
    }

    public Double getDouble(String key) {
        return Optional.ofNullable(get(key)).map(value -> Double.parseDouble(value.toString()))
                .orElse(null);
    }

    public Float getFloat(String key) {
        return Optional.ofNullable(get(key)).map(value -> Float.parseFloat(value.toString()))
                .orElse(null);
    }

    public <T> T getObject(String key) {
        return (T) Optional.ofNullable(get(key)).orElse(null);
    }

}
