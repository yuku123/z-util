package com.zifang.util.db.define;

import java.lang.annotation.*;

/**
 * SQL参数注解
 * 用于标识方法参数在SQL语句中的绑定名称
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String value();
}
