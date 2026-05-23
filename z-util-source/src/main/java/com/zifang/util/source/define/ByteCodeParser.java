package com.zifang.util.source.define;

import com.zifang.util.source.generator.info.ClassInfo;

/**
 * 字节码解析器接口
 */
public interface ByteCodeParser {

    /**
     * 解析字节码为类信息
     *
     * @param bytecode 字节码数组
     * @return 类信息
     */
    ClassInfo parse(byte[] bytecode);

    /**
     * 解析类为类信息
     *
     * @param clazz 目标类
     * @return 类信息
     */
    ClassInfo parse(Class<?> clazz);
}
