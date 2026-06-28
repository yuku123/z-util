# z-util-crawler

Java 爬虫框架，覆盖「静态 HTTP 请求 + 浏览器渲染 + Pipeline 处理 + CUA（Crawler User Agent）计划步骤」，可处理多种网页防爬策略。

> 包路径：`com.zifang.util.crawler.*`

## Features

### HTTP 抓取 (`http`)

- `CrawlerHttpClient` — 可配置 UA、Cookie、代理、超时、重试的 HTTP 客户端

### HTML / JSON 解析 (`parse`)

- `HtmlParser` — 基于选择器（CSS Selector / XPath）提取 DOM 字段
- `JsonExtractor` — JSONPath / 指针方式从 JSON 响应中取值

### Pipeline 处理 (`pipeline`)

- `CrawlerPipeline` — 责任链式处理器串联（下载 → 解析 → 清洗 → 存储）
- `Processor` — 单步处理器接口
- `PipelineContext` — 链路上下文，携带 URL、响应、中间产物

### 浏览器渲染（`browser`，Selenium 兼容）

- `BrowserClient` — 浏览器抽象（Selenium WebDriver 封装）
- `ChromeDriverHolder` — ChromeDriver 生命周期托管
- `JsEvaluator` — 在页面上下文中执行自定义 JS

### CUA 计划与步骤注册 (`cua`)

> CUA = Crawler User Agent，把浏览器动作拆成可编排的 Step，像写剧本一样描述一次爬取。

- **核心**：`CuaAgent`（执行者）、`OperationRegistry`（步骤注册中心）、`CuResult` / `StepResult`（结果）
- **规划 (`plan`)**：`RuleBasedPlanner` — 基于规则生成执行计划
- **内置步骤 (`steps`)**：
  - `NavigateStep` — 跳转 URL
  - `ClickStep` — 点击元素
  - `InputStep` — 输入文本
  - `WaitStep` — 显式等待（按条件 / 固定时长）
  - `ExtractStep` — 抽取字段
  - `ScreenshotStep` — 截屏
  - `SwitchWindowStep` — 切换窗口/标签页
  - `Step` — 自定义步骤基类

## Quick Start

### 1. 静态 HTML 爬取 + Pipeline

```java
CrawlerHttpClient http = new CrawlerHttpClient()
    .userAgent("Mozilla/5.0 ...")
    .timeout(10_000);

CrawlerPipeline pipeline = new CrawlerPipeline()
    .add(ctx -> ctx.setResponse(http.get(ctx.getUrl())))           // 1. 下载
    .add(ctx -> ctx.setItems(HtmlParser.of(ctx.body()).selectAll(".item")))  // 2. 解析
    .add(ctx -> saveToDb(ctx.getItems()));                         // 3. 落地

PipelineContext ctx = new PipelineContext("https://example.com/list");
pipeline.run(ctx);
```

### 2. CUA 浏览器剧本（模拟登录 + 提取数据）

```java
CuaAgent agent = CuaAgent.chrome()
    .headless(true);

agent.plan(
    Step.navigate("https://example.com/login"),
    Step.input("#username", "foo"),
    Step.input("#password", "bar"),
    Step.click("button.login"),
    Step.waitFor("#dashboard"),
    Step.extract("title",  ".dashboard h1"),
    Step.extract("points", ".points-badge"),
    Step.screenshot("/tmp/login-success.png")
);

CuResult res = agent.run();
System.out.println(res.get("title"));    // 仪表盘标题
System.out.println(res.get("points"));   // 积分数
agent.close();
```

### 3. JSON 接口提取

```java
String json = http.get("https://api.example.com/users/1").body();
JsonExtractor ext = JsonExtractor.of(json);

String name  = ext.string("$.data.name");
int    age   = ext.intValue("$.data.age");
List<String> tags = ext.list("$.data.tags[*]", String.class);
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-crawler</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```