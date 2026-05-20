package com.zifang.util.cli.parser;

import com.zifang.util.cli.model.Options;

/**
 * Very simple parser that passes arguments through unchanged.
 *
 * @deprecated since 1.3, use {@link DefaultParser} instead.
 */
@Deprecated
public class BasicParser extends Parser {

    public BasicParser() {}

    @Override
    protected String[] flatten(final Options options, final String[] arguments, final boolean stopAtNonOption) {
        return arguments;
    }
}
