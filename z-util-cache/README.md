# z-util-cache

纯内存缓存模块，对标 Google Guava Cache / Caffeine，支持 TTL 自动过期 + LRU/W-TinyLFU 淘汰。

## Features

- **MemoryCache** — 纯内存 KV 缓存，线程安全
    - 支持 TTL（过期时间）
    - 支持 LRU / W-TinyLFU 淘汰策略（最大容量限制）
    - 支持统计：hit/miss/evict
- **CacheBuilder** — 链式构建缓存实例（推荐入口）
- **CacheManager** — 多级缓存管理，支持缓存命名空间
- **LoadingCache / LoadingMemoryCache** — 自动加载语义
- **装饰器**：BoundedCache / ForwardingCache / MeteredCache / NullCache / TransactionalCache
- **CountMinSketch** — 频率估计（W-TinyLFU 内部使用）

## Quick Start

```java
import java.time.Duration;
import java.util.concurrent.TimeUnit;

MemoryCache<String, Object> cache = CacheBuilder.<String, Object>newBuilder()
    .name("demo-cache")
    .maximumSize(1000)
    .expireAfterWrite(Duration.ofMinutes(5))
    .build();

cache.put("key", "value");
Object val = cache.get("key");          // null if expired/evicted
Object compute = cache.get("k2", k -> "computed-" + k);  // 不存在时自动计算
cache.invalidate("key");
```

## LoadingCache

```java
LoadingCache<String, User> loadingCache = CacheBuilder.<String, User>newBuilder()
    .maximumSize(10_000)
    .expireAfterWrite(Duration.ofHours(1))
    .build(key -> userDao.findById(key));   // CacheLoader

User user = loadingCache.get("user-123");  // 首次自动加载，后续走缓存
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-cache</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```