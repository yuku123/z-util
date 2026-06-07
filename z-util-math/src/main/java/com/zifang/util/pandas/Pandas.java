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
/**
 * Pandas类。
 */
/**
 * Pandas类。
 */
public class Pandas {

    // ==================== DataFrame 创建 ====================

    /**
     * DataFrame方法。
     *      * @param data MapString,类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * DataFrame方法。
     *      * @param data MapString,类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame DataFrame(Map<String, double[]> data) {
        return new DataFrame(data);
    }

    /**
     * DataFrame方法。
     *      * @param data MapString,类型参数
     * @param index String[]类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * DataFrame方法。
     *      * @param data MapString,类型参数
     * @param index String[]类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame DataFrame(Map<String, double[]> data, String[] index) {
        return new DataFrame(data, index);
    }

    /**
     * DataFrame方法。
     *      * @param records ListMapString,类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * DataFrame方法。
     *      * @param records ListMapString,类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame DataFrame(List<Map<String, Object>> records) {
        return new DataFrame(records);
    }

    /**
     * DataFrame方法。
     *      * @param data Object[][]类型参数
     * @param columns String[]类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * DataFrame方法。
     *      * @param data Object[][]类型参数
     * @param columns String[]类型参数
     * @return static DataFrame类型返回值
     */
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

    /**
     * Series方法。
     *      * @param data double[]类型参数
     * @return static Series类型返回值
     */
    /**
     * Series方法。
     *      * @param data double[]类型参数
     * @return static Series类型返回值
     */
    public static Series Series(double[] data) {
        return new Series(data);
    }

    /**
     * Series方法。
     *      * @param data double[]类型参数
     * @param index String[]类型参数
     * @return static Series类型返回值
     */
    /**
     * Series方法。
     *      * @param data double[]类型参数
     * @param index String[]类型参数
     * @return static Series类型返回值
     */
    public static Series Series(double[] data, String[] index) {
        return new Series(data, index);
    }

    /**
     * Series方法。
     *      * @param data MapString,类型参数
     * @return static Series类型返回值
     */
    /**
     * Series方法。
     *      * @param data MapString,类型参数
     * @return static Series类型返回值
     */
    public static Series Series(Map<String, Double> data) {
        return Series.fromMap(data);
    }

    /**
     * Series方法。
     *      * @param data double[]类型参数
     * @param name String类型参数
     * @return static Series类型返回值
     */
    /**
     * Series方法。
     *      * @param data double[]类型参数
     * @param name String类型参数
     * @return static Series类型返回值
     */
    public static Series Series(double[] data, String name) {
        return new Series(data, name);
    }

    // ==================== Num 数组创建 ====================

    /**
     * array方法。
     *      * @param data double[]类型参数
     * @return static Num类型返回值
     */
    /**
     * array方法。
     *      * @param data double[]类型参数
     * @return static Num类型返回值
     */
    public static Num array(double[] data) {
        return new Num(data);
    }

    /**
     * array方法。
     *      * @param data double[][]类型参数
     * @return static Num类型返回值
     */
    /**
     * array方法。
     *      * @param data double[][]类型参数
     * @return static Num类型返回值
     */
    public static Num array(double[][] data) {
        return new Num(data);
    }

