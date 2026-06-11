package com.zifang.util.core.meta.exception;


import com.zifang.util.core.meta.StatusCode;

/**
 * 业务异常
 *
 * @author zifang
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1646453246258984129L;

    /**
     * BusinessException方法。
     *      * @param statusCode StatusCode类型参数
     * @param e Throwable类型参数
     */
    public BusinessException(StatusCode statusCode, Throwable e) {
        super(statusCode, e);
    }

    /**
     * BusinessException方法。
     *      * @param statusCode StatusCode类型参数
     */
    public BusinessException(StatusCode statusCode) {
        super(statusCode);
    }

    /**
     * BusinessException方法。
     *      * @param statusCode StatusCode类型参数
     * @param params Object...类型参数
     */
    public BusinessException(StatusCode statusCode, Object... params) {
        super(statusCode, params);
    }
}
