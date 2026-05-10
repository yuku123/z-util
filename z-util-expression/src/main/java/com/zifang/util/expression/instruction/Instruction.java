package com.zifang.util.expression.instruction;

public class Instruction {

    private String instructionCode;

    private Object[] params;

    public String getInstructionCode() {
        return instructionCode;
    }

    public void setInstructionCode(String instructionCode) {
        this.instructionCode = instructionCode;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Instruction{instructionCode=" + instructionCode + ", params=" + java.util.Arrays.toString(params) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instruction that = (Instruction) o;
        return java.util.Objects.equals(instructionCode, that.instructionCode) && java.util.Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = java.util.Objects.hash(instructionCode);
        result = 31 * result + java.util.Arrays.hashCode(params);
        return result;
    }

    public static Instruction of(String instructionCode, Object[] params) {
        Instruction instruction = new Instruction();
        instruction.setInstructionCode(instructionCode);
        instruction.setParams(params);
        return instruction;
    }
}
