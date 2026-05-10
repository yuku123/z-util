package com.zifang.util.compile.sorce2.generator.info;

public class FieldInfo {

    private String type;
    private String value;
    private int[] modifiers = new int[]{};
    private String initializer = "null";

    public FieldInfo() {
    }

    public FieldInfo(String type, String value) {
        this.type = type;
        this.value = value;
        this.modifiers = new int[]{};
        this.initializer = "null";
    }

    public FieldInfo(String type, String value, int[] modifiers, String initializer) {
        this.type = type;
        this.value = value;
        this.modifiers = modifiers;
        this.initializer = initializer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int[] getModifiers() {
        return modifiers;
    }

    public void setModifiers(int[] modifiers) {
        this.modifiers = modifiers;
    }

    public String getInitializer() {
        return initializer;
    }

    public void setInitializer(String initializer) {
        this.initializer = initializer;
    }

    public void setModifier(int... modifier) {
        modifiers = modifier;
    }

    @Override
    public boolean equals(Object obj) {
        return value.equals(((FieldInfo) obj).value);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (modifiers != null ? java.util.Arrays.hashCode(modifiers) : 0);
        result = 31 * result + (initializer != null ? initializer.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return type + " " + value + " = " + initializer + ";";
    }
}
