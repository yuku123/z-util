package com.zifang.util.http.base.define;

import java.lang.annotation.*;

/**
 * DELETE请求映射注解
 * <p>
 * 用于将HTTP DELETE请求映射到控制器的处理方法。
 * </p>
 *
 * @author zifang
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteMapping {

    /**
     * 请求的URL路径。
     *
     * @return URL路径
     */
    String value();
}
