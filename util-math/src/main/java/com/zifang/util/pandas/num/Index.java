package com.zifang.util.pandas.num;

import java.util.Arrays;

/**
 * Index 类 - 对标 pandas Index，用于标签索引
 */
public class Index {
    private final String[] labels;
    private final boolean isRangeIndex;
    private final int start;
    private final int step;

    /**
     * 创建默认的 RangeIndex
     */
    public static Index range(int size) {
        return new Index(size);
    }

    public static Index range(int start, int stop) {
        return new Index(start, stop, 1);
    }

    public static Index range(int start, int stop, int step) {
        return new Index(start, stop, step);
    }

    /**
     * 从字符串数组创建 Index
     */
    public static Index of(String... labels) {
        return new Index(labels);
    }

    // 私有构造函数
    private Index(int size) {
        this.isRangeIndex = true;
        this.start = 0;
        this.step = 1;
        this.labels = new String[size];
        for (int i = 0; i < size; i++) {
            labels[i] = String.valueOf(i);
        }
    }

    private Index(int start, int stop, int step) {
        this.isRangeIndex = true;
        this.start = start;
        this.step = step;
        int size = (stop - start + step - 1) / step;
        this.labels = new String[size];
        for (int i = 0; i < size; i++) {
            labels[i] = String.valueOf(start + i * step);
        }
    }

    private Index(String[] labels) {
        this.isRangeIndex = false;
        this.start = 0;
        this.step = 1;
        this.labels = labels.clone();
    }

    // 公共方法
    public int size() {
        return labels.length;
    }

    public String get(int i) {
        return labels[i];
    }

    public String[] toArray() {
        return labels.clone();
    }

    public int getLocation(String label) {
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].equals(label)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isRangeIndex() {
        return isRangeIndex;
    }

    public Index slice(int start, int stop) {
        String[] sliced = Arrays.copyOfRange(labels, start, stop);
        return new Index(sliced);
    }

    @Override
    public String toString() {
        if (isRangeIndex) {
            return "RangeIndex(start=" + start + ", stop=" + (start + labels.length * step) + ", step=" + step + ")";
        }
        return "Index(" + Arrays.toString(labels) + ")";
    }
}
