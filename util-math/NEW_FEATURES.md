# util-math 新增功能

本文档描述了 util-math 模块最近新增的功能，包括数据转换、字符串操作、插值、相关性分析、数据分箱等功能。

## 1. 数据转换和重塑 (Reshaper)

### 1.1 pivot - 数据透视表
将长格式数据转换为宽格式，类似于 Excel 的数据透视表。

```java
DataFrame pivotTable = Pandas.pivot(df, "product", "region", "sales");
// 或使用 Reshaper 直接调用
DataFrame pivotTable = Reshaper.pivot(df, "product", "region", "sales");
```

### 1.2 melt - 数据融合
将宽格式数据转换为长格式，是 pivot 的逆操作。

```java
java.util.List<String> idVars = java.util.Arrays.asList("id", "name");
java.util.List<String> valueVars = java.util.Arrays.asList("math", "english", "science");

DataFrame melted = Pandas.melt(df, idVars, valueVars);
// 或使用 Reshaper 直接调用
DataFrame melted = Reshaper.melt(df, idVars, valueVars);
```

### 1.3 stack/unstack - 堆叠/取消堆叠
在行列之间转换数据维度。

```java
// 将 DataFrame 转换为 Series（多级索引）
Series stacked = Pandas.stack(df);
// 或将 Series（多级索引）转换为 DataFrame
DataFrame unstacked = Pandas.unstack(series);
```

### 1.4 crosstab - 交叉表
创建两个分类变量的列联表。

```java
DataFrame crosstab = Reshaper.crosstab(df, "product", "region");
```

### 1.5 transpose - 转置
行列互换操作。

```java
DataFrame transposed = Reshaper.transpose(df);
// 或使用 T 别名
DataFrame transposed = Pandas.T(df);
```

## 2. 字符串操作 (StringAccessor)

通过 `Series.str()` 访问器提供丰富的字符串处理功能：

### 2.1 大小写转换
```java
Series str = Series.of("Hello", "WORLD", "Test");
Series lower = str.str().lower();  // ["hello", "world", "test"]
Series upper = str.str().upper();  // ["HELLO", "WORLD", "TEST"]
```

### 2.2 空白处理
```java
Series str = Series.of("  hello  ", "  world", "test  ");
Series stripped = str.str().strip();   // ["hello", "world", "test"]
Series lstripped = str.str().lstrip();  // ["hello  ", "world", "test  "]
Series rstripped = str.str().rstrip();  // ["  hello", "  world", "test"]
```

### 2.3 查找和替换
```java
Series str = Series.of("hello world", "hello java", "test");
Series contains = str.str().contains("hello");  // [true, true, false]
Series replaced = str.str().replace("hello", "hi");  // ["hi world", "hi java", "test"]
Series regexReplaced = str.str().replaceRegex("[aeiou]", "*");  // 替换元音
```

### 2.4 分割和提取
```java
Series str = Series.of("hello-world-test", "java-python-cpp", "single");
Series split = str.str().split("-", 0);  // 取第一部分
Series sliced = str.str().slice(0, 5);  // 取前5个字符

Series data = Series.of("2024-03-15", "2023-12-25", "invalid");
Series year = data.str().extract("(\\d{4})", 1);  // 提取年份
```

### 2.5 其他操作
```java
Series str = Series.of("hello", "world", "test");
Series lengths = str.str().length();  // [5, 5, 4]
Series patternMatched = str.str().match("^[a-z]+$");  // 是否全为小写字母
Series concatenated = str.str().cat("_suffix");  // ["hello_suffix", "world_suffix", "test_suffix"]
Series repeated = str.str().repeat(2);  // ["hellohello", "worldworld", "testtest"]
```

## 3. 缺失值插值 (Interpolator)

### 3.1 线性插值
```java
Series series = new Series(new double[]{1, Double.NaN, 3, Double.NaN, 5});
Series interpolated = Pandas.interpolate(series);  // [1, 2, 3, 4, 5]
// 或指定方法
Series interpolated = Pandas.interpolate(series, "linear");
```

### 3.2 前向填充和后向填充
```java
Series series = new Series(new double[]{1, Double.NaN, Double.NaN, 4, Double.NaN});
Series forward = Pandas.interpolate(series, "forward");  // [1, 1, 1, 4, 4]
Series backward = Pandas.interpolate(series, "backward");  // 使用后一个非缺失值填充
```

### 3.3 DataFrame 插值
```java
java.util.Map<String, double[]> data = new java.util.LinkedHashMap<>();
data.put("A", new double[]{1, Double.NaN, 3, Double.NaN, 5});
data.put("B", new double[]{Double.NaN, 2, 3, 4, Double.NaN});

DataFrame df = new DataFrame(data);
DataFrame interpolated = Pandas.interpolate(df);  // 对所有列进行线性插值
DataFrame interpolated = Pandas.interpolate(df, "forward");  // 使用前向填充
```

## 4. 相关性分析 (Correlation)

