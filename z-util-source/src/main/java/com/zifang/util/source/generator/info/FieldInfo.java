package com.zifang.util.source.generator.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 字段信息封装类
 * <p>
 * 用于封装Java类的字段（Field）的完整元数据信息，
 * 包括字段类型、名称、修饰符、初始值等属性。
 * 支持字段的构建、比较、哈希计算和字符串表示。
 *
 * @author zifang
 * @version 1.0.0
 */
public class FieldInfo {

    private String type;
    private String name;
    private int[] modifiers = new int[]{};
    private String initializer = "null";
    private List<AnnotationInfo> annotations = new ArrayList<>();

    public FieldInfo() {
    }

    public FieldInfo(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public FieldInfo(String type, String name, int[] modifiers, String initializer) {
        this.type = type;
        this.name = name;
        this.modifiers = modifiers;
        this.initializer = initializer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @deprecated Use {@link #getName()} instead
     */
    @Deprecated
    public String getValue() {
        return name;
    }

    /**
     * @deprecated Use {@link #setName(String)} instead
     */
    @Deprecated
    public void setValue(String value) {
        this.name = value;
    }

    public int[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(int[] modifiers) {
        this.modifiers = modifiers;
    }

    public void setModifier(int... modifier) {
        this.modifiers = modifier;
    }

    public String getInitializer() {
        return initializer;
    }

    public void setInitializer(String initializer) {
        this.initializer = initializer;
    }

    public List<AnnotationInfo> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationInfo> annotations) {
        this.annotations = annotations;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FieldInfo other = (FieldInfo) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + java.util.Arrays.hashCode(modifiers);
        result = 31 * result + (initializer != null ? initializer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return type + " " + name + " = " + initializer + ";";
    }
}
