package com.zifang.util.parser.proto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Proto Enum 模型。
 *
 * @author zifang
 */
public class ProtoEnum {

    private final String name;
    private final Map<String, Integer> values;

    public ProtoEnum(String name) {
        this.name = name;
        this.values = new LinkedHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addValue(String enumName, int number) {
        values.put(enumName, number);
    }

    public Map<String, Integer> getValues() {
        return values;
    }

    public int getValue(String enumName) {
        Integer val = values.get(enumName);
        return val != null ? val : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtoEnum protoEnum = (ProtoEnum) o;
        return Objects.equals(name, protoEnum.name) &&
                Objects.equals(values, protoEnum.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, values);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("enum ").append(name).append(" { ");
        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("; ");
        }
        sb.append("}");
        return sb.toString();
    }
}
