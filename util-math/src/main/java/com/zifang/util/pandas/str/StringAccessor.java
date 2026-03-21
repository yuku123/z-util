package com.zifang.util.pandas.str;

import com.zifang.util.pandas.Series;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringAccessor 类 - Series 的字符串操作方法
 * 对标 pandas.Series.str 访问器
 * 提供字符串处理、正则表达式匹配、文本提取等功能
 */
public class StringAccessor {

    private final Series series;

    public StringAccessor(Series series) {
        this.series = series;
    }

    /**
     * 获取字符串长度
     */
    public Series length() {
        double[] result = new double[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            result[i] = val.length();
        }
        return new Series(result, series.index(), "length", null);
    }

    /**
     * 转换为小写
     */
    public Series lower() {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            result[i] = String.valueOf(series.toArray()[i]).toLowerCase();
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 转换为大写
     */
    public Series upper() {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            result[i] = String.valueOf(series.toArray()[i]).toUpperCase();
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 去除首尾空白字符
     */
    public Series strip() {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            result[i] = String.valueOf(series.toArray()[i]).trim();
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 去除左侧空白字符
     */
    public Series lstrip() {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            result[i] = val.replaceAll("^\\s+", "");
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 去除右侧空白字符
     */
    public Series rstrip() {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            result[i] = val.replaceAll("\\s+$", "");
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 字符串替换
     */
    public Series replace(String oldStr, String newStr) {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            result[i] = val.replace(oldStr, newStr);
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 正则表达式替换
     */
    public Series replaceRegex(String pattern, String replacement) {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            result[i] = val.replaceAll(pattern, replacement);
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 检查是否包含子字符串
     */
    public Series contains(String subStr) {
        double[] result = new double[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            result[i] = val.contains(subStr) ? 1.0 : 0.0;
        }
        return new Series(result, series.index(), "contains", null);
    }

    /**
     * 正则表达式匹配
     */
    public Series match(String pattern) {
        double[] result = new double[series.length()];
        Pattern p = Pattern.compile(pattern);
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            Matcher m = p.matcher(val);
            result[i] = m.matches() ? 1.0 : 0.0;
        }
        return new Series(result, series.index(), "match", null);
    }

    /**
     * 提取匹配正则表达式的第一个分组
     */
    public Series extract(String pattern, int group) {
        String[] result = new String[series.length()];
        Pattern p = Pattern.compile(pattern);
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            Matcher m = p.matcher(val);
            if (m.find() && group <= m.groupCount()) {
                result[i] = m.group(group);
            } else {
                result[i] = "";
            }
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 分割字符串
     */
    public Series split(String delimiter, int index) {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            String[] parts = val.split(delimiter);
            if (index < parts.length) {
                result[i] = parts[index];
            } else {
                result[i] = "";
            }
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 切片字符串
     */
    public Series slice(int start, int end) {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            int s = start >= 0 ? start : Math.max(0, val.length() + start);
            int e = end >= 0 ? Math.min(val.length(), end) : val.length() + end;
            if (s >= e || s >= val.length()) {
                result[i] = "";
            } else {
                result[i] = val.substring(s, e);
            }
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 重复字符串
     */
    public Series repeat(int n) {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val = String.valueOf(series.toArray()[i]);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(val);
            }
            result[i] = sb.toString();
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    /**
     * 连接字符串（当前 Series 与另一个 Series 或常量）
     */
    public Series cat(String other) {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            result[i] = String.valueOf(series.toArray()[i]) + other;
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    public Series cat(Series other) {
        String[] result = new String[series.length()];
        for (int i = 0; i < series.length(); i++) {
            String val1 = String.valueOf(series.toArray()[i]);
            String val2 = i < other.length() ? String.valueOf(other.toArray()[i]) : "";
            result[i] = val1 + val2;
        }
        return Series.fromMap(createIndexValueMap(result));
    }

    // ==================== 辅助方法 ====================

    private Map<String, Double> createIndexValueMap(String[] values) {
        Map<String, Double> map = new LinkedHashMap<>();
        String[] index = series.index().toArray();
        for (int i = 0; i < values.length && i < index.length; i++) {
            try {
                map.put(index[i], Double.parseDouble(values[i]));
            } catch (NumberFormatException e) {
                map.put(index[i], Double.NaN);
            }
        }
        return map;
    }
}
