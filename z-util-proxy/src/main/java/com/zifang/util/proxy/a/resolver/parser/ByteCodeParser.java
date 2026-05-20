package com.zifang.util.proxy.a.resolver.parser;

import com.zifang.util.proxy.a.resolver.parser.struct.ClassFile;

import java.util.Objects;

/**
 * 字节码解析器<br>
 * 负责将字节数组解析为ClassFile结构
 */
public class ByteCodeParser {

    private byte[] bytes;

    private ClassFile classFile;

    /**
     * 获取字节数组
     *
     * @return 字节数组
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 设置字节数组
     *
     * @param bytes 字节数组
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 获取ClassFile对象
     *
     * @return ClassFile对象
     */
    public ClassFile getClassFile() {
        return classFile;
    }

    /**
     * 设置ClassFile对象
     *
     * @param classFile ClassFile对象
     */
    public void setClassFile(ClassFile classFile) {
        this.classFile = classFile;
    }

    /**
     * 解析ClassFile并返回
     *
     * @return ClassFile对象
     */
    public ClassFile solveClassFile() {
        if (classFile != null) {
            return classFile;
        } else {
            classFile = new ClassFile();
        }
        // 魔术
        solveMagic(classFile);
        return classFile;
    }

    private void solveMagic(ClassFile classFile) {

    }
}
