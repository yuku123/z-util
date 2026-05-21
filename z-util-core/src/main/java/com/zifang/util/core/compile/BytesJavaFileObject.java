package com.zifang.util.core.compile;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * 字节数组形式的 Java 文件对象。
 * <p>
 * 用于动态编译 Java 源码时，以字节数组形式存储编译后的类文件内容。
 *
 * @author zifang
 * @see SimpleJavaFileObject
 */
public class BytesJavaFileObject extends SimpleJavaFileObject {

    private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    public BytesJavaFileObject(String name, Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
    }

    public byte[] getBytes() {
        return bos.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return bos;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        bos.close();
    }
}
