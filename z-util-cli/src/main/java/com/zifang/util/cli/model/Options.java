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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Main container for all command-line options.
 */
public class Options implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Option> shortOpts = new HashMap<>();
    private final Map<String, Option> longOpts = new HashMap<>();
    private final Map<Option, OptionGroup> optionGroups = new HashMap<>();
    private final List<OptionGroup> optionGroupsList = new ArrayList<>();
    private final List<Option> requiredOptions = new ArrayList<>();

    public Options() {}

    public Options addOption(final Option option) {
        String key = option.getKey();
        if (option.getOpt() != null) {
            shortOpts.put(option.getOpt(), option);
        }
        if (option.getLongOpt() != null) {
            longOpts.put(option.getLongOpt(), option);
        }
        if (option.isRequired()) {
            requiredOptions.add(option);
        }
        return this;
    }

    public Options addOption(final String opt, final boolean hasArg, final String description) {
        return addOption(new Option(opt, hasArg, description));
    }

    public Options addOption(final String opt, final String longOpt, final boolean hasArg, final String description) {
        return addOption(new Option(opt, longOpt, hasArg, description));
    }

    public Options addRequiredOption(final String opt, final String longOpt, final boolean hasArg, final String description) {
        Option option = Option.builder()
                .opt(opt)
                .longOpt(longOpt)
                .hasArg(hasArg)
                .description(description)
                .required(true)
                .build();
        return addOption(option);
    }

    public Options addOptionGroup(final OptionGroup group) {
        for (Option option : group.getOptions()) {
            optionGroups.put(option, group);
            addOption(option);
        }
        optionGroupsList.add(group);
        if (group.isRequired()) {
            requiredOptions.addAll(group.getOptions());
        }
        return this;
    }

    public Option getOption(final String opt) {
        Option option = shortOpts.get(opt);
        if (option == null) {
            option = longOpts.get(opt);
        }
        return option;
    }

    public boolean hasOption(final String opt) {
        return shortOpts.containsKey(opt) || longOpts.containsKey(opt);
    }

    public boolean hasShortOption(final String opt) {
        return shortOpts.containsKey(opt);
    }

    public boolean hasLongOption(final String opt) {
        return longOpts.containsKey(opt);
    }

    public OptionGroup getOptionGroup(final Option option) {
        return optionGroups.get(option);
    }

    public Collection<Option> getOptions() {
        List<Option> opts = new ArrayList<>(shortOpts.values());
        return Collections.unmodifiableCollection(opts);
    }

    public List<OptionGroup> getOptionGroups() {
        return Collections.unmodifiableList(optionGroupsList);
    }

    public List<Option> getRequiredOptions() {
        return Collections.unmodifiableList(requiredOptions);
    }

    public List<Option> getOptionsSortedByKey() {
        List<Option> opts = new ArrayList<>(shortOpts.values());
        Collections.sort(opts, (o1, o2) -> o1.getKey().compareToIgnoreCase(o2.getKey()));
        return opts;
    }

    public List<String> getMatchingOptions(final String opt) {
        List<String> matching = new ArrayList<>();
        for (String longOpt : longOpts.keySet()) {
            if (longOpt.startsWith(opt)) {
                matching.add(longOpt);
            }
        }
        Collections.sort(matching);
        return matching;
    }

    public List<Option> helpOptions() {
        return new ArrayList<>(shortOpts.values());
    }
}
