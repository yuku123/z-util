package com.zifang.util.source.compiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 动态编译执行上下文
 * <p>
 * 封装了Java动态编译所需的全部组件和流程，
 * 包括编译器实例、文件管理器、类加载器、诊断收集器等。
 * 提供简洁的API来完成内存中的源代码编译和类加载。
 *
 * @author zifang
 * @version 1.0.0
 */
public class CompileContext {

    private static final Logger log = LoggerFactory.getLogger(CompileContext.class);

    /**
     * 上下文唯一标识，基于原子计数器生成
     */
    private final long id = idCounter.incrementAndGet();

    /**
     * 系统Java编译器实例
     */
    private JavaCompiler compiler;

    /**
     * 自定义编译文件管理器
     */
    private CustomerCompileJavaFileManager customerCompileJavaFileManager;

    /**
     * 自定义类加载器
     */
    private CustomerCompileClassLoader classLoader;

    /**
     * 标准Java文件管理器
     */
    private StandardJavaFileManager standardJavaFileManager;

    /**
     * 诊断信息收集器
     */
    private DiagnosticCollector<JavaFileObject> diagnosticCollector;

    /**
     * 待编译的Java文件对象列表
     */
    private List<CharSequenceJavaFileObject> charSequenceJavaFileObjects;

    /**
     * 是否已初始化
     */
    private volatile boolean initialized = false;

    private static final AtomicLong idCounter = new AtomicLong(0);

    public CompileContext() {
        this.charSequenceJavaFileObjects = new ArrayList<>();
        this.diagnosticCollector = new DiagnosticCollector<>();
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (this.compiler == null) {
            throw new IllegalStateException(
                    "无法获取系统Java编译器（ToolProvider.getSystemJavaCompiler() 返回 null）。" +
                    "请确保在 JDK 环境下运行（非 JRE），或添加 tools.jar 到 classpath。");
        }
        log.info("CompileContext 创建完成，id={}", id);
    }

    /**
     * 添加Java文件对象到编译上下文
     *
     * @param uri            URI
     * @param javaFileObject Java文件对象
     */
    public void addJavaObject(URI uri, CharSequenceJavaFileObject javaFileObject) {
        checkInitialized();
        customerCompileJavaFileManager.addJavaFileObject(uri, javaFileObject);
        charSequenceJavaFileObjects.add(javaFileObject);
    }

    /**
     * 初始化编译上下文。
     * 多次调用安全，但只会初始化一次。
     */
    public synchronized void initial() {
        if (initialized) {
            log.debug("CompileContext 已初始化，跳过: id={}", id);
            return;
        }
        standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        classLoader = new CustomerCompileClassLoader(Thread.currentThread().getContextClassLoader());
        customerCompileJavaFileManager = new CustomerCompileJavaFileManager(standardJavaFileManager, classLoader);
        initialized = true;
        log.info("CompileContext 初始化完成: id={}", id);
    }

    /**
     * 执行编译任务
     *
     * @return true 编译成功，false 编译失败
     */
    public boolean compile() {
        checkInitialized();

        List<String> options = new ArrayList<>();
        options.add("-source");
        options.add("1.8");
        options.add("-target");
        options.add("1.8");

        JavaCompiler.CompilationTask compilationTask = compiler.getTask(
                null,
                customerCompileJavaFileManager,
                diagnosticCollector,
                options,
                null,
                charSequenceJavaFileObjects
        );

        boolean result = Boolean.TRUE.equals(compilationTask.call());

        if (!result) {
            log.error("编译失败，诊断信息:");
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticCollector.getDiagnostics()) {
                log.error("  行 {}: {}", diagnostic.getLineNumber(), diagnostic.getMessage(null));
            }
        } else {
            log.info("编译成功: id={}, 文件数={}", id, charSequenceJavaFileObjects.size());
        }

        return result;
    }

    /**
     * 从当前编译上下文加载类
     *
     * @param className 类名
     * @return Class对象，如果加载失败返回null
     */
    public Class<?> load(String className) {
        checkInitialized();
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            log.error("加载类失败: {}", className, e);
            return null;
        }
    }

    /**
     * 获取诊断信息
     */
    public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        return diagnosticCollector.getDiagnostics();
    }

    /**
     * 获取上下文ID
     */
    public long getId() {
        return id;
    }

    private void checkInitialized() {
        if (!initialized) {
            throw new IllegalStateException("CompileContext 未初始化，请先调用 initial() 方法");
        }
    }
}
