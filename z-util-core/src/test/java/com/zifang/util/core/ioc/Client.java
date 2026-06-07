package com.zifang.util.core.ioc;

import com.google.inject.Inject;

/**
 * Client类。
 */
public class Client {

    private Service service;

    /**
     * Client方法。
     */
    public Client() {
    }

    @Inject                                    //依赖注入必须的标注
    /**
     * Client方法。
     *      * @param service Service类型参数
     */
    public Client(Service service) {
        System.out.println("This is constructor injector...");
        this.service = service;
    }

//	第二种，set注入方式
//	@Inject
//	public void setClient(Service service){
//		System.out.println("This is set injector...");
//		this.service = service;
//	}

    /**
     * sayHello方法。
     */
    public void sayHello() {
        service.sayHello();
    }

}