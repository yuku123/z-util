package com.zifang.util.cli.exception;

/**
 * Thrown when an unrecognized option is encountered during parsing.
 */
public class UnrecognizedOptionException extends ParseException {

    private static final long serialVersionUID = 1L;
    private final String option;

    public UnrecognizedOptionException(final String message) {
        this(message, null);
    }

    public UnrecognizedOptionException(final String message, final String option) {
        super(message);
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
