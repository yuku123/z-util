# z-util-validation

通用验证引擎，支持自定义校验器链式调用，对标 Hibernate Validator。

## Features

- **ValidationEngine** — 验证引擎核心
- **Validator** — 验证器接口
- **ValidateResult** — 验证结果
- **ValidationException** — 验证异常

内置校验器：

- **NotNullValidator** — 非空校验
- **PatternValidator** — 正则校验
- 长度、范围、邮箱、手机号等

## Quick Start

```java
ValidationEngine engine = new ValidationEngine();
engine.addValidator(new NotNullValidator());
engine.addValidator(new PatternValidator("^\\d{6}$"));

ValidateResult result = engine.validate(value);
if (!result.isValid()) {
    System.out.println(result.getErrors());
}
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-validation</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```