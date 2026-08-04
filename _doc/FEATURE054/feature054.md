# FEATURE054 · z-boot 通用中间件 Starter 沉淀

> 状态：📋 规划中
> 优先级：P1
> 创建时间：2026-07-23
> 关联：z-boot、z-mist、z-config、z-gw、z-rpc、z-mq、z-util

---

## 一、背景

### 1.1 问题

z-opc 各子模块存在大量重复的中间件级代码，各自实现、各自维护，缺乏统一抽象：

| 重复领域 | 涉及模块 | 重复程度 |
|---------|---------|---------|
| Netty 通信框架 | z-mist-common、z-config-common | 几乎完全重复（20+ 文件） |
| 负载均衡 | z-gw-core、z-rpc-core | 概念相同，接口不同 |
| 限流 | z-gw-core（4 种算法） | 独占，但通用性极强 |
| 业务异常 | z-task、z-oss、z-cache | 各自定义，结构相似 |

### 1.2 分层定位

| 层 | 仓库 | 定位 | 粒度 |
|---|------|------|------|
| 通用工具 | z-util | 轻量小包，无框架依赖 | Tuple、PageResult、ResultCode、CircuitBreaker |
| 中间件共享 | z-boot（z-opc 内） | 内聚的 Spring Boot Starter，有框架依赖 | Netty 通信、负载均衡、限流 |

**原则**：z-boot 下统一用 `-starter` 命名，每个 starter 足够内聚，职责单一。

### 1.3 z-util 已有覆盖（无需迁移）

| z-opc 代码 | z-util 已有替代 | 说明 |
|------------|-----------------|------|
| z-mq-remoting `Pair<T1,T2>` | `com.zifang.util.core.meta.Tuple` | 键值对 |
| z-ctc-common `PageResponseVO` | `com.zifang.util.core.meta.page.PageResult` | 分页响应 |
| z-gw-core `CircuitBreaker` | `com.zifang.util.core.resilience.CircuitBreaker` | 熔断器 |
| z-task `BusinessException` | `com.zifang.util.core.meta.ResultCode` + `Result` | 异常体系 |

---

## 二、新增 Starter 规划

### 2.1 z-boot-remoting-starter

**内聚职责**：Netty 通信框架（编解码、序列化、心跳、消息模型）

**来源**：整合 z-mist-common/connect 和 z-config-common/connect 的重复代码

**包结构**：

```
z-boot/z-boot-remoting-starter/
└── src/main/java/com/zifang/z/boot/remoting/
    ├── coder/
    │   ├── JsonDecoder.java          # JSON 解码器（4字节长度+内容）
    │   ├── JsonEncoder.java          # JSON 编码器
    │   ├── CustomProtocolDecoder.java # 自定义协议解码（z-config 独有）
    │   └── CustomProtocolEncoder.java # 自定义协议编码
    ├── handler/
    │   └── HeartbeatHandler.java     # 心跳处理器
    ├── message/
    │   ├── Message.java              # 消息基类
    │   ├── HeartbeatMessage.java     # 心跳消息
    │   ├── NormalMessage.java        # 业务消息
    │   └── NormalResponse.java       # 响应消息
    ├── serializer/
    │   ├── Serializer.java           # 序列化接口
    │   └── JsonSerializer.java       # JSON 序列化实现
    ├── RemotingHelper.java           # Netty 通道辅助工具
    ├── ServiceThread.java            # 服务线程基类（生命周期管理）
    ├── CommandType.java              # 命令类型枚举
    └── ProtocolConstant.java         # 协议常量
```

**依赖**：

```xml
<dependencies>
    <dependency>io.netty:netty-all</dependency>
    <dependency>com.fasterxml.jackson.core:jackson-databind</dependency>
    <dependency>org.slf4j:slf4j-api</dependency>
</dependencies>
```

**迁移来源对照**：

