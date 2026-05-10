package com.zifang.util.core.lang.tuples;

/**
 * @author zifang
 */
public class Quintet<A, B, C, D, E> extends Quartet<A, B, C, D> {
    protected E e;

    public Quintet(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        this.e = e;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "Quintet{a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Quintet<?, ?, ?, ?, ?> quintet = (Quintet<?, ?, ?, ?, ?>) o;
        return java.util.Objects.equals(e, quintet.e);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), e);
    }
}