### 4.1 相关系数矩阵
```java
java.util.Map<String, double[]> data = new java.util.HashMap<>();
data.put("X", new double[]{1, 2, 3, 4, 5});
data.put("Y", new double[]{2, 4, 6, 8, 10});  // Y = 2X

DataFrame df = new DataFrame(data);
DataFrame corr = Pandas.corr(df);
// 结果：
//       X    Y
// X   1.0  1.0
// Y   1.0  1.0
```

### 4.2 协方差矩阵
```java
DataFrame cov = Pandas.cov(df);
```

## 5. 数据分箱 (Discretizer)

### 5.1 等宽分箱 (cut)
```java
Series series = new Series(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
Series bins = Pandas.cut(series, 2);  // 分成 2 个等宽区间
// 结果：[0, 0, 0, 0, 0, 1, 1, 1, 1, 1]
// 1-5 在箱子 0，6-10 在箱子 1

Series bins = Pandas.cut(series, 5);  // 分成 5 个等宽区间
// 结果：[0, 0, 1, 1, 2, 2, 3, 3, 4, 4]
```

### 5.2 等频分箱 (qcut)
```java
Series series = new Series(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
Series bins = Pandas.qcut(series, 2);  // 分成 2 个等频区间
// 结果：每个箱子有 5 个数据点

Series bins = Pandas.qcut(series, 4);  // 分成 4 个等频区间（四分位数）
// 结果：每个箱子有 2-3 个数据点
```

## 6. 窗口函数

### 6.1 滚动窗口 (Rolling)
```java
Series series = new Series(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

Series rollingMean = series.rolling(3).mean();  // 3 期移动平均
Series rollingSum = series.rolling(3).sum();      // 3 期移动和
Series rollingMax = series.rolling(3).max();      // 3 期移动最大值
Series rollingStd = series.rolling(3).std();      // 3 期移动标准差
```

### 6.2 扩展窗口 (Expanding)
```java
Series expandingMean = series.expanding().mean();  // 扩展平均
Series expandingSum = series.expanding().sum();    // 扩展和
```

### 6.3 指数加权移动窗口 (EWM)
```java
Series ewmMean = series.ewm(0.5).mean();       // 使用 alpha=0.5
Series ewmMeanSpan = series.ewmSpan(10).mean(); // 使用 span=10
Series ewmMeanCom = series.ewmCom(5).mean();   // 使用 COM=5
```

## 7. 字符串操作

```java
Series str = Series.of("Hello", "WORLD", "Test 123");

// 大小写转换
Series lower = str.str().lower();  // ["hello", "world", "test 123"]
Series upper = str.str().upper();  // ["HELLO", "WORLD", "TEST 123"]

// 空白处理
Series stripped = str.str().strip();  // 去除首尾空白

// 查找和替换
Series contains = str.str().contains("ell");  // 是否包含子串
Series replaced = str.str().replace("Hello", "Hi");  // 替换字符串

// 分割和提取
Series split = str.str().split(" ", 0);  // 按空格分割取第一部分
Series sliced = str.str().slice(0, 5);    // 取前5个字符

// 其他操作
Series lengths = str.str().length();  // 字符串长度
Series matched = str.str().match("^[A-Za-z]+$");  // 正则匹配
```

## 8. 新增文件列表

### 源代码文件
1. `/util-math/src/main/java/com/zifang/util/pandas/transform/Reshaper.java` - 数据转换和重塑
2. `/util-math/src/main/java/com/zifang/util/pandas/str/StringAccessor.java` - 字符串操作
3. `/util-math/src/main/java/com/zifang/util/pandas/interpolate/Interpolator.java` - 缺失值插值
4. `/util-math/src/main/java/com/zifang/util/pandas/stats/Correlation.java` - 相关性分析
5. `/util-math/src/main/java/com/zifang/util/pandas/discretize/Discretizer.java` - 数据分箱

### 测试文件
1. `/util-math/src/test/java/com/zifang/util/pandas/transform/ReshaperTest.java` - 数据转换测试
2. `/util-math/src/test/java/com/zifang/util/pandas/str/StringAccessorTest.java` - 字符串操作测试
3. `/util-math/src/test/java/com/zifang/util/pandas/interpolate/InterpolatorTest.java` - 插值测试
4. `/util-math/src/test/java/com/zifang/util/pandas/stats/CorrelationTest.java` - 相关性分析测试
5. `/util-math/src/test/java/com/zifang/util/pandas/discretize/DiscretizerTest.java` - 数据分箱测试

## 9. 功能总览

### 新增功能清单
- ✅ 数据转换：pivot, melt, stack, unstack, crosstab, transpose
- ✅ 字符串操作：大小写转换、空白处理、查找替换、分割提取、正则匹配
- ✅ 插值功能：线性插值、前向填充、后向填充
- ✅ 相关性分析：Pearson 相关系数、协方差矩阵
- ✅ 数据分箱：等宽分箱(cut)、等频分箱(qcut)
- ✅ 窗口函数：rolling, expanding, ewm
- ✅ Series 增强：str() 访问器、插值方法
- ✅ DataFrame 增强：插值方法

这些新增功能使得 util-math 库更加完整，可以更好地替代 Python 的 numpy 和 pandas 在 Java 项目中的使用。