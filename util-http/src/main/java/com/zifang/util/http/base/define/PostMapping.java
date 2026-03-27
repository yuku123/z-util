package com.zifang.util.http.base.define;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = RequestMethod.GET)
public @interface PostMapping {
    String[] values() default {};
    String value() default "";

}
