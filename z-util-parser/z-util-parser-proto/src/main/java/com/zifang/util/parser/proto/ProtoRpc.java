package com.zifang.util.parser.proto;

import java.util.Objects;

/**
 * Proto RPC 方法模型。
 *
 * @author zifang
 */
public class ProtoRpc {

    private final String name;
    private final String inputType;
    private final String outputType;

    public ProtoRpc(String name, String inputType, String outputType) {
        this.name = name;
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public String getName() {
        return name;
    }

    public String getInputType() {
        return inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtoRpc protoRpc = (ProtoRpc) o;
        return Objects.equals(name, protoRpc.name) &&
                Objects.equals(inputType, protoRpc.inputType) &&
                Objects.equals(outputType, protoRpc.outputType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, inputType, outputType);
    }

    @Override
    public String toString() {
        return "rpc " + name + "(" + inputType + ") returns (" + outputType + ");";
    }
}