| 新位置（z-boot-remoting-starter） | 旧位置（z-mist-common） | 旧位置（z-config-common） |
|---|---|---|
| coder/JsonDecoder | connect/coder/JsonDecoder | connect/coder/JsonDecoder |
| coder/JsonEncoder | connect/coder/JsonEncoder | connect/coder/JsonEncoder |
| coder/CustomProtocolDecoder | - | connect/coder/CustomProtocolDecoder |
| coder/CustomProtocolEncoder | - | connect/coder/CustomProtocolEncoder |
| handler/HeartbeatHandler | connect/handler/HeartbeatHandler | connect/handler/HeartbeatHandler |
| message/Message | connect/message/Message | connect/message/Message |
| message/HeartbeatMessage | connect/message/HeartbeatMessage | connect/message/HeartbeatMessage |
| message/NormalMessage | connect/message/NormalMessage | connect/message/NormalMessage |
| message/NormalResponse | connect/message/NormalResponse | connect/message/NormalResponse |
| serializer/Serializer | connect/serializer/Serializer | connect/serializer/Serializer |
| serializer/JsonSerializer | connect/serializer/JsonSerializer | connect/serializer/JsonSerializer |
| RemotingHelper | - | -（来自 z-mq-remoting） |
| ServiceThread | - | -（来自 z-mq-remoting） |
| CommandType | connect/CommandType | connect/CommandType + BizCommandType |
| ProtocolConstant | connect/ProtocolConstant | connect/ProtocolConstant |

**整合要点**：

- Message 基类：z-mist 版是具体类（Serializable），z-config 版是抽象类（带 data 字段），需统一为抽象基类 + 泛型扩展
- Serializer 接口：z-mist 版无异常声明，z-config 版 throws IOException + 含 Factory，取 z-config 版（更完善）
- CommandType：z-mist 用枚举，z-config 用 short 常量，统一为枚举 + code 映射
- JsonDecoder：z-mist 版用 4 字节长度前缀（防粘包），z-config 版直接读全部字节，统一用 4 字节长度前缀方案

---

### 2.2 z-boot-loadbalance-starter

**内聚职责**：负载均衡算法（统一接口 + 多种实现）

**来源**：整合 z-gw-core/loadbalance 和 z-rpc-core/loadbalance

**包结构**：

```
z-boot/z-boot-loadbalance-starter/
└── src/main/java/com/zifang/z/boot/loadbalance/
    ├── LoadBalancer.java            # 统一负载均衡接口
    ├── AbstractLoadBalancer.java    # 抽象基类（权重计算、健康过滤）
    ├── LoadBalanceContext.java      # 上下文（clientIp、requestUri、serviceName）
    ├── ServiceInstance.java         # 服务实例模型
    ├── RandomLoadBalance.java       # 随机（带权重）
    ├── RoundRobinLoadBalance.java   # 轮询（带权重）
    ├── LeastActiveLoadBalance.java  # 最少活跃调用
    ├── IpHashLoadBalance.java       # IP 哈希
    ├── WeightedLoadBalance.java     # 加权轮询（Smooth Weighted Round Robin）
    └── LoadBalancerFactory.java     # 工厂（按名称获取实例）
```

**依赖**：

```xml
<dependencies>
    <dependency>org.slf4j:slf4j-api</dependency>
</dependencies>
```

纯算法模块，无框架依赖。

**迁移来源对照**：

| 新位置 | z-gw-core 来源 | z-rpc-core 来源 | 整合策略 |
|--------|---------------|-----------------|---------|
| LoadBalancer | LoadBalancer（Instance 内部类） | LoadBalance（泛型 Invoker） | 统一为 ServiceInstance 模型 |
| AbstractLoadBalancer | - | AbstractLoadBalance | 取 z-rpc 版（含权重计算） |
| RandomLoadBalance | RandomLoadBalancer | RandomLoadBalance | 取 z-rpc 版（带权重） |
| RoundRobinLoadBalance | RoundRobinLoadBalancer | RoundRobinLoadBalance | 取 z-rpc 版（带权重轮询） |
| LeastActiveLoadBalance | LeastConnectionsLoadBalancer | LeastActiveLoadBalance | 统一命名，取 z-rpc 版 |
| IpHashLoadBalance | IpHashLoadBalancer | - | 从 z-gw 迁入 |
| WeightedLoadBalance | WeightedLoadBalancer | - | 从 z-gw 迁入 |

**整合要点**：

- 统一实例模型：z-gw 用 `LoadBalancer.Instance`，z-rpc 用 `Invoker<T>`，抽象为 `ServiceInstance`（id/host/port/weight/healthy/activeConnections）
- 统一上下文：z-gw 用 `LoadBalanceContext`，z-rpc 用 `URL + Invocation`，抽象为 `LoadBalanceContext`（clientIp/requestUri/serviceName）
- z-gw 和 z-rpc 各自适配：z-gw 的 GatewayHandler 将 ServiceInstance 转为内部 Instance；z-rpc 的 Cluster 将 Invoker 适配为 ServiceInstance

---

### 2.3 z-boot-resilience-starter

