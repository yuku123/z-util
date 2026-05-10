package com.zifang.util.core.lang.tuples;

public class Septet<A, B, C, D, E, F, G> extends Sextet<A, B, C, D, E, F> {
    protected G g;

    public Septet(A a, B b, C c, D d, E e, F f, G g) {
        super(a, b, c, d, e, f);
        this.g = g;
    }

    public G getG() {
        return g;
    }

    public void setG(G g) {
        this.g = g;
    }

    @Override
    public String toString() {
        return "Septet{a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + ", g=" + g + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Septet<?, ?, ?, ?, ?, ?, ?> septet = (Septet<?, ?, ?, ?, ?, ?, ?>) o;
        return java.util.Objects.equals(g, septet.g);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), g);
    }
}