package com.zifang.util.core.lang.validator;


import com.zifang.util.core.lang.StringUtil;
import com.zifang.util.core.lang.exception.BaseException;
import com.zifang.util.core.lang.exception.BusinessException;
import com.zifang.util.core.lang.exception.ParamValidateStatusCode;
import com.zifang.util.core.meta.StatusCode;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Validator类。
 */
public class Validator {

    /**
     * 防御，为true成功
     *
     * @param isTrue 是否成功
     * @param msg    错误消息
     * @param param  入参
     */
    /**
     * defenseIfTrue方法。
     *      * @param isTrue boolean类型参数
     * @param msg String类型参数
     * @param param Object类型参数
     * @return static void类型返回值
     */
    public static void defenseIfTrue(boolean isTrue, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, isTrue, msg, param);
    }

    /**
     * 防御，为false成功
     *
     * @param isNotTrue 是否不成功
     * @param msg       错误消息
     * @param param     入参
     */
    /**
     * defenseIfNotTrue方法。
     *      * @param isNotTrue boolean类型参数
     * @param msg String类型参数
     * @param param Object类型参数
     * @return static void类型返回值
     */
    public static void defenseIfNotTrue(boolean isNotTrue, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, !isNotTrue, msg, param);
    }

    /**
     * 直接防御
     *
     * @param msg 错误消息
     */
    /**
     * defenseDirectly方法。
     *      * @param msg String类型参数
     * @return static void类型返回值
     */
    public static void defenseDirectly(String msg) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, true, msg, null);
    }


    /**
     * 入参不能为空 为空抛出参数错误异常
     *
     * @param parameter 入参
     * @param msg       错误信息
     */
    /**
     * requireNonNull方法。
     *      * @param parameter Object类型参数
     * @param msg String类型参数
     * @return static void类型返回值
     */
    public static void requireNonNull(Object parameter, String msg) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, parameter == null, msg, null);
    }

    /**
     * 使用谓词验证值，如果不满足谓词条件则抛出异常
     *
     * @param value     待验证的值
     * @param predicate 验证谓词，如果谓词测试结果为true则抛出异常
     * @param supplier  异常消息的提供者
     * @param <T>       值类型
     * @throws IllegalArgumentException 如果值不满足谓词条件
     */
    /**
     * validate方法。
     *      * @param value T类型参数
     * @param predicate PredicateT类型参数
     * @param supplier final类型参数
     * @return static <T> void类型返回值
     */
    public static <T> void validate(T value, Predicate<T> predicate, final Supplier<String> supplier) {
        if (predicate.test(value)) {
            throw new IllegalArgumentException(supplier.get());
        }
    }

    /**
     * 入参不能为空 为空抛出参数错误异常
     *
     * @param parameter 入参
     * @param msg       错误信息
     * @param param     嵌套了parameter的外层参数
     */
    /**
     * requireNonNull方法。
     *      * @param parameter Object类型参数
     * @param msg String类型参数
     * @param param Object类型参数
     * @return static void类型返回值
     */
    public static void requireNonNull(Object parameter, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, parameter == null, msg, param);
    }

    /**
     * 需要参数为空
     *
     * @param parameter
     * @param msg
     * @param param
     */
    /**
     * requireIsNull方法。
     *      * @param parameter Object类型参数
     * @param msg String类型参数
     * @param param Object类型参数
     * @return static void类型返回值
     */
    public static void requireIsNull(Object parameter, String msg, Object param) {
        defenseIfTrue(ParamValidateStatusCode.PARAMETER_ERROR, parameter != null, msg, param);
    }

    /**
     * 创建异常实例
     *
     * @param args 异常构造参数
     * @return 异常实例
     */
    BaseException newException(Object... args) {
        return null;
    }

    /**
     * 创建带根因异常的实例
     *
     * @param t    根因异常
     * @param args 异常构造参数
     * @return 异常实例
     */
    BaseException newException(Throwable t, Object... args) {
        return null;
    }

    /**
     * 验证对象非空，为空时抛出通过newException创建的异常
     *
     * @param obj 待验证的对象
     */
    /**
     * notNull方法。
     *      * @param obj Object类型参数
     */
    public void notNull(Object obj) {
        if (obj == null) {
            throw newException(obj);
        }
    }

    /**
     * 验证对象非空，为空时抛出通过newException创建的异常
     *
     * @param obj  待验证的对象
     * @param args 异常构造参数
     */
    /**
     * notNull方法。
     *      * @param obj Object类型参数
     * @param args Object...类型参数
     */
    public void notNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    /**
     * 验证对象非空，为空时抛出IllegalArgumentException
     *
     * @param object  待验证的对象
     * @param message 异常消息
     */
    /**
     * notNull方法。
     *      * @param object Object类型参数
     * @param message String类型参数
     * @return static void类型返回值
     */
    public static void notNull(Object object, String message) {
        if (Conditions.IS_NULL.test(object)) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * 防御，为true成功
     *
     * @param statusCode 错误状态码
     * @param isTrue     是否成功
     * @param msg        错误消息
     * @param param      入参
     */
    private static void defenseIfTrue(StatusCode statusCode, boolean isTrue, String msg, Object param) {
        if (!isTrue) {
            return;
        }
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(statusCode, msg);
        } else {
            throw new BusinessException(statusCode);
        }
    }
}
