package com.zifang.util.http.base.define;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PutMapping {
    String value();
}
