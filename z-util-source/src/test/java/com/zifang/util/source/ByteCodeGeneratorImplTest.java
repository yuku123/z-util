package com.zifang.util.source;

import com.zifang.util.source.generator.ByteCodeGeneratorImpl;
import com.zifang.util.source.generator.info.ClassInfo;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ByteCodeGeneratorImpl 测试
 */
public class ByteCodeGeneratorImplTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateNullClassInfo() {
        ByteCodeGeneratorImpl generator = new ByteCodeGeneratorImpl();
        generator.generate(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGenerateThrowsUnsupported() {
        ByteCodeGeneratorImpl generator = new ByteCodeGeneratorImpl();
        ClassInfo classInfo = new ClassInfo();
        classInfo.setSimpleClassName("Test");
        generator.generate(classInfo);
    }
}