package com.zifang.util.core.lang.tuples;

/**
 * 六元组，包含6个元素的不可变元组。
 *
 * @author zifang
 * @param <A> 第一个元素类型
 * @param <B> 第二个元素类型
 * @param <C> 第三个元素类型
 * @param <D> 第四个元素类型
 * @param <E> 第五个元素类型
 * @param <F> 第六个元素类型
 * @see Quintet
 */
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