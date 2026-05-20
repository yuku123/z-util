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
