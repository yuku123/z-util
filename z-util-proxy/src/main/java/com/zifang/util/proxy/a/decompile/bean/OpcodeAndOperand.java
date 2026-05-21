package com.zifang.util.proxy.a.decompile.bean;

/**
 * 操作码和操作数组合类
 * <p>
 * JVM字节码由操作码和操作数组成，
 * 用于存储单条字节码指令及其参数。
 */
public class OpcodeAndOperand {
    private String opcode;
    private Object operand;

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public Object getOperand() {
        return operand;
    }

    public void setOperand(Object operand) {
        this.operand = operand;
    }


}
