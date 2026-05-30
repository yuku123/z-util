# z-util-proxy

动态代理工厂模块，支持 JDK 动态代理、CGLIB 字节码代理，提供灵活 AOP 能力。

## Features

- **JdkProxyFactory** — JDK 动态代理，基于 `java.lang.reflect.Proxy`
- **CglibProxyFactory** — CGLIB 字节码代理，运行时生成子类
- **ProxyFactory** — 统一入口，智能选择代理方式（JDK优先，fallback到CGLIB）
- **ParamsHandler / ParamsConvertor** — 方法参数处理和转换
- **AccessFlagConvertor** — 访问标志转换（public/protected/private等）
- **CodeConvertor** — 字节码转换工具

## Quick Start

```java
// JDK 代理（接口优先）
SomeInterface proxy = ProxyFactory.createJdkProxy(target, SomeInterface.class);

// CGLIB 代理（无接口）
SomeClass proxy = ProxyFactory.createCglibProxy(target);

// 统一代理（自动选择）
Object proxy = ProxyFactory.create(target);
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-proxy</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```