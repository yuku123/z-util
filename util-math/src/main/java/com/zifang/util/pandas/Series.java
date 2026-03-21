package com.zifang.util.pandas;

import com.zifang.util.pandas.num.DType;
import com.zifang.util.pandas.num.Index;
import com.zifang.util.pandas.num.Num;
import com.zifang.util.pandas.num.Nums;

import java.util.*;
import java.util.function.Function;

/**
 * Series 类 - 对标 pandas Series
 * 带标签的一维数组
 */
public class Series {

    private Num data;
    private Index index;
    private String name;
    private DType dtype;

    // ==================== 构造函数 ====================

    public Series(Object data) {
        this(data, (Index) null, null, null);
    }

    public Series(Object data, String[] index) {
        this(data, Index.of(index), null, null);
    }

    public Series(Object data, Index index) {
        this(data, index, null, null);
    }

    public Series(Object data, String name) {
        this(data, (Index) null, name, null);
    }

    public Series(Object data, Index index, String name, DType dtype) {
        if (data instanceof Num) {
            this.data = (Num) data;
        } else if (data instanceof double[]) {
            this.data = new Num(data);
        } else if (data instanceof int[]) {
            this.data = Nums.array(data);
        } else if (data instanceof List) {
            this.data = Nums.array((List<?>) data);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + data.getClass());
        }

        if (index == null) {
            this.index = Index.range(this.data.size());
        } else {
            if (index.size() != this.data.size()) {
                throw new IllegalArgumentException("Index length " + index.size() +
                        " does not match data length " + this.data.size());
            }
            this.index = index;
        }

