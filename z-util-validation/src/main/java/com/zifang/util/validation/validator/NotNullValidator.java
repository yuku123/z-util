package com.zifang.util.validation.validator;

import com.zifang.util.validation.annotation.NotNull;
import com.zifang.util.validation.core.ValidateResult;
import com.zifang.util.validation.core.Validator;

import java.lang.reflect.Field;

/**
 * 非空校验器
 */
public class NotNullValidator implements Validator<NotNull> {

    @Override
    public void validate(Object target, Field field, NotNull annotation, ValidateResult result) {
        Object value;
        try {
            field.setAccessible(true);
            value = field.get(target);
        } catch (IllegalAccessException e) {
            result.addError(field.getName(), annotation.message());
            return;
        }

        if (value == null) {
            result.addError(field.getName(), annotation.message());
        }
    }
}