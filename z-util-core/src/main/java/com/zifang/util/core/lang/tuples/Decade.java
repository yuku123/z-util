package com.zifang.util.core.lang.tuples;

/**
 * @author zifang
 */
public class Decade<A, B, C, D, E, F, G, H, I, J> extends Ennead<A, B, C, D, E, F, G, H, I> {
    protected J j;

    public Decade(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j) {
        super(a, b, c, d, e, f, g, h, i);
        this.j = j;
    }

    public J getJ() {
        return j;
    }

    public void setJ(J j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "Decade{a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + ", g=" + g + ", h=" + h + ", i=" + i + ", j=" + j + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Decade<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> decade = (Decade<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
        return java.util.Objects.equals(j, decade.j);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), j);
    }
}