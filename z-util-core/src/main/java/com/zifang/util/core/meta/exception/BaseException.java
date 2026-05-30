package com.zifang.util.core.meta.exception;

import com.zifang.util.core.meta.StatusCode;
import com.zifang.util.core.meta.Result;

import static com.zifang.util.core.meta.Result.buildMessage;


/**
 * 基础的异常类
 */
/**
 * BaseException类。
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 2059913032332171665L;

    /**
     * 错误
     */
    private final StatusCode statusCode;

    /**
     * 错误信息
     */
    private String message;

    /**
     * BaseException方法。
     *      * @param statusCode StatusCode类型参数
     */
    public BaseException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
        this.message = statusCode.getMessage();
    }

    /**
     * BaseException方法。
     *      * @param statusCode StatusCode类型参数
     * @param params Object...类型参数
     */
    public BaseException(StatusCode statusCode, Object... params) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
        String baseMessage = statusCode.getMessage();
        this.message = buildMessage(baseMessage, params);
    }

    /**
     * BaseException方法。
     *      * @param statusCode StatusCode类型参数
     * @param e Throwable类型参数
     */
    public BaseException(StatusCode statusCode, Throwable e) {
        super(statusCode.getMessage(), e);
        this.statusCode = statusCode;
        this.message = statusCode.getMessage();
    }

    /**
     * BaseException方法。
     *      * @param result Result?类型参数
     * @param params Object...类型参数
     */
    public BaseException(Result<?> result, Object... params) {
        this(new StatusCode() {
            @Override
    /**
     * getCode方法。
     * @return int类型返回值
     */
            public int getCode() {
                return result.getCode();
            }

            @Override
    /**
     * getMessage方法。
     * @return String类型返回值
     */
            public String getMessage() {
                return result.getMessage();
            }
        }, params);
    }

    /**
     * BaseException方法。
     *      * @param result Result?类型参数
     * @param e Throwable类型参数
     */
    public BaseException(Result<?> result, Throwable e) {
        this(new StatusCode() {
            @Override
    /**
     * getCode方法。
     * @return int类型返回值
     */
            public int getCode() {
                return result.getCode();
            }

            @Override
    /**
     * getMessage方法。
     * @return String类型返回值
     */
            public String getMessage() {
                return result.getMessage();
            }
        }, e);
    }

    /**
     * BaseException方法。
     *      * @param result Result?类型参数
     */
    public BaseException(Result<?> result) {
        this(new StatusCode() {
            @Override
    /**
     * getCode方法。
     * @return int类型返回值
     */
            public int getCode() {
                return result.getCode();
            }

            @Override
    /**
     * getMessage方法。
     * @return String类型返回值
     */
            public String getMessage() {
                return result.getMessage();
            }
        });
    }

    /**
     * getCode方法。
     * @return int类型返回值
     */
    public int getCode() {
        return statusCode.getCode();
    }

    @Override
    /**
     * getMessage方法。
     * @return String类型返回值
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * getStatusCode方法。
     * @return StatusCode类型返回值
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return String
                .format("BaseException[status:%s(%s),message:%s]", statusCode, statusCode.getCode(),
                        message);
    }
}
