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
