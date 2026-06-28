# z-util-workflow

工作流引擎：节点 + 连接器 + 上下文；支持 Java / Python / Spark 多种执行引擎；支持 BPMN 2.0 XML 导入；可持久化、可缓存。

> 包路径：`com.zifang.util.workflow.*`

## 设计原则

> 从小到大排序：
> 执行方法 → 执行节点（WorkflowNode） → 执行单元组 → 执行工作流（WorkflowConfiguration） → 执行工作流上下文（WorkFlowApplicationContext） → 工作流执行引擎（WorkflowRuntimeEngine）
>
> 每个节点暴露生命周期方法，由工作流委托调用（门面模式）；每个上下文掌控工作流，拥有生命周期与运行时状态全量查看权限。

## Features

### 核心配置模型 (`config`)

- **工作流**：`WorkflowConfiguration`（一个 DAG 图）
- **节点**：`WorkflowNode` / `ExecutableWorkflowNode`（带执行方法与参数）
- **连接器**：`Connector`（节点之间的连线 / 数据流）
- **生命周期**：`NodeLifeCycle`（init / run / success / fail / destroy）
- **执行引擎声明**：`Engine`（Java / Python / Spark…） + `CacheEngine`（节点级缓存配置）
- **配置总入口**：`Configurations`

### 应用上下文与构建 (`conponents`)

- `WorkFlowApplication` — 运行态工作流应用
- `WorkFlowApplicationContext` — 上下文（状态、变量、节点结果）
- `WorkFlowApplicationBuilder` — 构造器
- `Task` — 子任务封装

### 多执行引擎 (`engine`)

- **运行时引擎 (`runtime`)**：
  - `WorkflowRuntimeEngine` — 总执行引擎（唯一入口）
  - `ExecutionResult` — 执行结果（成功/失败 + 输出）
  - `GatewayEvaluator` — 网关（条件分支 / 并行 / 汇聚）求值
- **Java 引擎 (`java`)**：
  - `JavaEngine` — 执行 Java 代码/反射调用
  - `services.JavaHandle` — Java 处理器
- **Python 引擎 (`python`)**：
  - `PythonEngine` — 执行 Python 脚本
- **Spark 引擎 (`spark`)**：
  - `SparkEngine` / `AbstractSparkEngine`
  - `AbstractSparkEngineService` — Spark 服务基类
  - `CacheEngineService` — 节点结果缓存服务
  - **内置 Spark 处理器 (`services`)**：
    - `SelectColumnHandler` / `CastColumnHandler` / `ChangeColumnNameHandler`
    - `FilterHander` / `OrderLimitHander`
    - `JoinHandler` / `PivotHandler` / `WindowHandler`
    - `SqlHandler` / `ResourceHandler`
    - `DiscriptionHandler` / `StandardSingleViewHandler`
    - `SegmentationHandler` / `CodificationHandler`
    - `AlgrithmHandler`（算法处理，含 `algrithm.churn.LogisticTargetVaribleHandler`）
    - `UdfHandler` / `SaveDataHandler` / `EmptyHandler`
  - **解析器 (`services.praser`)**：`PivotA` / `PivotANode`
  - **拦截器 (`services.interceptor`)**：`CacheInterceptor`
- **SPI (`interfaces`)**：
  - `AbstractEngine` / `AbstractEngineService` — 可扩展的引擎/服务基类
  - `EngineFactory` — 引擎工厂
  - `@EngineService` / `@EngineServiceInterceptor` — 注解声明与拦截

### BPMN 支持 (`bpmn`)

- `Bpmn` — BPMN 文档总模型
- `BpmnDiagram` — 图信息
- `BpmnProcess` — 流程
- `BpmnFlowNode` — 节点（Task / Gateway / Event 等）
- `BpmnSequenceFlow` — 顺序流
- `BpmnXmlParser` — 从 BPMN 2.0 XML 文件解析
- `BpmnModelConverter` — 把 BPMN 模型转换为 WorkflowConfiguration

### 序列化 (`json`)

- `WorkflowConfigurationSerializer` — JSON 序列化/反序列化工作流配置

### 持久化 (`persistence`)

- `WorkflowPersistencePlugin`（接口） — 持久化 SPI
- `FileWorkflowPersistencePlugin` — 基于本地文件的实现
- `WorkflowSnapshot` — 工作流运行时快照（用于恢复）

## Quick Start

### 1. 用 JavaEngine 跑一个简单工作流

```java
WorkflowConfiguration cfg = new WorkflowConfiguration()
    .addNode(new ExecutableWorkflowNode("start")
        .engine(Engine.JAVA).invokeMethod("com.example.Startup.run")
        .invokeParameter(new Object[] { "hello" }))
    .addNode(new ExecutableWorkflowNode("process")
        .engine(Engine.JAVA).invokeMethod("com.example.Processor.exec")
        .dependsOn("start"))
    .addNode(new ExecutableWorkflowNode("end")
        .engine(Engine.JAVA).invokeMethod("com.example.Finisher.ok")
        .dependsOn("process"));

WorkFlowApplicationContext ctx = new WorkFlowApplicationBuilder()
    .configuration(cfg)
    .build();

WorkflowRuntimeEngine engine = WorkflowRuntimeEngine.get();
ExecutionResult res = engine.run(ctx);

System.out.println(res.isSuccess());
System.out.println(ctx.nodeResult("process"));
```

### 2. 导入 BPMN XML

```java
BpmnXmlParser parser = new BpmnXmlParser();
Bpmn bpmn = parser.parse(Files.newInputStream(Paths.get("order-flow.bpmn")));

BpmnModelConverter conv = new BpmnModelConverter();
WorkflowConfiguration cfg = conv.convert(bpmn);

// 然后用 RuntimeEngine 执行即可
```

### 3. SparkEngine 做数据处理

```java
WorkflowConfiguration cfg = new WorkflowConfiguration()
    .addNode(new ExecutableWorkflowNode("read")
        .engine(Engine.SPARK).handler("ResourceHandler")
        .param("path", "/data/order.csv"))
    .addNode(new ExecutableWorkflowNode("filter")
        .engine(Engine.SPARK).handler("FilterHander")
        .param("condition", "amount > 100")
        .dependsOn("read"))
    .addNode(new ExecutableWorkflowNode("save")
        .engine(Engine.SPARK).handler("SaveDataHandler")
        .param("output", "/data/order-high.parquet")
        .dependsOn("filter"));

// 指定 Spark 环境后运行
```

### 4. 持久化运行时快照

```java
WorkflowRuntimeEngine engine = WorkflowRuntimeEngine.get();
ExecutionResult res = engine.run(ctx);

if (!res.isSuccess()) {
    // 失败时落盘，后续可恢复
    WorkflowPersistencePlugin storage = new FileWorkflowPersistencePlugin("/tmp/snapshots");
    storage.save(new WorkflowSnapshot(ctx));
}
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-workflow</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```