# util-math 功能特性

本文档描述了 util-math 模块的主要功能特性，这是一个对标 Python numpy 和 pandas 的 Java 数学计算库。

## 核心数据结构

### 1. Num 类 - 多维数组
- 对标 numpy.ndarray
- 支持多维数组创建和操作
- 提供数组切片、索引、变形等操作
- 支持广播机制

### 2. Series 类 - 一维标记数组
- 对标 pandas.Series
- 带标签的一维数组
- 支持多种数据选择方式（位置、标签、布尔索引）
- 提供丰富的统计方法
- **窗口操作**：rolling, expanding, ewm
- **字符串操作**：str() 访问器

### 3. DataFrame 类 - 二维表格数据
- 对标 pandas.DataFrame
- 二维表格数据结构
- 列操作（添加、删除、重命名）
- 行操作（切片、排序、过滤）
- 分组聚合（groupby）
- 数据合并（join, merge）
- 数据清洗（fillna, dropna, dropDuplicates）
- 数据导出（CSV, JSON）

## 数据转换和重塑 (Reshaper)

提供类似于 pandas 的数据重塑功能：

### 1. pivot - 数据透视表
```java
DataFrame pivotTable = Reshaper.pivot(df, index, columns, values);
```
将长格式数据转换为宽格式，类似于 Excel 的数据透视表。

### 2. melt - 数据融合
```java
DataFrame melted = Reshaper.melt(df, idVars, valueVars, varName, valueName);
```
将宽格式数据转换为长格式，是 pivot 的逆操作。

### 3. stack/unstack - 堆叠/取消堆叠
```java
Series stacked = Reshaper.stack(df);
DataFrame unstacked = Reshaper.unstack(series);
```
在行列之间转换数据维度。

### 4. crosstab - 交叉表
```java
DataFrame crosstab = Reshaper.crosstab(df, rowVar, colVar);
```
创建两个分类变量的列联表。

### 5. transpose - 转置
```java
DataFrame transposed = Reshaper.transpose(df);
```
行列互换操作。

## 字符串操作 (StringAccessor)

通过 `Series.str()` 访问器提供丰富的字符串处理功能：

### 大小写转换
- `lower()` - 转换为小写
- `upper()` - 转换为大写

### 空白处理
- `strip()` - 去除首尾空白
- `lstrip()` - 去除左侧空白
- `rstrip()` - 去除右侧空白

### 查找和替换
- `contains(subStr)` - 检查是否包含子串
- `replace(oldStr, newStr)` - 字符串替换
- `replaceRegex(pattern, replacement)` - 正则表达式替换

### 分割和提取
- `split(delimiter, index)` - 字符串分割
- `slice(start, end)` - 字符串切片
- `extract(pattern, group)` - 正则表达式提取

### 其他操作
- `length()` - 字符串长度
- `match(pattern)` - 正则表达式匹配
- `cat(other)` - 字符串连接
- `repeat(n)` - 字符串重复

## 窗口函数

提供时间序列分析常用的窗口计算功能：

### Rolling - 滚动窗口
```java
Series result = series.rolling(5).mean();  // 5期移动平均
```
支持的方法：mean, sum, max, min, std, var, count

### Expanding - 扩展窗口
```java
Series result = series.expanding().mean();  // 扩展平均
```

### EWM - 指数加权移动
```java
Series result = series.ewm(0.5).mean();     // 使用 alpha
Series result = series.ewmSpan(10).mean();  // 使用 span
```

## 数学函数库 (Maths)

提供全面的数学计算功能：

### 三角函数
- sin, cos, tan
- arcsin, arccos, arctan
- sinh, cosh, tanh

### 指数和对数
- exp, expm1
- log, log10, log1p
- log2

### 幂和根
- pow, sqrt, cbrt
- hypot

### 舍入函数
- round, floor, ceil, trunc
- abs

### 统计函数
- min, max
- sum, prod
- mean, std, var

## 线性代数 (Linalg)

提供矩阵运算功能：

### 矩阵乘法
- dot - 向量点积
- matmul - 矩阵乘法
- inner, outer - 内积和外积

### 矩阵分解
- svd - 奇异值分解
- qr - QR 分解
- eig - 特征值分解
- cholesky - Cholesky 分解

### 矩阵操作
- inv - 矩阵求逆
- pinv - 伪逆
- det - 行列式
- solve - 解线性方程组

## 随机数生成 (NumRandom)

提供各种分布的随机数生成：

### 基本随机数
- rand - 均匀分布 [0, 1)
- randn - 标准正态分布

### 特定分布
- uniform - 均匀分布 [low, high)
- normal - 正态分布
- randint - 随机整数

### 采样和排列
- choice - 随机采样
- shuffle - 随机打乱
- permutation - 随机排列
- seed - 设置随机种子

## CSV I/O

提供 CSV 文件读写功能：

### 读取 CSV
- `Pandas.read_csv(filePath)` - 读取 CSV 文件
- 支持自定义分隔符、编码、是否包含表头等

### 写入 CSV
- `Pandas.to_csv(df, filePath)` - 写入 CSV 文件
- 支持自定义分隔符、是否包含表头和索引等

## 使用示例

```java
import com.zifang.util.pandas.*;

// 创建 Series
Series s = Series.of(1, 2, 3, 4, 5);

// 创建 DataFrame
DataFrame df = Pandas.DataFrame(
    new HashMap<String, double[]>() {{
        put("A", new double[]{1, 2, 3});
        put("B", new double[]{4, 5, 6});
    }}
);

// 数据透视
DataFrame pivot = Reshaper.pivot(df, "index", "columns", "values");

// 字符串操作
Series str = Series.of("hello", "world");
Series upper = str.str().upper();

// 窗口函数
Series rollingMean = s.rolling(3).mean();
```

## 总结

util-math 提供了丰富的数学计算和数据处理功能，涵盖了：

1. **核心数据结构**：Num, Series, DataFrame
2. **数据转换**：pivot, melt, stack, transpose 等
3. **字符串处理**：大小写转换、分割、替换、正则匹配等
4. **窗口函数**：rolling, expanding, ewm
5. **数学函数**：三角函数、指数对数、统计函数等
6. **线性代数**：矩阵运算、分解、求逆等
7. **随机数生成**：各种分布的随机数
8. **CSV I/O**：文件读写功能

这些功能使得 util-math 成为一个功能完整的 Java 数学计算库，可以替代 Python 的 numpy 和 pandas 在 Java 项目中使用。