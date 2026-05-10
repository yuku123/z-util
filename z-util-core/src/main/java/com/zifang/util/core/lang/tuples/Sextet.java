package com.zifang.util.core.lang.tuples;

public class Sextet<A, B, C, D, E, F> extends Quintet<A, B, C, D, E> {

    protected F f;

    public Sextet(A a, B b, C c, D d, E e, F f) {
        super(a, b, c, d, e);
        this.f = f;
    }

    public F getF() {
        return f;
    }

    public void setF(F f) {
        this.f = f;
    }

    @Override
    public String toString() {
        return "Sextet{a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sextet<?, ?, ?, ?, ?, ?> sextet = (Sextet<?, ?, ?, ?, ?, ?>) o;
        return java.util.Objects.equals(f, sextet.f);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), f);
    }
}