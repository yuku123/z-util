package com.zifang.util.core.pattern.state;

/**
 * 状态码
 */
public class State {

    private String code;

    private String name;

    public State() {
    }

    public State(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "State{code=" + code + ", name=" + name + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return java.util.Objects.equals(code, state.code) &&
                java.util.Objects.equals(name, state.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(code, name);
    }
}