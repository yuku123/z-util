# z-util-distribute

分布式环境下 ID 生成器模块，支持高性能全局唯一 ID。

## Features

- **SnowflakeIdWorker** — Twitter Snowflake 算法实现
  - 不依赖外部存储
  - 毫秒级时间戳 + 机器 ID + 序列号
  - 支持分布式多节点
- **Sequence** — 顺序号生成器
- **SystemClock** — 系统时钟工具

## Quick Start

```java
SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);
long id = idWorker.nextId();

// 多节点
SnowflakeIdWorker worker1 = new SnowflakeIdWorker(1, 1);
SnowflakeIdWorker worker2 = new SnowflakeIdWorker(1, 2);
```

## 架构

```
Snowflake ID 格式（64bit）:
| timestamp(41bit) | machine(10bit) | sequence(12bit) |
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-distribute</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```