    /**
     * array方法。
     *      * @param data int[]类型参数
     * @return static Num类型返回值
     */
    /**
     * array方法。
     *      * @param data int[]类型参数
     * @return static Num类型返回值
     */
    public static Num array(int[] data) {
        double[] arr = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            arr[i] = data[i];
        }
        return new Num(arr);
    }

    /**
     * zeros方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    /**
     * zeros方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    public static Num zeros(int... shape) {
        return Num.zeros(shape);
    }

    /**
     * ones方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    /**
     * ones方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    public static Num ones(int... shape) {
        return Num.ones(shape);
    }

    /**
     * eye方法。
     *      * @param n int类型参数
     * @return static Num类型返回值
     */
    /**
     * eye方法。
     *      * @param n int类型参数
     * @return static Num类型返回值
     */
    public static Num eye(int n) {
        double[][] arr = new double[n][n];
        for (int i = 0; i < n; i++) {
            arr[i][i] = 1.0;
        }
        return new Num(arr);
    }

    /**
     * full方法。
     *      * @param shape int[]类型参数
     * @param value double类型参数
     * @return static Num类型返回值
     */
    /**
     * full方法。
     *      * @param shape int[]类型参数
     * @param value double类型参数
     * @return static Num类型返回值
     */
    public static Num full(int[] shape, double value) {
        return Num.fill(shape, value);
    }

    /**
     * arange方法。
     *      * @param start double类型参数
     * @param stop double类型参数
     * @param step double类型参数
     * @return static Num类型返回值
     */
    /**
     * arange方法。
     *      * @param start double类型参数
     * @param stop double类型参数
     * @param step double类型参数
     * @return static Num类型返回值
     */
    public static Num arange(double start, double stop, double step) {
        return Num.arange(start, stop, step);
    }

    /**
     * arange方法。
     *      * @param stop double类型参数
     * @return static Num类型返回值
     */
    /**
     * arange方法。
     *      * @param stop double类型参数
     * @return static Num类型返回值
     */
    public static Num arange(double stop) {
        return Num.arange(0, stop, 1);
    }

    /**
     * linspace方法。
     *      * @param start double类型参数
     * @param stop double类型参数
     * @param num int类型参数
     * @return static Num类型返回值
     */
    /**
     * linspace方法。
     *      * @param start double类型参数
     * @param stop double类型参数
     * @param num int类型参数
     * @return static Num类型返回值
     */
    public static Num linspace(double start, double stop, int num) {
        return Num.linspace(start, stop, num);
    }

    /**
     * linspace方法。
     *      * @param start double类型参数
     * @param stop double类型参数
     * @param num int类型参数
     * @param endpoint boolean类型参数
     * @return static Num类型返回值
     */
    /**
     * linspace方法。
     *      * @param start double类型参数
     * @param stop double类型参数
     * @param num int类型参数
     * @param endpoint boolean类型参数
     * @return static Num类型返回值
     */
    public static Num linspace(double start, double stop, int num, boolean endpoint) {
        return Num.linspace(start, stop, num, endpoint);
    }

    // ==================== 随机数 ====================

    /**
     * random方法。
     * @return static NumRandom类型返回值
     */
    /**
     * random方法。
     * @return static NumRandom类型返回值
     */
    public static NumRandom random() {
        return Nums.random;
    }

    /**
     * rand方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    /**
     * rand方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    public static Num rand(int... shape) {
        return Nums.random.rand(shape);
    }

    /**
     * randn方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    /**
     * randn方法。
     *      * @param shape int...类型参数
     * @return static Num类型返回值
     */
    public static Num randn(int... shape) {
        return Nums.random.randn(shape);
    }

    /**
     * randint方法。
     *      * @param low int类型参数
     * @param high int类型参数
     * @param shape int...类型参数
     * @return static Num类型返回值
     */
    /**
     * randint方法。
     *      * @param low int类型参数
     * @param high int类型参数
     * @param shape int...类型参数
     * @return static Num类型返回值
     */
    public static Num randint(int low, int high, int... shape) {
        return Nums.random.randint(low, high, shape);
    }

    /**
     * seed方法。
     *      * @param seed long类型参数
     * @return static void类型返回值
     */
    /**
     * seed方法。
     *      * @param seed long类型参数
     * @return static void类型返回值
     */
    public static void seed(long seed) {
        Nums.random.seed(seed);
    }

    // ==================== 文件 I/O ====================

    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame read_csv(String filePath) throws IOException {
        return CSVReader.builder().read(filePath);
    }

    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param delimiter char类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param delimiter char类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame read_csv(String filePath, char delimiter) throws IOException {
        return CSVReader.builder().delimiter(delimiter).read(filePath);
    }

    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param hasHeader boolean类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param hasHeader boolean类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame read_csv(String filePath, boolean hasHeader) throws IOException {
        return CSVReader.builder().hasHeader(hasHeader).read(filePath);
    }

    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param encoding String类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param encoding String类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame read_csv(String filePath, String encoding) throws IOException {
        return CSVReader.builder().encoding(encoding).read(filePath);
    }

    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param skipRows int类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param skipRows int类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame read_csv(String filePath, int skipRows) throws IOException {
        return CSVReader.builder().skipRows(skipRows).read(filePath);
    }

    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param skipRows int类型参数
     * @param maxRows int类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * read_csv方法。
     *      * @param filePath String类型参数
     * @param skipRows int类型参数
     * @param maxRows int类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame read_csv(String filePath, int skipRows, int maxRows) throws IOException {
        return CSVReader.builder().skipRows(skipRows).maxRows(maxRows).read(filePath);
    }

    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @return static void类型返回值
     */
    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @return static void类型返回值
     */
    public static void to_csv(DataFrame df, String filePath) throws IOException {
        CSVWriter.builder().write(df, filePath);
    }

    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param delimiter char类型参数
     * @return static void类型返回值
     */
    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param delimiter char类型参数
     * @return static void类型返回值
     */
    public static void to_csv(DataFrame df, String filePath, char delimiter) throws IOException {
        CSVWriter.builder().delimiter(delimiter).write(df, filePath);
    }

    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param includeHeader boolean类型参数
     * @return static void类型返回值
     */
    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param includeHeader boolean类型参数
     * @return static void类型返回值
     */
    public static void to_csv(DataFrame df, String filePath, boolean includeHeader) throws IOException {
        CSVWriter.builder().includeHeader(includeHeader).write(df, filePath);
    }

    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param includeHeader boolean类型参数
     * @param includeIndex boolean类型参数
     * @return static void类型返回值
     */
    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param includeHeader boolean类型参数
     * @param includeIndex boolean类型参数
     * @return static void类型返回值
     */
    public static void to_csv(DataFrame df, String filePath, boolean includeHeader, boolean includeIndex) throws IOException {
        CSVWriter.builder()
            .includeHeader(includeHeader)
            .includeIndex(includeIndex)
            .write(df, filePath);
    }

    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param encoding String类型参数
     * @return static void类型返回值
     */
    /**
     * to_csv方法。
     *      * @param df DataFrame类型参数
     * @param filePath String类型参数
     * @param encoding String类型参数
     * @return static void类型返回值
     */
    public static void to_csv(DataFrame df, String filePath, String encoding) throws IOException {
        CSVWriter.builder().encoding(encoding).write(df, filePath);
    }

    /**
     * to_csv_string方法。
     *      * @param df DataFrame类型参数
     * @return static String类型返回值
     */
    /**
     * to_csv_string方法。
     *      * @param df DataFrame类型参数
     * @return static String类型返回值
     */
    public static String to_csv_string(DataFrame df) {
        return CSVWriter.toCSVString(df);
    }

    // ==================== 数学运算 ====================

    /**
     * sin方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * sin方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num sin(Num x) {
        return Maths.sin(x);
    }

    /**
     * cos方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * cos方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num cos(Num x) {
        return Maths.cos(x);
    }

    /**
     * tan方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * tan方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num tan(Num x) {
        return Maths.tan(x);
    }

    /**
     * exp方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * exp方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num exp(Num x) {
        return Maths.exp(x);
    }

    /**
     * log方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * log方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num log(Num x) {
        return Maths.log(x);
    }

    /**
     * log10方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * log10方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num log10(Num x) {
        return Maths.log10(x);
    }

    /**
     * sqrt方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * sqrt方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num sqrt(Num x) {
        return Maths.sqrt(x);
    }

    /**
     * abs方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    /**
     * abs方法。
     *      * @param x Num类型参数
     * @return static Num类型返回值
     */
    public static Num abs(Num x) {
        return Maths.abs(x);
    }

    /**
     * pow方法。
     *      * @param x Num类型参数
     * @param y double类型参数
     * @return static Num类型返回值
     */
    /**
     * pow方法。
     *      * @param x Num类型参数
     * @param y double类型参数
     * @return static Num类型返回值
     */
    public static Num pow(Num x, double y) {
        return Maths.pow(x, y);
    }

    // ==================== 线性代数 ====================

    /**
     * dot方法。
     *      * @param a Num类型参数
     * @param b Num类型参数
     * @return static Num类型返回值
     */
    /**
     * dot方法。
     *      * @param a Num类型参数
     * @param b Num类型参数
     * @return static Num类型返回值
     */
    public static Num dot(Num a, Num b) {
        return Linalg.dot(a, b);
    }

    /**
     * matmul方法。
     *      * @param a Num类型参数
     * @param b Num类型参数
     * @return static Num类型返回值
     */
    /**
     * matmul方法。
     *      * @param a Num类型参数
     * @param b Num类型参数
     * @return static Num类型返回值
     */
    public static Num matmul(Num a, Num b) {
        return Linalg.matmul(a, b);
    }

    /**
     * inv方法。
     *      * @param a Num类型参数
     * @return static Num类型返回值
     */
    /**
     * inv方法。
     *      * @param a Num类型参数
     * @return static Num类型返回值
     */
    public static Num inv(Num a) {
        return Linalg.inv(a);
    }

    /**
     * pinv方法。
     *      * @param a Num类型参数
     * @return static Num类型返回值
     */
    /**
     * pinv方法。
     *      * @param a Num类型参数
     * @return static Num类型返回值
     */
    public static Num pinv(Num a) {
        return Linalg.pinv(a);
    }

    /**
     * det方法。
     *      * @param a Num类型参数
     * @return static double类型返回值
     */
    /**
     * det方法。
     *      * @param a Num类型参数
     * @return static double类型返回值
     */
    public static double det(Num a) {
        return Linalg.det(a);
    }

    /**
     * eig方法。
     *      * @param a Num类型参数
     * @return static Linalg.EigenResult类型返回值
     */
    /**
     * eig方法。
     *      * @param a Num类型参数
     * @return static Linalg.EigenResult类型返回值
     */
    public static Linalg.EigenResult eig(Num a) {
        return Linalg.eig(a);
    }

    /**
     * svd方法。
     *      * @param a Num类型参数
     * @return static Linalg.SVDResult类型返回值
     */
    /**
     * svd方法。
     *      * @param a Num类型参数
     * @return static Linalg.SVDResult类型返回值
     */
    public static Linalg.SVDResult svd(Num a) {
        return Linalg.svd(a);
    }

    /**
     * qr方法。
     *      * @param a Num类型参数
     * @return static Linalg.QRResult类型返回值
     */
    /**
     * qr方法。
     *      * @param a Num类型参数
     * @return static Linalg.QRResult类型返回值
     */
    public static Linalg.QRResult qr(Num a) {
        return Linalg.qr(a);
    }

    /**
     * solve方法。
     *      * @param a Num类型参数
     * @param b Num类型参数
     * @return static Num类型返回值
     */
    /**
     * solve方法。
     *      * @param a Num类型参数
     * @param b Num类型参数
     * @return static Num类型返回值
     */
    public static Num solve(Num a, Num b) {
        return Linalg.solve(a, b);
    }

    /**
     * trace方法。
     *      * @param a Num类型参数
     * @return static double类型返回值
     */
    /**
     * trace方法。
     *      * @param a Num类型参数
     * @return static double类型返回值
     */
    public static double trace(Num a) {
        return Linalg.trace(a);
    }

    /**
     * norm方法。
     *      * @param x Num类型参数
     * @return static double类型返回值
     */
    /**
     * norm方法。
     *      * @param x Num类型参数
     * @return static double类型返回值
     */
    public static double norm(Num x) {
        return Linalg.norm(x);
    }

    /**
     * norm方法。
     *      * @param x Num类型参数
     * @param ord int类型参数
     * @return static double类型返回值
     */
    /**
     * norm方法。
     *      * @param x Num类型参数
     * @param ord int类型参数
     * @return static double类型返回值
     */
    public static double norm(Num x, int ord) {
        return Linalg.norm(x, ord);
    }

    /**
     * matrix_rank方法。
     *      * @param a Num类型参数
     * @return static int类型返回值
     */
    /**
     * matrix_rank方法。
     *      * @param a Num类型参数
     * @return static int类型返回值
     */
    public static int matrix_rank(Num a) {
        return Linalg.matrix_rank(a);
    }

    // ==================== 数据转换操作 ====================


    /**
     * 带聚合函数的数据透视表
     */
    /**
     * pivot方法。
     *      * @param df DataFrame类型参数
     * @param index String类型参数
     * @param columns String类型参数
     * @param values String类型参数
     * @param aggFunc String类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * pivot方法。
     *      * @param df DataFrame类型参数
     * @param index String类型参数
     * @param columns String类型参数
     * @param values String类型参数
     * @param aggFunc String类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame pivot(DataFrame df, String index, String columns, String values, String aggFunc) {
        return com.zifang.util.pandas.transform.Reshaper.pivot(df, index, columns, values, aggFunc);
    }

    /**
     * 数据融合 - 将宽格式数据转换为长格式
     * pivot 的逆操作
     */
    /**
     * melt方法。
     *      * @param df DataFrame类型参数
     * @param idVars ListString类型参数
     * @param valueVars ListString类型参数
     * @param varName String类型参数
     * @param valueName String类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * melt方法。
     *      * @param df DataFrame类型参数
     * @param idVars ListString类型参数
     * @param valueVars ListString类型参数
     * @param varName String类型参数
     * @param valueName String类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame melt(DataFrame df, List<String> idVars, List<String> valueVars,
                                  String varName, String valueName) {
        return com.zifang.util.pandas.transform.Reshaper.melt(df, idVars, valueVars, varName, valueName);
    }

    /**
     * 简化版 melt - 使用默认列名
     */
    /**
     * melt方法。
     *      * @param df DataFrame类型参数
     * @param idVars ListString类型参数
     * @param valueVars ListString类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * melt方法。
     *      * @param df DataFrame类型参数
     * @param idVars ListString类型参数
     * @param valueVars ListString类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame melt(DataFrame df, List<String> idVars, List<String> valueVars) {
        return com.zifang.util.pandas.transform.Reshaper.melt(df, idVars, valueVars);
    }

    /**
     * 堆叠操作 - 将列索引转换为行索引
     * 将 DataFrame 转换为 Series（多级索引）
     */
    /**
     * stack方法。
     *      * @param df DataFrame类型参数
     * @return static Series类型返回值
     */
    /**
     * stack方法。
     *      * @param df DataFrame类型参数
     * @return static Series类型返回值
     */
    public static Series stack(DataFrame df) {
        return com.zifang.util.pandas.transform.Reshaper.stack(df);
    }

    /**
     * 取消堆叠操作 - 将行索引转换为列索引
     * 将 Series（多级索引）转换为 DataFrame
     */
    /**
     * unstack方法。
     *      * @param series Series类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * unstack方法。
     *      * @param series Series类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame unstack(Series series) {
        return com.zifang.util.pandas.transform.Reshaper.unstack(series);
    }

    /**
     * 交叉表 - 创建列联表
     * 统计两个分类变量的频数分布
     */
    /**
     * crosstab方法。
     *      * @param df DataFrame类型参数
     * @param rowVar String类型参数
     * @param colVar String类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * crosstab方法。
     *      * @param df DataFrame类型参数
     * @param rowVar String类型参数
     * @param colVar String类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame crosstab(DataFrame df, String rowVar, String colVar) {
        return com.zifang.util.pandas.transform.Reshaper.crosstab(df, rowVar, colVar);
    }

    /**
     * 转置 DataFrame
     * 行列互换
     */
    /**
     * transpose方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * transpose方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame transpose(DataFrame df) {
        return com.zifang.util.pandas.transform.Reshaper.transpose(df);
    }

    /**
     * 简化版转置 - 使用 T 别名
     */
    /**
     * T方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * T方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame T(DataFrame df) {
        return transpose(df);
    }

    // ==================== 插值操作 ====================

    /**
     * 对 Series 进行线性插值
     */
    /**
     * interpolate方法。
     *      * @param series Series类型参数
     * @return static Series类型返回值
     */
    /**
     * interpolate方法。
     *      * @param series Series类型参数
     * @return static Series类型返回值
     */
    public static Series interpolate(Series series) {
        return com.zifang.util.pandas.interpolate.Interpolator.linear(series);
    }

    /**
     * 对 Series 进行指定方法的插值
     */
    /**
     * interpolate方法。
     *      * @param series Series类型参数
     * @param method String类型参数
     * @return static Series类型返回值
     */
    /**
     * interpolate方法。
     *      * @param series Series类型参数
     * @param method String类型参数
     * @return static Series类型返回值
     */
    public static Series interpolate(Series series, String method) {
        switch (method.toLowerCase()) {
            case "forward":
            case "ffill":
                return com.zifang.util.pandas.interpolate.Interpolator.forward(series);
            case "backward":
            case "bfill":
                return com.zifang.util.pandas.interpolate.Interpolator.backward(series);
            case "linear":
            default:
                return com.zifang.util.pandas.interpolate.Interpolator.linear(series);
        }
    }

    /**
     * 对 DataFrame 进行线性插值
     */
    /**
     * interpolate方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * interpolate方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame interpolate(DataFrame df) {
        return df.interpolate("linear");
    }

    /**
     * 对 DataFrame 进行指定方法的插值
     */
    /**
     * interpolate方法。
     *      * @param df DataFrame类型参数
     * @param method String类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * interpolate方法。
     *      * @param df DataFrame类型参数
     * @param method String类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame interpolate(DataFrame df, String method) {
        return df.interpolate(method);
    }

    // ==================== 统计分析和相关性 ====================

    /**
     * 计算 DataFrame 的相关系数矩阵
     */
    /**
     * corr方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * corr方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame corr(DataFrame df) {
        return com.zifang.util.pandas.stats.Correlation.corr(df);
    }

    /**
     * 计算两个 Series 的 Pearson 相关系数
     */
    /**
     * corr方法。
     *      * @param x Series类型参数
     * @param y Series类型参数
     * @return static double类型返回值
     */
    /**
     * corr方法。
     *      * @param x Series类型参数
     * @param y Series类型参数
     * @return static double类型返回值
     */
    public static double corr(Series x, Series y) {
        return com.zifang.util.pandas.stats.Correlation.corr(
            new DataFrame(java.util.Collections.singletonMap("x", x.toArray()))
                .addColumn("y", y.toArray())
        ).get("y").toArray()[0];
    }

    /**
     * 计算 DataFrame 的协方差矩阵
     */
    /**
     * cov方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * cov方法。
     *      * @param df DataFrame类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame cov(DataFrame df) {
        return com.zifang.util.pandas.stats.Correlation.cov(df);
    }

    // ==================== 数据离散化和分箱 ====================

    /**
     * 等宽分箱 (cut)
     * 将数据分成等宽的区间
     */
    /**
     * cut方法。
     *      * @param series Series类型参数
     * @param bins int类型参数
     * @return static Series类型返回值
     */
    /**
     * cut方法。
     *      * @param series Series类型参数
     * @param bins int类型参数
     * @return static Series类型返回值
     */
    public static Series cut(Series series, int bins) {
        return com.zifang.util.pandas.discretize.Discretizer.cut(series, bins);
    }

    /**
     * 等频分箱 (qcut)
     * 将数据分成每个箱子中样本数量相等的区间
     */
    /**
     * qcut方法。
     *      * @param series Series类型参数
     * @param q int类型参数
     * @return static Series类型返回值
     */
    /**
     * qcut方法。
     *      * @param series Series类型参数
     * @param q int类型参数
     * @return static Series类型返回值
     */
    public static Series qcut(Series series, int q) {
        return com.zifang.util.pandas.discretize.Discretizer.qcut(series, q);
    }

    // ==================== 数据转换快捷方法 ====================

    /**
     * 数据透视表的快捷方法
     */
    /**
     * pivot方法。
     *      * @param df DataFrame类型参数
     * @param index String类型参数
     * @param columns String类型参数
     * @param values String类型参数
     * @return static DataFrame类型返回值
     */
    /**
     * pivot方法。
     *      * @param df DataFrame类型参数
     * @param index String类型参数
     * @param columns String类型参数
     * @param values String类型参数
     * @return static DataFrame类型返回值
     */
    public static DataFrame pivot(DataFrame df, String index, String columns, String values) {
        return com.zifang.util.pandas.transform.Reshaper.pivot(df, index, columns, values);
    }

    // ==================== 常量 ====================

    public static final double PI = Math.PI;
    public static final double E = Math.E;
    public static final double INF = Double.POSITIVE_INFINITY;
    public static final double NINF = Double.NEGATIVE_INFINITY;
    public static final double NAN = Double.NaN;
    public static final double EPSILON = 2.220446049250313e-16;
}
