package com.zifang.util.core.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * GuiceMain类。
 */
public class GuiceMain {

    /**
     * main方法。
     *      * @param args String[]类型参数
     * @return static void类型返回值
     */
    public static void main(String[] args) {

        Service service = new ServiceImp();
//		Service service = new MockService();  // 第二个service的实现

        Client client = new Client(service);    //构造器注入

        Injector injector = Guice.createInjector(new TestModule());//向Guice要注射器
        injector.injectMembers(client);
        client.sayHello();
    }
}