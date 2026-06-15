package com.zifang.util.core.ioc;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * TestModule类。
 */
public class TestModule implements Module {

    /* (non-Javadoc)
     * @see com.google.inject.Module#configure(com.google.inject.Binder)
     */

    /**
     * configure方法。
     * * @param binder Binder类型参数
     */
    public void configure(Binder binder) {
        binder.bind(Service.class).to(ServiceImp.class);
    }

}