package com.zifang.util.zex.bust.ex;

/**
 * Java编程知识整理类。
 * <p>
 * 此类整理了Java编程中的各类知识点。
 * 包含基础语法、并发、IO、设计模式等内容。
 *
 * @author zifang
 * @version 1.0
 */
class CloneField implements Cloneable {

    private Long l = System.currentTimeMillis();

}
public class CloneableTest implements Cloneable {

    public String name;

    public CloneField field = new CloneField();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        CloneableTest cloneableTest = new CloneableTest();
        cloneableTest.name = "吃饭";

        CloneableTest cloneableTest1 = (CloneableTest) cloneableTest.clone();
        System.out.println(cloneableTest1.name);
        System.out.println(cloneableTest.field == cloneableTest1.field);

    }
}

