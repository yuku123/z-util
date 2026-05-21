package com.zifang.util.core.compile;

/**
 * 自定义类加载器。
 * <p>
 * 支持从字节数组加载并定义类。
 *
 * @author zifang
 * @see ClassLoader
 */
public class CustomerClassLoader extends ClassLoader{

    public CustomerClassLoader(ClassLoader parent) {
        super(parent);
    }
    public Class<?> defineClass(String className, byte[] bytes) {

        try {
            return super.defineClass(className,bytes,0,bytes.length);
        } catch (Exception | LinkageError e){
            e.printStackTrace();
        }
        return null;
    }
}
