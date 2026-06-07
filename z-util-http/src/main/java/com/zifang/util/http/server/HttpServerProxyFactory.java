package com.zifang.util.http.server;

import com.zifang.util.http.base.pojo.HttpRequestDefinition;

/**
 * HTTP 服务端代理工厂
 * 用于创建服务端代理实例
 *
 * @param <T> 服务接口类型
 */
/**
 * HttpServerProxyFactory类。
 */
/**
 * HttpServerProxyFactory类。
 */
public class HttpServerProxyFactory<T> {

    private final Class<T> interfaceClass;
    private final T target;
    private final HttpServerRequestHandler requestHandler;

    /**
     * HttpServerProxyFactory方法。
     *      * @param interfaceClass ClassT类型参数
     * @param target T类型参数
     */
    /**
     * HttpServerProxyFactory方法。
     *      * @param interfaceClass ClassT类型参数
     * @param target T类型参数
     */
    public HttpServerProxyFactory(Class<T> interfaceClass, T target) {
        this.interfaceClass = interfaceClass;
        this.target = target;
        this.requestHandler = new HttpServerRequestHandler(target);
    }

    /**
     * 处理 HTTP 请求
     *
     * @param requestDefinition HTTP 请求定义
     * @return 处理结果
     */
    /**
     * handleRequest方法。
     *      * @param requestDefinition HttpRequestDefinition类型参数
     * @return Object类型返回值
     */
    /**
     * handleRequest方法。
     *      * @param requestDefinition HttpRequestDefinition类型参数
     * @return Object类型返回值
     */
    public Object handleRequest(HttpRequestDefinition requestDefinition) {
        return requestHandler.handleRequest(requestDefinition);
    }

    /**
     * 获取服务接口类
     */
    /**
     * getInterfaceClass方法。
     * @return Class<T>类型返回值
     */
    /**
     * getInterfaceClass方法。
     * @return Class<T>类型返回值
     */
    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * 获取目标对象
     */
    /**
     * getTarget方法。
     * @return T类型返回值
     */
    /**
     * getTarget方法。
     * @return T类型返回值
     */
    public T getTarget() {
        return target;
    }

}
