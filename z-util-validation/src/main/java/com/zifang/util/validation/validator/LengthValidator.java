package com.zifang.util.validation.validator;

import com.zifang.util.validation.annotation.Length;
import com.zifang.util.validation.core.ValidateResult;
import com.zifang.util.validation.core.Validator;

import java.lang.reflect.Field;

/**
 * 长度校验器
 */
public class LengthValidator implements Validator<Length> {

    @Override
    public void validate(Object target, Field field, Length annotation, ValidateResult result) {
        Object value;
        try {
            field.setAccessible(true);
            value = field.get(target);
        } catch (IllegalAccessException e) {
            result.addError(field.getName(), annotation.message());
            return;
        }

        if (value == null) {
            return; // null值由NotNull校验，这里只校验长度
        }

        String str = value.toString();
        int len = str.length();
        if (len < annotation.min() || len > annotation.max()) {
            result.addError(field.getName(), annotation.message());
        }
    }
}