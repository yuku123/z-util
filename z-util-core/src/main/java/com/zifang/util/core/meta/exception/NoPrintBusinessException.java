package com.zifang.util.core.meta.exception;


import com.zifang.util.core.meta.StatusCode;

/**
 * 不打印堆栈的业务异常。
 * <p>
 * 与普通 BusinessException 不同，此类异常在记录日志时不会打印完整的堆栈信息，
 * 适用于已知业务流程中的预期异常，可减少日志噪音。
 *
 * @author zifang
 * @see BaseException
 * @see StatusCode
 */
/**
 * NoPrintBusinessException类。
 */
/**
 * NoPrintBusinessException类。
 */
public class NoPrintBusinessException extends BaseException {

    private static final long serialVersionUID = 2519740137508800641L;

    /**
     * NoPrintBusinessException方法。
     *      * @param statusCode StatusCode类型参数
     * @param e Throwable类型参数
     */
    /**
     * NoPrintBusinessException方法。
     *      * @param statusCode StatusCode类型参数
     * @param e Throwable类型参数
     */
    public NoPrintBusinessException(StatusCode statusCode, Throwable e) {
        super(statusCode, e);
    }

    /**
     * NoPrintBusinessException方法。
     *      * @param statusCode StatusCode类型参数
     */
    /**
     * NoPrintBusinessException方法。
     *      * @param statusCode StatusCode类型参数
     */
    public NoPrintBusinessException(StatusCode statusCode) {
        super(statusCode);
    }

    /**
     * NoPrintBusinessException方法。
     *      * @param statusCode StatusCode类型参数
     * @param params Object...类型参数
     */
    /**
     * NoPrintBusinessException方法。
     *      * @param statusCode StatusCode类型参数
     * @param params Object...类型参数
     */
    public NoPrintBusinessException(StatusCode statusCode, Object... params) {
        super(statusCode, params);
    }

}
