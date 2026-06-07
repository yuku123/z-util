package com.zifang.util.http.parser.curl;

/**
 * cURL 解析异常
 * <p>
 * 当 cURL 命令解析过程中发生错误时抛出此异常，例如命令格式错误、缺少必要参数等。
 * </p>
 *
 * @author zifang
 * @see RuntimeException
 */
/**
 * CurlParseException类。
 */
/**
 * CurlParseException类。
 */
public class CurlParseException extends RuntimeException {

    /**
     * 构造一个带指定消息的解析异常。
     *
     * @param message 异常消息
     */
    /**
     * CurlParseException方法。
     *      * @param message String类型参数
     */
    /**
     * CurlParseException方法。
     *      * @param message String类型参数
     */
    public CurlParseException(String message) {
        super(message);
    }

    /**
     * 构造一个带指定消息和根因的解析异常。
     *
     * @param message 异常消息
     * @param cause   根因异常
     */
    /**
     * CurlParseException方法。
     *      * @param message String类型参数
     * @param cause Throwable类型参数
     */
    /**
     * CurlParseException方法。
     *      * @param message String类型参数
     * @param cause Throwable类型参数
     */
    public CurlParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
