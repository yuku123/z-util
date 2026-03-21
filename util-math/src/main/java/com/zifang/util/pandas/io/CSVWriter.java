package com.zifang.util.pandas.io;

import com.zifang.util.pandas.DataFrame;
import com.zifang.util.pandas.Series;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * CSV 写入器 - 支持将数据写入 CSV 文件
 */
public class CSVWriter {

    private char delimiter = ',';
    private char quoteChar = '"';
    private boolean includeHeader = true;
    private boolean includeIndex = true;
    private String encoding = "UTF-8";
    private String lineEnding = "\n";
    private boolean quoteAll = false;
    private boolean escapeSpecialChars = true;

    public CSVWriter() {}

    public static CSVWriter builder() {
        return new CSVWriter();
    }

    public CSVWriter delimiter(char delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public CSVWriter quoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
        return this;
    }

    public CSVWriter includeHeader(boolean includeHeader) {
        this.includeHeader = includeHeader;
        return this;
    }

    public CSVWriter includeIndex(boolean includeIndex) {
        this.includeIndex = includeIndex;
        return this;
    }

    public CSVWriter encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public CSVWriter lineEnding(String lineEnding) {
        this.lineEnding = lineEnding;
        return this;
    }

    public CSVWriter quoteAll(boolean quoteAll) {
        this.quoteAll = quoteAll;
        return this;
    }

    public CSVWriter escapeSpecialChars(boolean escapeSpecialChars) {
        this.escapeSpecialChars = escapeSpecialChars;
        return this;
    }

    /**
     * 写入文件路径
     */
    public void write(DataFrame df, String filePath) throws IOException {
        write(df, new File(filePath));
    }

    /**
     * 写入 File 对象
     */
    public void write(DataFrame df, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), encoding))) {
            write(df, writer);
        }
    }

    /**
     * 写入 OutputStream
     */
    public void write(DataFrame df, OutputStream outputStream) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(outputStream, encoding))) {
            write(df, writer);
        }
    }

    /**
     * 核心写入方法
     */
    private void write(DataFrame df, BufferedWriter writer) throws IOException {
        List<String> columns = df.columns();
        int nRows = df.nRows();

        // 写入表头
        if (includeHeader) {
            StringBuilder header = new StringBuilder();

            if (includeIndex) {
                header.append(quoteIfNeeded("index"));
                header.append(delimiter);
            }

            for (int i = 0; i < columns.size(); i++) {
                if (i > 0) {
                    header.append(delimiter);
                }
                header.append(quoteIfNeeded(columns.get(i)));
            }

            writer.write(header.toString());
            writer.write(lineEnding);
        }

        // 写入数据行
        for (int row = 0; row < nRows; row++) {
            StringBuilder line = new StringBuilder();

            if (includeIndex) {
                line.append(quoteIfNeeded(df.index().get(row)));
                line.append(delimiter);
            }

            for (int col = 0; col < columns.size(); col++) {
                if (col > 0) {
                    line.append(delimiter);
                }

                String colName = columns.get(col);
                Series series = df.get(colName);
                double value = series.toArray()[row];

                String valueStr;
                if (Double.isNaN(value)) {
                    valueStr = "";
                } else if (Double.isInfinite(value)) {
                    valueStr = value > 0 ? "Infinity" : "-Infinity";
                } else {
                    // 格式化数字，避免科学计数法
                    if (value == Math.floor(value)) {
                        valueStr = String.valueOf((long) value);
                    } else {
                        valueStr = String.valueOf(value);
                    }
                }

                line.append(quoteIfNeeded(valueStr));
            }

            writer.write(line.toString());
            writer.write(lineEnding);
        }

        writer.flush();
    }

    /**
     * 根据需要对字段进行引号包裹
     */
    private String quoteIfNeeded(String field) {
        if (field == null) {
            return "";
        }

        boolean needsQuoting = quoteAll;

        if (!needsQuoting) {
            // 检查是否包含特殊字符
            if (field.indexOf(delimiter) >= 0 ||
                    field.indexOf(quoteChar) >= 0 ||
                    field.indexOf('\n') >= 0 ||
                    field.indexOf('\r') >= 0) {
                needsQuoting = true;
            }
        }

        if (!needsQuoting) {
            // 检查是否以空白字符开头或结尾
            if (field.length() > 0) {
                char first = field.charAt(0);
                char last = field.charAt(field.length() - 1);
                if (first == ' ' || first == '\t' || last == ' ' || last == '\t') {
                    needsQuoting = true;
                }
            }
        }

        if (needsQuoting) {
            StringBuilder quoted = new StringBuilder();
            quoted.append(quoteChar);

            for (int i = 0; i < field.length(); i++) {
                char c = field.charAt(i);
                if (c == quoteChar) {
                    // 转义引号
                    quoted.append(quoteChar).append(quoteChar);
                } else if (escapeSpecialChars && c == '\\') {
                    quoted.append('\\').append(c);
                } else {
                    quoted.append(c);
                }
            }

            quoted.append(quoteChar);
            return quoted.toString();
        }

        return field;
    }

    /**
     * 将 DataFrame 写入 CSV 字符串
     */
    public static String toCSVString(DataFrame df) {
        StringBuilder sb = new StringBuilder();
        try {
            CSVWriter writer = new CSVWriter();
            writer.write(df, new OutputStream() {
                @Override
                public void write(int b) {
                    sb.append((char) b);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error converting DataFrame to CSV", e);
        }
        return sb.toString();
    }
}
