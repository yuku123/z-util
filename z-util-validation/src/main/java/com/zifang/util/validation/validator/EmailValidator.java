package com.zifang.util.validation.validator;

import com.zifang.util.validation.annotation.Email;
import com.zifang.util.validation.core.ValidateResult;
import com.zifang.util.validation.core.Validator;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * 邮箱格式校验器
 */
public class EmailValidator implements Validator<Email> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    @Override
    public void validate(Object target, Field field, Email annotation, ValidateResult result) {
        Object value;
        try {
            field.setAccessible(true);
            value = field.get(target);
        } catch (IllegalAccessException e) {
            result.addError(field.getName(), annotation.message());
            return;
        }

        if (value == null) {
            return; // null值由NotNull校验
        }

        if (!EMAIL_PATTERN.matcher(value.toString()).matches()) {
            result.addError(field.getName(), annotation.message());
        }
    }
}