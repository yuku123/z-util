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
import org.junit.Test;

import java.io.*;

public class SerializableTest {

    @Test
    public void test0() throws IOException, ClassNotFoundException {
        //初始化一个User实例，并填充一部分数据
        User user = new User();
        user.setName("aa");
        user.setAge(23);
        System.out.println(user);

        //使用ObjectOutputStream开始写入一个文件
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempFile"));
        oos.writeObject(user);
        oos.close();

        //从文件内反序列化
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("tempFile")));
        User newUser = (User) ois.readObject();
        System.out.println(newUser);
    }
}

class User implements Serializable {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{name=" + name + ", age=" + age + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age && java.util.Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, age);
    }
}
