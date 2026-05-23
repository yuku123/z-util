package com.zifang.util.source.compiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户自定义编译文件管理器
 * <p>
 * 继承自ForwardingJavaFileManager，用于在动态编译过程中管理Java文件对象。
 * 支持自定义源代码和字节码的存储、检索，以及覆盖默认的类加载器行为。
 * 主要用于内存中编译场景，无需写入文件系统。
 *
 * @author zifang
 * @version 1.0.0
 */
public class CustomerCompileJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private static final Logger log = LoggerFactory.getLogger(CustomerCompileJavaFileManager.class);

    private final CustomerCompileClassLoader classLoader;
    private final Map<URI, JavaFileObject> javaFileObjectMap = new ConcurrentHashMap<>();

    public CustomerCompileJavaFileManager(JavaFileManager fileManager, CustomerCompileClassLoader classLoader) {
        super(fileManager);
        this.classLoader = classLoader;
    }

    public static URI fromLocation(Location location, String packageName, String relativeName) {
        try {
            return new URI(location.getName() + '/' + packageName + '/' + relativeName);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("无效的文件位置: " + location, e);
        }
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        JavaFileObject javaFileObject = new CharSequenceJavaFileObject(className, kind);
        classLoader.addJavaFileObject(className, javaFileObject);
        log.debug("创建输出文件对象: className={}, kind={}", className, kind);
        return javaFileObject;
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof CharSequenceJavaFileObject) {
            // 返回全限定类名（带包路径）
            return file.getName().replace('/', '.').replace(".java", "").replace(".class", "");
        }
        return super.inferBinaryName(location, file);
    }

    @Override
    public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
        List<JavaFileObject> result = new ArrayList<>();

        // 从内存缓存中添加
        for (JavaFileObject file : javaFileObjectMap.values()) {
            if (file.getKind() == JavaFileObject.Kind.CLASS && kinds.contains(JavaFileObject.Kind.CLASS)) {
                if (isInPackage(packageName, file.getName())) {
                    result.add(file);
                }
            }
            if (file.getKind() == JavaFileObject.Kind.SOURCE && kinds.contains(JavaFileObject.Kind.SOURCE)) {
                if (isInPackage(packageName, file.getName())) {
                    result.add(file);
                }
            }
        }

        // CLASS_PATH 时也添加类加载器中的文件
        if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
            result.addAll(classLoader.listJavaFileObject());
        }

        // 添加标准文件管理器中的文件
        Iterable<JavaFileObject> superResult = super.list(location, packageName, kinds, recurse);
        for (JavaFileObject javaFileObject : superResult) {
            result.add(javaFileObject);
        }

        return result;
    }

    private boolean isInPackage(String packageName, String fileName) {
        String prefix = packageName.isEmpty() ? "" : packageName.replace('.', '/') + '/';
        return fileName.startsWith(prefix) || fileName.startsWith(packageName + "/");
    }

    public void addJavaFileObject(Location location, String packageName, String relativeName, JavaFileObject javaFileObject) {
        javaFileObjectMap.put(fromLocation(location, packageName, relativeName), javaFileObject);
        log.debug("添加JavaFileObject: location={}, package={}, name={}", location, packageName, relativeName);
    }

    public void addJavaFileObject(URI uri, JavaFileObject javaFileObject) {
        javaFileObjectMap.put(uri, javaFileObject);
        log.debug("添加JavaFileObject: uri={}", uri);
    }

    @Override
    public void close() throws IOException {
        javaFileObjectMap.clear();
        super.close();
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }
}
