package com.zifang.util.source;

import com.zifang.util.source.compiler.CustomerCompileClassLoader;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * CustomerCompileClassLoader 测试
 */
public class CustomerCompileClassLoaderTest {

    @Test
    public void testClassExtension() {
        // 验证类文件扩展名
        assertEquals(".class", CustomerCompileClassLoader.CLASS_EXTENSION);
    }

    @Test
    public void testConstructor() {
        CustomerCompileClassLoader classLoader = new CustomerCompileClassLoader(Thread.currentThread().getContextClassLoader());
        assertNotNull(classLoader);
    }

    @Test
    public void testGetResourceAsStream() {
        CustomerCompileClassLoader classLoader = new CustomerCompileClassLoader(Thread.currentThread().getContextClassLoader());

        // 尝试获取一个系统类作为测试
        InputStream is = classLoader.getResourceAsStream("java/lang/String.class");
        assertNotNull(is);
    }
}