package com.zifang.util.monitor.jvm;

import java.util.*;

/**
 * Java内存泄露演示类。
 * <p>
 * 演示Java内存泄露的原因和常见场景，包括：
 * <ul>
 *   <li>静态集合类引起的内存泄露</li>
 *   <li>集合中的对象属性被修改后导致remove失效</li>
 *   <li>监听器未正确移除</li>
 *   <li>各种连接未正确关闭</li>
 *   <li>内部类和外部模块的引用</li>
 *   <li>单例模式持有外部对象引用</li>
 * </ul>
 *
 * @author zifang
 */
public class MemoryLeakDemo {

    /**
     * 1、静态集合类引起内存泄露：
     * 像HashMap、Vector等的使用最容易出现内存泄露，这些静态变量的生命周期和应用程序一致，他们所引用的所有的对象Object也不能被释放，
     * 因为他们也将一直被Vector等引用着。
     */
    static Vector v = new Vector(10);
    static List list = new ArrayList();

    /**
     * test1方法。
     *
     * @return static void类型返回值
     */
    public static void test1() {
        for (int i = 1; i < 100; i++) {
            Object o = new Integer(i);
            v.add(o);
            o = null;
            Person u = new Person("aa" + i, i);
            list.add(u);
            u.setUserName("bb" + i);
            u = null;
        }

        for (int i = 0; i < v.size(); i++) {
            System.out.println(v.get(i));
            System.out.println(list.get(i));
        }
    }

    /**
     * 2、当集合里面的对象属性被修改后，再调用remove（）方法时不起作用。
     */
    public static void test2() {
        Set<Person> set = new HashSet<Person>();
        Person p1 = new Person("唐僧", 25);
        Person p2 = new Person("孙悟空", 26);
        Person p3 = new Person("猪八戒", 27);
        set.add(p1);
        set.add(p2);
        set.add(p3);
        System.out.println("总共有:" + set.size() + " 个元素!"); // 结果：总共有:3 个元素!
        p3.setAge(2); // 修改p3的年龄,此时p3元素对应的hashcode值发生改变
        boolean flag = set.remove(p3); // 此时remove不掉，造成内存泄漏
        System.out.println("删除结果：" + flag);

        set.add(p3); // 重新添加，居然添加成功
        System.out.println("总共有:" + set.size() + " 个元素!"); // 结果：总共有:4 个元素!
        for (Person person : set) {
            System.out.println(person);
        }
    }

    /**
     * 3、监听器
     * 在java 编程中，我们都需要和监听器打交道，通常一个应用当中会用到很多监听器，我们会调用一个控件的诸如addXXXListener()等方法来增加监听器，
     * 但往往在释放对象的时候却没有记住去删除这些监听器，从而增加了内存泄漏的机会。
     */
    public static void main(String[] args) {
        test1();
        test2();
    }

    static class Person {
        private String userName;
        private int age;

        /**
         * Person方法。
         * * @param userName String类型参数
         *
         * @param age int类型参数
         */
        public Person(String userName, int age) {
            super();
            this.userName = userName;
            this.age = age;
        }

        /**
         * getUserName方法。
         *
         * @return String类型返回值
         */
        public String getUserName() {
            return userName;
        }

        /**
         * setUserName方法。
         * * @param userName String类型参数
         */
        public void setUserName(String userName) {
            this.userName = userName;
        }

        /**
         * getAge方法。
         *
         * @return int类型返回值
         */
        public int getAge() {
            return age;
        }

        /**
         * setAge方法。
         * * @param age int类型参数
         */
        public void setAge(int age) {
            this.age = age;
        }

        @Override
        /**
         * hashCode方法。
         * @return int类型返回值
         */
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + age;
            result = prime * result + ((userName == null) ? 0 : userName.hashCode());
            return result;
        }

        @Override
        /**
         * equals方法。
         *      * @param obj Object类型参数
         * @return boolean类型返回值
         */
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Person other = (Person) obj;
            if (age != other.age)
                return false;
            if (userName == null) {
                return other.userName == null;
            } else return userName.equals(other.userName);
        }

        @Override
        /**
         * toString方法。
         * @return String类型返回值
         */
        public String toString() {
            return "User [userName=" + userName + ", age=" + age + "]";
        }
    }
}