**内聚职责**：限流算法（统一接口 + 多种实现）

**来源**：z-gw-core/ratelimit

**包结构**：

```
z-boot/z-boot-resilience-starter/
└── src/main/java/com/zifang/z/boot/resilience/
    ├── RateLimiter.java             # 限流接口
    ├── RateLimitConfig.java         # 限流配置
    ├── RateLimitStatus.java         # 限流状态
    ├── FixedWindowRateLimiter.java  # 固定窗口
    ├── SlidingWindowRateLimiter.java # 滑动窗口
    └── TokenBucketRateLimiter.java  # 令牌桶
```

**依赖**：

```xml
<dependencies>
    <dependency>org.slf4j:slf4j-api</dependency>
</dependencies>
```

纯算法模块，无框架依赖。

**迁移来源对照**：

| 新位置 | z-gw-core 来源 |
|--------|---------------|
| RateLimiter | ratelimit/RateLimiter（含内部类 RateLimitStatus、RateLimitConfig） |
| RateLimitConfig | RateLimiter.RateLimitConfig → 独立类 |
| RateLimitStatus | RateLimiter.RateLimitStatus → 独立类 |
| FixedWindowRateLimiter | ratelimit/FixedWindowRateLimiter |
| SlidingWindowRateLimiter | ratelimit/SlidingWindowRateLimiter |
| TokenBucketRateLimiter | ratelimit/TokenBucketRateLimiter |

**说明**：

- 熔断器（CircuitBreaker）已在 z-util-core/resilience 中实现，不重复迁移
- 限流与熔断虽同属韧性领域，但限流是独立算法族，单独成 starter 更内聚
- z-gw-core 的 CircuitBreaker 后续改为依赖 z-util-core 的实现

---

## 三、z-boot 整体架构

```
z-boot/
├── z-boot-dependencies/                    # 依赖管理（BOM）
├── z-boot-starter/                         # 基础启动器
│   ├── z-boot-base/                        # 基础接口（ApplicationModuleDescription）
│   ├── z-boot-datasource-starter/         # 数据源模板
│   └── z-boot-web-starter/                # Web 启动器
├── z-boot-remoting-starter/                # NEW: Netty 通信框架
├── z-boot-loadbalance-starter/             # NEW: 负载均衡
├── z-boot-resilience-starter/              # NEW: 限流
├── z-boot-integration-starters/            # 集成启动器（已有）
│   ├── z-config-spring-boot-starter/
│   ├── z-ctc-spring-boot-starter/
│   ├── z-ext-spring-boot-starter/
│   ├── z-lc-spring-boot-starter/
│   ├── z-mist-spring-boot-starter/
│   ├── z-rpc-spring-boot-starter/
│   └── z-tool-webide-spring-boot-starter/
└── z-boot-middleware-starters/             # 中间件启动器（已有）
    └── z-msg-spring-boot-starter/
```

---

## 四、依赖关系

```
z-boot-remoting-starter          z-boot-loadbalance-starter       z-boot-resilience-starter
    │                                  │                               │
    │ Netty + Jackson                  │ 纯算法                         │ 纯算法
    ▼                                  ▼                               ▼
z-mist-common ──依赖──→              z-gw-core ──依赖──→             z-gw-core ──依赖──→
z-config-common ──依赖──→            z-rpc-core ──依赖──→
z-mq-remoting ──依赖──→
```

各子模块改为依赖 starter 后，删除自身重复代码：

| 子模块 | 改为依赖 | 删除自身代码 |
|--------|---------|------------|
| z-mist-common | z-boot-remoting-starter | connect/ 整个目录 |
| z-config-common | z-boot-remoting-starter | connect/ 整个目录 |
| z-mq-remoting | z-boot-remoting-starter | common/Pair、common/ServiceThread、common/RemotingHelper |
| z-gw-core | z-boot-loadbalance-starter + z-boot-resilience-starter | loadbalance/ + ratelimit/ 目录 |
| z-rpc-core | z-boot-loadbalance-starter | loadbalance/ 目录 |

---

## 五、实施计划

### Phase 1: z-boot-remoting-starter（最高优先级，消除最大重复）