        this.name = name;
        this.dtype = dtype != null ? dtype : this.data.dtype();
    }

    // ==================== 静态工厂方法 ====================

    public static Series fromMap(Map<String, ?> map) {
        String[] keys = map.keySet().toArray(new String[0]);
        double[] values = new double[map.size()];
        int i = 0;
        for (Object value : map.values()) {
            if (value instanceof Number) {
                values[i++] = ((Number) value).doubleValue();
            } else {
                throw new IllegalArgumentException("Map values must be numeric");
            }
        }
        return new Series(values, keys);
    }

    // ==================== 数据访问 ====================

    public double get(int i) {
        return getDataAsDoubleArray()[i];
    }

    public double get(String label) {
        int i = index.getLocation(label);
        if (i < 0) {
            throw new NoSuchElementException("Label '" + label + "' not found in index");
        }
        return get(i);
    }

    public Series get(String[] labels) {
        double[] values = new double[labels.length];
        for (int i = 0; i < labels.length; i++) {
            values[i] = get(labels[i]);
        }
        return new Series(values, labels, name, dtype);
    }

    public Series get(int[] indices) {
        double[] data = getDataAsDoubleArray();
        double[] values = new double[indices.length];
        String[] newIndex = new String[indices.length];
        for (int i = 0; i < indices.length; i++) {
            values[i] = data[indices[i]];
            newIndex[i] = index.get(indices[i]);
        }
        return new Series(values, newIndex, name, dtype);
    }

    public Series slice(int start, int stop) {
        return get(Arrays.copyOfRange(getIndexAsIntArray(), start, stop));
    }

    public Series loc(String start, String end) {
        int startIdx = index.getLocation(start);
        int endIdx = index.getLocation(end);
        if (startIdx < 0 || endIdx < 0) {
            throw new NoSuchElementException("Start or end label not found");
        }
        return slice(Math.min(startIdx, endIdx), Math.max(startIdx, endIdx) + 1);
    }

    public Series iloc(int start, int end) {
        return slice(start, end);
    }

    // ==================== 布尔索引和过滤 ====================

    public Series filter(Function<Double, Boolean> condition) {
        double[] data = getDataAsDoubleArray();
        List<Double> values = new ArrayList<>();
        List<String> newIndex = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if (condition.apply(data[i])) {
                values.add(data[i]);
                newIndex.add(index.get(i));
            }
        }
        return new Series(values.stream().mapToDouble(Double::doubleValue).toArray(),
                         newIndex.toArray(new String[0]), name, dtype);
    }

    public Series where(Function<Double, Boolean> condition, double thenValue, double elseValue) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = condition.apply(data[i]) ? thenValue : elseValue;
        }
        return new Series(result, index, name, dtype);
    }

    // ==================== 算术运算 ====================

    public Series add(Series other) {
        checkAlignment(other);
        return new Series(data.add(other.data), index, name, dtype);
    }

    public Series add(double scalar) {
        return new Series(data.add(scalar), index, name, dtype);
    }

    public Series subtract(Series other) {
        checkAlignment(other);
        return new Series(data.subtract(other.data), index, name, dtype);
    }

    public Series subtract(double scalar) {
        return new Series(data.subtract(scalar), index, name, dtype);
    }

    public Series multiply(Series other) {
        checkAlignment(other);
        return new Series(data.multiply(other.data), index, name, dtype);
    }

    public Series multiply(double scalar) {
        return new Series(data.multiply(scalar), index, name, dtype);
    }

    public Series divide(Series other) {
        checkAlignment(other);
        return new Series(data.divide(other.data), index, name, dtype);
    }

    public Series divide(double scalar) {
        return new Series(data.divide(scalar), index, name, dtype);
    }

    public Series pow(double exponent) {
        return new Series(data.pow(exponent), index, name, dtype);
    }

    public Series abs() {
        return new Series(data.abs(), index, name, dtype);
    }

    public Series sqrt() {
        return new Series(data.sqrt(), index, name, dtype);
    }

    public Series log() {
        return new Series(data.log(), index, name, dtype);
    }

    public Series exp() {
        return new Series(data.exp(), index, name, dtype);
    }

    // ==================== 统计方法 ====================

    public double sum() {
        return data.sum();
    }

    public double mean() {
        return data.mean();
    }

    public double max() {
        return data.max();
    }

    public double min() {
        return data.min();
    }

    public double std() {
        return data.std();
    }

    public double var() {
        return data.var();
    }

    public double median() {
        double[] sorted = getDataAsDoubleArray().clone();
        java.util.Arrays.sort(sorted);
        int n = sorted.length;
        if (n % 2 == 0) {
            return (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
        } else {
            return sorted[n / 2];
        }
    }

    public double percentile(double q) {
        if (q < 0 || q > 100) {
            throw new IllegalArgumentException("Percentile must be between 0 and 100");
        }
        double[] sorted = getDataAsDoubleArray().clone();
        java.util.Arrays.sort(sorted);
        double index = q / 100.0 * (sorted.length - 1);
        int lower = (int) Math.floor(index);
        int upper = (int) Math.ceil(index);
        double weight = index - lower;
        return sorted[lower] * (1 - weight) + sorted[upper] * weight;
    }

    public long count() {
        return getDataAsDoubleArray().length;
    }

    public double sem() {
        return std() / Math.sqrt(count());
    }

    public Series cumsum() {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        double sum = 0;
        for (int i = 0; i < data.length; i++) {
            sum += data[i];
            result[i] = sum;
        }
        return new Series(result, index, name, dtype);
    }

    public Series cumprod() {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        double prod = 1;
        for (int i = 0; i < data.length; i++) {
            prod *= data[i];
            result[i] = prod;
        }
        return new Series(result, index, name, dtype);
    }

    public Series diff(int periods) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            if (i < periods) {
                result[i] = Double.NaN;
            } else {
                result[i] = data[i] - data[i - periods];
            }
        }
        return new Series(result, index, name, dtype);
    }

    public Series diff() {
        return diff(1);
    }

    public Series pct_change(int periods) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            if (i < periods || data[i - periods] == 0) {
                result[i] = Double.NaN;
            } else {
                result[i] = (data[i] - data[i - periods]) / data[i - periods];
            }
        }
        return new Series(result, index, name, dtype);
    }

    public Series pct_change() {
        return pct_change(1);
    }

    // ==================== 比较运算 ====================

    public Series eq(double value) {
        return compare(v -> v == value);
    }

    public Series eq(Series other) {
        checkAlignment(other);
        double[] a = getDataAsDoubleArray();
        double[] b = other.getDataAsDoubleArray();
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] == b[i] ? 1.0 : 0.0;
        }
        return new Series(result, index, name, dtype);
    }

    public Series ne(double value) {
        return compare(v -> v != value);
    }

    public Series gt(double value) {
        return compare(v -> v > value);
    }

    public Series ge(double value) {
        return compare(v -> v >= value);
    }

    public Series lt(double value) {
        return compare(v -> v < value);
    }

    public Series le(double value) {
        return compare(v -> v <= value);
    }

    private Series compare(java.util.function.DoublePredicate condition) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = condition.test(data[i]) ? 1.0 : 0.0;
        }
        return new Series(result, index, name, dtype);
    }

    public boolean all() {
        double[] data = getDataAsDoubleArray();
        for (double v : data) {
            if (v == 0.0 || Double.isNaN(v)) {
                return false;
            }
        }
        return true;
    }

    public boolean any() {
        double[] data = getDataAsDoubleArray();
        for (double v : data) {
            if (v != 0.0 && !Double.isNaN(v)) {
                return true;
            }
        }
        return false;
    }

    // ==================== 缺失值处理 ====================

    public Series isna() {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = Double.isNaN(data[i]) ? 1.0 : 0.0;
        }
        return new Series(result, index, name, dtype);
    }

    public Series notna() {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = !Double.isNaN(data[i]) ? 1.0 : 0.0;
        }
        return new Series(result, index, name, dtype);
    }

    public Series fillna(double value) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = Double.isNaN(data[i]) ? value : data[i];
        }
        return new Series(result, index, name, dtype);
    }

    public Series dropna() {
        double[] data = getDataAsDoubleArray();
        List<Double> values = new ArrayList<>();
        List<String> newIndex = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            if (!Double.isNaN(data[i])) {
                values.add(data[i]);
                newIndex.add(index.get(i));
            }
        }
        double[] result = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return new Series(result, Index.of(newIndex.toArray(new String[0])), name, dtype);
    }

    // ==================== 其他方法 ====================

    public Series copy() {
        return new Series(data, index, name, dtype);
    }

    public Series head(int n) {
        return slice(0, Math.min(n, (int)count()));
    }

    public Series head() {
        return head(5);
    }

    public Series tail(int n) {
        int size = (int) count();
        return slice(Math.max(0, size - n), size);
    }

    public Series tail() {
        return tail(5);
    }

    public Series sort_values() {
        return sort_values(true);
    }

    public Series sort_values(boolean ascending) {
        double[] data = getDataAsDoubleArray();
        Integer[] indices = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, (i1, i2) -> {
            int cmp = Double.compare(data[i1], data[i2]);
            return ascending ? cmp : -cmp;
        });
        double[] sortedData = new double[data.length];
        String[] sortedIndex = new String[data.length];
        for (int i = 0; i < indices.length; i++) {
            sortedData[i] = data[indices[i]];
            sortedIndex[i] = index.get(indices[i]);
        }
        return new Series(sortedData, Index.of(sortedIndex), name, dtype);
    }

    public Series sort_index() {
        return sort_index(true);
    }

    public Series sort_index(boolean ascending) {
        String[] labels = index.toArray();
        Integer[] indices = new Integer[labels.length];
        for (int i = 0; i < labels.length; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, (i1, i2) -> {
            int cmp = labels[i1].compareTo(labels[i2]);
            return ascending ? cmp : -cmp;
        });
        double[] data = getDataAsDoubleArray();
        double[] sortedData = new double[data.length];
        String[] sortedIndex = new String[data.length];
        for (int i = 0; i < indices.length; i++) {
            sortedData[i] = data[indices[i]];
            sortedIndex[i] = labels[indices[i]];
        }
        return new Series(sortedData, Index.of(sortedIndex), name, dtype);
    }

    public Series round(int decimals) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        double factor = Math.pow(10, decimals);
        for (int i = 0; i < data.length; i++) {
            result[i] = Math.round(data[i] * factor) / factor;
        }
        return new Series(result, index, name, dtype);
    }

    public Series round() {
        return round(0);
    }

    public Series clip(double lower, double upper) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = Math.max(lower, Math.min(data[i], upper));
        }
        return new Series(result, index, name, dtype);
    }

    public Series between(double left, double right) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (data[i] >= left && data[i] <= right) ? 1.0 : 0.0;
        }
        return new Series(result, index, name, dtype);
    }

    public Series unique() {
        double[] data = getDataAsDoubleArray();
        Set<Double> seen = new LinkedHashSet<>();
        for (double v : data) {
            seen.add(v);
        }
        double[] result = new double[seen.size()];
        int i = 0;
        for (Double v : seen) {
            result[i++] = v;
        }
        return new Series(result, name);
    }

    public int nunique() {
        return (int) unique().count();
    }

    public Series value_counts() {
        double[] data = getDataAsDoubleArray();
        Map<Double, Integer> counts = new HashMap<>();
        for (double v : data) {
            counts.put(v, counts.getOrDefault(v, 0) + 1);
        }
        double[] values = new double[counts.size()];
        String[] index = new String[counts.size()];
        int i = 0;
        for (Map.Entry<Double, Integer> entry : counts.entrySet()) {
            values[i] = entry.getValue();
            index[i] = String.valueOf(entry.getKey());
            i++;
        }
        return new Series(values, Index.of(index), "count", DType.INT64);
    }

    public boolean is_unique() {
        return nunique() == count();
    }

    public boolean is_monotonic_increasing() {
        double[] data = getDataAsDoubleArray();
        for (int i = 1; i < data.length; i++) {
            if (data[i] < data[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public boolean is_monotonic_decreasing() {
        double[] data = getDataAsDoubleArray();
        for (int i = 1; i < data.length; i++) {
            if (data[i] > data[i - 1]) {
                return false;
            }
        }
        return true;
    }

    // ==================== apply 和 map ====================

    public Series apply(Function<Double, Double> func) {
        double[] data = getDataAsDoubleArray();
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = func.apply(data[i]);
        }
        return new Series(result, index, name, dtype);
    }

    public Series map(Map<Double, Double> mapping) {
        return apply(v -> mapping.getOrDefault(v, v));
    }

    // ==================== 重命名和元数据 ====================

    public Series rename(String newName) {
        return new Series(data, index, newName, dtype);
    }

    public String name() {
        return name;
    }

    public Series setName(String name) {
        this.name = name;
        return this;
    }

    public Index index() {
        return index;
    }

    public Series setIndex(Index newIndex) {
        if (newIndex.size() != this.index.size()) {
            throw new IllegalArgumentException("New index must have same size as old index");
        }
        return new Series(data, newIndex, name, dtype);
    }

    public Series setIndex(String[] newIndex) {
        return setIndex(Index.of(newIndex));
    }

    public DType dtype() {
        return dtype;
    }

    // ==================== 数据导出 ====================

    public double[] toArray() {
        return getDataAsDoubleArray().clone();
    }

    public List<Double> toList() {
        double[] arr = getDataAsDoubleArray();
        List<Double> list = new ArrayList<>(arr.length);
        for (double v : arr) {
            list.add(v);
        }
        return list;
    }

    public Map<String, Double> toMap() {
        Map<String, Double> map = new LinkedHashMap<>();
        double[] data = getDataAsDoubleArray();
        for (int i = 0; i < data.length; i++) {
            map.put(index.get(i), data[i]);
        }
        return map;
    }

    public Num toNum() {
        return data;
    }

    // ==================== 描述和打印 ====================

    public String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("count    ").append(String.format("%.0f", (double) count())).append("\n");
        sb.append("mean     ").append(String.format("%.6f", mean())).append("\n");
        sb.append("std      ").append(String.format("%.6f", std())).append("\n");
        sb.append("min      ").append(String.format("%.6f", min())).append("\n");
        sb.append("25%      ").append(String.format("%.6f", percentile(25))).append("\n");
        sb.append("50%      ").append(String.format("%.6f", median())).append("\n");
        sb.append("75%      ").append(String.format("%.6f", percentile(75))).append("\n");
        sb.append("max      ").append(String.format("%.6f", max())).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name != null && !name.isEmpty()) {
            sb.append(name).append("\n");
        }
        double[] data = getDataAsDoubleArray();
        int displayCount = Math.min(data.length, 10);
        for (int i = 0; i < displayCount; i++) {
            sb.append(index.get(i)).append("    ").append(data[i]).append("\n");
        }
        if (data.length > 10) {
            sb.append("...\n");
            sb.append("Length: ").append(data.length).append(", dtype: ").append(dtype);
        }
        return sb.toString();
    }

    // ==================== 私有辅助方法 ====================

    private double[] getDataAsDoubleArray() {
        if (data.data() instanceof double[]) {
            return (double[]) data.data();
        }
        throw new UnsupportedOperationException("Cannot convert data to double array");
    }

    private int[] getIndexAsIntArray() {
        int[] result = new int[index.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    private void checkAlignment(Series other) {
        if (this.index.size() != other.index.size()) {
            throw new IllegalArgumentException("Series must have the same length for this operation");
        }
    }
}
