package com.zifang.util.core.lang.tuples;

/**
 * @author zifang
 */
public class Triplet<A, B, C> extends Pair<A, B> {

    protected C c;

    public Triplet(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Triplet{a=" + a + ", b=" + b + ", c=" + c + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return java.util.Objects.equals(c, triplet.c);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), c);
    }
}