- [ ] 创建 `z-boot/z-boot-remoting-starter/` 模块骨架 + pom.xml
- [ ] z-boot pom.xml 注册 `<module>z-boot-remoting-starter</module>`
- [ ] 整合 Message 基类（统一 z-mist 具体类 + z-config 抽象类 → 抽象基类 + 泛型）
- [ ] 整合 Serializer 接口（取 z-config 版含 Factory + IOException）
- [ ] 整合 JsonSerializer（统一 ProtocolConstant）
- [ ] 整合 JsonDecoder（统一 4 字节长度前缀方案）
- [ ] 整合 JsonEncoder
- [ ] 迁移 CustomProtocolDecoder/Encoder（z-config 独有）
- [ ] 迁移 HeartbeatHandler
- [ ] 迁移 HeartbeatMessage / NormalMessage / NormalResponse
- [ ] 迁移 CommandType（统一枚举 + code 映射）
- [ ] 迁移 ProtocolConstant
- [ ] 迁移 RemotingHelper（来自 z-mq-remoting）
- [ ] 迁移 ServiceThread（来自 z-mq-remoting）
- [ ] z-mist-common pom.xml 添加 z-boot-remoting-starter 依赖，删除 connect/ 目录
- [ ] z-config-common pom.xml 添加 z-boot-remoting-starter 依赖，删除 connect/ 目录
- [ ] z-mq-remoting pom.xml 添加 z-boot-remoting-starter 依赖，删除 common/ 重复类
- [ ] 全量编译验证

### Phase 2: z-boot-loadbalance-starter

- [ ] 创建 `z-boot/z-boot-loadbalance-starter/` 模块骨架 + pom.xml
- [ ] z-boot pom.xml 注册 `<module>z-boot-loadbalance-starter</module>`
- [ ] 定义统一 ServiceInstance 模型
- [ ] 定义统一 LoadBalancer 接口 + LoadBalanceContext
- [ ] 实现 AbstractLoadBalancer（权重计算、健康过滤）
- [ ] 迁移 RandomLoadBalance（取 z-rpc 版，带权重）
- [ ] 迁移 RoundRobinLoadBalance（取 z-rpc 版，带权重轮询）
- [ ] 迁移 LeastActiveLoadBalance（统一命名）
- [ ] 迁移 IpHashLoadBalance（来自 z-gw）
- [ ] 迁移 WeightedLoadBalance（来自 z-gw）
- [ ] 实现 LoadBalancerFactory
- [ ] z-gw-core 适配：LoadBalancer.Instance → ServiceInstance 转换
- [ ] z-rpc-core 适配：Invoker → ServiceInstance 适配器
- [ ] z-gw-core pom.xml 添加 z-boot-loadbalance-starter 依赖，删除 loadbalance/ 目录
- [ ] z-rpc-core pom.xml 添加 z-boot-loadbalance-starter 依赖，删除 loadbalance/ 目录
- [ ] 全量编译验证

### Phase 3: z-boot-resilience-starter

- [ ] 创建 `z-boot/z-boot-resilience-starter/` 模块骨架 + pom.xml
- [ ] z-boot pom.xml 注册 `<module>z-boot-resilience-starter</module>`
- [ ] 迁移 RateLimiter 接口（内部类拆为独立类）
- [ ] 迁移 RateLimitConfig
- [ ] 迁移 RateLimitStatus
- [ ] 迁移 FixedWindowRateLimiter
- [ ] 迁移 SlidingWindowRateLimiter
- [ ] 迁移 TokenBucketRateLimiter
- [ ] z-gw-core pom.xml 添加 z-boot-resilience-starter 依赖，删除 ratelimit/ 目录
- [ ] z-gw-core 的 CircuitBreaker 改为依赖 z-util-core/resilience 的实现
- [ ] 全量编译验证

### Phase 4: 清理与统一

- [ ] 根 pom.xml dependencyManagement 注册 3 个新 starter 版本
- [ ] 删除各子模块中已被 starter 覆盖的重复代码
- [ ] 统一 import 路径
- [ ] 全量编译 + 启动验证

---

## 六、风险与注意事项

| 风险 | 应对 |
|------|------|
| Message 基类不兼容 | 统一为抽象基类，z-mist/z-config 各自扩展子类，保持序列化兼容 |
| CommandType 枚举冲突 | 统一枚举 + code 映射，z-mist/z-config 的业务命令各自扩展 |
| z-rpc Invoker 适配 | 提供适配器将 Invoker 转为 ServiceInstance，不修改 z-rpc 核心接口 |
| 序列化格式变更 | JsonDecoder 统一用 4 字节长度前缀，需确认 z-mist/z-config 客户端兼容 |
| 循环依赖 | starter 不依赖任何 z-opc 子模块，只被依赖 |
