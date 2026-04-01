# util-math API 文档

## 概述

util-math 是一个对标 Python numpy 和 pandas 的 Java 数学计算库，提供高性能的多维数组操作和数据分析功能。

## 核心组件

### 1. Num - 多维数组 (对标 numpy.ndarray)

```java
// 创建数组
Num arr1 = Num.zeros(3, 4);           // 3x4 零数组
Num arr2 = Num.ones(2, 2, 2);         // 2x2x2 全1数组
Num arr3 = Num.arange(0, 10, 2);      // [0, 2, 4, 6, 8]
Num arr4 = Num.linspace(0, 1, 100);   // 100个均匀分布的点

// 数组操作
Num reshaped = arr1.reshape(6, 2);    // 重塑形状
Num transposed = arr2.transpose();      // 转置
Num flattened = arr1.flatten();         // 展平

// 算术运算
Num sum = arr1.add(arr2);           // 加法
Num diff = arr1.subtract(arr2);     // 减法
Num prod = arr1.multiply(arr2);     // 元素乘法
Num quot = arr1.divide(arr2);       // 除法
Num matmul = Num.dot(arr1, arr2);    // 矩阵乘法

// 统计方法
double sum = arr1.sum();            // 求和
double mean = arr1.mean();          // 均值
double max = arr1.max();            // 最大值
double min = arr1.min();            // 最小值
double std = arr1.std();            // 标准差
double var = arr1.var();            // 方差

// 数学函数
Num s = Num.sin(arr1);          // 正弦
Num c = Num.cos(arr1);          // 余弦
Num e = Num.exp(arr1);          // 指数
Num l = Num.log(arr1);          // 自然对数
Num sq = Num.sqrt(arr1);        // 平方根
```

### 2. Series - 一维带标签数组 (对标 pandas.Series)

```java
// 创建 Series
Series s1 = new Series(new double[]{1, 2, 3, 4, 5});
Series s2 = new Series(new double[]{1, 2, 3}, new String[]{"a", "b", "c"});
Series s3 = Series.fromMap(Map.of("a", 1.0, "b", 2.0, "c", 3.0));

// 数据访问
double val1 = s1.get(0);              // 位置索引
double val2 = s2.get("a");            // 标签索引
Series subset = s2.get(new String[]{"a", "b"});
Series sliced = s2.slice(0, 2);       // 切片

// 标签操作
Series renamed = s2.rename("new_name");
Series reindexed = s2.setIndex(new String[]{"x", "y", "z"});

// 算术运算
Series sum = s1.add(s2);
Series diff = s1.subtract(s2);
Series prod = s1.multiply(s2);
Series quot = s1.divide(s2);
Series scaled = s1.multiply(2.0);

// 统计方法
double mean = s1.mean();
double std = s1.std();
double median = s1.median();
double percentile = s1.percentile(95);

// 数据清洗
Series filled = s1.fillna(0.0);
Series dropped = s1.dropna();
Series clipped = s1.clip(0, 100);
Series rounded = s1.round(2);

// 变换
Series sorted = s1.sort_values();
Series cumulative = s1.cumsum();
Series differenced = s1.diff();

// 唯一值和计数
Series unique = s1.unique();
int nunique = s1.nunique();
Series counts = s1.value_counts();

// 应用函数
Series applied = s1.apply(x -> x * 2);
Series mapped = s1.map(Map.of(1.0, 100.0, 2.0, 200.0));

// 布尔索引
Series condition = s1.gt(3);          // > 3
Series filtered = s1.filter(x -> x > 2);

// 描述统计
String desc = s1.describe();          // 打印统计摘要
```

### 3. DataFrame - 二维表格数据 (对标 pandas.DataFrame)

