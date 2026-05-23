package com.zifang.util.source.generator;

import com.zifang.util.source.define.ByteCodeGenerator;
import com.zifang.util.source.generator.info.ClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于 ASM 的字节码生成实现
 */
public class ByteCodeGeneratorImpl implements ByteCodeGenerator {

    private static final Logger log = LoggerFactory.getLogger(ByteCodeGeneratorImpl.class);

    public ByteCodeGeneratorImpl() {
    }

    @Override
    public byte[] generate(ClassInfo classInfo) {
        if (classInfo == null) {
            throw new IllegalArgumentException("classInfo 不能为 null");
        }
        log.debug("生成字节码: className={}", classInfo.getFullName());
        // TODO: 基于 ASM 实现完整的字节码生成
        throw new UnsupportedOperationException("字节码生成尚未实现，请使用 JavaSourceGenerator");
    }
}
