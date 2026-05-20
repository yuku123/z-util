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
import java.util.List;

/**
 * Represents a group of options where only one option in the group may be selected.
 */
public class OptionGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Option> options = new ArrayList<>();
    private String description;
    private boolean required = false;
    private Option selected;

    public OptionGroup() {}

    public OptionGroup(final String description) {
        this.description = description;
    }

    public OptionGroup addOption(final Option option) {
        options.add(option);
        return this;
    }

    public List<Option> getOptions() {
        return options;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public Option getSelected() {
        return selected;
    }

    public void setSelected(final Option option) {
        this.selected = option;
    }

    public String toString() {
        return options.toString();
    }
}
