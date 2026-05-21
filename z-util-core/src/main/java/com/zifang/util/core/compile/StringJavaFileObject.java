package com.zifang.util.core.compile;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * 字符串形式的 Java 源码文件对象。
 * <p>
 * 用于将 Java 源码字符串封装为 JavaFileObject，供动态编译使用。
 *
 * @author zifang
 * @param <T> 内容类型
 * @see SimpleJavaFileObject
 */
public class StringJavaFileObject<T> extends SimpleJavaFileObject {

    private T content;

    public StringJavaFileObject(String className, T content) {
        super(URI.create("string:///" + className.replace('.', '/')
                + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }

    @Override
    public String getCharContent(boolean ignoreEncodingErrors) {
        return content.toString();
    }
}
