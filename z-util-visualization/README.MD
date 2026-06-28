# z-util-visualization

基于 Swing 的桌面可视化与机器人自动化工具：折线图、柱状图、网络图、算法过程可视化、桌面机器人（键盘/鼠标编排）。

> 包路径：`com.zifang.util.visuallization.*`（注意双 `l`：`visuallization`）

## Features

### 图表 (`chart`)

- `LineChart` — 折线图（多系列、可定制颜色）
- `BarChart` — 柱状图
- `NetworkGraph` — 网络图 / 关系图（节点 + 边）
- `ChartFrame` — 通用图表 Swing 窗口
- `ChartSeries` — 数据序列（名称 + X/Y 点）
- `ChartColors` — 内置配色方案
- `NNVisualizer` — 神经网络结构可视化
- `GAVisualizer` — 遗传算法种群演化可视化

### Swing 窗口与面板 (`swing`)

- **`App`** — Swing 应用入口
- **管理框架 (`manager`)**：
  - `ManagerFrame` — 主窗口（左侧树 + 右侧面板）
  - `tree.TreeNode` / `RegisterTreeNode` / `RegisterTreeNodeHelper` / `TreeComponent` — 树结构
  - `subpanels.SubPanelRegister` / `CommonPanel` / `UserObject` — 子面板注册
- **算法过程可视化 (`algrithm`)**：
  - `AlgoFrame` — 算法动画画布窗口
  - `AlgoVisualizer` — 动画渲染驱动
  - `AlgoVisHelper` — 绘图辅助（圆、线、文字、暂停）
  - 内置多课（lesson2/lesson3...）演示样例

### 桌面机器人 (`robot`)

- **`RobotEngine`** / **`Robots`** — 机器人引擎门面
- **动作编排**：
  - `common.OperationAction` — 单个动作
  - `common.OperationActionChain` — 动作链 / 宏
  - `define.OperationEnum` — 动作类型枚举（鼠标移动/点击、键盘输入、截图、延时……）
- **示例**：
  - `QinMaRobot` — 秦码输入法机器人
  - `RobotPrintILoveYou` — 键盘自动打字演示
  - `RobotTest` / `RobotTest2` — 基本功能样例

## Quick Start

### 1. 画一张折线图

```java
ChartSeries trainLoss = new ChartSeries("train loss")
    .add(1, 2.3).add(2, 1.5).add(3, 0.9).add(4, 0.5).add(5, 0.3);

ChartSeries valLoss = new ChartSeries("val loss")
    .add(1, 2.5).add(2, 1.8).add(3, 1.3).add(4, 1.0).add(5, 0.8);

new ChartFrame("训练曲线")
    .size(800, 500)
    .add(trainLoss)
    .add(valLoss)
    .xLabel("epoch")
    .yLabel("loss")
    .show();
```

### 2. 柱状图

```java
BarChart chart = new BarChart("Q2 销售统计");
chart.addCategory("北京", 120);
chart.addCategory("上海", 150);
chart.addCategory("广州", 90);
chart.addCategory("深圳", 140);
new ChartFrame(chart).show();
```

### 3. 机器人自动打字 + 截屏

```java
RobotEngine robot = Robots.create();

robot.chain()
     .type("Hello, z-util!")       // 键盘输入字符串
     .pressEnter()                  // 回车
     .sleepMs(500)
     .screenshot(new File("/tmp/screen.png"))
     .run();
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-visualization</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```