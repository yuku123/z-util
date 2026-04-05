# util-math 功能实现总结

## 概述

util-math 模块现已实现为一个功能完整的 Java 数学计算库，全面对标 Python 的 numpy 和 pandas 库。以下是所有已实现的功能汇总。

## 核心数据结构

### 1. Num 类 - 多维数组 (numpy.ndarray)
- 多维数组创建和操作
- 数组切片、索引、变形
- 广播机制支持
- 算术运算、统计方法
- 文件 I/O (save/load)

### 2. Series 类 - 一维标记数组 (pandas.Series)
- 带标签的一维数组
- 多种数据选择方式（位置、标签、布尔索引）
- 丰富的统计方法（mean, std, sum, count, min, max, median, percentile 等）
- 数据清洗（fillna, dropna, isna, notna）
- **窗口函数** (rolling, expanding, ewm)
- **字符串操作** (str() 访问器)
- 插值方法 (interpolate)

### 3. DataFrame 类 - 二维表格数据 (pandas.DataFrame)
- 二维表格数据结构
- 列操作（添加、删除、重命名）
- 行操作（切片、排序、过滤）
- 分组聚合（groupby with sum, mean, count, min, max, std, var）
- 数据合并（join, merge）
- 数据清洗（fillna, dropna, dropDuplicates）
- **插值方法** (interpolate with linear, forward, backward)
- 数据导出（CSV, JSON）

## 新增功能模块

### 1. 数据转换和重塑 (transform.Reshaper)

#### pivot - 数据透视表
```java
DataFrame pivotTable = Reshaper.pivot(df, "index_column", "columns_column", "values_column");
```
将长格式数据转换为宽格式。

#### melt - 数据融合
```java
DataFrame melted = Reshaper.melt(df, idVars, valueVars, "variable", "value");
```
将宽格式数据转换为长格式。

#### stack/unstack - 堆叠/取消堆叠
```java
Series stacked = Reshaper.stack(df);
DataFrame unstacked = Reshaper.unstack(series);
```
在行列之间转换数据维度。

#### crosstab - 交叉表
```java
DataFrame crosstab = Reshaper.crosstab(df, "row_column", "column_column");
```
创建两个分类变量的列联表。

#### transpose - 转置
```java
DataFrame transposed = Reshaper.transpose(df);
```
行列互换操作。

### 2. 字符串操作 (str.StringAccessor)

通过 `Series.str()` 访问器提供丰富的字符串处理功能。

#### 大小写转换
- `lower()` - 转换为小写
- `upper()` - 转换为大写

#### 空白处理
- `strip()` - 去除首尾空白
- `lstrip()` - 去除左侧空白
- `rstrip()` - 去除右侧空白

#### 查找和替换
- `contains(subStr)` - 检查是否包含子串
- `replace(oldStr, newStr)` - 字符串替换
- `replaceRegex(pattern, replacement)` - 正则表达式替换

#### 分割和提取
- `split(delimiter, index)` - 字符串分割
- `slice(start, end)` - 字符串切片
- `extract(pattern, group)` - 正则表达式提取

#### 其他操作
- `length()` - 字符串长度
- `match(pattern)` - 正则表达式匹配
- `cat(other)` - 字符串连接
- `repeat(n)` - 字符串重复

### 3. 缺失值插值 (interpolate.Interpolator)

#### 线性插值
```java
Series interpolated = Interpolator.linear(series);
```
使用相邻的非缺失值进行线性插值。

#### 前向填充
```java
Series filled = Interpolator.forward(series);
```
使用前一个非缺失值填充。

#### 后向填充
```java
Series filled = Interpolator.backward(series);
```
使用后一个非缺失值填充。

#### DataFrame 插值
```java
DataFrame interpolated = df.interpolate();  // 线性插值
DataFrame interpolated = df.interpolate("forward");  // 前向填充
DataFrame interpolated = df.interpolate("backward");  // 后向填充
```

### 4. 相关性分析 (stats.Correlation)

#### 相关系数矩阵
```java
DataFrame corr = Correlation.corr(df);  // Pearson 相关系数
DataFrame corr = Correlation.pearson(df);  // 同上
```

#### 协方差矩阵
```java
DataFrame cov = Correlation.cov(df);
```

### 5. 数据分箱 (discretize.Discretizer)

#### 等宽分箱 (cut)
```java
Series bins = Discretizer.cut(series, 2);  // 分成 2 个等宽区间
Series bins = Discretizer.cut(series, 5);  // 分成 5 个等宽区间
```

#### 等频分箱 (qcut)
```java
Series bins = Discretizer.qcut(series, 2);  // 分成 2 个等频区间（二分位数）
Series bins = Discretizer.qcut(series, 4);  // 分成 4 个等频区间（四分位数）
```

## Pandas 入口类快捷方法

在 `Pandas.java` 中添加了以下静态快捷方法：

### 数据转换
- `Pandas.pivot(df, index, columns, values)`
- `Pandas.melt(df, idVars, valueVars)`
- `Pandas.stack(df)`
- `Pandas.unstack(series)`

### 插值
- `Pandas.interpolate(series)` - Series 线性插值
- `Pandas.interpolate(series, method)` - Series 指定方法插值
- `Pandas.interpolate(df)` - DataFrame 线性插值
- `Pandas.interpolate(df, method)` - DataFrame 指定方法插值

### 相关性分析
- `Pandas.corr(df)` - 相关系数矩阵
- `Pandas.cov(df)` - 协方差矩阵

### 数据分箱
- `Pandas.cut(series, bins)` - 等宽分箱
- `Pandas.qcut(series, q)` - 等频分箱

### 转置
- `Pandas.T(df)` - 转置 DataFrame

## 文件清单

### 源代码文件 (6个)
1. `transform/Reshaper.java` - 数据转换和重塑
2. `str/StringAccessor.java` - 字符串操作
3. `interpolate/Interpolator.java` - 缺失值插值
4. `stats/Correlation.java` - 相关性分析
5. `discretize/Discretizer.java` - 数据分箱

### 测试文件 (5个)
1. `transform/ReshaperTest.java` - 数据转换测试
2. `str/StringAccessorTest.java` - 字符串操作测试
3. `interpolate/InterpolatorTest.java` - 插值测试
4. `stats/CorrelationTest.java` - 相关性分析测试
5. `discretize/DiscretizerTest.java` - 数据分箱测试

### 文档文件 (3个)
1. `FEATURES.md` - 完整功能文档
2. `NEW_FEATURES.md` - 新增功能文档（本文档）
3. `IMPLEMENTATION_SUMMARY.md` - 实现总结

## 总结

util-math 模块现在是一个功能完整的 Java 数学计算库，包含：

- **核心数据结构**：Num, Series, DataFrame
- **数据转换**：pivot, melt, stack, unstack, crosstab, transpose
- **字符串操作**：完整的字符串处理功能
- **插值功能**：线性插值、前向填充、后向填充
- **相关性分析**：Pearson 相关系数、协方差矩阵
- **数据分箱**：等宽分箱(cut)、等频分箱(qcut)
- **窗口函数**：rolling, expanding, ewm
- **统计功能**：描述统计、聚合运算

所有功能都提供了 Pandas 入口类的快捷方法，使用方式与 Python pandas 非常相似，便于 Java 开发者快速上手。