package com.zifang.util.parser.csv;

/**
 * Exception thrown when CSV parsing errors occur.
 */
public class CsvException extends RuntimeException {

    public CsvException(String message) {
        super(message);
    }

    public CsvException(String message, Throwable cause) {
        super(message, cause);
    }
}
