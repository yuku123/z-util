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
package com.zifang.util.cli.parser;

import com.zifang.util.cli.exception.ParseException;
import com.zifang.util.cli.model.CommandLine;
import com.zifang.util.cli.model.Options;

import java.util.Properties;

/**
 * Interface for command-line parsers.
 */
public interface CommandLineParser {

    /**
     * Parse the command-line arguments.
     *
     * @param options the Options to parse
     * @param arguments the command-line arguments
     * @return the parsed CommandLine
     * @throws ParseException if parsing fails
     */
    CommandLine parse(Options options, String[] arguments) throws ParseException;

    /**
     * Parse the command-line arguments with stop-at-non-option behavior.
     *
     * @param options the Options to parse
     * @param arguments the command-line arguments
     * @param stopAtNonOption if true, stop parsing at first non-option argument
     * @return the parsed CommandLine
     * @throws ParseException if parsing fails
     */
    CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException;

    /**
     * Parse the command-line arguments with default properties.
     *
     * @param options the Options to parse
     * @param arguments the command-line arguments
     * @param properties default option values
     * @return the parsed CommandLine
     * @throws ParseException if parsing fails
     */
    CommandLine parse(Options options, String[] arguments, Properties properties) throws ParseException;

    /**
     * Parse the command-line arguments with all options.
     *
     * @param options the Options to parse
     * @param arguments the command-line arguments
     * @param properties default option values
     * @param stopAtNonOption if true, stop parsing at first non-option argument
     * @return the parsed CommandLine
     * @throws ParseException if parsing fails
     */
    CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) throws ParseException;
}
