# z-util-monitor

JVM / 操作系统 / 网络 / 线程池 的全方位监控框架：Metrics 采集、注册表、快照、HTML/JSON 导出、可观测线程池 + 告警策略。

> 包路径：`com.zifang.util.monitor.*`

## Features

### 核心 Metrics

- `MetricsRegistry` — 指标注册表（Counter / Gauge / Timer / Histogram）
- `MetricsCollector` — 指标采集器（把运行时状态灌进注册表）
- `MetricsSnapshot` — 采集快照（可持久化 / 跨节点传输）
- `MonitorServer` — 内嵌 HTTP 监控服务（可选）

### JVM 监控 (`jvm`)

- `JvmMonitor` — 汇总：堆/非堆、元空间、GC、类加载、JIT 编译、线程
- **MXBean 演示 (`management`)**：
  - `MemoryMXBeanDemo` / `MemoryPoolMXBeanDemo`
  - `GarbageCollectorMXBeanDemo`
  - `ThreadMXBeanDemo` / `ClassLoadingMXBeanDemo`
  - `CompilationMXBeanDemo`
  - `RuntimeMXBeanDemo` / `OperatingSystemMXBeanDemo`
- **引用 / 内存诊断 (`ref`)**：
  - `StrongReferenceDemo` / `SoftReferenceDemo` / `WeakReferenceDemo` / `PhantomReferenceDemo`
  - `ReferenceQueueDemo` / `RefMainThread`
- **内存学习样例**：`MemoryLeakDemo`、`StringDemo`、`MultiThread`、`MBeanDemo`

### OS 监控 (`os`)

- `OsMonitor` — CPU / 内存 / 磁盘 / 负载

### 网络监控 (`net`)

- `NetMonitor` — TCP/UDP 连接、网卡流量

### 线程池监控 (`thread`)

- `ThreadMonitor` / `MonitorManager` — 统一线程池监控入口
- **可观测线程池 (`executor`)**：
  - `MonitorableExecutor`（接口）
  - `FixedMonitorableExecutor` — 固定大小可监控线程池
  - `ExecutorManager` — 线程池生命周期管理
  - `ThreadPoolConfigUnit` — 配置单元
- **状态模型**：
  - `Monitorable` / `Status` / `StatusLevel` / `ThreadPoolStatus`
- **任务封装 (`task`)**：
  - `MonitorTask` — 带耗时/异常采集的 Runnable 包装
- **告警 (`alarm`)**：
  - `AlarmPolicy`（接口） / `ThreadPoolOvertimeAlarmPolicy` — 线程池超时策略
  - `Alarmable`（被告警对象）
  - `AlarmService`（接口） / `LogAlarmService` — 日志告警服务
- **工具 (`utility`)**：`TimeUtil`、`DateUtils`
- **常量 (`constant`)**：`MonitorConstant`

### 导出 (`exporter`)

- `JsonExporter` — 把 MetricsSnapshot 序列化为 JSON
- `HtmlExporter` — 生成可读的 HTML 监控报告页

## Quick Start

### 1. 采集一次 JVM + OS 快照并导出 JSON

```java
MetricsRegistry registry = new MetricsRegistry();
MetricsCollector collector = new MetricsCollector(registry);

collector.register(new JvmMonitor());
collector.register(new OsMonitor());
collector.collect();

MetricsSnapshot snap = registry.snapshot();

String json = new JsonExporter().export(snap);
Files.write(Paths.get("/tmp/metrics.json"), json.getBytes());
```

### 2. 使用可观测线程池 + 超时告警

```java
LogAlarmService alarm = new LogAlarmService();
alarm.addPolicy(new ThreadPoolOvertimeAlarmPolicy(Duration.ofSeconds(30)));

FixedMonitorableExecutor pool = new FixedMonitorableExecutor(
    "order-pool",
    10,
    new ThreadPoolConfigUnit(),
    alarm
);

for (int i = 0; i < 100; i++) {
    pool.submit(new MonitorTask("process-" + i, () -> processOrder(i)));
}

ThreadPoolStatus status = pool.status();
System.out.println("队列中: " + status.queueSize() + " / 活跃: " + status.activeCount());
```

### 3. 生成一份 HTML 监控报告

```java
MetricsSnapshot snap = registry.snapshot();
String html = new HtmlExporter().export(snap);
Files.write(Paths.get("/tmp/monitor.html"), html.getBytes());
// 浏览器直接打开即可看到总览、JVM、OS、线程池 4 张表
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-monitor</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```