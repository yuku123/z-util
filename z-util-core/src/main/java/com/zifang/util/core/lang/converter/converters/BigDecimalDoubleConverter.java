package com.zifang.util.core.lang.converter.converters;

import com.zifang.util.core.lang.converter.IConverter;

import java.math.BigDecimal;

/**
 * BigDecimal 到 Double 的转换器实现。
 *
 * @author zifang
 * @see IConverter
 */
public class BigDecimalDoubleConverter implements IConverter<BigDecimal, Double> {

    @Override
    public Double to(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.doubleValue();
    }

    public Double to(BigDecimal value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.doubleValue();
    }
}