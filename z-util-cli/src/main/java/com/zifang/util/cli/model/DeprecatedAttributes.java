package com.zifang.util.cli.model;

import java.io.Serializable;

/**
 * Represents deprecation metadata for an option.
 */
public class DeprecatedAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String since;
    private final String description;
    private final boolean forRemoval;

    public DeprecatedAttributes() {
        this(null, null);
    }

    public DeprecatedAttributes(final String since, final String description) {
        this(since, description, false);
    }

    public DeprecatedAttributes(final String since, final String description, final boolean forRemoval) {
        this.since = since;
        this.description = description;
        this.forRemoval = forRemoval;
    }

    public String getSince() {
        return since;
    }

    public String getDescription() {
        return description;
    }

    public boolean isForRemoval() {
        return forRemoval;
    }

    public boolean isDeprecated() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[Deprecated");
        if (forRemoval) {
            sb.append(" for removal");
        }
        if (since != null && !since.isEmpty()) {
            sb.append(" since ").append(since);
        }
        if (description != null && !description.isEmpty()) {
            sb.append(". ").append(description);
        }
        sb.append("]");
        return sb.toString();
    }
}
