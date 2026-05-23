package com.zifang.util.parser.csv;

import java.io.*;
import java.util.*;

/**
 * CSV Reader that implements Iterator for streaming large files.
 * Supports RFC 4180 format with custom delimiter, quoting, and escaping.
 */
public class CsvReader implements Iterator<String[]> {

    private final BufferedReader reader;
    private final char delimiter;
    private final boolean skipEmptyLines;
    private final boolean trimFields;

    private String[] nextLine;
    private boolean hasNext;
    private boolean closed;

    /**
     * Create a CsvReader from an InputStream with default settings
     */
    public CsvReader(InputStream inputStream) {
        this(inputStream, ',', true, false);
    }

    /**
     * Create a CsvReader from an InputStream with custom settings
     */
    public CsvReader(InputStream inputStream, char delimiter, boolean skipEmptyLines, boolean trimFields) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.delimiter = delimiter;
        this.skipEmptyLines = skipEmptyLines;
        this.trimFields = trimFields;
        this.closed = false;
        advance();
    }

    /**
     * Create a CsvReader from a Reader with default settings
     */
    public CsvReader(Reader reader) {
        this(reader, ',', true, false);
    }

    /**
     * Create a CsvReader from a Reader with custom settings
     */
    public CsvReader(Reader reader, char delimiter, boolean skipEmptyLines, boolean trimFields) {
        this.reader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        this.delimiter = delimiter;
        this.skipEmptyLines = skipEmptyLines;
        this.trimFields = trimFields;
        this.closed = false;
        advance();
    }

    /**
     * Create a CsvReader from a file path
     */
    public CsvReader(String filePath) throws FileNotFoundException {
        this(new FileInputStream(filePath), ',', true, false);
    }

    /**
     * Create a CsvReader from a File
     */
    public CsvReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file), ',', true, false);
    }

    private void advance() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (skipEmptyLines && line.trim().isEmpty()) {
                    continue;
                }
                nextLine = parseLine(line);
                hasNext = true;
                return;
            }
            nextLine = null;
            hasNext = false;
        } catch (IOException e) {
            throw new CsvException("Error reading CSV", e);
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext && !closed;
    }

    @Override
    public String[] next() {
        if (!hasNext) {
            throw new NoSuchElementException("No more lines in CSV");
        }
        String[] result = nextLine;
        advance();
        return result;
    }

    /**
     * Read all remaining lines
     */
    public List<String[]> readAll() {
        List<String[]> lines = new ArrayList<>();
        while (hasNext()) {
            lines.add(next());
        }
        return lines;
    }

    /**
     * Close the reader
     */
    public void close() {
        if (!closed) {
            try {
                reader.close();
            } catch (IOException e) {
                throw new CsvException("Error closing CSV reader", e);
            } finally {
                closed = true;
            }
        }
    }

    /**
     * Parse a single CSV line using state machine
     */
    private String[] parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        int len = line.length();

        for (int i = 0; i < len; i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < len && line.charAt(i + 1) == '"') {
                        // Escaped quote ""
                        field.append('"');
                        i++;
                    } else {
                        // End of quoted field
                        inQuotes = false;
                    }
                } else {
                    field.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == delimiter) {
                    // End of field
                    fields.add(trimFields ? field.toString().trim() : field.toString());
                    field = new StringBuilder();
                } else {
                    field.append(c);
                }
            }
        }

        // Add last field
        fields.add(trimFields ? field.toString().trim() : field.toString());

        return fields.toArray(new String[0]);
    }

    // Builder pattern for configuration
    public static class Builder {
        private char delimiter = ',';
        private boolean skipEmptyLines = true;
        private boolean trimFields = false;

        public Builder delimiter(char delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public Builder skipEmptyLines(boolean skipEmptyLines) {
            this.skipEmptyLines = skipEmptyLines;
            return this;
        }

        public Builder trimFields(boolean trimFields) {
            this.trimFields = trimFields;
            return this;
        }

        public CsvReader build(InputStream inputStream) {
            return new CsvReader(inputStream, delimiter, skipEmptyLines, trimFields);
        }

        public CsvReader build(Reader reader) {
            return new CsvReader(reader, delimiter, skipEmptyLines, trimFields);
        }

        public CsvReader build(String filePath) throws FileNotFoundException {
            return new CsvReader(new FileInputStream(filePath), delimiter, skipEmptyLines, trimFields);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
