# z-util-cli

命令行参数解析模块，完整覆盖 Apache Commons CLI 能力，支持多种解析模式。

## Features

- **Parser** — 基础解析器
- **BasicParser** — 基本解析实现
- **PosixParser** — POSIX 风格解析（`program -a -b value`）
- **TypeHandler** — 类型转换处理
- **PatternOptionBuilder** — 模式选项构建器
- **CommandLineParser** — 命令行解析入口

## Quick Start

```java
Options options = new Options();
options.addOption("h", "help", false, "显示帮助");
options.addOption("v", "verbose", false, "详细输出");
options.addOption("f", "file", true, "输入文件");

CommandLineParser parser = new PosixParser();
CommandLine cmd = parser.parse(options, args);

if (cmd.hasOption("help")) {
    // 显示帮助
}
String file = cmd.getOptionValue("file");
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-cli</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```