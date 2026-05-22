package com.zifang.util.proxy.a.decompile.adapter;

import com.zifang.util.proxy.a.decompile.bean.Class_info;
import com.zifang.util.proxy.a.decompile.bean.constant.Constant_Class_info;
import com.zifang.util.proxy.a.decompile.bean.constant.Constant_Utf8_info;
import com.zifang.util.proxy.a.decompile.bean.constant.Constant_X_info;
import com.zifang.util.proxy.a.resolver.ByteCodeResolver;
import com.zifang.util.proxy.a.model.ClassFile;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * ClassFileAdapter 测试类
 * <p>
 * 测试将 resolver2.ClassFile 转换为 decompile.bean.Class_info 的功能。
 */
public class ClassFileAdapterTest {

    /**
     * 测试基本转换功能
     */
    @Test
    public void testAdaptBasic() {
        // 获取测试类的字节码
        InputStream is = getClass().getResourceAsStream("/testclass/TestClassParse1.class");
        assertNotNull("测试类字节码资源未找到", is);

        // 解析字节码
        ClassFile classFile = ByteCodeResolver.parseFromStream(is);

        // 转换为 Class_info
        Class_info classInfo = ClassFileAdapter.adapt(classFile);

        // 验证基本结构
        assertNotNull("Class_info 不应为 null", classInfo);
        assertNotNull("Magic 不应为 null", classInfo.getMagic());
        assertTrue("Magic 应为 0xCAFEBABE", classInfo.getMagic().contains("CAFEBABE"));
        assertNotNull("常量池不应为 null", classInfo.getConstant_pool_Map());
        assertTrue("常量池应有内容", classInfo.getConstant_pool_Map().size() > 0);

        try {
            is.close();
        } catch (Exception e) {
            // 忽略
        }
    }

    /**
     * 测试常量池转换
     */
    @Test
    public void testConstantPoolConversion() {
        InputStream is = getClass().getResourceAsStream("/testclass/TestClassParse1.class");
        assertNotNull("测试类字节码资源未找到", is);

        ClassFile classFile = ByteCodeResolver.parseFromStream(is);
        Class_info classInfo = ClassFileAdapter.adapt(classFile);

        // 验证常量池项
        Map<Integer, Constant_X_info> poolMap = classInfo.getConstant_pool_Map();
        boolean foundUtf8 = false;
        boolean foundClass = false;

        for (Constant_X_info constant : poolMap.values()) {
            String type = constant.getConstant_pool_info_Type();
            if ("Constant_Utf8_info".equals(type)) {
                foundUtf8 = true;
                assertNotNull("UTF8 info bytes 不应为 null", 
                    ((Constant_Utf8_info) constant).getBytes());
            }
            if ("Constant_Class_info".equals(type)) {
                foundClass = true;
                assertNotNull("Class info index 不应为 null", 
                    ((Constant_Class_info) constant).getIndex() > 0);
            }
        }

        assertTrue("常量池应包含 Utf8_info", foundUtf8);
        assertTrue("常量池应包含 Class_info", foundClass);

        try {
            is.close();
        } catch (Exception e) {
            // 忽略
        }
    }

    /**
     * 测试类名解析
     */
    @Test
    public void testClassNameParsing() {
        InputStream is = getClass().getResourceAsStream("/testclass/TestClassParse1.class");
        assertNotNull("测试类字节码资源未找到", is);

        ClassFile classFile = ByteCodeResolver.parseFromStream(is);
        Class_info classInfo = ClassFileAdapter.adapt(classFile);

        // 通过常量池获取类名
        int thisClassIndex = classInfo.getThis_class_index() - 1;
        Constant_X_info classConstant = classInfo.getConstant_pool_Map().get(thisClassIndex);
        assertNotNull("This class constant 不应为 null", classConstant);
        assertTrue("This class 应为 Constant_Class_info", 
            classConstant instanceof Constant_Class_info);

        int nameIndex = ((Constant_Class_info) classConstant).getIndex() - 1;
        Constant_X_info nameConstant = classInfo.getConstant_pool_Map().get(nameIndex);
        assertNotNull("类名常量不应为 null", nameConstant);
        assertTrue("类名应为 Constant_Utf8_info", 
            nameConstant instanceof Constant_Utf8_info);

        String className = ((Constant_Utf8_info) nameConstant).getBytes();
        assertTrue("类名应包含 TestClassParse1", className.contains("TestClassParse1"));

        try {
            is.close();
        } catch (Exception e) {
            // 忽略
        }
    }
}