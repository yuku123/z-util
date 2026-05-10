package com.zifang.util.core.lang.tuples;

/**
 * @author zifang
 */
public class Octet<A, B, C, D, E, F, G, H> extends Septet<A, B, C, D, E, F, G> {

    protected H h;

    public Octet(A a, B b, C c, D d, E e, F f, G g, H h) {
        super(a, b, c, d, e, f, g);
        this.h = h;
    }

    public H getH() {
        return h;
    }

    public void setH(H h) {
        this.h = h;
    }

    @Override
    public String toString() {
        return "Octet{a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + ", g=" + g + ", h=" + h + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Octet<?, ?, ?, ?, ?, ?, ?, ?> octet = (Octet<?, ?, ?, ?, ?, ?, ?, ?>) o;
        return java.util.Objects.equals(h, octet.h);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), h);
    }
}