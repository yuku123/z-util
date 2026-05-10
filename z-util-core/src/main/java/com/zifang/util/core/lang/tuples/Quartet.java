package com.zifang.util.core.lang.tuples;

/**
 * @author zifang
 */
public class Quartet<A, B, C, D> extends Triplet<A, B, C> {
    protected D d;

    public Quartet(A a, B b, C c, D d) {
        super(a, b, c);
        this.d = d;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "Quartet{a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Quartet<?, ?, ?, ?> quartet = (Quartet<?, ?, ?, ?>) o;
        return java.util.Objects.equals(d, quartet.d);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), d);
    }
}