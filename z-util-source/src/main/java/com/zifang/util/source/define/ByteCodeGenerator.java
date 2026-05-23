package com.zifang.util.source.define;

import com.zifang.util.source.generator.info.ClassInfo;

/**
 * 字节码生成器接口
 */
public interface ByteCodeGenerator {

    /**
     * 根据类信息生成字节码
     *
     * @param classInfo 类信息
     * @return 字节码数组
     */
    byte[] generate(ClassInfo classInfo);
}
