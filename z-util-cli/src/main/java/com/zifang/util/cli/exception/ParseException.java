package com.zifang.util.cli.exception;

/**
 * Base exception for command-line parsing errors.
 */
public class ParseException extends Exception {

    private static final long serialVersionUID = 1L;

    public ParseException(final String message) {
        super(message);
    }

    public ParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
