package com.zifang.util.http.base.define;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteMapping {
    String value();
}
