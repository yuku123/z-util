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
import java.util.List;

/**
 * Represents a command-line option. Supports short opt (-s), long opt (--long),
 * argument requirements, value conversion, and deprecation metadata.
 */
public class Option implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String opt;
    private final String longOpt;
    private final boolean hasArg;
    private final String description;
    private final boolean required;
    private final String argName;
    private final boolean optionalArg;
    private final int maxArgs;
    private final List<String> values = new ArrayList<>();
    private DeprecatedAttributes deprecated;

    protected Option(final String opt, final boolean hasArg, final String description) {
        this(opt, null, hasArg, null, description, false, false, 1, null);
    }

    protected Option(final String opt, final String longOpt, final boolean hasArg, final String description) {
        this(opt, longOpt, hasArg, null, description, false, false, 1, null);
    }

    protected Option(final String opt, final String longOpt, final boolean hasArg,
                    final String argName, final String description, final boolean required,
                    final boolean optionalArg, final int maxArgs) {
        this(opt, longOpt, hasArg, argName, description, required, optionalArg, maxArgs, null);
    }

    protected Option(final String opt, final String longOpt, final boolean hasArg,
                    final String argName, final String description, final boolean required,
                    final boolean optionalArg, final int maxArgs, final DeprecatedAttributes deprecated) {
        if (opt == null && longOpt == null) {
            throw new IllegalArgumentException("opt and longOpt cannot both be null");
        }
        this.opt = opt;
        this.longOpt = longOpt;
        this.hasArg = hasArg;
        this.argName = argName;
        this.description = description;
        this.required = required;
        this.optionalArg = optionalArg;
        this.maxArgs = maxArgs;
        this.deprecated = deprecated;
    }

    // Builder pattern support
    protected Option(final Builder builder) {
        this(builder.opt, builder.longOpt, builder.hasArg, builder.argName,
             builder.description, builder.required, builder.optionalArg,
             builder.maxArgs, builder.deprecated);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getKey() {
        return opt != null ? opt : longOpt;
    }

    public String getOpt() {
        return opt;
    }

    public String getLongOpt() {
        return longOpt;
    }

    public boolean hasArg() {
        return hasArg;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    public String getArgName() {
        return argName;
    }

    public boolean hasOptionalArg() {
        return optionalArg;
    }

    public int getMaxArgs() {
        return maxArgs;
    }

    public DeprecatedAttributes getDeprecated() {
        return deprecated;
    }

    public boolean isDeprecated() {
        return deprecated != null && deprecated.isDeprecated();
    }

    public String getSince() {
        return deprecated != null ? deprecated.getSince() : null;
    }

    public List<String> getValues() {
        return values;
    }

    public String getValue() {
        return values.isEmpty() ? null : values.get(0);
    }

    public String getValue(final int index) {
        return values.get(index);
    }

    public String getValue(final String defaultValue) {
        return values.isEmpty() ? defaultValue : values.get(0);
    }

    public boolean hasValue() {
        return !values.isEmpty();
    }

    public void addValueForProcessing(final String value) {
        if (!hasArg) {
            throw new IllegalStateException("Cannot add value to non-argument option");
        }
        values.add(value);
    }

    public void processValue(final String value) {
        addValueForProcessing(value);
    }

    public boolean isValuesEmpty() {
        return values.isEmpty();
    }

    public void clearValues() {
        values.clear();
    }

    public boolean acceptsArg() {
        return hasArg && (values.size() < maxArgs || maxArgs < 1);
    }

    public void setDeprecated(final DeprecatedAttributes deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public Object clone() {
        try {
            Option copy = (Option) super.clone();
            copy.values.clear();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (opt != null) {
            sb.append("-").append(opt);
        }
        if (longOpt != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append("--").append(longOpt);
        }
        if (hasArg) {
            sb.append(" <").append(argName != null ? argName : "arg").append(">");
        }
        return sb.toString();
    }

    public static class Builder {
        private String opt;
        private String longOpt;
        private boolean hasArg = false;
        private String argName;
        private String description;
        private boolean required = false;
        private boolean optionalArg = false;
        private int maxArgs = 1;
        private DeprecatedAttributes deprecated;

        public Builder() {}

        public Builder opt(final String opt) {
            this.opt = opt;
            return this;
        }

        public Builder longOpt(final String longOpt) {
            this.longOpt = longOpt;
            return this;
        }

        public Builder hasArg(final boolean hasArg) {
            this.hasArg = hasArg;
            return this;
        }

        public Builder hasArg(final String argName) {
            this.hasArg = true;
            this.argName = argName;
            return this;
        }

        public Builder argName(final String argName) {
            this.argName = argName;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public Builder required(final boolean required) {
            this.required = required;
            return this;
        }

        public Builder optionalArg(final boolean optionalArg) {
            this.optionalArg = optionalArg;
            return this;
        }

        public Builder maxArgs(final int maxArgs) {
            this.maxArgs = maxArgs;
            return this;
        }

        public Builder deprecated(final DeprecatedAttributes deprecated) {
            this.deprecated = deprecated;
            return this;
        }

        public Builder deprecated(final String since, final String description) {
            this.deprecated = new DeprecatedAttributes(since, description);
            return this;
        }

        public Option build() {
            if (opt == null && longOpt == null) {
                throw new IllegalArgumentException("opt or longOpt must be specified");
            }
            return new Option(this);
        }
    }
}
