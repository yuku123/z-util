package com.zifang.util.source.compiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义编译类加载器
 * <p>
 * 继承自ClassLoader，用于加载内存中编译生成的类。
 * 支持从CharSequenceJavaFileObject中获取字节码数据，
 * 配合CustomerCompileJavaFileManager实现纯内存的动态编译和加载。
 *
 * @author zifang
 * @version 1.0.0
 */
public class CustomerCompileClassLoader extends ClassLoader {

    private static final Logger log = LoggerFactory.getLogger(CustomerCompileClassLoader.class);

    public static final String CLASS_EXTENSION = JavaFileObject.Kind.CLASS.extension;

    private final Map<String, JavaFileObject> javaFileObjectMap = new ConcurrentHashMap<>();

    public CustomerCompileClassLoader(ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaFileObject javaFileObject = javaFileObjectMap.get(name);
        if (javaFileObject != null) {
            if (javaFileObject instanceof CharSequenceJavaFileObject) {
                CharSequenceJavaFileObject charSeqFile = (CharSequenceJavaFileObject) javaFileObject;
                byte[] byteCode = charSeqFile.getByteCode();
                if (byteCode != null && byteCode.length > 0) {
                    return defineClass(name, byteCode, 0, byteCode.length);
                }
            } else {
                log.warn("JavaFileObject 类型不匹配，无法加载: {} 类型={}", name, javaFileObject.getClass().getName());
            }
        }
        return super.findClass(name);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (name.endsWith(CLASS_EXTENSION)) {
            String qualifiedClassName = name.substring(0, name.length() - CLASS_EXTENSION.length()).replace('/', '.');
            JavaFileObject javaFileObject = javaFileObjectMap.get(qualifiedClassName);
            if (javaFileObject instanceof CharSequenceJavaFileObject) {
                CharSequenceJavaFileObject charSeqFile = (CharSequenceJavaFileObject) javaFileObject;
                byte[] byteCode = charSeqFile.getByteCode();
                if (byteCode != null && byteCode.length > 0) {
                    return new ByteArrayInputStream(byteCode);
                }
            }
        }
        return super.getResourceAsStream(name);
    }

    void addJavaFileObject(String qualifiedClassName, JavaFileObject javaFileObject) {
        javaFileObjectMap.put(qualifiedClassName, javaFileObject);
        log.debug("添加JavaFileObject到ClassLoader: {}", qualifiedClassName);
    }

    Collection<JavaFileObject> listJavaFileObject() {
        return Collections.unmodifiableCollection(javaFileObjectMap.values());
    }

    /**
     * 清空缓存的JavaFileObject
     */
    public void clear() {
        javaFileObjectMap.clear();
    }

    /**
     * 获取缓存的类数量
     */
    public int getCachedClassCount() {
        return javaFileObjectMap.size();
    }
}