```java
// 创建 DataFrame
Map<String, double[]> data = new HashMap<>();
data.put("A", new double[]{1, 2, 3, 4, 5});
        data.put("B", new double[]{5, 4, 3, 2, 1});
        data.put("C", new double[]{1, 1, 1, 1, 1});

DataFrame df = new DataFrame(data, new String[]{"a", "b", "c", "d", "e"});

// 数据访问
Series colA = df.get("A");                    // 获取列
DataFrame subset = df.get(new String[]{"A", "B"});  // 获取多列
double value = df.get("A").get(0);            // 获取特定值

// 行操作
DataFrame sliced = df.slice(0, 3);            // 切片
DataFrame first5 = df.head(5);                // 前5行
DataFrame last5 = df.tail(5);                 // 后5行

// 索引操作
df.setIndex(new String[]{"x", "y", "z", "w", "v"});

// 列操作
        df.addColumn("D", new double[]{2, 2, 2, 2, 2});
        df.dropColumn("C");

// 重命名列
DataFrame renamed = df.rename(Map.of("A", "Alpha", "B", "Beta"));

// 连接和合并
DataFrame df1 = new DataFrame();
DataFrame df2 = new DataFrame();
DataFrame joined = df1.join(df2, "left");    // left, right, inner, outer
DataFrame merged = DataFrame.merge(df1, df2, "A", "B", "inner");

// 分组聚合
DataFrame grouped = df.groupby("A").sum();
DataFrame means = df.groupby("B").mean();
DataFrame counts = df.groupby("A").count();

// 统计方法
Series sums = df.sum();
Series means = df.mean();
Series stds = df.std();
DataFrame stats = df.describe();  // 统计摘要

// 数据清洗
DataFrame filled = df.fillna(0.0);
DataFrame dropped = df.dropna();
DataFrame deduped = df.dropDuplicates();

// 排序
DataFrame sortedByA = df.sort_values("A", true);  // 按列A升序
DataFrame sortedByIndex = df.sort_index();

// 过滤
DataFrame filtered = df.filter(row -> row.get("A") > 2, "A");

// 导出数据
double[][] array = df.toArray();
Map<String, double[]> map = df.toMap();
String csv = df.toCSV();

// 打印
System.out.println(df);
```

### 4. 线性代数 (Linalg) - 对标 numpy.linalg

```java
// 矩阵乘法
Num c = Linalg.dot(a, b);           // 或 matmul

// 向量点积
double d = Linalg.inner(a, b);

// 外积
Num outer = Linalg.outer(a, b);

// 张量积
Num tensordot = Linalg.tensordot(a, b, new int[]{1});

// 克罗内克积
Num kron = Linalg.kron(a, b);

// 矩阵分解
Linalg.SVDResult svd = Linalg.svd(a);     // 奇异值分解
Num u = svd.U;
Num s = svd.S;
Num vh = svd.Vh;

Linalg.QRResult qr = Linalg.qr(a);        // QR 分解
Num q = qr.Q;
Num r = qr.R;

Num cholesky = Linalg.cholesky(a);        // Cholesky 分解

// 矩阵属性
double det = Linalg.det(a);               // 行列式
int rank = Linalg.matrix_rank(a);         // 秩
double trace = Linalg.trace(a);           // 迹

// 矩阵求逆
Num inv = Linalg.inv(a);                  // 逆矩阵
Num pinv = Linalg.pinv(a);                // 伪逆

// 解线性方程组
Num x = Linalg.solve(a, b);               // Ax = b

// 范数
double norm1 = Linalg.norm(x);             // 2-范数
double norm2 = Linalg.norm(x, 1);          // 1-范数
double normInf = Linalg.norm(x, Integer.MAX_VALUE);  // 无穷范数
```

### 5. 数学函数 (Maths) - 对标 numpy

```java
// 三角函数
Num sin = Maths.sin(x);
Num cos = Maths.cos(x);
Num tan = Maths.tan(x);
Num arcsin = Maths.arcsin(x);
Num arccos = Maths.arccos(x);
Num arctan = Maths.arctan(x);
Num arctan2 = Maths.arctan2(y, x);

// 双曲函数
Num sinh = Maths.sinh(x);
Num cosh = Maths.cosh(x);
Num tanh = Maths.tanh(x);

// 指数和对数
Num exp = Maths.exp(x);
Num expm1 = Maths.expm1(x);
Num log = Maths.log(x);
Num log10 = Maths.log10(x);
Num log2 = Maths.log2(x);
Num log1p = Maths.log1p(x);

// 幂函数
Num sqrt = Maths.sqrt(x);
Num cbrt = Maths.cbrt(x);
Num pow = Maths.pow(x, 2);

// 舍入函数
Num round = Maths.round(x);
Num floor = Maths.floor(x);
Num ceil = Maths.ceil(x);
Num trunc = Maths.trunc(x);

// 绝对值和符号
Num abs = Maths.abs(x);
Num sign = Maths.sign(x);

// 统计函数
double max = Maths.max(x);
double min = Maths.min(x);
double sum = Maths.sum(x);
double mean = Maths.mean(x);
double std = Maths.std(x);
double var = Maths.var(x);

// 常量
double pi = Maths.PI;
double e = Maths.E;
double inf = Maths.INF;
double nan = Maths.NAN;
```

