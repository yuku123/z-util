/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zifang.util.cli.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a parsed command line. Contains all options and their values
 * found during parsing.
 */
public class CommandLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Option> options = new HashMap<>();
    private final List<String> argList = new ArrayList<>();

    protected CommandLine() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final CommandLine cmd = new CommandLine();

        public CommandLine get() {
            return cmd;
        }
    }

    // package-private for parser access
    public void addOption(final Option option) {
        options.put(option.getKey(), option);
    }

    // package-private for parser access
    public void addArg(final String arg) {
        argList.add(arg);
    }

    public boolean hasOption(final String opt) {
        Option option = getOptionObject(opt);
        return option != null && option.hasValue();
    }

    public boolean hasOption(final Option opt) {
        return options.containsKey(opt.getKey());
    }

    public Option getOptionObject(final String opt) {
        return options.get(opt);
    }

    public String getOptionValue(final String opt) {
        Option option = getOptionObject(opt);
        return option != null ? option.getValue() : null;
    }

    public String getOptionValue(final String opt, final String defaultValue) {
        String value = getOptionValue(opt);
        return value != null ? value : defaultValue;
    }

    public String getOptionValue(final Option option) {
        return hasOption(option) ? option.getValue() : null;
    }

    public String getOptionValue(final Option option, final String defaultValue) {
        String value = getOptionValue(option);
        return value != null ? value : defaultValue;
    }

    public String[] getOptionValues(final String opt) {
        Option option = getOptionObject(opt);
        if (option == null) {
            return null;
        }
        List<String> values = option.getValues();
        return values.toArray(new String[0]);
    }

    public List<String> getArgList() {
        return argList;
    }

    public String[] getArgs() {
        return argList.toArray(new String[0]);
    }

    public Collection<Option> getOptions() {
        return options.values();
    }

    public boolean hasOptions() {
        return !options.isEmpty();
    }

    public boolean hasArgs() {
        return !argList.isEmpty();
    }
}
