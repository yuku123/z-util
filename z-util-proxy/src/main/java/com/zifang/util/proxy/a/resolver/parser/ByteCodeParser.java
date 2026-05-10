package com.zifang.util.proxy.a.resolver.parser;

import com.zifang.util.proxy.a.resolver.parser.struct.ClassFile;

import java.util.Objects;

/**
 * @author zifang
 */
public class ByteCodeParser {

    private byte[] bytes;

    private ClassFile classFile;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public ClassFile getClassFile() {
        return classFile;
    }

    public void setClassFile(ClassFile classFile) {
        this.classFile = classFile;
    }

    @Override
    public String toString() {
        return "ByteCodeParser{bytes=" + (bytes != null ? bytes.length : 0) + ", classFile=" + classFile + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteCodeParser that = (ByteCodeParser) o;
        return Objects.equals(bytes, that.bytes) &&
                Objects.equals(classFile, that.classFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bytes, classFile);
    }

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
