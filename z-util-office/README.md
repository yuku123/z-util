# z-util-office

Office 文档处理模块，支持 PDF、Excel、Word 等格式的读写和转换。

## Features

- **PoiUtils** — Apache POI 工具类（Excel/Word 读写）
- **PdfConverter** — PDF 格式转换
- **PdfEditor** — PDF 编辑
- **PdtExtractor** — PDF 文本提取
- **ExcelUtils** — Excel 操作工具
- **ImagePdfTest** — 图片转 PDF

## Quick Start

```java
// Excel 读写
ExcelUtils.readExcel(file);
ExcelUtils.writeExcel(data, outputFile);

// PDF 读取
String text = PdtExtractor.extract(pdfFile);

// PDF 转换
PdfConverter.toImage(pdfFile, outputDir);
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-office</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```