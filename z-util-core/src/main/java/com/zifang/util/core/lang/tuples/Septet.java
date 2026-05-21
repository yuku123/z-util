package com.zifang.util.core.lang.tuples;

/**
 * 七元组，包含7个元素的不可变元组。
 *
 * @author zifang
 * @param <A> 第一个元素类型
 * @param <B> 第二个元素类型
 * @param <C> 第三个元素类型
 * @param <D> 第四个元素类型
 * @param <E> 第五个元素类型
 * @param <F> 第六个元素类型
 * @param <G> 第七个元素类型
 * @see Sextet
 */
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