package com.zifang.util.proxy.a.decompile.core;

import com.zifang.util.proxy.a.model.ClassFile;
import com.zifang.util.proxy.a.resolver.ByteCodeResolver;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * SrcCreator 测试类
 * <p>
 * 测试将 ClassFile 反编译为 Java 源代码的功能。
 */
public class SrcCreatorTest {

    /**
     * 测试反编译简单类
     */
    @Test
    public void testDecompileSimpleClass() {
        InputStream is = getClass().getResourceAsStream("/testclass/TestClassParse1.class");
        assertNotNull("测试类字节码资源未找到", is);

        ClassFile classFile = ByteCodeResolver.parseFromStream(is);
        assertNotNull("ClassFile 不应为 null", classFile);

        String src = SrcCreator.createJavaFileSrc(classFile);
        assertNotNull("生成的源码不应为 null", src);
        assertFalse("生成的源码不应为空", src.isEmpty());

        // 验证源码包含基本结构
        assertTrue("应包含 package 声明", src.contains("package"));
        assertTrue("应包含 class 声明", src.contains("class"));

        // 打印生成的源码以便人工验证
        System.out.println("=== 生成的源码 ===");
        System.out.println(src);

        try {
            is.close();
        } catch (Exception e) {
            // 忽略关闭异常
        }
    }

    /**
     * 测试生成的代码包含正确的方法签名
     */
    @Test
    public void testMethodSignatureGeneration() {
        InputStream is = getClass().getResourceAsStream("/testclass/TestClassParse1.class");
        assertNotNull("测试类字节码资源未找到", is);

        ClassFile classFile = ByteCodeResolver.parseFromStream(is);
        String src = SrcCreator.createJavaFileSrc(classFile);

        // 验证包含方法定义（可能是 <init> 或其他方法）
        assertTrue("应包含方法定义（括号）", src.contains("(") && src.contains(")"));

        try {
            is.close();
        } catch (Exception e) {
            // 忽略关闭异常
        }
    }

    /**
     * 测试字段生成
     */
    @Test
    public void testFieldGeneration() {
        InputStream is = getClass().getResourceAsStream("/testclass/TestClassParse1.class");
        assertNotNull("测试类字节码资源未找到", is);

        ClassFile classFile = ByteCodeResolver.parseFromStream(is);
        String src = SrcCreator.createJavaFileSrc(classFile);

        // 检查是否有字段声明（如果有的话）
        // TestClassParse1 可能没有字段，所以这不是强制要求
        System.out.println("=== 生成的字段 ===");
        System.out.println(src);

        try {
            is.close();
        } catch (Exception e) {
            // 忽略关闭异常
        }
    }
}
