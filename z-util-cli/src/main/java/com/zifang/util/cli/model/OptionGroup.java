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
