package com.zifang.util.parser.csv;

import java.io.*;
import java.util.List;

/**
 * CSV Writer for producing RFC 4180 compliant CSV output.
 * Features:
 * - Custom delimiter (default comma)
 * - Automatic quoting of fields containing delimiters or quotes
 * - Escaping of quotes by doubling
 * - Support for writing String[] rows
 */
public class CsvWriter implements Closeable, Flushable {

    private final Writer writer;
    private final char delimiter;
    private final boolean trimFields;

    /**
     * Create a CsvWriter to an OutputStream with default settings
     */
    public CsvWriter(OutputStream outputStream) {
        this(outputStream, ',', false);
    }

    /**
     * Create a CsvWriter to an OutputStream with custom delimiter
     */
    public CsvWriter(OutputStream outputStream, char delimiter) {
        this(outputStream, delimiter, false);
    }

    /**
     * Create a CsvWriter to an OutputStream with custom settings
     */
    public CsvWriter(OutputStream outputStream, char delimiter, boolean trimFields) {
        this.writer = new OutputStreamWriter(outputStream);
        this.delimiter = delimiter;
        this.trimFields = trimFields;
    }

    /**
     * Create a CsvWriter to a Writer with default settings
     */
    public CsvWriter(Writer writer) {
        this(writer, ',', false);
    }

    /**
     * Create a CsvWriter to a Writer with custom delimiter
     */
    public CsvWriter(Writer writer, char delimiter) {
        this(writer, delimiter, false);
    }

    /**
     * Create a CsvWriter to a Writer with custom settings
     */
    public CsvWriter(Writer writer, char delimiter, boolean trimFields) {
        this.writer = writer;
        this.delimiter = delimiter;
        this.trimFields = trimFields;
    }

    /**
     * Create a CsvWriter to a file
     */
    public CsvWriter(String filePath) throws FileNotFoundException {
        this(new FileOutputStream(filePath), ',', false);
    }

    /**
     * Create a CsvWriter to a file with custom delimiter
     */
    public CsvWriter(String filePath, char delimiter) throws FileNotFoundException {
        this(new FileOutputStream(filePath), delimiter, false);
    }

    /**
     * Write a single row
     */
    public void writeRow(String... fields) throws IOException {
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                writer.write(delimiter);
            }
            String field = trimFields ? (fields[i] != null ? fields[i].trim() : "") : (fields[i] != null ? fields[i] : "");
            writer.write(escapeField(field));
        }
        writer.write("\n");
    }

    /**
     * Write a single row from a list
     */
    public void writeRow(List<String> fields) throws IOException {
        for (int i = 0; i < fields.size(); i++) {
            if (i > 0) {
                writer.write(delimiter);
            }
            String field = trimFields ? (fields.get(i) != null ? fields.get(i).trim() : "") : (fields.get(i) != null ? fields.get(i) : "");
            writer.write(escapeField(field));
        }
        writer.write("\n");
    }

    /**
     * Write multiple rows
     */
    public void writeRows(List<String[]> rows) throws IOException {
        for (String[] row : rows) {
            writeRow(row);
        }
    }

    /**
     * Write multiple rows from varargs
     */
    public void writeRows(String[]... rows) throws IOException {
        for (String[] row : rows) {
            writeRow(row);
        }
    }

    /**
     * Escape a field according to RFC 4180:
     * - Fields containing quotes must be wrapped in quotes
     * - Quotes within fields must be doubled
     * - Fields containing delimiter, quotes, or newlines must be quoted
     */
    private String escapeField(String field) {
        if (field == null) {
            return "";
        }

        boolean needsQuoting = false;
        int quoteCount = 0;

        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if (c == '"') {
                quoteCount++;
                needsQuoting = true;
            } else if (c == delimiter || c == '\n' || c == '\r') {
                needsQuoting = true;
            }
        }

        if (!needsQuoting) {
            return field;
        }

        // Build escaped string with quotes and doubled quotes
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < field.length(); i++) {
            char c = field.charAt(i);
            if (c == '"') {
                sb.append('"'); // double the quote
            }
            sb.append(c);
        }
        sb.append('"');
        return sb.toString();
    }

    /**
     * Check if a field needs quoting (for number fields that should not be quoted)
     */
    private boolean isNumeric(String field) {
        if (field == null || field.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(field);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    // Builder pattern for configuration
    public static class Builder {
        private char delimiter = ',';
        private boolean trimFields = false;

        public Builder delimiter(char delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public Builder trimFields(boolean trimFields) {
            this.trimFields = trimFields;
            return this;
        }

        public CsvWriter build(OutputStream outputStream) {
            return new CsvWriter(outputStream, delimiter, trimFields);
        }

        public CsvWriter build(Writer writer) {
            return new CsvWriter(writer, delimiter, trimFields);
        }

        public CsvWriter build(String filePath) throws FileNotFoundException {
            return new CsvWriter(new FileOutputStream(filePath), delimiter, trimFields);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
