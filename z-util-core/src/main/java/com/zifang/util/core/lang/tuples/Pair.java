package com.zifang.util.core.lang.tuples;

/**
 * @author zifang
 */
public class Pair<A, B> extends Unit<A> {
    protected B b;

    public Pair(A a, B b) {
        super(a);
        this.b = b;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return a + ":" + b;
    }

    @Override
    public boolean equals(Object o) {
        if (this.toString().equals(o.toString())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(a, b);
    }
}