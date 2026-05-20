package com.zifang.util.cli.exception;

import com.zifang.util.cli.model.Option;

/**
 * Thrown when an option requiring an argument is not provided with one.
 */
public class MissingArgumentException extends ParseException {

    private static final long serialVersionUID = 1L;
    private final Option option;

    public MissingArgumentException(final Option option) {
        super("Missing argument for option: " + (option != null ? option.getKey() : "null"));
        this.option = option;
    }

    public MissingArgumentException(final String message) {
        super(message);
        this.option = null;
    }

    public Option getOption() {
        return option;
    }
}
