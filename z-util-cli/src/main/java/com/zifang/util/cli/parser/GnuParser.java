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

import com.zifang.util.cli.model.Option;
import com.zifang.util.cli.model.Options;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser for GNU-style command-line arguments.
 * Supports combining short options (-abc), long options with equals (--foo=value),
 * and property-style options (-Dkey=value).
 *
 * @deprecated since 1.3, use {@link DefaultParser} instead.
 */
@Deprecated
public class GnuParser extends Parser {

    public GnuParser() {}

    @Override
    protected String[] flatten(final Options options, final String[] arguments, final boolean stopAtNonOption) {
        List<String> tokens = new ArrayList<>();
        boolean eatTheRest = false;

        for (int i = 0; i < arguments.length; i++) {
            String arg = arguments[i];
            if (arg == null) continue;

            if ("--".equals(arg)) {
                eatTheRest = true;
                tokens.add("--");
            } else if ("-".equals(arg)) {
                tokens.add("-");
            } else if (arg.startsWith("--")) {
                String opt = arg.substring(2);
                if (options.hasOption(opt)) {
                    tokens.add(arg);
                } else {
                    int equalIndex = DefaultParser.indexOfEqual(opt);
                    if (equalIndex != -1 && options.hasOption(opt.substring(0, equalIndex))) {
                        tokens.add(arg.substring(0, arg.indexOf('=')));
                        tokens.add(arg.substring(arg.indexOf('=') + 1));
                    } else if (options.hasOption(arg.substring(0, 2))) {
                        tokens.add(arg.substring(0, 2));
                        tokens.add(arg.substring(2));
                    } else {
                        eatTheRest = stopAtNonOption;
                        tokens.add(arg);
                    }
                }
            } else if (arg.startsWith("-")) {
                String opt = arg.substring(1);
                if (options.hasOption(opt)) {
                    tokens.add(arg);
                } else {
                    int equalIndex = DefaultParser.indexOfEqual(opt);
                    if (equalIndex != -1 && options.hasOption(opt.substring(0, equalIndex))) {
                        tokens.add(arg.substring(0, arg.indexOf('=')));
                        tokens.add(arg.substring(arg.indexOf('=') + 1));
                    } else if (options.hasOption(arg.substring(0, 2))) {
                        tokens.add(arg.substring(0, 2));
                        tokens.add(arg.substring(2));
                    } else {
                        eatTheRest = stopAtNonOption;
                        tokens.add(arg);
                    }
                }
            } else {
                tokens.add(arg);
            }

            if (eatTheRest) {
                for (i++; i < arguments.length; i++) {
                    if (arguments[i] != null) {
                        tokens.add(arguments[i]);
                    }
                }
            }
        }

        return tokens.toArray(new String[0]);
    }
}
