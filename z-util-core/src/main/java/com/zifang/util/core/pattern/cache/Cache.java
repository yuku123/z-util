package com.zifang.util.core.pattern.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 简单的内存缓存工具类
 *
 * @author zifang
 */
/**
 * Cache类。
 */
public class Cache {

    //键值对集合
    private final static Map<String, Entity> map = new HashMap<>();

    //定时器线程池，用于清除过期缓存
    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * 添加缓存
     *
     * @param key  键
     * @param data 值
     */
    /**
     * put方法。
     *      * @param key String类型参数
     * @param data Object类型参数
     * @return synchronized static void类型返回值
     */
    public synchronized static void put(String key, Object data) {
        Cache.put(key, data, 0);
    }

    /**
     * 添加缓存
     *
     * @param key    键
     * @param data   值
     * @param expire 过期时间，单位：毫秒， 0表示无限长
     */
    /**
     * put方法。
     *      * @param key String类型参数
     * @param data Object类型参数
     * @param expire long类型参数
     * @return synchronized static void类型返回值
     */
    public synchronized static void put(String key, Object data, long expire) {
        //清除原键值对
        Cache.remove(key);
        //设置过期时间
        if (expire > 0) {
            Future future = executor.schedule(new Runnable() {
                @Override
    /**
     * run方法。
     */
                public void run() {
                    //过期后清除该键值对
                    synchronized (Cache.class) {
                        map.remove(key);
                    }
                }
            }, expire, TimeUnit.MILLISECONDS);
            map.put(key, new Entity(data, future));
        } else {
            //不设置过期时间
            map.put(key, new Entity(data, null));
        }
    }

    /**
     * 读取缓存
     *
     * @param key 键
     * @return 缓存的值，如果键不存在则返回null
     */
    /**
     * get方法。
     *      * @param key String类型参数
     * @return synchronized static Object类型返回值
     */
    public synchronized static Object get(String key) {
        Entity entity = map.get(key);
        return entity == null ? null : entity.getValue();
    }

    /**
     * 读取缓存并转换为指定类型
     *
     * @param key   键
     * @param clazz 值类型，用于将结果强制转换
     * @param <T>   泛型类型
     * @return 转换后的缓存值，如果键不存在则返回null
     */
    /**
     * get方法。
     *      * @param key String类型参数
     * @param clazz ClassT类型参数
     * @return synchronized static <T> T类型返回值
     */
    public synchronized static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(Cache.get(key));
    }

    /**
     * 清除缓存
     *
     * @param key 键
     * @return 被移除的缓存值，如果键不存在则返回null
     */
    /**
     * remove方法。
     *      * @param key String类型参数
     * @return synchronized static Object类型返回值
     */
    public synchronized static Object remove(String key) {
        //清除原缓存数据
        Entity entity = map.remove(key);
        if (entity == null) {
            return null;
        }
        //清除原键值对定时器
        Future future = entity.getFuture();
        if (future != null) {
            future.cancel(true);
        }
        return entity.getValue();
    }

    /**
     * 查询当前缓存的键值对数量
     *
     * @return 当前缓存中的键值对数量
     */
    /**
     * size方法。
     * @return synchronized static int类型返回值
     */
    public synchronized static int size() {
        return map.size();
    }

    /**
     * 缓存实体类
     */
    private static class Entity {
        //键值对的value
        private Object value;
        //定时器Future
        private Future future;

    /**
     * Entity方法。
     *      * @param value Object类型参数
     * @param future Future类型参数
     */
        public Entity(Object value, Future future) {
            this.value = value;
            this.future = future;
        }

        /**
         * 获取值
         *
         * @return
         */
    /**
     * getValue方法。
     * @return Object类型返回值
     */
        public Object getValue() {
            return value;
        }

        /**
         * 获取Future对象
         *
         * @return
         */
    /**
     * getFuture方法。
     * @return Future类型返回值
     */
        public Future getFuture() {
            return future;
        }
    }
}
