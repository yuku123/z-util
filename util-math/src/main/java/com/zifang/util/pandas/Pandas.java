package com.zifang.util.pandas;

import com.zifang.util.pandas.io.CSVReader;
import com.zifang.util.pandas.io.CSVWriter;
import com.zifang.util.pandas.matrix.Linalg;
import com.zifang.util.pandas.num.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Pandas 工具类 - 提供类似 pandas 的便捷功能入口
 * 所有方法都是静态的，方便直接调用
 */
public class Pandas {

    // ==================== DataFrame 创建 ====================

    public static DataFrame DataFrame(Map<String, double[]> data) {
        return new DataFrame(data);
    }

    public static DataFrame DataFrame(Map<String, double[]> data, String[] index) {
        return new DataFrame(data, index);
    }

    public static DataFrame DataFrame(List<Map<String, Object>> records) {
        return new DataFrame(records);
    }

    public static DataFrame DataFrame(Object[][] data, String[] columns) {
        Map<String, double[]> map = new java.util.LinkedHashMap<>();
        for (int col = 0; col < columns.length; col++) {
            double[] colData = new double[data.length];
            for (int row = 0; row < data.length; row++) {
                Object val = data[row][col];
                colData[row] = val instanceof Number ? ((Number) val).doubleValue() : Double.NaN;
            }
            map.put(columns[col], colData);
        }
        return new DataFrame(map);
    }

    // ==================== Series 创建 ====================

    public static Series Series(double[] data) {
        return new Series(data);
    }

    public static Series Series(double[] data, String[] index) {
        return new Series(data, index);
    }

    public static Series Series(Map<String, Double> data) {
        return Series.fromMap(data);
    }

    public static Series Series(double[] data, String name) {
        return new Series(data, name);
    }

    // ==================== Num 数组创建 ====================

    public static Num array(double[] data) {
        return new Num(data);
    }

    public static Num array(double[][] data) {
        return new Num(data);
    }

