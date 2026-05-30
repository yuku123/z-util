# z-util-cache

纯内存缓存模块，对标 Google Guava Cache，支持 TTL 自动过期 + LRU 淘汰。

## Features

- **MemoryCache** — 纯内存 KV 缓存，线程安全
  - 支持 TTL（过期时间）
  - 支持 LRU 淘汰策略（最大容量限制）
  - 支持统计：hit/miss/evict
- **CacheManager** — 多级缓存管理，支持缓存命名空间
- **CacheFactory** — 工厂模式创建缓存实例

## Quick Start

```java
MemoryCache<String, Object> cache = CacheFactory.create()
    .maximumSize(1000)
    .expireAfterWrite(5, TimeUnit.MINUTES)
    .build();

cache.put("key", "value");
Object val = cache.get("key");          // null if expired/evicted
cache.invalidate("key");
```

## Export/Import

```java
// 持久化到文件
cache.exportToFile("cache.dat");

// 从文件恢复
MemoryCache<String, Object> restored = MemoryCache.importFromFile("cache.dat");
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-cache</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```