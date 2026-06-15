# z-util-ch

中文工具集，处理中国特色业务场景的实用工具。

## Features

- **PinyinGeneratorUtil** — 中文拼音生成
    - 汉字转全拼/简拼
    - 支持声调
- **IdcardUtil** — 身份证工具
    - 身份证号验证
    - 提取出生日期/性别/地区
- **MoneyUtil** — 金额工具
    - 数字转中文大写
    - 金额格式化

## Quick Start

```java
// 汉字转拼音
String pinyin = PinyinGeneratorUtil.toPinyin("你好");
String shortPinyin = PinyinGeneratorUtil.toPinyin("北京", true); // "BJ"

// 身份证验证
boolean valid = IdcardUtil.isValid("110101199001011234");
String birth = IdcardUtil.getBirth("110101199001011234");
String gender = IdcardUtil.getGender("110101199001011234"); // "M" or "F"

// 金额转大写
String upper = MoneyUtil.toChineseUpper(1234.56); // "壹仟贰佰叁拾肆元伍角陆分"
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-ch</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```