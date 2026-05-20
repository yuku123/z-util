package com.zifang.util.http.base.define;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.POST)
public @interface PostMapping {
    String[] values() default {};
    String value() default "";
}
