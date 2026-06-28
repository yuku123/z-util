# z-util-source

Java 源码与字节码工具箱：解析 `.class` 文件、内存中编译源码、字节码/源码生成、类信息池与增量比较。

> 包路径：`com.zifang.util.source.*`

## Features

### 字节码解析 (`parser`)

- `ByteCodeParser`（接口） / `ByteCodeParserImpl` — 读取 `.class` 字节，解析出类结构（常量池、字段表、方法表、属性表）
- `SourceCodeParser` — 源码级语法/语义分析入口
- `define.ByteCodeParser` — 解析器 SPI 定义

### 字节码 / 源码生成 (`generator`)

- **源码生成**：`JavaSourceGenerator` — 根据类元信息（ClassInfo）生成 `.java` 源码字符串
- **字节码生成**：`ByteCodeGenerator`（接口） / `ByteCodeGeneratorImpl` — 直接生成 `.class` 字节
- `define.ByteCodeGenerator` — 生成器 SPI 定义
- **类信息元模型**：
  - `ClassInfo` — 类元信息（修饰符、超类、接口、注解、字段、方法）
  - `FieldInfo` — 字段元信息
  - `MethodInfo` / `MethodParameterPair` — 方法与参数
  - `AnnotationInfo` — 注解元信息
  - `ModifierAdapter` — 修饰符工具
- **类信息池**：`info.ClassInfoPool` — 可缓存、可查询的 ClassInfo 集合
- **增量比较 (`diff`)**：
  - `ClassInfoDiffer` — 两个版本之间类级差异（增删改）
  - `FieldDiffer` — 字段级差异
  - `MethodDiffer` — 方法级差异
  - `diff.ClassInfoPool` — 差异汇总池

### 运行时编译 (`compiler`)

- `CompileContext` — 编译上下文（源码 → 字节 → Class）
- `SourceJavaFileObject` / `CharSequenceJavaFileObject` — 内存中 JavaFileObject 实现（无需写入磁盘）
- `CustomerCompileJavaFileManager` — 自定义 JavaFileManager（支持内存输出）
- `CustomerCompileClassLoader` — 自定义类加载器，加载编译结果

### 源码分析 (`analyser`)

- `AnalysisContext` — 语法/语义分析上下文

## Quick Start

### 1. 解析已有的 .class

```java
byte[] classBytes = Files.readAllBytes(Paths.get("MyClass.class"));

ByteCodeParser parser = new ByteCodeParserImpl();
ClassInfo info = parser.parse(classBytes);

System.out.println(info.getName());              // 类名
System.out.println(info.getSuperName());         // 父类
for (FieldInfo f : info.getFields()) { ... }     // 遍历字段
for (MethodInfo m : info.getMethods()) { ... }   // 遍历方法
```

### 2. 内存中编译一段 Java 源码

```java
String source =
    "package com.example;\n" +
    "public class Hello {\n" +
    "  public String say() { return \"hello\"; }\n" +
    "}";

CompileContext ctx = new CompileContext();
Class<?> clazz = ctx.compile("com.example.Hello", source);

Object obj = clazz.getDeclaredConstructor().newInstance();
String result = (String) clazz.getMethod("say").invoke(obj);  // "hello"
```

### 3. 从 ClassInfo 生成源码

```java
ClassInfo ci = new ClassInfo();
ci.setName("com.example.Demo");
ci.addField(new FieldInfo("name", "Ljava/lang/String;"));
ci.addMethod(new MethodInfo("getName", "()Ljava/lang/String;"));

JavaSourceGenerator gen = new JavaSourceGenerator();
String src = gen.generate(ci);     // 可直接写出到 .java 文件
```

### 4. 两个版本的类做增量比较

```java
ClassInfo oldV = ...;
ClassInfo newV = ...;

ClassInfoDiffer differ = new ClassInfoDiffer(oldV, newV);
System.out.println("新增字段: " + differ.addedFields());
System.out.println("删除方法: " + differ.removedMethods());
System.out.println("修改方法: " + differ.modifiedMethods());
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-source</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```