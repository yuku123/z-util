# z-util

> 一个面向日常 Java 开发的多模块工具库集合 —— 目标是把零散的「轮子」以 Maven 多模块的方式沉淀下来，按需引用、按需升级。

[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue.svg)](https://maven.apache.org/)
[![JDK](https://img.shields.io/badge/JDK-8%2B-orange.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](#许可证)

`z-util` 是一组 **Maven 多模块** 的 Java 工具库，当前版本 `1.0.2-SNAPSHOT`，由统一的父 POM 管理版本与依赖。每个子模块都专注于一个领域，相互之间保持低耦合：

- 可以 **整体引入** `z-util-all`，一次性拿到所有能力；
- 也可以 **按需引入** 某一个子模块，最小化依赖体积；
- 还可以把部分子模块当作 **学习样例**，研究对应主题的工程实现。

---

## 目录

- [特性](#特性)
- [项目地图](#项目地图)
- [模块清单](#模块清单)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [构建与测试](#构建与测试)
- [各模块使用指南](#各模块使用指南)
  - [z-util-core（基础工具）](#z-util-core基础工具)
  - [z-util-cache（缓存）](#z-util-cache缓存)
  - [z-util-ioc（IoC 容器）](#z-util-ioc容器)
  - [z-util-aop（AOP）](#z-util-aopaop)
  - [z-util-proxy（动态代理与字节码）](#z-util-proxy动态代理与字节码)
  - [z-util-validation（校验）](#z-util-validation校验)
  - [z-util-dsl（自研 DSL）](#z-util-dsl自研-dsl)
  - [z-util-parser（多格式解析器）](#z-util-parser多格式解析器)
  - [z-util-expr（表达式引擎）](#z-util-expr表达式引擎)
  - [z-util-workflow（工作流）](#z-util-workflow工作流)
  - [z-util-http（HTTP 与 Netty）](#z-util-httphttp-与-netty)
  - [z-util-crawler（爬虫与 CUA）](#z-util-crawler爬虫与-cua)
  - [z-util-jdbc（数据库与事务）](#z-util-jdbc数据库与事务)
  - [z-util-math（数学与数据分析）](#z-util-math数学与数据分析)
  - [z-util-ml（机器学习）](#z-util-ml机器学习)
  - [z-util-office（Office 文档）](#z-util-officeoffice-文档)
  - [z-util-media（图像与媒体）](#z-util-media图像与媒体)
  - [z-util-visualization（可视化）](#z-util-visualization可视化)
  - [z-util-monitor（监控）](#z-util-monitormonitor)
  - [z-util-devops（运维）](#z-util-devopsdevops)
  - [z-util-source（字节码与源码）](#z-util-source字节码与源码)
  - [z-util-distribute（分布式 ID）](#z-util-distribute分布式-id)
  - [z-util-cli（命令行解析）](#z-util-cli命令行解析)
  - [z-util-ch（中文工具）](#z-util-ch中文工具)
  - [z-util-zex（练习场）](#z-util-zex练习场)
  - [z-util-it（集成测试）](#z-util-it集成测试)
  - [z-util-all（聚合入口）](#z-util-all聚合入口)
- [架构与设计](#架构与设计)
- [发布到 GitHub Packages](#发布到-github-packages)
- [开发约定](#开发约定)
- [常见问题](#常见问题)
- [许可证](#许可证)

---

## 特性

- **多模块按需引入**：覆盖表达式、工作流、AI/ML、爬虫、HTTP、Netty、数学计算、字节码、分布式 ID、缓存、AOP、IoC、校验、监控、运维、可视化等场景。
- **JDK 8 兼容**：默认 `<source>1.8</source>`，不绑定任何特定的 Spring/SpringBoot 版本，可直接在传统 Java 工程中使用。
- **统一版本管理**：所有子模块通过 `${util.version}` 与父 POM 保持一致，升级一行即可。
- **丰富的内部工具类**：覆盖 Guava / commons-collections 的常用集合与字符串能力。
- **依赖精简原则**：同名依赖统一取最高版本；能用自研工具（`StringUtil` / `BeanUtil` / `Base64Utils` / `FileUtil` / `XmlUtil` …）替代的，不引入第三方。
- **可作为学习样例**：从 DSL/表达式解析、字节码、AOP、IoC，到 NumPy/Pandas 风格的数据结构，再到 ML/RL 算法实现，均提供可读的源码。
- **CI/CD 内建**：内置 GitHub Actions 发布到 GitHub Packages，源码包自动附加。

---

## 项目地图

```
z-util/
├── pom.xml                 # 父 POM（统一版本、依赖、插件）
├── README.md               # 本文件
├── CLAUDE.md               # 给 AI 助手的协作约定
├── note.md                 # 项目笔记
├── build.sh / push.sh      # 本地构建/发布脚本
├── fix_pkg.py              # 包名修正脚本
├── fix_unicode_escapes.py  # Unicode 转义修正脚本
├── add_javadoc.py          # Javadoc 补充脚本
├── .github/workflows/      # GitHub Actions 发布流水线
├── z-util-core/            # 基础工具（集合/字符串/JWT/IO/并发/网络...）
├── z-util-cache/           # 缓存
├── z-util-ioc/             # IoC 容器
├── z-util-aop/             # AOP
├── z-util-proxy/           # 动态代理 + 自研字节码解析
├── z-util-validation/      # 校验
├── z-util-dsl/             # 自研 DSL（Lexer/Parser/AST）
├── z-util-parser/          # 解析器集合（JSON/XML/YAML/CSV/INI/Properties/Proto/TOML）
├── z-util-expr/            # 表达式引擎（EL/Groovy/JS/Lua/SQL）
├── z-util-workflow/        # 工作流
├── z-util-http/            # HTTP + Netty IM 示例
├── z-util-crawler/         # 爬虫与 CUA
├── z-util-jdbc/            # JDBC + 事务 + ORM
├── z-util-math/            # NumPy/Pandas 风格数学库
├── z-util-ml/              # 机器学习算法集
├── z-util-office/          # Office 文档
├── z-util-media/           # 图像与媒体
├── z-util-visualization/   # 图表与 Swing 可视化
├── z-util-monitor/         # 监控
├── z-util-devops/          # 运维（Docker / Git / Nexus / GitHub）
├── z-util-source/          # 字节码与源码
├── z-util-distribute/      # 分布式 ID
├── z-util-cli/             # 命令行解析
├── z-util-ch/              # 中文工具
├── z-util-zex/             # 练习/实验场
├── z-util-it/              # 集成测试
└── z-util-all/             # 聚合 pom（一次性引入全部）
```

---

## 模块清单

> 模块名均以 `z-util-` 为前缀；下面「Artifact」一列对应 Maven `<artifactId>`。
> 各模块都有自己的 `README.md`/`README.MD`，可通过目录直接进入查看更详细的使用说明。

| 类别 | 模块 (Artifact) | 主要能力 |
|---|---|---|
| 基础 | [z-util-core](./z-util-core) | 集合/字符串/IO/并发/反射/网络/JWT/加密/特性开关/任务调度/责任链/对象池/限流/熔断/邮件/布隆过滤器/分页… |
| 缓存 | [z-util-cache](./z-util-cache) | 纯内存缓存、TTL + LRU/W-TinyLFU、CacheBuilder、CacheManager、装饰器（Null/Bounded/Forwarding/Metered/Transactional） |
| 容器 | [z-util-ioc](./z-util-ioc) | 轻量级 IoC、对标 Guice/Spring IoC、JSR-330、生命周期管理、AOP 集成 |
| 切面 | [z-util-aop](./z-util-aop) | `@Advise` / `Intercept` 拦截器、代理工厂 |
| 代理 | [z-util-proxy](./z-util-proxy) | JDK / CGLIB 代理、字节码模型（class 文件解析器、属性表、常量池…） |
| 校验 | [z-util-validation](./z-util-validation) | 校验引擎 + 内置注解（@NotNull/@Pattern/@Length/@Range/@Email） |
| DSL | [z-util-dsl](./z-util-dsl) | 自研词法/语法解析框架，支持 G4 文法动态加载 |
| 解析 | [z-util-parser](./z-util-parser) | JSON / XML / YAML / CSV / INI / Properties / Protobuf / TOML（每个子模块均含 ANTLR g4 词法/语法） |
| 表达式 | [z-util-expr](./z-util-expr) | EL / Groovy / JS（ANTLR + PlayScript）/ Lua / SQL |
| 工作流 | [z-util-workflow](./z-util-workflow) | 节点 + 连接器 + 上下文、Java/Python/Spark 多执行引擎、BPMN 模型 |
| 网络 | [z-util-http](./z-util-http) | 注解式 HTTP 客户端、curl 解析、简易 HTTP 服务端、Netty IM 示例 |
| 爬虫 | [z-util-crawler](./z-util-crawler) | 浏览器渲染、HTML/JSON 解析、Pipeline、CUA 计划与步骤注册 |
| 数据库 | [z-util-jdbc](./z-util-jdbc) | 数据源/事务/注解式 Mapper/Repository 代理/SQL 构造/分布式锁/序列 |
| 数学 | [z-util-math](./z-util-math) | `Num` / `Series` / `DataFrame`、`Linalg` 线性代数、`Maths` 数学函数、统计与窗口 |
| 机器学习 | [z-util-ml](./z-util-ml) | 神经网络（Dense/CNN/RNN/LSTM/GAN/VAE/Transformer）、树模型（RF/XGBoost）、聚类、PCA、遗传算法、强化学习、序列模型 |
| 文档 | [z-util-office](./z-util-office) | POI 读写 Excel/Word、PDF 解析/编辑/转图片/转 PDF |
| 媒体 | [z-util-media](./z-util-media) | 验证码、颜色转换、GIF 编码、图像比对、QRCode 编解码 |
| 可视化 | [z-util-visualization](./z-util-visualization) | Swing 图表（折线/柱状/网络）、机器人框架、算法可视化 |
| 监控 | [z-util-monitor](./z-util-monitor) | JVM 监控、线程池监控、告警、Metrics 采集、Exporter（HTML/JSON） |
| 运维 | [z-util-devops](./z-util-devops) | Docker、Git (JGit/Shell)、GitHub API、Nexus |
| 源码 | [z-util-source](./z-util-source) | 字节码解析与生成、Class 信息池、增量比较、源码生成 |
| 分布式 | [z-util-distribute](./z-util-distribute) | Snowflake、Segment、NanoId、UUID v7、SystemClock |
| CLI | [z-util-cli](./z-util-cli) | POSIX/GNU/Basic/Default 解析、OptionGroup、HelpFormatter |
| 中文 | [z-util-ch](./z-util-ch) | 拼音、身份证、金额大写 |
| 练习 | [z-util-zex](./z-util-zex) | LeetCode、排序、字节码、Guava 实战、并发样例 |
| 集成 | [z-util-it](./z-util-it) | 端到端集成测试（默认 profile 跳过，`-Pit` 开启） |
| 聚合 | [z-util-all](./z-util-all) | `pom` 形式聚合，import 即用 |

---

## 环境要求

- **JDK**：8 及以上
- **Maven**：3.6 及以上
- **网络**：能访问 `https://maven.pkg.github.com/yuku123/z-maven-repo`（用于发布与拉取 GitHub Packages）

> 项目使用 `flatten-maven-plugin` 生成 CI 友好的 POM，并通过 `maven-source-plugin` 在 `install` 阶段自动附加源码包。

---

## 快速开始

### 1. 整体引入（快速试用）

通过 `z-util-all` 一次性引入全部子模块的依赖管理：

```xml
<dependency>
    <groupId>com.zifang</groupId>
    <artifactId>z-util-all</artifactId>
    <version>1.0.2-SNAPSHOT</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

或者直接在 `<dependencies>` 中引入（不推荐，会把测试代码也带进来）：

```xml
<dependency>
    <groupId>com.zifang</groupId>
    <artifactId>z-util-all</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```

### 2. 按需引入（生产环境推荐）

```xml
<!-- 基础工具，几乎所有场景都需要 -->
<dependency>
    <groupId>com.zifang</groupId>
    <artifactId>z-util-core</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>

<!-- 例如：纯内存缓存 -->
<dependency>
    <groupId>com.zifang</groupId>
    <artifactId>z-util-cache</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>

<!-- 例如：分布式 ID -->
<dependency>
    <groupId>com.zifang</groupId>
    <artifactId>z-util-distribute</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>

<!-- 例如：JSON 解析（自研 ANTLR 版） -->
<dependency>
    <groupId>com.zifang</groupId>
    <artifactId>z-util-parser-json</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```

> 提示：父 POM 已在 `dependencyManagement` 中统一维护了 `com.zifang:z-util-*` 的版本号，如果你把父 POM 也 import 进来，子模块里可以省略 `<version>`。

---

## 构建与测试

### 完整构建（包含单元测试）

```bash
mvn clean install
```

### 跳过测试的构建

```bash
mvn clean install -DskipTests=true
```

### 仅构建某个子模块（带依赖）

```bash
# 仅构建 z-util-core 及其依赖
mvn -pl z-util-core -am clean install

# 仅构建 z-util-ml，并把依赖装到本地
mvn -pl z-util-ml -am clean install
```

### 运行测试

```bash
# 全部测试
mvn test

# 某个测试类
mvn test -Dtest=TestClassName

# 某个测试方法
mvn test -Dtest=TestClassName#testMethod

# 仅在指定模块跑测试
mvn -pl z-util-math test
```

### 运行集成测试（`z-util-it`）

`z-util-it` 默认 **不跑**（避免污染日常 build，被锁/IO/时序拖慢），需要时显式打开：

```bash
mvn -pl z-util-it -Pit test
```

### 升级版本号

通过 `versions-maven-plugin` 一键升级：

```bash
mvn versions:set -DnewVersion=1.0.3-SNAPSHOT
```

修改完后可以执行 `mvn versions:commit` 提交，或 `mvn versions:revert` 回退。

### 辅助脚本

仓库根目录还提供了几个本地脚本：

```bash
./build.sh         # 调 mvn install
./push.sh          # 调 mvn deploy
python add_javadoc.py         # 给指定类补 Javadoc
python fix_pkg.py             # 批量修正包名
python fix_unicode_escapes.py # 修正 Unicode 转义
```

---

## 各模块使用指南

下面给出每个子模块的**功能摘要 + 快速示例**。完整 API 请以各模块的 `README` 与源码为准。

### z-util-core（基础工具）

> 包路径：`com.zifang.util.core.*`
> 几乎所有其他子模块都依赖它。

包含 **30+ 个** 主题工具类：

| 主题 | 关键类 | 说明 |
|---|---|---|
| 字符串/校验 | `StringUtil` / `Assert` / `Validator` / `Conditions` | 判空、判长、判格式、断言式校验 |
| 集合 | `CollectionUtil` / `Lists` / `Maps` / `Sets` / `Venn` / `Streams` | 借鉴 Guava/commons-collections，支持韦恩图操作 |
| 基本类型 | `Ints` / `Longs` / `Doubles` / `Bytes` / `Booleans` / `BitUtil` | 对应包装类的 `List` 化、位运算 |
| 反射/类加载 | `ClassUtil` / `ReflectUtil` / `ClassLoaderUtil` / `PackageScanner` | 扫描包、调用方法、读写字段 |
| 动态类 | `DynamicClass` / `DynamicClassUtil` / `DynamicField` / `DynamicMethod` | 运行时动态生成类 |
| 元编程 | `AnnotationUtil` / `CombinationAnnotationElement` | 注解合并读取 |
| 元组 | `Tuples` / `Pair` / `Triplet` / … `Decade` | 1~10 元组 |
| 字符串相似度 | `StringSimilarity` | Levenshtein 等 |
| 随机 | `RandomUtil` | 各种随机数/字符串 |
| 时间 | `StopWatch` | 计时器 |
| 重试 | `Retry` | 失败重试 |
| 进程 | `ProcessExecutor` | 启动子进程 |
| 脚本 | `ScriptUtil` | 执行 JS/Groovy |
| 事件 | `EventBus` | 简易事件总线 |
| 枚举 | `EnumUtil` | 枚举遍历 |
| 哈希 | `HashUtil` / `ConsistentHash` / `Counter` / `BloomFilter` | 一致性哈希、计数器、布隆过滤器 |
| 数字 | `NumberUtil` / `BigDecimalUtil` | 数字/金额操作 |
| 字符串 | `CharUtil` | 字符判断 |
| XML | `XmlUtil` | XML 读写 |
| 动态 | `DynamicClass` | 动态类生成 |
| 实体 | `BeanUtil` | 反射拷贝 |
| 雪花 ID | `SnowFlakeIdUtil` / `SnowFlakeIdWorker` | Snowflake ID 生成 |
| 通用对象 | `ObjectUtil` | 对象工具 |
| 锁 | `DistributedLock` / `FileDistributedLock` | 分布式锁（基于文件） |
| 特征开关 | `FeatureManager` / `FeatureStore` | 特性开关 |
| 邮件 | `Mail` / `SendMailUtil` | 发送邮件 |
| 限流 | `RateLimiter` / `TokenBucketRateLimiter` / `SlidingWindowRateLimiter` | 限流 |
| 弹性 | `CircuitBreaker` / `Retry` / `Bulkhead` / `TimeLimiter` | 熔断/重试/隔离/超时 |
| 任务调度 | `SchedulerManager` / `Job` / `Trigger` / `CronTrigger` | 自研调度器 |
| 责任链 | `Chain` / `ChainBuilder` / `ChainExecutor` | 责任链模式 |
| 对象池 | `ObjectPool` / `KeyedObjectPool` / `SoftReferenceObjectPool` | 各种对象池 |
| 设计模式 | `Command` / `Composite` / `State` / `Memento` / `Spi` / `Register` | 23 种模式样例 |
| 注解 IoC | `AnnotationConfigApplicationContext` | 极简注解容器 |
| JWT | `Jwt` / `Claims` / `HmacSha256` / `HmacSha512` | JWT 签发与校验 |
| IO | `FileUtil` / `FileDirUtil` / `FilePathUtil` / `ZipUtil` / `JarUtil` | 文件/压缩 |
| 并发 | `ThreadUtil` / `NameThreadFactory` / `ThreadLocalMapUtil` | 线程工具 |
| 加密 | `Base64Utils` / `DESUtils` / `MD5Utils` / `RsaUtil` / `HMAC` | 常用加密 |
| 转换 | `Converters` / `ConvertCaller` | 类型转换 |
| 网络 | `NetClient` / `NetServer` / `NetworkUtil` / `UrlUtil` | 网络工具 |
| 异常 | `BaseException` / `BusinessException` / `ExceptionUtil` | 统一异常 |
| 分页 | `PageRequest` / `PaginationResponse` | 通用分页 |
| 通用响应 | `BaseResponse` / `Result` / `ResultCode` | 统一返回结构 |

**快速示例：**

```java
import com.zifang.util.core.lang.StringUtil;
import com.zifang.util.core.lang.collection.Lists;
import com.zifang.util.core.lang.collection.Venn;

List<String> a = Lists.of("a", "b", "c");
List<String> b = Lists.of("b", "c", "d");

List<String> inter = Venn.intersection(a, b);   // [b, c]
List<String> union = Venn.union(a, b);         // [a, b, c, d]
List<String> diff  = Venn.difference(a, b);    // [a]

boolean blank = StringUtil.isBlank("  ");       // true
```

---

### z-util-cache（缓存）

> 包路径：`com.zifang.util.cache.*`
> 对标 Google Guava Cache / Caffeine，提供 TTL + LRU/W-TinyLFU。

- **`MemoryCache`** — 纯内存 KV 缓存，线程安全
- **`CacheBuilder`** — 链式构建：最大容量、过期时间、淘汰策略、移除监听器
- **`CacheManager`** — 多级缓存管理 + 命名空间
- **`LoadingCache`** / **`LoadingMemoryCache`** — 自动加载语义
- **`WTinyLfuCache`** — 高命中率的 TinyLFU 实现
- **装饰器**：`BoundedCache` / `ForwardingCache` / `MeteredCache` / `NullCache` / `TransactionalCache`
- **`CountMinSketch`** — 频率估计

**示例：**

```java
MemoryCache<String, Object> cache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(5, TimeUnit.MINUTES)
    .removalListener((k, v, cause) -> System.out.println("evicted: " + k))
    .build();

cache.put("k", "v");
String v = cache.getIfPresent("k");
cache.invalidate("k");
```

---

### z-util-ioc（容器）

> 包路径：`com.zifang.util.ioc.*`
> 轻量级 IoC 容器，对标 Guice / Spring IoC，无第三方 IOC 依赖，支持 JSR-330 + JSR-250。

- **绑定**：`Module` / `AbstractModule` / `Binder` / `BindingBuilder` / `ConstantBindingBuilder`
- **作用域**：`Scopes`（Singleton / Prototype）
- **注解**：`@Bean` / `@Component` / `@Configuration`
- **注入点**：`Injector` / `InjectionPoint` / `ConstructorInjector` / `FieldInjector`
- **元数据**：`BeanDefinition` / `BeanRegistry` / `BindingKey`
- **异常**：`NoSuchBeanException` / `CircularDependencyException` / `BeanCreationException`
- **AOP 集成**：`AopModule` / `AopProxyPostProcessor` / `MethodMatcher`
- **生命周期**：`LifecycleManager`
- **入口**：`Injector` / `ClassPathApplicationContext`

---

### z-util-aop（AOP）

> 包路径：`com.zifang.util.aop.*`

- **`@Advise`** / **`Intercept`** — 切面/拦截器注解
- **`ProxyFactory`** — 代理工厂
- **日志切面**：`@Loggable` / `LoggableAdvise`

---

### z-util-proxy（动态代理与字节码）

> 包路径：`com.zifang.util.proxy.*`
> 同时承担 **代理工厂** 与 **自研字节码解析** 两类能力。

代理：

- **`ProxyFactory`** — 统一入口（自动选 JDK/CGLIB）
- **`JdkProxyFactory`** / **`CglibProxyFactory`**
- **`JdkInterceptor`** / **`CglibInterceptor`**
- **`ProxyUtil`** — 静态工具
- 切面：`Aspect` / `SimpleAspect` / `TimeRecordAspect`

自研字节码（class 文件级）：

- 常量池：`Utf8Info` / `Integer` / `Float` / `Long` / `Double` / `Class` / `String` / `FieldRef` / `MethodRef` / `InterfaceMethodRef` / `NameAndType` / `MethodHandle` / `MethodType` / `InvokeDynamic`
- 属性表：`Code` / `ConstantValue` / `LineNumberTable` / `LocalVariableTable` / `SourceFile` / `Exceptions` / `InnerClasses` / `Synthetic` / `Deprecated`
- 模型：`ClassFile` / `ClassAttribute` / `MethodInfo` / `FieldInfo` / `Interface`
- 工具：`AccessFlagConvertor` / `ParamsConvertor` / `CodeConvertor` / `DataHandler` / `OperandBytesJudge`
- 解析器：`ByteCodeResolver`

> 适合用作「读懂 class 文件 / 实现轻量级字节码增强」的学习样例。

---

### z-util-validation（校验）

> 包路径：`com.zifang.util.validation.*`
> 通用校验引擎，对标 Hibernate Validator。

- 注解：`@NotNull` / `@Pattern` / `@Length` / `@Range` / `@Email`
- 校验器：`NotNullValidator` / `PatternValidator` / `LengthValidator` / `RangeValidator` / `EmailValidator`
- 引擎：`ValidationEngine` / `Validator` / `ValidateResult` / `ValidationException`

```java
ValidationEngine engine = new ValidationEngine();
engine.addValidator(new NotNullValidator());
engine.addValidator(new PatternValidator("^\\d{6}$"));

ValidateResult r = engine.validate("110101");
if (!r.isValid()) {
    System.out.println(r.getErrors());
}
```

---

### z-util-dsl（自研 DSL）

> 包路径：`com.zifang.util.dsl.*`

- **核心**：`Lexer` / `Parser` / `TokenReader` / `ASTNode` / `ASTFactory`
- **G4 集成**：`G4FileParser` / `DynamicLexer` / `DynamicParser`（可解析 ANTLR g4 文本动态生成 lexer/parser）
- **Token**：`Token` / `TokenType` / `SimpleToken`
- **AST**：`SimpleASTNode`

> 适合作为「自己实现 DSL 框架」的参考实现。

---

### z-util-parser（多格式解析器）

> 父模块：`z-util-parser`，每个子模块独立发布。

| 子模块 | 说明 | 特色 |
|---|---|---|
| [z-util-parser-json](./z-util-parser/z-util-parser-json) | JSON 解析与美化 | ANTLR g4 + 自研 `JsonObject`/`JsonArray` 模型 + `BeautifyJsonUtils` |
| [z-util-parser-xml](./z-util-parser/z-util-parser-xml) | XML 解析、XPath 查询、格式化 | 自研 Tokenizer + `XPathQuery` |
| [z-util-parser-yaml](./z-util-parser/z-util-parser-yaml) | YAML 解析 | ANTLR + 外观模式 `YamlFacade`（默认走 SnakeYAML） |
| [z-util-parser-csv](./z-util-parser/z-util-parser-csv) | CSV 读写 | ANTLR + `CsvReader`/`CsvWriter` |
| [z-util-parser-ini](./z-util-parser/z-util-parser-ini) | INI 解析 | ANTLR + `IniFile`/`IniSection` |
| [z-util-parser-properties](./z-util-parser/z-util-parser-properties) | .properties 解析 | ANTLR |
| [z-util-parser-proto](./z-util-parser/z-util-parser-proto) | Protobuf .proto 解析 | 支持 message / enum / rpc / service |
| [z-util-parser-toml](./z-util-parser/z-util-parser-toml) | TOML 解析 | ANTLR |

**JSON 快速示例：**

```java
JSONParser parser = new JSONParser();
JsonNode node = parser.parse("{\"a\":1,\"b\":[true,false]}");
System.out.println(node.get("a").asInt());     // 1
System.out.println(node.get("b").size());      // 2

// 美化
String pretty = BeautifyJsonUtils.beautify(jsonStr);
```

---

### z-util-expr（表达式引擎）

> 父模块：`z-util-expr`，每个子模块对应一个脚本引擎。

| 子模块 | 引擎 | 入口 |
|---|---|---|
| [z-util-expr-el](./z-util-expr/z-util-expr-el) | EL（统一表达式语言） | `ElEvaluator` |
| [z-util-expr-groovy](./z-util-expr/z-util-expr-groovy) | Groovy 脚本 | `GroovyExecutor` |
| [z-util-expr-js](./z-util-expr/z-util-expr-js) | JS（ANTLR 自研 + PlayScript 参考） | `SimpleScript` / `SimpleParser` / `SimpleCalculator` / `Lexer` / `CommandParser` |
| [z-util-expr-lua](./z-util-expr/z-util-expr-lua) | Lua 脚本 | `LuaExecutor` |
| [z-util-expr-sql](./z-util-expr/z-util-expr-sql) | SQL 解析 | `SqlParser` / `SqlFunctionRegistry` |

**JS 表达式快速示例：**

```java
// 自研脚本引擎
SimpleScript script = new SimpleScript("a + b * 2");
Map<String, Object> env = new HashMap<>();
env.put("a", 1);
env.put("b", 2);
Object r = script.execute(env);  // 5
```

---

### z-util-workflow（工作流）

> 包路径：`com.zifang.util.workflow.*`

- **核心模型**：`Engine` / `WorkflowConfiguration` / `NodeLifeCycle` / `ExecutableWorkflowNode` / `WorkflowNode` / `Connector` / `CacheEngine` / `Configurations`
- **上下文**：`WorkFlowApplication` / `WorkFlowApplicationBuilder` / `WorkFlowApplicationContext` / `Task`
- **执行引擎**：
  - `JavaEngine`（含 `JavaHandle`）
  - `PythonEngine`
  - `SparkEngine` + `AbstractSparkEngine` + `AbstractSparkEngineService` + `CacheEngineService`
  - 通用 `AbstractEngine` / `AbstractEngineService` / `EngineFactory`
- **运行时**：`WorkflowRuntimeEngine` / `GatewayEvaluator` / `ExecutionResult`
- **BPMN**：`Bpmn` / `BpmnModelConverter` / `BpmnXmlParser` / `BpmnDiagram` / `BpmnProcess` / `BpmnFlowNode` / `BpmnSequenceFlow`
- **Spark 算法节点**：`JoinHandler` / `FilterHander` / `OrderLimitHander` / `PivotHandler` / `CodificationHandler` / `WindowHandler` / `SqlHandler` / `UdfHandler` … 18+ 个
- **持久化**：`FileWorkflowPersistencePlugin` / `WorkflowPersistencePlugin` / `WorkflowSnapshot`
- **JSON**：`WorkflowConfigurationSerializer`
- **AOP 拦截**：`EngineService` / `EngineServiceInterceptor`

> 适合做「拖拽式工作流 / 规则引擎」的可执行参考实现。

---

### z-util-http（HTTP 与 Netty）

> 包路径：`com.zifang.util.http.*`

- **注解式 HTTP 客户端**（仿 SpringMVC 风格）：`@RequestMapping` / `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping` / `@PatchMapping` / `@RequestBody` / `@RequestParam` / `@PathVariable` / `@RequestHeader` / `@MatrixVariable` / `@ModelAttribute` / `@RestController` / `@RestControllerAdvice` / `@InitBinder` / `@ExceptionHandler` / `@ResponseBody` / `@ResponseStatus` / `@RequestPart` / `@RequestAttribute` / `@RequestHeaders` / `@BasicAuth` / `@CookieValue`
- **客户端核心**：`HttpExecutor` / `HttpRequestInvocationHandler` / `HttpExecutionResult`
- **POJO**：`HttpRequestBody` / `HttpRequestHeader` / `HttpRequestLine` / `HttpRequestDefinition`
- **简易 HTTP 服务端**：`AllPathHttpServer` / `HttpServerBuilder` / `HttpServerInvocationHandler` / `HttpServerProxy` / `HttpServerProxyFactory` / `HttpServerRequestHandler`
- **curl 解析**：`CurlBuilder` / `CurlLexer` / `CurlParser` / `CurlParserFactory` / `CurlParserUtils` / `CurlTokenParser`
- **Netty IM 示例工程**（基于「Netty 入门与实战：仿写微信 IM」）
  - `resources/netty/im/ch2~ch9`：从 Bio/Nio 入门到自定义协议实现
  - 包含 `Packet` / `Command` / `Serializer` / `LoginRequestPacket` / `PacketCodeC` / `LoginUtil` / `Attributes` / `Session` / `SessionUtil` 等

**注解式 HTTP 客户端示例：**

```java
public interface GitHubService {
    @GetMapping("https://api.github.com/repos/{owner}/{repo}")
    String getRepo(@PathVariable("owner") String owner,
                   @PathVariable("repo")  String repo);
}

GitHubService gh = HttpExecutor.createProxy(GitHubService.class);
String body = gh.getRepo("yuku123", "z-util");
```

---

### z-util-crawler（爬虫与 CUA）

> 包路径：`com.zifang.util.crawler.*`

- **浏览器渲染**：`BrowserClient` / `ChromeDriverHolder` / `JsEvaluator`
- **CUA 智能体**：`CuaAgent` / `RuleBasedPlanner` / `OperationRegistry` / `CuResult` / `StepResult`
  - 步骤：`NavigateStep` / `ClickStep` / `InputStep` / `ExtractStep` / `WaitStep` / `ScreenshotStep` / `SwitchWindowStep`
- **HTTP 客户端**：`CrawlerHttpClient`
- **解析**：`HtmlParser` / `JsonExtractor`
- **管道**：`CrawlerPipeline` / `PipelineContext` / `Processor`

---

### z-util-jdbc（数据库与事务）

> 包路径：`com.zifang.util.db.*`
> 对标 MyBatis / Spring Data，自研极简 ORM 框架。

- **数据源**：`DataSourceContext` / `DataSourceManager` / `DatasourceContextManager` / `DatasourceFactory`
- **注解**：`@Select` / `@Insert` / `@Update` / `@Delete` / `@Param` / `@Transactional` / `@Propagation` / `@Isolation`
- **SQL 构造**：`SqlBuilder` / `SqlGenerator` / `SqlAnalyser`
- **代码生成**：`JpaStratege` / `MybaitsStratige` + FreeMarker 模板（entity/mapper/mapperXml/service/serviceImpl）
- **元数据**：`ColumnDTO` / `DataSourceDTO` / `DatabaseDTO` / `TableDTO` / `DataSourceTableColumnDTO` / `DataSourceTableDTO`
- **仓储**：`CrudRepository` / `Repository` / `MetaRepository` / `BoundSql` / `ResultSetHandler` / `RepositoryProxy` / `BaseRepositoryInvocationHandler` / `BaseRepositoryAspect`
- **事务**：`TransactionManager` / `TransactionTemplate` / `TransactionInterceptor`
- **锁**：`DbDistributedLock`
- **序列**：`Sequence` / `SnowflakeSequence` / `DatabaseSequence` / `NodeIdAllocator`
- **SQL 执行**：`SqlExecutor`
- **监控**：`DatasourceDescription` / `MetadataDescription`

---

### z-util-math（数学与数据分析）

> 包路径：`com.zifang.util.numpy` / `com.zifang.util.pandas`
> 对标 NumPy / Pandas 的 Java 数学计算库。

- **`NdArray` / `Num`** — 多维数组
- **`Series`** — 一维带标签数组
- **`DataFrame`** — 二维表格数据
- **`Numpy` / `Nums`** — 数组创建（zeros/ones/eye/arange/linspace）
- **`Maths`** — 三角、指数、对数、舍入、统计、常量
- **`Linalg`** — 矩阵乘法、分解（SVD/QR/Cholesky）、求逆、解方程、范数
- **`Matrix` / `MatrixUtil` / `MatrixGenerator`**
- **`DType` / `Shape` / `Slice` / `Array` / `Fft`**
- **统计**：`Correlation`
- **时间序列**：`DateTimeIndex`
- **离散化**：`Discretizer`
- **插值**：`Interpolator`
- **窗口**：`Rolling` / `Expanding` / `EWM`
- **重塑**：`Reshaper`
- **IO**：`CSVReader` / `CSVWriter`
- **字符串**：`StringAccessor`
- **随机**：`NumRandom` / `NumberGennerator`
- **Pandas 入口**：`Pandas`

> 详细 API 见 [z-util-math/README.MD](./z-util-math/README.MD)。

---

### z-util-ml（机器学习）

> 包路径：`com.zifang.util.ml.*`

**神经网络**（`nn`）：

- 层：`Dense` / `Conv2d` / `AvgPool2d` / `MaxPool2d` / `Dropout` / `Flatten` / `Reshape` / `Softmax` / `BatchNorm2d` / `LayerNorm`
- 模型：`Sequential` / `GAN` / `VAE` / `TransformerEncoder`
- 循环：`RNN` / `LSTM`
- 激活：`ReLU` / `LeakyReLU` / `ELU` / `GELU` / `Tanh` / `Softplus` / `Swish`

**经典网络**（`nnet`）：`NeuralNetwork` / `NeuralNet` / `Layer` / `Neuron` / `ActivationFunction` / `ReLUActivation` / `SigmoidActivation` / `HiddenLayer(Impl)` / `InputLayer` / `OutputLayer` / `LossFunction` / `MSELoss`

**损失**（`loss`）：`L1Loss` / `MSELoss` / `BinaryCrossEntropyLoss` / `BCEWithLogitsLoss` / `CrossEntropyLoss` / `LossFunction`

**优化器**（`optim`）：`SGD` / `Adam` / `Adagrad` / `RMSprop` + 调度器 `StepLR` / `CosineAnnealingLR` / `ReduceLROnPlateau` / `LrScheduler` / `Parameter`

**树模型**（`tree`）：`DecisionTree` / `RandomForest` / `XGBoost` / `AdaBoost`

**集成**（`ensemble`）：`Bagging` / `Stacking` / `Voting` + `Estimator`

**聚类**（`clustering`）：`KMeans` / `DBSCAN` / `HierarchicalClustering` / `GMM`

**降维**（`decomposition`）：`PCA` / `tSNE` / `UMAP`

**线性模型**（`linear`）：`LinearRegression` / `LogisticRegression` / `Perceptron` / `SVM`

**异常检测**（`anomaly`）：`LOF` / `IsolationForest` / `OneClassSVM`

**关联规则**（`association`）：`Apriori` / `FPGrowth`

**强化学习**（`rl`）：`QLearning` / `SARSA` / `DQN` / `PolicyGradient`

**序列模型**（`sequence`）：`HMM` / `CRF`

**遗传算法**（`ga`）：`GeneticAlgorithmEngine` / `Individual` / `Population` / `BinaryGenotype` / `CrossoverOperator` / `SinglePointCrossover` / `MutationOperator` / `BinaryMutation` / `SelectionOperator` / `FitnessFunction` / `TerminationCondition` / `TargetFitnessTermination`

**数据**（`data`）：`Dataset` / `TensorDataset` / `DataLoader`

> 适合作为「教学/练手」性质的 ML 工具集，不追求工业级性能，但能跑通端到端流程。

---

### z-util-office（Office 文档）

> 包路径：`com.zifang.util.office.*`

- **Excel**：`ExcelUtils`（基于 Apache POI）
- **PDF**：`PoiUtils` / `PdfUtil` / `PdfOperator` / `PdfConverter` / `PdfEditor` / `PdtExtractor`（抽取/转图片/编辑）
- **Word**：`TocUpdateDemo` / `U`（含 `Tuple` 辅助）
- **图片转 PDF**：`ImagePdfTest`

```java
ExcelUtils.readExcel(file);
PdfConverter.toImage(pdfFile, outputDir);
String text = PdtExtractor.extract(pdfFile);
```

---

### z-util-media（图像与媒体）

> 包路径：`com.zifang.util.media.graph.*`

- **图像**：`ImageUtil` / `ImageProcessor` / `ImageReadWrite` / `ImageCompare` / `ImageCaptcha`
- **验证码**：`CaptchaUtil`
- **颜色**：`ColorUtil`（Hex/RGB/HSL 互转）
- **GIF**：`GifBuilder` / `GifEncoder` / `Quant` / `Encoder`
- **二维码**：`QRCodeUtil` / `QRCodeEncoder` / `ReedSolomonEncoder` / `BitMatrix` / `ErrorCorrectionLevel` / `MatrixToImageWriter` / `MatrixToImageWriterEx` / `MatrixToImageConfig` / `MatrixToLogoImageConfig`
- **解码**：`Binarizer` / `BinaryBitmap` / `FinderPatternFinder` / `QRCodeDecoder`

---

### z-util-visualization（可视化）

> 包路径：`com.zifang.util.visuallization.*`

- **图表（Swing）**：`BarChart` / `LineChart` / `ChartFrame` / `ChartSeries` / `ChartColors` / `NetworkGraph`
- **可视化辅助**：`GAVisualizer`（遗传算法）/ `NNVisualizer`（神经网络）
- **算法可视化**：`lesson2/3` 多个 `AlgoVisualizer` + `AlgoFrame` + `AlgoVisHelper`（刘汝佳算法课样例）
- **机器人框架**：`QinMaRobot` / `Robots` / `RobotEngine` / `OperationAction` / `OperationActionChain` / `OperationEnum` / `RobotPrintILoveYou` / `RobotTest` / `RobotTest2`
- **Swing 面板管理**：`App` / `ManagerFrame` / `SubPanelRegister` / `CommonPanel` / `UserObject` / `TreeComponent` / `TreeNode` / `RegisterTreeNode` / `RegisterTreeNodeHelper`

---

### z-util-monitor（监控）

> 包路径：`com.zifang.util.monitor.*`

- **JVM**：`JvmMonitor` + 各种 `MXBean` 演示（ClassLoading / Compilation / GC / Memory / MemoryManager / MemoryPool / OS / Runtime / Thread）+ 引用类型（强/软/弱/虚）Demo
- **线程池监控**：`ThreadMonitor` / `MonitorableExecutor` / `FixedMonitorableExecutor` / `ExecutorManager` / `ThreadPoolConfigUnit`
  - 告警：`AlarmPolicy` / `AlarmService` / `Alarmable` / `LogAlarmService` / `ThreadPoolOvertimeAlarmPolicy`
  - 状态：`Status` / `StatusLevel` / `ThreadPoolStatus` / `MonitorManager` / `Monitorable`
  - 任务与工具：`MonitorTask` / `DateUtils` / `TimeUtil` / `MonitorConstant`
- **OS**：`OsMonitor`
- **网络**：`NetMonitor`
- **通用**：`MetricsCollector` / `MetricsRegistry` / `MetricsSnapshot` / `MonitorServer`
- **导出**：`HtmlExporter` / `JsonExporter`

---

### z-util-devops（运维）

> 包路径：`com.zifang.util.devops.*`

- **Docker**：`DockerClient` / `DockerCommandClient` / `DockerCommandResult` + DTO（Container / Image / Network / Volume）
- **Git（命令实现）**：`GitClient` / `GitException` / `GitResult` + 领域模型（`GitRepository` / `GitCommit` / `GitBranch` / `GitTag` / `GitStatus` / `GitDiffEntry` / `GitAuthor` / `GitRemote`）
- **JGit 实现**：`JGitExecutor`
- **Shell 实现**：`ShellExecutor`
- **GitHub API 包装**（`git/github`）：
  - `GithubApiWrapper` + 各大子模块：repo / pr / issue / release / org / user / action / config / holder
- **Nexus**：`NexusComponentManager` / `NexusConfig` + 资产模型（`Component` / `Asset` / `Checksum`）
- **GAV**：`GavInfo`

---

### z-util-source（字节码与源码）

> 包路径：`com.zifang.util.source.*`

- **字节码模型**：`ClassInfo` / `FieldInfo` / `MethodInfo` / `AnnotationInfo` / `MethodParameterPair` / `ModifierAdapter`
- **字节码信息池**：`ClassInfoPool` / `ClassInfoDiffer` / `FieldDiffer` / `MethodDiffer`
- **字节码解析**：`ByteCodeParser` / `ByteCodeParserImpl` / `SourceCodeParser`
- **字节码生成**：`ByteCodeGenerator` / `ByteCodeGeneratorImpl` / `JavaSourceGenerator`
- **编译时**：`CompileContext` / `SourceJavaFileObject` / `CharSequenceJavaFileObject` / `CustomerCompileClassLoader` / `CustomerCompileJavaFileManager`
- **分析上下文**：`AnalysisContext`
- **测试**：`A.java`（提供 `A.java` 样本）

---

### z-util-distribute（分布式 ID）

> 包路径：`com.zifang.util.distributes.sequence.*`

- **`SnowflakeIdWorker`** — Twitter Snowflake 实现（41bit 时间戳 + 10bit 机器 + 12bit 序列）
- **`SegmentIdGenerator`** — 号段模式
- **`NanoId`** — URL 友好的短 ID
- **`UuidV7`** — UUID v7（时间有序）
- **`Sequence`** — 顺序号生成
- **`SystemClock`** — 高性能系统时钟（解决 `System.currentTimeMillis()` 性能问题）

```java
SnowflakeIdWorker id = new SnowflakeIdWorker(workerId: 1, datacenterId: 1);
long next = id.nextId();
```

---

### z-util-cli（命令行解析）

> 包路径：`com.zifang.util.cli.*`
> 对标 Apache Commons CLI。

- **解析器**：`CommandLineParser` / `Parser` / `BasicParser` / `PosixParser` / `GnuParser` / `DefaultParser`
- **模型**：`Options` / `Option` / `OptionGroup` / `CommandLine` / `DeprecatedAttributes`
- **帮助**：`HelpFormatter`
- **类型转换**：`TypeHandler` / `PatternOptionBuilder`
- **异常**：`ParseException` / `MissingOptionException` / `MissingArgumentException` / `UnrecognizedOptionException` / `AmbiguousOptionException` / `AlreadySelectedException`
- **入口**：`CLI`

```java
Options options = new Options();
options.addOption("h", "help", false, "show help");
options.addOption("f", "file", true, "input file");

CommandLineParser parser = new PosixParser();
CommandLine cmd = parser.parse(options, args);
```

---

### z-util-ch（中文工具）

> 包路径：`com.zifang.util.ch.*`

- **`PinyinGeneratorUtil`** — 汉字 → 拼音（全拼/简拼/带声调）
- **`IdcardUtil`** — 身份证号校验、提取出生日期/性别/地区
- **`MoneyUtil`** — 数字 ↔ 中文大写金额

```java
String pinyin = PinyinGeneratorUtil.toPinyin("你好");      // "ni hao"
String shortP = PinyinGeneratorUtil.toPinyin("北京", true); // "BJ"
boolean ok = IdcardUtil.isValid("110101199001011234");
String upper = MoneyUtil.toChineseUpper(1234.56);          // "壹仟贰佰叁拾肆元伍角陆分"
```

---

### z-util-zex（练习场）

> 包路径：`com.zifang.util.zex.*`

一个「沙盒」模块，集中放平时写的小练习与样例：

- **`bust`** — 《码出高效》等书章节的样例代码（基础语法/集合/泛型/IO/NIO/并发/注解/网络）
- **`sort`** — 各种排序（冒泡/选择/快排/双轴快排/归并/堆排/希尔）
- **`leetcode`** — LeetCode 题解（题号命名的类 + 题解 `.md`）
- **`guava`** — Guava 各组件实战（Joiner/Splitter/Cache/Multimap/Multiset/Table/Range/BiMap/ClassToInstanceMap）
- **`source`** — BitSet / Integer 缓存 / StampedLock / SynchronousQueue / ThreadLocal / Timer
- **`bytecode`** — ASM / Javassist（`ctclass`）示例
- **`disrupt`** — Disruptor 入门
- **`dict`** — 字典/排序训练
- **`interview`** — 面试手写（链表/代理/手写 RPC 等）
- **`HttpClientUtils`** — HttpClient 工具
- **`TransformMobi2Epub`** — mobi → epub 转换
- **`ClassLayoutTest`** — JVM 对象布局

> 这里代码不一定经过严格测试，主要用作「自留地」与「面试复盘」。

---

### z-util-it（集成测试）

> 跨子模块的端到端验证（依赖 `z-util-core` / `z-util-cache` / `z-util-aop` / `z-util-distribute` / `z-util-jdbc` + H2）。
> 默认 profile 关闭测试，使用 `-Pit` 开启：

```bash
mvn -pl z-util-it -Pit test
```

---

### z-util-all（聚合入口）

> 纯 `pom` 模块，把所有 `z-util-*` 放进 `dependencyManagement`。
> 建议在父工程中以 `<scope>import</scope>` 方式引入，简化版本管理。

---

## 架构与设计

### 1. 模块依赖图（精简）

```
                       z-util-core
                            │
   ┌──────────┬─────────────┼──────────────┬────────────┐
   │          │             │              │            │
z-util-cache  z-util-proxy  z-util-validation  z-util-parser  ...
               │   │
        ┌──────┴───┴──────┐
        │   z-util-ioc    │  (proxy + aop)
        │   z-util-aop    │
        └─────────────────┘

z-util-ml / z-util-math / z-util-workflow / z-util-http / z-util-crawler
                            │
                       z-util-core
```

> 完整依赖以各子模块的 `pom.xml` 为准。

### 2. 设计模式应用

`z-util-core` 中以「设计模式」分包组织代码，可在以下包名直接定位：

- `pattern.chain` — 责任链
- `pattern.command` — 命令模式 + 自研栈机
- `pattern.composite` — 组合模式（树/图/网）
- `pattern.factory` — 工厂
- `pattern.ioc` — 控制反转
- `pattern.memento` — 备忘录
- `pattern.pool` — 对象池（含监控）
- `pattern.register` — 注册表
- `pattern.spi` — SPI 加载
- `pattern.state` — 状态机

### 3. 命名约定

- 静态工具类统一以 `*Util` / `*Utils` 结尾（`StringUtil`、`ExcelUtils` …）
- 构造器 / 工厂类以 `*Factory` / `*Builder` 结尾（`ProxyFactory`、`GifBuilder` …）
- 算法实现以领域名直接命名（`LSTM` / `XGBoost` / `Apriori` …）
- 注解采用 JSR-330 / JSR-250 标准或自研 `@Component` / `@Bean` / `@Configuration`
- 业务异常继承 `BaseException` / `BusinessException`

### 4. 日志约定

- 日志门面统一使用 SLF4J 1.7.36，运行期推荐 Log4j2 2.25.4（参考 `z-util-core` 的 `log4j2.xml`）。
- 取 logger 的标准写法：`private static final Logger log = LoggerFactory.getLogger(Xxx.class);`，也可以直接使用 `com.zifang.util.core.trace.log.Logs` 的工具方法。
### 5. 第三方依赖原则

项目对第三方依赖的态度是 **「能不引就不引，能自己写就自己写」**：

1. **同名依赖统一取最高版本** — 父 POM 的 `dependencyManagement` 是唯一来源，子模块不写 `<version>` 即可复用。
2. **能用自研实现的就不引三方**，对照表如下：

   | 领域 | 我们自己的实现 | 可以替代的第三方 |
   |---|---|---|
   | 字符串 / 判空 / 断言 / `isBlank` / `isNotBlank` | `com.zifang.util.core.lang.StringUtil` / `Assert` | commons-lang3（**已自研替代**） |
   | CPU 架构探测 `ArchUtils.getProcessor()` | `com.zifang.util.core.sys.ArchUtils` / `Processor` / `Arch` | commons-lang3（**已自研替代**） |
   | 集合 / Venn / Tuple | `CollectionUtil` / `Venn` / `Tuples` | commons-collections、Guava Collections |
   | 文件 / IO | `FileUtil` / `ZipUtil` / `JarUtil` | commons-io |
   | Base64 / MD5 / RSA / DES | `core.security.*` | commons-codec |
   | Bean 拷贝 / 反射 | `BeanUtil` / `ReflectUtil` | commons-beanutils |
   | XML 读写 | `XmlUtil` | dom4j、jdom |
   | 对象池 | `ObjectPool` | commons-pool2 |
   | JWT | `core.security.jwt.*` | nimbus-jose-jwt、jjwt |

3. **保留的第三方依赖只用于「自研成本过高」的场景**：Netty、OkHttp、ANTLR、JAXB、POI、PDFBox、Selenium、JGit、Github-API 等。
   > 注：commons-lang3 的 jar 仍会作为部分三方库（github-api / webdrivermanager / unirest-java）的传递依赖出现在运行时 classpath 中，但我们的源码已经不再直接依赖它。

### 6. 当前依赖管理范围

父 POM `dependencyManagement` 收录 **~50 个** 第三方依赖（去除 lombok / pinyin4j 重复 / 未使用条目后），覆盖：

- **基础设施**：SLF4J 1.7.36、Log4j2 2.25.4（`log4j-slf4j-impl` + `log4j-1.2-api`）
- **测试**：JUnit 4.13.1、JUnit Jupiter 5.10.2、Mockito 4.11.0
- **DI / 校验**：javax.inject 1、javax.annotation-api 1.3.2、validation-api 2.0.1、hibernate-validator 6.2.5
- **工具**：Guava 32.1.2、commons-dbutils 1.8、commons-pool2 2.12、disruptor 3.2
- **数据库**：druid 1.2.23、mongo-java-driver 3.12.14、c3p0 0.9.1.2（JDBC 子模块用）
- **HTTP / 网络**：OkHttp 4.12、httpclient 4.4、httpmime 4.5、netty-all 4.1.66、guice 5.0.1
- **序列化**：fastjson 1.2.83、gson 2.11、jackson-{core,databind} 2.18.6
- **文档 / 渲染**：POI 4.1.2、PDFBox 2.0.31、Docx4j 6.1.2、JAXB 2.3.1
- **解析**：ANTLR 4.13.1
- **脚本引擎**：spring-expression 5.3.39
- **媒体**：zxing 3.5.3、thumbnailator 0.4.20
- **运维**：github-api 1.321、gitlab4j-api 5.2.0、JGit 5.13.4、httpmime
- **爬虫**：selenium-java 3.141.59、webdrivermanager 3.8.1、jsoup 1.18.1
- **字节码**：javassist 3.30.2、jol-core 0.17、javaparser-symbol-solver-core 3.26.3、asm 9.7

> 所有模块的依赖声明都通过父 POM 的 `<dependencyManagement>` 统一管理；子模块不写 `<version>` 即可锁定版本。

---

## 发布到 GitHub Packages

父 POM 已配置 `distributionManagement` 指向 GitHub Packages：

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/yuku123/z-maven-repo</url>
    </repository>
</distributionManagement>
```

### 本地发布

1. 在 GitHub 中创建一个有 `write:packages` 权限的 PAT。
2. 配置环境变量 `MAVEN_GITHUB_TOKEN` / `GITHUB_TOKEN`。
3. 把 token 写入本地 `~/.m2/settings.xml`：

   ```xml
   <settings>
     <servers>
       <server>
         <id>github</id>
         <username>你的 GitHub 用户名</username>
         <password>${MAVEN_GITHUB_TOKEN}</password>
       </server>
     </servers>
   </settings>
   ```

4. 执行：

   ```bash
   mvn clean deploy
   ```

### 通过 CI 发布

仓库内置两个 GitHub Actions 工作流（见 [.github/workflows/](./.github/workflows/)）：

- `maven-publish.yml` — Release 触发，自动发布到 GitHub Packages
- `publish-github-packages.yml` — push main/master 或 `v*.*.*` tag 或手动触发

> GitHub 2023+ 默认 `GITHUB_TOKEN` 是只读，写 Packages 必须在 workflow 中显式声明 `permissions: packages: write`。

---

## 开发约定

- **依赖复用**：所有公共版本号统一在父 `pom.xml` 的 `dependencyManagement` / `build` 节点；子模块新增依赖请优先复用。
- **版本号**：所有子模块的 `<version>` 用 `${util.version}`，不要硬编码。
- **构建顺序**：修改了某个子模块后，建议使用 `mvn -pl <module> -am install` 一起构建依赖。
- **依赖检查**：定期执行 `mvn dependency:analyze` 排查未声明或多余依赖。
- **代码风格**：遵循通用 Java 编码规范；可启用 IDE 的 Checkstyle / Spotless（如需可后续加入）。
- **AI 协作约定**：见 [CLAUDE.md](./CLAUDE.md)；项目笔记见 [note.md](./note.md)。
- **辅助脚本**：`build.sh` / `push.sh` / `add_javadoc.py` / `fix_pkg.py` / `fix_unicode_escapes.py`。

---

## 常见问题

1. **构建失败 / 编译错误**
   - 确认 Maven 3.6+、JDK 8+
   - 删除 `~/.m2/repository/com/zifang` 后重新 `mvn install`
2. **某个测试一直挂起**
   - 一些 `*Test` 用例依赖网络 / 数据库，先 `mvn -DskipTests=true install`
3. **集成测试想跑**
   - `mvn -pl z-util-it -Pit test`
4. **依赖冲突**
   - 父 POM 的 `dependencyManagement` 已固定主流第三方版本，子模块中尽量 **不写版本号** 来复用。
5. **GitHub Packages 401 / 403**
   - 检查 PAT 是否带 `write:packages`；Maven `<server>` id 必须是 `github` 与 POM 中一致。

---

## 许可证

本仓库如未特别声明，按 **MIT License** 处理。子模块内可能包含参考实现的来源注释，使用前请遵守对应来源的协议。
