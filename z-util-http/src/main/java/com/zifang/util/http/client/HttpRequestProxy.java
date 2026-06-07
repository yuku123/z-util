package com.zifang.util.http.client;

import com.zifang.util.proxy.ProxyUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求代理工厂类。
 * <p>
 * 该类提供静态方法用于创建HTTP请求接口的动态代理对象。
 * 通过代理对象调用方法时，会自动将方法调用转换为HTTP请求。
 * </p>
 *
 * @author zifang
 * @see HttpRequestInvocationHandler
 */
/**
 * HttpRequestProxy类。
 */
/**
 * HttpRequestProxy类。
 */
public class HttpRequestProxy {

    /**
     * 创建HTTP请求接口的代理对象，使用默认空上下文参数。
     *
     * @param requestInterface HTTP请求接口类，必须带有 {@link RestController} 注解
     * @param <T>              请求接口的类型
     * @return 请求接口的代理对象
     * @throws IllegalArgumentException 如果 requestInterface 为 null
     */
    /**
     * proxy方法。
     *      * @param requestInterface ClassT类型参数
     * @return static <T> T类型返回值
     */
    /**
     * proxy方法。
     *      * @param requestInterface ClassT类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T proxy(Class<T> requestInterface) {
        return proxy(requestInterface, new HashMap<>());
    }

    /**
     * 创建HTTP请求接口的代理对象，使用指定的上下文参数。
     *
     * @param requestInterface HTTP请求接口类，必须带有 {@link RestController} 注解
     * @param contextParams    上下文参数映射，用于在请求过程中传递额外数据（如header、cookie等）
     * @param <T>              请求接口的类型
     * @return 请求接口的代理对象
     * @throws IllegalArgumentException 如果 requestInterface 为 null
     */
    /**
     * proxy方法。
     *      * @param requestInterface ClassT类型参数
     * @param contextParams MapString,Object类型参数
     * @return static <T> T类型返回值
     */
    /**
     * proxy方法。
     *      * @param requestInterface ClassT类型参数
     * @param contextParams MapString,Object类型参数
     * @return static <T> T类型返回值
     */
    public static <T> T proxy(Class<T> requestInterface, Map<String,Object> contextParams) {
        HttpRequestInvocationHandler httpRequestInvocationHandler = new HttpRequestInvocationHandler(requestInterface);
        httpRequestInvocationHandler.setContextParams(contextParams);
        return ProxyUtil.newProxyInstance(httpRequestInvocationHandler, requestInterface);
    }
}
