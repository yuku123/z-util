package com.zifang.util.pandas.io;

import com.zifang.util.pandas.DataFrame;
import com.zifang.util.pandas.Series;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * CSVWriter 测试
 */
public class CSVWriterTest {

    @Test
    public void testWriteAndReadRoundtrip() throws IOException {
        DataFrame df = new DataFrame(Map.of(
                "name", new double[]{1.0, 2.0},
                "age", new double[]{25.0, 30.0}
        ));

        String csv = CSVWriter.toCSVString(df);
        assertNotNull(csv);
        assertTrue(csv.contains("name"));
        assertTrue(csv.contains("age"));
    }

    @Test
    public void testBuilderFluentInterface() {
        CSVWriter writer = CSVWriter.builder()
                .delimiter(';')
                .quoteChar('\'')
                .includeHeader(true)
                .includeIndex(false)
                .encoding("UTF-8")
                .lineEnding("\r\n")
                .quoteAll(false)
                .escapeSpecialChars(true);

        assertNotNull(writer);
    }

    @Test
    public void testToCSVString() throws IOException {
        DataFrame df = new DataFrame(Map.of(
                "A", new double[]{1.0, 2.0},
                "B", new double[]{3.0, 4.0}
        ));

        String csv = CSVWriter.toCSVString(df);
        assertNotNull(csv);
        String[] lines = csv.split("\n");
        assertTrue(lines.length >= 2);
    }

    @Test
    public void testWriteWithInfinity() throws IOException {
        DataFrame df = new DataFrame(Map.of("col", new double[]{1.0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}));
        String csv = CSVWriter.toCSVString(df);
        assertNotNull(csv);
    }

    @Test
    public void testWriteIntegerValues() throws IOException {
        DataFrame df = new DataFrame(Map.of(
                "int_col", new double[]{1.0, 2.0, 3.0},
                "float_col", new double[]{1.5, 2.5, 3.5}));
        String csv = CSVWriter.toCSVString(df);
        assertNotNull(csv);
    }

    @Test
    public void testWriteWithCustomDelimiter() throws IOException {
        DataFrame df = new DataFrame(Map.of(
                "A", new double[]{1.0},
                "B", new double[]{2.0}
        ));

        CSVWriter writer = CSVWriter.builder()
                .delimiter(';')
                .includeIndex(false);
        StringBuilder sb = new StringBuilder();
        writer.write(df, new java.io.OutputStream() {
            @Override
            public void write(int b) {
                sb.append((char) b);
            }
        });

        String csv = sb.toString();
        assertNotNull(csv);
    }
}