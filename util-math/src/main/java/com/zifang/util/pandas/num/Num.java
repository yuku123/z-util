package com.zifang.util.pandas.num;

import com.zifang.util.core.lang.ArraysUtil;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Num 类 - Java 版本的 numpy ndarray
 * 提供多维数组的创建、操作和计算功能
 */
public class Num {

    private Object array;
    private DType dType;
    private int[] shape;
    private int size;

    public Num(Object array) {
        if (!array.getClass().isArray()) {
            throw new RuntimeException("param is not an array");
        }
        this.array = array;
        this.dType = inferDType(array);
        this.shape = computeShape();
        this.size = computeSize();
    }

    /**
     * 从给定的形状和数据类型创建 Num
     */
    public static Num zeros(int... shape) {
        return zeros(shape, DType.FLOAT64);
    }

    public static Num zeros(int[] shape, DType dtype) {
        Object array = createArray(shape, dtype);
        return new Num(array);
    }

    public static Num ones(int... shape) {
        return fill(shape, 1.0);
    }

    public static Num fill(int[] shape, double value) {
        Object array = createFilledArray(shape, value);
        return new Num(array);
    }

    public static Num arange(double start, double stop, double step) {
        int size = (int) Math.ceil((stop - start) / step);
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = start + i * step;
        }
        return new Num(array);
    }

    public static Num arange(int stop) {
        return arange(0, stop, 1);
    }

    public static Num linspace(double start, double stop, int num) {
        return linspace(start, stop, num, true);
    }

    public static Num linspace(double start, double stop, int num, boolean endpoint) {
        double step = endpoint ? (stop - start) / (num - 1) : (stop - start) / num;
        double[] array = new double[num];
        for (int i = 0; i < num; i++) {
            array[i] = start + i * step;
        }
        return new Num(array);
    }

    // ==================== 算术运算 ====================

    public Num add(Num other) {
        return elementWiseOp(other, (a, b) -> a + b);
    }

    public Num add(double scalar) {
        return elementWiseOp(scalar, (a, b) -> a + b);
    }

    public Num subtract(Num other) {
        return elementWiseOp(other, (a, b) -> a - b);
    }

    public Num subtract(double scalar) {
        return elementWiseOp(scalar, (a, b) -> a - b);
    }

    public Num multiply(Num other) {
        return elementWiseOp(other, (a, b) -> a * b);
    }

    public Num multiply(double scalar) {
        return elementWiseOp(scalar, (a, b) -> a * b);
    }

    public Num divide(Num other) {
        return elementWiseOp(other, (a, b) -> a / b);
    }

    public Num divide(double scalar) {
        return elementWiseOp(scalar, (a, b) -> a / b);
    }

    public Num pow(double exponent) {
        return elementWiseOp(exponent, Math::pow);
    }

    // ==================== 统计方法 ====================

    public double sum() {
        return reduce((a, b) -> a + b, 0.0);
    }

    public double mean() {
        return sum() / size;
    }

    public double max() {
        return reduce((a, b) -> Math.max(a, b), Double.NEGATIVE_INFINITY);
    }

    public double min() {
        return reduce((a, b) -> Math.min(a, b), Double.POSITIVE_INFINITY);
    }

    public double std() {
        return Math.sqrt(var());
    }

    public double var() {
        double mean = mean();
        return reduce((acc, val) -> acc + Math.pow(val - mean, 2), 0.0) / size;
    }

    // ==================== 数组操作 ====================

    public Num reshape(int... newShape) {
        int newSize = 1;
        for (int dim : newShape) {
            newSize *= dim;
        }
        if (newSize != size) {
            throw new IllegalArgumentException("Cannot reshape array of size " + size + " into shape " + Arrays.toString(newShape));
        }
        Num result = new Num(copyArray());
        result.shape = newShape.clone();
        return result;
    }

    public Num transpose() {
        if (shape.length < 2) {
            return this;
        }
        // 简化的转置实现（仅支持2D）
        if (shape.length == 2) {
            int rows = shape[0];
            int cols = shape[1];
            double[][] transposed = new double[cols][rows];
            double[][] original = (double[][]) array;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    transposed[j][i] = original[i][j];
                }
            }
            Num result = new Num(transposed);
            result.shape = new int[]{cols, rows};
            return result;
        }
        throw new UnsupportedOperationException("Transpose for dimensions > 2 not yet implemented");
    }

    public Num flatten() {
        return reshape(size);
    }

    // ==================== 数学函数 ====================

    public Num sin() {
        return apply(Math::sin);
    }

    public Num cos() {
        return apply(Math::cos);
    }

    public Num tan() {
        return apply(Math::tan);
    }

    public Num exp() {
        return apply(Math::exp);
    }

    public Num log() {
        return apply(Math::log);
    }

    public Num log10() {
        return apply(Math::log10);
    }

    public Num sqrt() {
        return apply(Math::sqrt);
    }

    public Num abs() {
        return apply(Math::abs);
    }

    // ==================== 工具方法 ====================

    @Override
    public String toString() {
        return "Num(shape=" + Arrays.toString(shape) + ", dtype=" + dType + ", data=" + Arrays.deepToString((Object[]) array) + ")";
    }

    public int[] shape() {
        return shape.clone();
    }

    public int nDim() {
        return shape.length;
    }

    public int size() {
        return size;
    }

    public DType dtype() {
        return dType;
    }

    public Object data() {
        return array;
    }

    // ==================== 私有辅助方法 ====================

    private DType inferDType(Object array) {
        String name = array.getClass().getName();
        if (name.contains("D") || name.contains("F")) {
            return DType.FLOAT64;
        }
        return DType.FLOAT64; // 默认使用 float64
    }

    private int[] computeShape() {
        java.util.List<Integer> dims = new java.util.ArrayList<>();
        Object current = array;
        while (current.getClass().isArray()) {
            dims.add(java.lang.reflect.Array.getLength(current));
            if (java.lang.reflect.Array.getLength(current) > 0) {
                current = java.lang.reflect.Array.get(current, 0);
            } else {
                break;
            }
        }
        int[] result = new int[dims.size()];
        for (int i = 0; i < dims.size(); i++) {
            result[i] = dims.get(i);
        }
        return result;
    }

    private int computeSize() {
        if (shape.length == 0) return 0;
        int total = 1;
        for (int dim : shape) {
            total *= dim;
        }
        return total;
    }

    private static Object createArray(int[] shape, DType dtype) {
        if (shape.length == 1) {
            return new double[shape[0]];
        } else if (shape.length == 2) {
            return new double[shape[0]][shape[1]];
        }
        throw new UnsupportedOperationException("Arrays with dimension > 2 not yet fully supported");
    }

    private static Object createFilledArray(int[] shape, double value) {
        Object array = createArray(shape, DType.FLOAT64);
        fillArray(array, value);
        return array;
    }

    private static void fillArray(Object array, double value) {
        if (array instanceof double[]) {
            double[] arr = (double[]) array;
            java.util.Arrays.fill(arr, value);
        } else if (array instanceof double[][]) {
            double[][] arr = (double[][]) array;
            for (double[] row : arr) {
                java.util.Arrays.fill(row, value);
            }
        }
    }

    private Object copyArray() {
        if (array instanceof double[]) {
            return ((double[]) array).clone();
        } else if (array instanceof double[][]) {
            double[][] original = (double[][]) array;
            double[][] copy = new double[original.length][];
            for (int i = 0; i < original.length; i++) {
                copy[i] = original[i].clone();
            }
            return copy;
        }
        return array;
    }

    private Num elementWiseOp(Num other, java.util.function.DoubleBinaryOperator op) {
        // 简化的实现，假设两个数组形状相同
        if (array instanceof double[] && other.array instanceof double[]) {
            double[] a = (double[]) array;
            double[] b = (double[]) other.array;
            double[] result = new double[a.length];
            for (int i = 0; i < a.length; i++) {
                result[i] = op.applyAsDouble(a[i], b[i]);
            }
            return new Num(result);
        }
        throw new UnsupportedOperationException("Element-wise operations for this shape not yet implemented");
    }

    private Num elementWiseOp(double scalar, java.util.function.DoubleBinaryOperator op) {
        if (array instanceof double[]) {
            double[] a = (double[]) array;
            double[] result = new double[a.length];
            for (int i = 0; i < a.length; i++) {
                result[i] = op.applyAsDouble(a[i], scalar);
            }
            return new Num(result);
        } else if (array instanceof double[][]) {
            double[][] a = (double[][]) array;
            double[][] result = new double[a.length][];
            for (int i = 0; i < a.length; i++) {
                result[i] = new double[a[i].length];
                for (int j = 0; j < a[i].length; j++) {
                    result[i][j] = op.applyAsDouble(a[i][j], scalar);
                }
            }
            return new Num(result);
        }
        throw new UnsupportedOperationException("Element-wise scalar operations for this shape not yet implemented");
    }

    private double reduce(java.util.function.DoubleBinaryOperator op, double identity) {
        if (array instanceof double[]) {
            double[] a = (double[]) array;
            double result = identity;
            for (double v : a) {
                result = op.applyAsDouble(result, v);
            }
            return result;
        } else if (array instanceof double[][]) {
            double[][] a = (double[][]) array;
            double result = identity;
            for (double[] row : a) {
                for (double v : row) {
                    result = op.applyAsDouble(result, v);
                }
            }
            return result;
        }
        throw new UnsupportedOperationException("Reduction operations for this shape not yet implemented");
    }

    private Num apply(java.util.function.DoubleUnaryOperator op) {
        if (array instanceof double[]) {
            double[] a = (double[]) array;
            double[] result = new double[a.length];
            for (int i = 0; i < a.length; i++) {
                result[i] = op.applyAsDouble(a[i]);
            }
            return new Num(result);
        } else if (array instanceof double[][]) {
            double[][] a = (double[][]) array;
            double[][] result = new double[a.length][];
            for (int i = 0; i < a.length; i++) {
                result[i] = new double[a[i].length];
                for (int j = 0; j < a[i].length; j++) {
                    result[i][j] = op.applyAsDouble(a[i][j]);
                }
            }
            return new Num(result);
        }
        throw new UnsupportedOperationException("Apply operations for this shape not yet implemented");
    }
}
