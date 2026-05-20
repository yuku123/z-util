package com.zifang.util.cli.exception;

import com.zifang.util.cli.model.Option;
import com.zifang.util.cli.model.OptionGroup;

/**
 * Thrown when more than one option in an option group has been provided.
 */
public class AlreadySelectedException extends ParseException {

    private static final long serialVersionUID = 1L;
    private final OptionGroup optionGroup;
    private final Option option;

    public AlreadySelectedException(final OptionGroup optionGroup, final Option option) {
        this(String.format("The option '%s' was specified but an option from this group has already been selected: '%s'",
                option.getKey(), optionGroup.getSelected()), optionGroup, option);
    }

    private AlreadySelectedException(final String message, final OptionGroup optionGroup, final Option option) {
        super(message);
        this.optionGroup = optionGroup;
        this.option = option;
    }

    public Option getOption() {
        return option;
    }

    public OptionGroup getOptionGroup() {
        return optionGroup;
    }
}