    public static Num array(int[] data) {
        double[] arr = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            arr[i] = data[i];
        }
        return new Num(arr);
    }

    public static Num zeros(int... shape) {
        return Num.zeros(shape);
    }

    public static Num ones(int... shape) {
        return Num.ones(shape);
    }

    public static Num eye(int n) {
        double[][] arr = new double[n][n];
        for (int i = 0; i < n; i++) {
            arr[i][i] = 1.0;
        }
        return new Num(arr);
    }

    public static Num full(int[] shape, double value) {
        return Num.fill(shape, value);
    }

    public static Num arange(double start, double stop, double step) {
        return Num.arange(start, stop, step);
    }

    public static Num arange(double stop) {
        return Num.arange(0, stop, 1);
    }

    public static Num linspace(double start, double stop, int num) {
        return Num.linspace(start, stop, num);
    }

    public static Num linspace(double start, double stop, int num, boolean endpoint) {
        return Num.linspace(start, stop, num, endpoint);
    }

    // ==================== 随机数 ====================

    public static NumRandom random() {
        return Nums.random;
    }

    public static Num rand(int... shape) {
        return Nums.random.rand(shape);
    }

    public static Num randn(int... shape) {
        return Nums.random.randn(shape);
    }

    public static Num randint(int low, int high, int... shape) {
        return Nums.random.randint(low, high, shape);
    }

    public static void seed(long seed) {
        Nums.random.seed(seed);
    }

    // ==================== 文件 I/O ====================

    public static DataFrame read_csv(String filePath) throws IOException {
        return CSVReader.builder().read(filePath);
    }

    public static DataFrame read_csv(String filePath, char delimiter) throws IOException {
        return CSVReader.builder().delimiter(delimiter).read(filePath);
    }

    public static DataFrame read_csv(String filePath, boolean hasHeader) throws IOException {
        return CSVReader.builder().hasHeader(hasHeader).read(filePath);
    }

    public static DataFrame read_csv(String filePath, String encoding) throws IOException {
        return CSVReader.builder().encoding(encoding).read(filePath);
    }

    public static DataFrame read_csv(String filePath, int skipRows) throws IOException {
        return CSVReader.builder().skipRows(skipRows).read(filePath);
    }

    public static DataFrame read_csv(String filePath, int skipRows, int maxRows) throws IOException {
        return CSVReader.builder().skipRows(skipRows).maxRows(maxRows).read(filePath);
    }

    public static void to_csv(DataFrame df, String filePath) throws IOException {
        CSVWriter.builder().write(df, filePath);
    }

    public static void to_csv(DataFrame df, String filePath, char delimiter) throws IOException {
        CSVWriter.builder().delimiter(delimiter).write(df, filePath);
    }

    public static void to_csv(DataFrame df, String filePath, boolean includeHeader) throws IOException {
        CSVWriter.builder().includeHeader(includeHeader).write(df, filePath);
    }

    public static void to_csv(DataFrame df, String filePath, boolean includeHeader, boolean includeIndex) throws IOException {
        CSVWriter.builder()
            .includeHeader(includeHeader)
            .includeIndex(includeIndex)
            .write(df, filePath);
    }

    public static void to_csv(DataFrame df, String filePath, String encoding) throws IOException {
        CSVWriter.builder().encoding(encoding).write(df, filePath);
    }

    public static String to_csv_string(DataFrame df) {
        return CSVWriter.toCSVString(df);
    }

    // ==================== 数学运算 ====================

    public static Num sin(Num x) {
        return Maths.sin(x);
    }

    public static Num cos(Num x) {
        return Maths.cos(x);
    }

    public static Num tan(Num x) {
        return Maths.tan(x);
    }

    public static Num exp(Num x) {
        return Maths.exp(x);
    }

    public static Num log(Num x) {
        return Maths.log(x);
    }

    public static Num log10(Num x) {
        return Maths.log10(x);
    }

    public static Num sqrt(Num x) {
        return Maths.sqrt(x);
    }

    public static Num abs(Num x) {
        return Maths.abs(x);
    }

    public static Num pow(Num x, double y) {
        return Maths.pow(x, y);
    }

    // ==================== 线性代数 ====================

    public static Num dot(Num a, Num b) {
        return Linalg.dot(a, b);
    }

    public static Num matmul(Num a, Num b) {
        return Linalg.matmul(a, b);
    }

    public static Num inv(Num a) {
        return Linalg.inv(a);
    }

    public static Num pinv(Num a) {
        return Linalg.pinv(a);
    }

    public static double det(Num a) {
        return Linalg.det(a);
    }

    public static Linalg.EigenResult eig(Num a) {
        return Linalg.eig(a);
    }

    public static Linalg.SVDResult svd(Num a) {
        return Linalg.svd(a);
    }

    public static Linalg.QRResult qr(Num a) {
        return Linalg.qr(a);
    }

    public static Num solve(Num a, Num b) {
        return Linalg.solve(a, b);
    }

    public static double trace(Num a) {
        return Linalg.trace(a);
    }

    public static double norm(Num x) {
        return Linalg.norm(x);
    }

    public static double norm(Num x, int ord) {
        return Linalg.norm(x, ord);
    }

    public static int matrix_rank(Num a) {
        return Linalg.matrix_rank(a);
    }

    // ==================== 数据转换操作 ====================

    /**
     * 数据透视表 - 创建类似 Excel 的数据透视表
     * 将长格式数据转换为宽格式
     */
    public static DataFrame pivot(DataFrame df, String index, String columns, String values) {
        return com.zifang.util.pandas.transform.Reshaper.pivot(df, index, columns, values);
    }

    /**
     * 带聚合函数的数据透视表
     */
    public static DataFrame pivot(DataFrame df, String index, String columns, String values, String aggFunc) {
        return com.zifang.util.pandas.transform.Reshaper.pivot(df, index, columns, values, aggFunc);
    }

    /**
     * 数据融合 - 将宽格式数据转换为长格式
     * pivot 的逆操作
     */
    public static DataFrame melt(DataFrame df, List<String> idVars, List<String> valueVars,
                                  String varName, String valueName) {
        return com.zifang.util.pandas.transform.Reshaper.melt(df, idVars, valueVars, varName, valueName);
    }

    /**
     * 简化版 melt - 使用默认列名
     */
    public static DataFrame melt(DataFrame df, List<String> idVars, List<String> valueVars) {
        return com.zifang.util.pandas.transform.Reshaper.melt(df, idVars, valueVars);
    }

    /**
     * 堆叠操作 - 将列索引转换为行索引
     * 将 DataFrame 转换为 Series（多级索引）
     */
    public static Series stack(DataFrame df) {
        return com.zifang.util.pandas.transform.Reshaper.stack(df);
    }

    /**
     * 取消堆叠操作 - 将行索引转换为列索引
     * 将 Series（多级索引）转换为 DataFrame
     */
    public static DataFrame unstack(Series series) {
        return com.zifang.util.pandas.transform.Reshaper.unstack(series);
    }

    /**
     * 交叉表 - 创建列联表
     * 统计两个分类变量的频数分布
     */
    public static DataFrame crosstab(DataFrame df, String rowVar, String colVar) {
        return com.zifang.util.pandas.transform.Reshaper.crosstab(df, rowVar, colVar);
    }

    /**
     * 转置 DataFrame
     * 行列互换
     */
    public static DataFrame transpose(DataFrame df) {
        return com.zifang.util.pandas.transform.Reshaper.transpose(df);
    }

    /**
     * 简化版转置 - 使用 T 别名
     */
    public static DataFrame T(DataFrame df) {
        return transpose(df);
    }

    // ==================== 常量 ====================

    public static final double PI = Math.PI;
    public static final double E = Math.E;
    public static final double INF = Double.POSITIVE_INFINITY;
    public static final double NINF = Double.NEGATIVE_INFINITY;
    public static final double NAN = Double.NaN;
    public static final double EPSILON = 2.220446049250313e-16;
}
