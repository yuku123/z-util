package com.zifang.util.cli.exception;

import java.util.List;

/**
 * Thrown when required options are missing from the command line.
 */
public class MissingOptionException extends ParseException {

    private static final long serialVersionUID = 1L;
    private final List missingOptions;

    public MissingOptionException(final String message) {
        super(message);
        this.missingOptions = null;
    }

    public MissingOptionException(final List missingOptions) {
        super(createMessage(missingOptions));
        this.missingOptions = missingOptions;
    }

    private static String createMessage(final List<?> missingOptions) {
        StringBuilder buf = new StringBuilder("Missing required option");
        buf.append(missingOptions.size() == 1 ? "" : "s").append(": ");
        String string = missingOptions.toString();
        return buf.append(string.substring(1, string.length() - 1)).toString();
    }

    public List getMissingOptions() {
        return missingOptions;
    }
}
