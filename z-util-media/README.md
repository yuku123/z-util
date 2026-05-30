# z-util-media

图像处理工具集，涵盖验证码生成、图像比较、颜色转换、GIF 构建等常见场景。

## Features

- **ImageCaptcha** — 图像验证码生成
- **ColorUtil** — 颜色工具（Hex/RGB/HSL 互转）
- **GifBuilder** — GIF 动画构建
- **ImageCompare** — 图像相似度比较
- **ImageReadWrite** — 图片读写（支持 JPG/PNG/GIF/BMP）
- **CaptchaUtil** — 通用验证码工具
- **ImageUtil** — 图像处理基础工具
- **ImageProcessor** — 图像处理器

## Quick Start

```java
// 生成图形验证码
ImageCaptcha captcha = new ImageCaptcha(120, 40);
BufferedImage image = captcha.generate();
String code = captcha.getCode();

// 图像比较
double similarity = ImageCompare.similarity(img1, img2);

// 颜色转换
String hex = ColorUtil.rgbToHex(255, 128, 0);
int[] hsl = ColorUtil.hexToHsl("#FF8000");
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-media</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```