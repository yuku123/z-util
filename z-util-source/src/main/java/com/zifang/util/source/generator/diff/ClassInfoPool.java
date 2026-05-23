package com.zifang.util.source.generator.diff;

import com.zifang.util.source.generator.info.ClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassInfo 池
 * <p>
 * 用于缓存和管理 ClassInfo 实例，避免重复解析同一类。
 *
 * @author zifang
 * @version 1.0.0
 */
public class ClassInfoPool {

    private static final Logger log = LoggerFactory.getLogger(ClassInfoPool.class);

    private final Map<String, ClassInfo> pool = new ConcurrentHashMap<>();

    public ClassInfoPool() {
    }

    /**
     * 获取 ClassInfo，若不存在则解析后缓存
     */
    public ClassInfo get(Class<?> clazz) {
        String key = clazz.getName();
        return pool.computeIfAbsent(key, k -> {
            log.debug("解析并缓存 ClassInfo: {}", key);
            return ClassInfo.parser(clazz);
        });
    }

    /**
     * 获取 ClassInfo，若不存在则使用 provided
     */
    public ClassInfo get(String className, ClassInfo provided) {
        return pool.computeIfAbsent(className, k -> provided);
    }

    /**
     * 手动放入
     */
    public void put(String className, ClassInfo classInfo) {
        pool.put(className, classInfo);
    }

    /**
     * 是否已缓存
     */
    public boolean contains(String className) {
        return pool.containsKey(className);
    }

    /**
     * 清除所有缓存
     */
    public void clear() {
        pool.clear();
        log.debug("ClassInfoPool 已清空");
    }

    /**
     * 获取缓存数量
     */
    public int size() {
        return pool.size();
    }
}