## 使用示例

```java
import com.zifang.util.pandas.*;
import com.zifang.util.pandas.num.*;

public class Example {
    public static void main(String[] args) {
        // 创建 Num 数组
        Num arr = Num.arange(0, 10, 0.5);
        System.out.println(arr);

        // 创建 Series
        Series s = new Series(new double[]{1, 2, 3, 4, 5}, new String[]{"a", "b", "c", "d", "e"});
        System.out.println(s.mean());

        // 创建 DataFrame
        Map<String, double[]> data = new HashMap<>();
        data.put("A", new double[]{1, 2, 3});
        data.put("B", new double[]{4, 5, 6});
        DataFrame df = new DataFrame(data);
        System.out.println(df.describe());

        // 线性代数
        Num a = Num.ones(3, 3);
        Num b = Num.eye(3);
        Num c = Linalg.dot(a, b);
        System.out.println(c);
    }
}
```

## 架构设计

```
util-math
├── num/
│   ├── Num.java          - 多维数组 (numpy.ndarray)
│   ├── Series.java       - 一维带标签数组 (pandas.Series)
│   ├── DataFrame.java    - 二维表格数据 (pandas.DataFrame)
│   ├── Index.java        - 标签索引
│   ├── DType.java        - 数据类型
│   ├── Nums.java         - 数组创建工具类
│   ├── NumRandom.java    - 随机数生成
│   └── Maths.java        - 数学函数库
├── matrix/
│   ├── Matrix.java       - 矩阵类
│   ├── Linalg.java       - 线性代数 (numpy.linalg)
│   └── MatrixUtil.java   - 矩阵工具
└── pandas/
    ├── DataFrame.java    - DataFrame 主类
    └── Series.java       - Series 主类
```

## 功能对比表

| 功能 | numpy/pandas | util-math |
|------|-------------|-----------|
| 多维数组 | ndarray | Num |
| 一维带标签数组 | Series | Series |
| 二维表格 | DataFrame | DataFrame |
| 数组创建 | zeros, ones, arange, linspace | Num.zeros(), Num.ones(), Num.arange(), Num.linspace() |
| 数组操作 | reshape, transpose, flatten | Num.reshape(), Num.transpose(), Num.flatten() |
| 算术运算 | +, -, *, /, @ | Num.add(), Num.subtract(), Num.multiply(), Num.divide(), Num.dot() |
| 统计方法 | sum, mean, max, min, std, var | Num.sum(), Num.mean(), Num.max(), Num.min(), Num.std(), Num.var() |
| 数学函数 | sin, cos, exp, log, sqrt | Maths.sin(), Maths.cos(), Maths.exp(), Maths.log(), Maths.sqrt() |
| 矩阵乘法 | dot, matmul | Linalg.dot(), Linalg.matmul() |
| 矩阵分解 | svd, qr, cholesky | Linalg.svd(), Linalg.qr(), Linalg.cholesky() |
| 矩阵求逆 | inv | Linalg.inv() |
| 解方程 | solve | Linalg.solve() |
| 数据选择 | loc, iloc | Series.loc(), Series.iloc() |
| 数据过滤 | filter, where | Series.filter(), Series.where() |
| 数据清洗 | fillna, dropna | Series.fillna(), Series.dropna() |
| 分组聚合 | groupby | DataFrame.groupby() |
| 连接合并 | merge, join | DataFrame.merge(), DataFrame.join() |

## 许可证

MIT License
