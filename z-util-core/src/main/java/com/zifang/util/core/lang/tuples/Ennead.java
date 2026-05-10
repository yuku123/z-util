package com.zifang.util.core.lang.tuples;

/**
 * @author zifang
 */
public class Ennead<A, B, C, D, E, F, G, H, I> extends Octet<A, B, C, D, E, F, G, H> {
    protected I i;

    public Ennead(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
        super(a, b, c, d, e, f, g, h);
        this.i = i;
    }

    public I getI() {
        return i;
    }

    public void setI(I i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "Ennead{a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ", e=" + e + ", f=" + f + ", g=" + g + ", h=" + h + ", i=" + i + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ennead<?, ?, ?, ?, ?, ?, ?, ?, ?> ennead = (Ennead<?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
        return java.util.Objects.equals(i, ennead.i);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), i);
    }
}