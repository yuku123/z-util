package com.zifang.util.source.compiler;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 源代码文件对象
 * <p>
 * 继承自SimpleJavaFileObject，用于存储Java源代码内容。
 * 在动态编译场景中用于包装用户提供的源代码字符串。
 *
 * @author zifang
 * @version 1.0.0
 */
public class SourceJavaFileObject extends SimpleJavaFileObject {

    private final String sourceCode;

    protected SourceJavaFileObject(URI uri, Kind kind) {
        super(uri, kind);
        this.sourceCode = null;
    }

    public SourceJavaFileObject(String className, String sourceCode) {
        super(fromClassName(className + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
    }

    /**
     * 返回源代码内容
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceCode;
    }

    private static URI fromClassName(String className) {
        try {
            return new URI(className);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(className, e);
        }
    }
}
