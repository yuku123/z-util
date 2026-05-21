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
public class NoPrintBusinessException extends BaseException {

    private static final long serialVersionUID = 2519740137508800641L;

    public NoPrintBusinessException(StatusCode statusCode, Throwable e) {
        super(statusCode, e);
    }

    public NoPrintBusinessException(StatusCode statusCode) {
        super(statusCode);
    }

    public NoPrintBusinessException(StatusCode statusCode, Object... params) {
        super(statusCode, params);
    }

}
