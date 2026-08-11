# z-util

> 一个面向日常 Java 开发的多模块工具库集合 —— 目标是把零散的「轮子」以 Maven 多模块的方式沉淀下来，按需引用、按需升级。

[![Maven Central](https://img.shields.io/maven-central/v/io.github.yuku123/z-util-all.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.yuku123/z-util-all)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](./LICENSE)
[![JDK](https://img.shields.io/badge/JDK-8%2B-orange.svg)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue.svg)](https://maven.apache.org/)
[![Modules](https://img.shields.io/badge/modules-30+-purple.svg)](./项目地图)

`z-util` 是一组 **Maven 多模块** 的 Java 工具库，当前稳定版 **`1.0.3`**（开发线 `1.0.4-SNAPSHOT`），
由统一的父 POM 管理版本与依赖。每个子模块都专注于一个领域，相互之间保持低耦合：

- 可以 **整体引入** `z-util-all`，一次性拿到所有能力；
- 也可以 **按需引入** 某一个子模块，最小化依赖体积；
- 还可以把部分子模块当作 **学习样例**，研究对应主题的工程实现。

> **最新发布**：[`1.0.3`](https://central.sonatype.com/artifact/io.github.yuku123/z-util-all/1.0.3)
> · GPG 签名验证通过 · deployment `056e55d3-a424-403f-9639-377a0472c4ce`

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

> 包路径：`com.zifang.util.db.*`
> 对标 MyBatis / Spring Data，自研极简 ORM 框架。

- **数据源**：`DataSourceContext` / `DataSourceManager` / `DatasourceContextManager` / `DatasourceFactory`
- **注解**：`@Select` / `@Insert` / `@Update` / `@Delete` / `@Param` / `@Transactional` / `@Propagation` / `@Isolation`
- **SQL 构造**：`SqlBuilder` / `SqlGenerator` / `SqlAnalyser`
- **代码生成**：`JpaStratege` / `MybaitsStratige` + FreeMarker 模板（entity/mapper/mapperXml/service/serviceImpl）
- **元数据**：`ColumnDTO` / `DataSourceDTO` / `DatabaseDTO` / `TableDTO` / `DataSourceTableColumnDTO` /
  `DataSourceTableDTO`
- **仓储**：`CrudRepository` / `Repository` / `MetaRepository` / `BoundSql` / `ResultSetHandler` / `RepositoryProxy` /
  `BaseRepositoryInvocationHandler` / `BaseRepositoryAspect`
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

- 层：`Dense` / `Conv2d` / `AvgPool2d` / `MaxPool2d` / `Dropout` / `Flatten` / `Reshape` / `Softmax` / `BatchNorm2d` /
  `LayerNorm`
- 模型：`Sequential` / `GAN` / `VAE` / `TransformerEncoder`
- 循环：`RNN` / `LSTM`
- 激活：`ReLU` / `LeakyReLU` / `ELU` / `GELU` / `Tanh` / `Softplus` / `Swish`

**经典网络**（`nnet`）：`NeuralNetwork` / `NeuralNet` / `Layer` / `Neuron` / `ActivationFunction` / `ReLUActivation` /
`SigmoidActivation` / `HiddenLayer(Impl)` / `InputLayer` / `OutputLayer` / `LossFunction` / `MSELoss`

**损失**（`loss`）：`L1Loss` / `MSELoss` / `BinaryCrossEntropyLoss` / `BCEWithLogitsLoss` / `CrossEntropyLoss` /
`LossFunction`

**优化器**（`optim`）：`SGD` / `Adam` / `Adagrad` / `RMSprop` + 调度器 `StepLR` / `CosineAnnealingLR` /
`ReduceLROnPlateau` / `LrScheduler` / `Parameter`

**树模型**（`tree`）：`DecisionTree` / `RandomForest` / `XGBoost` / `AdaBoost`

**集成**（`ensemble`）：`Bagging` / `Stacking` / `Voting` + `Estimator`

**聚类**（`clustering`）：`KMeans` / `DBSCAN` / `HierarchicalClustering` / `GMM`

**降维**（`decomposition`）：`PCA` / `tSNE` / `UMAP`

**线性模型**（`linear`）：`LinearRegression` / `LogisticRegression` / `Perceptron` / `SVM`

**异常检测**（`anomaly`）：`LOF` / `IsolationForest` / `OneClassSVM`

**关联规则**（`association`）：`Apriori` / `FPGrowth`

**强化学习**（`rl`）：`QLearning` / `SARSA` / `DQN` / `PolicyGradient`

**序列模型**（`sequence`）：`HMM` / `CRF`

**遗传算法**（`ga`）：`GeneticAlgorithmEngine` / `Individual` / `Population` / `BinaryGenotype` / `CrossoverOperator` /
`SinglePointCrossover` / `MutationOperator` / `BinaryMutation` / `SelectionOperator` / `FitnessFunction` /
`TerminationCondition` / `TargetFitnessTermination`

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
- **二维码**：`QRCodeUtil` / `QRCodeEncoder` / `ReedSolomonEncoder` / `BitMatrix` / `ErrorCorrectionLevel` /
  `MatrixToImageWriter` / `MatrixToImageWriterEx` / `MatrixToImageConfig` / `MatrixToLogoImageConfig`
- **解码**：`Binarizer` / `BinaryBitmap` / `FinderPatternFinder` / `QRCodeDecoder`

---

### z-util-visualization（可视化）

> 包路径：`com.zifang.util.visuallization.*`

- **图表（Swing）**：`BarChart` / `LineChart` / `ChartFrame` / `ChartSeries` / `ChartColors` / `NetworkGraph`
- **可视化辅助**：`GAVisualizer`（遗传算法）/ `NNVisualizer`（神经网络）
- **算法可视化**：`lesson2/3` 多个 `AlgoVisualizer` + `AlgoFrame` + `AlgoVisHelper`（刘汝佳算法课样例）
- **机器人框架**：`QinMaRobot` / `Robots` / `RobotEngine` / `OperationAction` / `OperationActionChain` /
  `OperationEnum` / `RobotPrintILoveYou` / `RobotTest` / `RobotTest2`
- **Swing 面板管理**：`App` / `ManagerFrame` / `SubPanelRegister` / `CommonPanel` / `UserObject` / `TreeComponent` /
  `TreeNode` / `RegisterTreeNode` / `RegisterTreeNodeHelper`

---

### z-util-monitor（监控）

> 包路径：`com.zifang.util.monitor.*`

- **JVM**：`JvmMonitor` + 各种 `MXBean` 演示（ClassLoading / Compilation / GC / Memory / MemoryManager / MemoryPool / OS /
  Runtime / Thread）+ 引用类型（强/软/弱/虚）Demo
- **线程池监控**：`ThreadMonitor` / `MonitorableExecutor` / `FixedMonitorableExecutor` / `ExecutorManager` /
  `ThreadPoolConfigUnit`
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
- **Git（命令实现）**：`GitClient` / `GitException` / `GitResult` + 领域模型（`GitRepository` / `GitCommit` / `GitBranch` /
  `GitTag` / `GitStatus` / `GitDiffEntry` / `GitAuthor` / `GitRemote`）
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
- **编译时**：`CompileContext` / `SourceJavaFileObject` / `CharSequenceJavaFileObject` / `CustomerCompileClassLoader` /
  `CustomerCompileJavaFileManager`
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
SnowflakeIdWorker id = new SnowflakeIdWorker(1L, 1L);
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
- **异常**：`ParseException` / `MissingOptionException` / `MissingArgumentException` / `UnrecognizedOptionException` /
  `AmbiguousOptionException` / `AlreadySelectedException`
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

z-util-ml / z-util-math / z-util-workflow / z-util-http
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
- 取 logger 的标准写法：`private static final Logger log = LoggerFactory.getLogger(Xxx.class);`，也可以直接使用
  `com.zifang.util.core.trace.log.Logs` 的工具方法。

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
   > 注：commons-lang3 的 jar 仍会作为部分三方库（github-api / webdrivermanager / unirest-java）的传递依赖出现在运行时
   classpath 中，但我们的源码已经不再直接依赖它。

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

## 发布与分发

`z-util` 通过两条渠道分发：

| 渠道 | 仓库地址 | 适用场景 |
|------|---------|---------|
| **Maven Central**（主） | https://repo1.maven.org/maven2/io/github/yuku123/ | 对外发布，所有人可直接拉取 |
| **GitHub Packages**（备） | https://maven.pkg.github.com/yuku123/z-maven-repo | 内部预发 / CI 临时验证 |

### Maven Central 一键发布（推荐）

仓库内置 `deploy_maven_center.sh` 一键脚本，封装所有凭证加载、GPG 签名、bundle 上传与轮询：

```bash
# 1) 首次：生成 GPG 密钥并写入 ./.env（只做一次）
./deploy_maven_center.sh gpg-init

# 2) 升版本号到稳定版
mvn versions:set -DnewVersion=1.0.4
mvn versions:commit

# 3) 真发（**必须在你自己的 macOS 终端**，不走 sandbox）
./deploy_maven_center.sh publish

# 4) 5~15 分钟后验证
./deploy_maven_center.sh verify

# 5) 改回 SNAPSHOT 继续开发
mvn versions:set -DnewVersion=1.0.5-SNAPSHOT
mvn versions:commit
```

**典型输出**：
```
[INFO] Uploaded bundle successfully, deployment name: Deployment,
       deploymentId: 056e55d3-a424-403f-9639-377a0472c4ce.
       Deployment will publish automatically
[INFO] BUILD SUCCESS
```

### 发布前置（必须人工，AI 助手无法代办）

1. **注册 Sonatype Central Portal** — https://central.sonatype.com/ → Sign in with GitHub
2. **生成 User Token** — Profile → Generate User Token，把 Username + Secret 写到 `~/.env`
3. **namespace 验证** — `io.github.yuku123` 用 GitHub Pages 验证（5 分钟过审）
4. **GPG 密钥** — `brew install gnupg` 然后跑 `gpg-init` 子命令

> ⚠️ **凭证安全**：`CENTRAL_TOKEN` / `CENTRAL_GPG_PASSPHRASE` 是长期 API 凭证，**绝对不要贴到对话里**。
> 已通过 `.gitignore` 排除 `.env` 和 `.gnupg/`，但仍要妥善保管。

### 发布链路要点

- **POM 元数据**：父 POM 已包含 `name`/`description`/`licenses`/`developers`/`scm`（Maven Central 强制要求）
- **GPG 签名**：所有 jar / pom 用 ED25519 签名（`9D064F0E20AC7385`）
- **三件套**：每个模块自动生成 `*.jar` + `*-sources.jar` + `*-javadoc.jar`
- **跳过模块**：`z-util-all`（聚合 pom）/ `z-util-zex`（练习场，不发布）
- **超时配置**：`waitMaxTime=1800`（30 分钟）防 mvn 提前放弃

### GitHub Packages（备选仓库）

如需内部预发或 CI 临时验证，父 POM 同时配置了 GitHub Packages `distributionManagement`：

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/yuku123/z-maven-repo</url>
    </repository>
</distributionManagement>
```

发布到 GitHub Packages 需要在 `~/.m2/settings.xml` 配置 `github` server：

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>你的 GitHub 用户名</username>
      <password>${env.MAVEN_GITHUB_TOKEN}</password>
    </server>
  </servers>
</settings>
```

发布命令：

```bash
mvn clean deploy
```

> GitHub 2023+ 默认 `GITHUB_TOKEN` 是只读，写 Packages 必须在 workflow 中显式声明 `permissions: packages: write`。

### 详细发布文档

- **通用发布指引（AI / 工程师都能从零开始）**：见 [发布指引.md](./发布指引.md)
- **z-util 特定操作手册**：见 [RELEASE_TO_MAVEN_CENTRAL.md](./RELEASE_TO_MAVEN_CENTRAL.md)

---

## 开发约定

- **依赖复用**：所有公共版本号统一在父 `pom.xml` 的 `dependencyManagement` / `build` 节点；子模块新增依赖请优先复用。
- **版本号**：所有子模块的 `<version>` 用 `${revision}`，不要硬编码。
- **构建顺序**：修改了某个子模块后，建议使用 `mvn -pl <module> -am install` 一起构建依赖。
- **依赖检查**：定期执行 `mvn dependency:analyze` 排查未声明或多余依赖。
- **代码风格**：遵循通用 Java 编码规范；可启用 IDE 的 Checkstyle / Spotless（如需可后续加入）。
- **AI 协作约定**：见 [CLAUDE.md](./CLAUDE.md)。
- **辅助脚本**：`deploy_maven_center.sh`（publish/verify/gpg-init/readme 子命令），凭证隔离在 `.env`。

---

## 常见问题

### 构建与运行

1. **构建失败 / 编译错误**
    - 确认 Maven 3.6+、JDK 8+
    - 删除 `~/.m2/repository/io/github/yuku123` 后重新 `mvn install`
2. **某个测试一直挂起**
    - 一些 `*Test` 用例依赖网络 / 数据库，先 `mvn -DskipTests=true install`
3. **集成测试想跑**
    - `mvn test -pl <module>`
4. **依赖冲突**
    - 父 POM 的 `dependencyManagement` 已固定主流第三方版本，子模块中尽量 **不写版本号** 来复用。

### Maven Central 发布

5. **`Component with package url '...' already exists`**
    - 之前失败 publish 的残骸留在 Central Portal。先到 https://central.sonatype.com/publishing/deployments
      把 FAILED 的 deployment `Drop` 掉，或直接升版本号（如 `1.0.3` → `1.0.4`）绕开冲突。
6. **mvn deploy 在 z-util-ioc 失败（5 分钟就放弃）**
    - `waitMaxTime` 默认 30 分钟就够，但某些模块（聚合 pom 的 staging）可能慢。
    - 已配置 `waitMaxTime=1800`（30 分钟），不会 5 分钟就放弃。
    - **必须在用户自己 macOS 终端跑**，sandbox 限制 64KB 出站 POST，157MB bundle 必然失败。
7. **`name equals artifactId` 验证失败**
    - 子模块 `<name>` 不能等于 `<artifactId>`。应该写成 `"z-util :: core (描述)"` 这种形式。
8. **GPG 签名验证失败**
    - 公钥需上传到 keys.openpgp.org（`gpg --keyserver hkps://keys.openpgp.org --send-keys $KEY_ID`）
    - 同步需要 30 分钟 ~ 24 小时。
9. **Central Portal 401 Unauthorized**
    - 用 `Authorization: UserToken base64(username:secret)` 头，不是 `Bearer xxx`。
    - `<server><id>central</id>` 必须与 POM `<publishingServerId>` 一致。

### GitHub Packages

10. **GitHub Packages 401 / 403**
    - 检查 PAT 是否带 `write:packages`；Maven `<server>` id 必须是 `github` 与 POM 中一致。

---

## 项目状态

| 维度 | 状态 |
|------|------|
| **最新发布** | `1.0.3`（2026-08-10，已发布到 Maven Central） |
| **开发线** | `1.0.4-SNAPSHOT` |
| **依赖的 Java** | JDK 8+ |
| **依赖的 Maven** | 3.6+ |
| **CI / CD** | GitHub Actions + Maven Central Portal |
| **安全公告** | GitHub Dependabot 自动扫描 |
| **License** | MIT |
| **维护者** | yuku123（单人项目，欢迎贡献） |

### 版本路线

| 版本 | 状态 | 说明 |
|------|------|------|
| `0.x` | 历史 | 原 `com.zifang` 内部版本，未对外发布 |
| `1.0.2` | 仅 metadata | sandbox 早期试发，缺 jar/sources/javadoc |
| **`1.0.3`** | **当前稳定** | 38 模块全部 PUBLISHED，含 GPG 签名 |
| `1.0.4-SNAPSHOT` | 开发中 | 持续迭代 |

---

## 安装

### Maven（Maven Central 推荐）

```xml
<dependency>
    <groupId>io.github.yuku123</groupId>
    <artifactId>z-util-core</artifactId>
    <version>1.0.3</version>
</dependency>
```

如使用 Maven Central 主仓库，无需任何额外配置。Maven 默认即从 `repo1.maven.org` 拉取。

### Gradle

```gradle
dependencies {
    implementation 'io.github.yuku123:z-util-core:1.0.3'
}
```

### SBT

```scala
libraryDependencies += "io.github.yuku123" % "z-util-core" % "1.0.3"
```

### 验证安装

```bash
# 直接从 Maven Central 下载验证
curl -I https://repo1.maven.org/maven2/io/github/yuku123/z-util-core/1.0.3/z-util-core-1.0.3.jar

# 验证 GPG 签名
curl -O https://repo1.maven.org/maven2/io/github/yuku123/z-util-core/1.0.3/z-util-core-1.0.3.jar
curl -O https://repo1.maven.org/maven2/io/github/yuku123/z-util-core/1.0.3/z-util-core-1.0.3.jar.asc
gpg --verify z-util-core-1.0.3.jar.asc z-util-core-1.0.3.jar
# 预期：Good signature from "yuku123 <1340947819@qq.com>"
```

---

## 贡献

欢迎以任何形式参与贡献：

- **提 Issue**：报告 Bug / 提出功能建议 / 询问用法
- **发 PR**：修复 Bug / 添加新功能 / 改进文档
- **完善示例**：各模块的 README 与测试用例
- **分享使用案例**：你的项目用了哪些模块，怎么用的

### 提 PR 流程

1. Fork 仓库到你的 GitHub 账号
2. 新建分支：`git checkout -b feature/your-feature`
3. 提交改动：`git commit -m "feat: 描述你的改动"`
4. Push 到你的 fork：`git push origin feature/your-feature`
5. 在 GitHub 创建 Pull Request

### 开发规范

- 保持子模块间低耦合，新增功能优先放在合适的子模块
- 公共工具类放 `z-util-core`；领域工具（如 ML / 爬虫）放对应专业模块
- 第三方依赖走父 POM 的 `dependencyManagement`，子模块不写 `<version>`
- 提交前跑一次 `./deploy_maven_center.sh readme` 看发布链路是否完整

---

## 致谢

`z-util` 的很多实现参考了以下开源项目：

| 项目 | 用途 |
|------|------|
| [ANTLR](https://www.antlr.org/) | 多格式解析器（JSON / XML / YAML / CSV / TOML 等）的语法框架 |
| [Apache POI / PDFBox / Docx4j](https://poi.apache.org/) | Office 文档读写与转换 |
| [OkHttp](https://square.github.io/okhttp/) | HTTP 客户端底层 |
| [Netty](https://netty.io/) | IM 示例 / 网络编程 |
| [Guava](https://github.com/google/guava) | 集合 / 缓存 / 并发设计参考 |
| [Selenium](https://www.selenium.dev/) | 浏览器自动化 |
| [JGit](https://www.eclipse.org/jgit/) | Git 操作 |
| [刘汝佳《算法竞赛入门经典》](https://github.com/liurujia/) | Swing 算法可视化样例 |
| 《码出高效》《Java 并发编程实战》等 | 设计模式与编码规范 |

也感谢所有提交 Issue、PR 和使用 `z-util` 的同学。

---

## 许可证

本仓库采用 **MIT License** — 见 [LICENSE](./LICENSE) 文件。

子模块内可能包含参考实现的来源注释，使用前请遵守对应来源的协议。
