/*
 * 文件名：UserBo.java
 * 版权：Copyright 2007-2019 zxiaofan.com. Co. Ltd. All Rights Reserved.
 * 描述： 用户业务对象类
 * 修改人：zxiaofan
 * 修改时间：2019年12月27日
 * 修改内容：新增
 */
package com.zifang.util.zex.guava.collect;

/**
 * 用户业务对象类。
 * <p>
 * 此类是用户数据的传输对象，包含用户的名称和年龄信息。
 *
 * @author zifang
 * @version 1.0
 */
public class UserBo {
    private String name;

    private Integer age;

    public UserBo() {
        super();
    }

    public UserBo(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
