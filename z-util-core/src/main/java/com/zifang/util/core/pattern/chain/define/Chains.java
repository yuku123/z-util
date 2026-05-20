///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.zifang.util.core.pattern.chain.define;
//
//import java.util.Map;
//
//import static java.lang.String.format;
//
///**
// * 简单的流式链式调用DSL，用于简化 {@link Chain} 实例的调用。
// *
// * @since 2.0
// * @version $Id$
// */
//public final class Chains {
//
//    /**
//     * 定义要执行的目标链。
//     *
//     * @param <K> 上下文键类型
//     * @param <V> 上下文值类型
//     * @param <C> 与此命令关联的上下文类型
//     * @param <CH> 要执行的 {@link Chain} 类型
//     * @param chain 要执行的链实例引用
//     * @return 下一个链构建器
//     * @throws IllegalArgumentException 如果 chain 为 null
//     */
//    public static <K, V, C extends Map<K, V>, CH extends Chain<K, V, C>> ToExecutorCommandSetter<K, V, C> on(CH chain) {
//        return new DefaultCommandSetter<K, V, C>(checkNotNullArgument(chain, "Null Chain can not be executed"));
//    }
//
//    /**
//     * 定义要调用的目标 {@link org.apache.commons.chain2.Catalog}。
//     *
//     * @param <K> 上下文键类型
//     * @param <V> 上下文值类型
//     * @param <C> 与此命令关联的上下文类型
//     * @param <CA> 要执行的 {@link org.apache.commons.chain2.Catalog} 类型
//     * @param catalog 要设置的核心实例引用
//     * @return 下一个链构建器
//     * @throws IllegalArgumentException 如果 catalog 为 null
//     */
//    public static <K, V, C extends Map<K, V>, CA extends org.apache.commons.chain2.Catalog<K, V, C>> NamedCommandSetter<K, V, C> on(CA catalog) {
//        return new DefaultNamedCommandSetter<K, V, C>(checkNotNullArgument(catalog, "Null Catalog can not be setup"));
//    }
//
//    /**
//     * 私有构造函数，此类不能被直接实例化。
//     */
//    private Chains() {
//        // do nothing
//    }
//
//    /**
//     * 默认的命令设置器实现，用于将命令添加到链中。
//     *
//     * @param <K> 上下文键类型
//     * @param <V> 上下文值类型
//     * @param <C> 与此命令关联的上下文类型
//     */
//    private static class DefaultCommandSetter<K, V, C extends Map<K, V>> implements org.apache.commons.chain2.ToExecutorCommandSetter<K, V, C> {
//
//        private final Chain<K, V, C> chain;
//
//        /**
//         * 构造一个默认命令设置器。
//         *
//         * @param chain 要操作的链实例
//         */
//        public DefaultCommandSetter(Chain<K, V, C> chain) {
//            this.chain = chain;
//        }
//
//        /**
//         * 将命令添加到链中并返回链执行器。
//         *
//         * @param <CMD> 要添加的命令类型
//         * @param command 要添加的命令实例，不能为 null
//         * @return 链执行器，用于后续操作
//         * @throws IllegalArgumentException 如果 command 为 null
//         */
//        public <CMD extends Command<K, V, C>> ChainExecutor<K, V, C> add(CMD command) {
//            chain.addCommand(checkNotNullArgument(command, "Chain does not accept null Command instances"));
//            return new DefaultChainExecutor<K, V, C>(chain);
//        }
//
//    }
//
//    /**
//     * 默认的链执行器实现，用于执行链中的命令。
//     *
//     * @param <K> 上下文键类型
//     * @param <V> 上下文值类型
//     * @param <C> 与此命令关联的上下文类型
//     */
//    private static final class DefaultChainExecutor<K, V, C extends Map<K, V>> implements ChainExecutor<K, V, C> {
//
//        private final Chain<K, V, C> chain;
//
//        /**
//         * 构造一个默认链执行器。
//         *
//         * @param chain 要执行的链实例
//         */
//        public DefaultChainExecutor(Chain<K, V, C> chain) {
//            this.chain = chain;
//        }
//
//        /**
//         * 将命令添加到链中并返回当前执行器（支持链式调用）。
//         *
//         * @param <CMD> 要添加的命令类型
//         * @param command 要添加的命令实例，不能为 null
//         * @return 当前执行器，用于继续添加更多命令
//         * @throws IllegalArgumentException 如果 command 为 null
//         */
//        public <CMD extends Command<K, V, C>> ChainExecutor<K, V, C> add(CMD command) {
//            chain.addCommand(checkNotNullArgument(command, "Chain does not accept null Command instances"));
//            return this;
//        }
//
//        /**
//         * 执行链中的所有命令。
//         *
//         * @param context 要处理的上下文实例，不能为 null
//         * @return 处理结果，{@link Processing#FINISHED} 表示处理完成，{@link Processing#CONTINUE} 表示继续处理
//         * @throws IllegalArgumentException 如果 context 为 null
//         * @throws ChainException 如果执行过程中发生异常
//         */
//        public Processing execute(C context) {
//            return chain.execute(checkNotNullArgument(context, "Chain cannot be applied to a null context."));
//        }
//
//    }
//
//    /**
//     * 默认的命名命令设置器实现，用于在目录中添加命名命令。
//     *
//     * @param <K> 上下文键类型
//     * @param <V> 上下文值类型
//     * @param <C> 与此命令关联的上下文类型
//     */
//    private static final class DefaultNamedCommandSetter<K, V, C extends Map<K, V>>
//        implements NamedCommandSetter<K, V, C> {
//
//        private final org.apache.commons.chain2.Catalog<K, V, C> catalog;
//
//        /**
//         * 构造一个默认命名命令设置器。
//         *
//         * @param catalog 要操作的目录实例
//         */
//        public DefaultNamedCommandSetter(org.apache.commons.chain2.Catalog<K, V, C> catalog) {
//            this.catalog = catalog;
//        }
//
//        /**
//         * 添加命令并返回名称设置器。
//         *
//         * @param <CMD> 要添加的命令类型
//         * @param command 要添加的命令实例，不能为 null
//         * @return 名称设置器，用于设置命令名称
//         * @throws IllegalArgumentException 如果 command 为 null
//         */
//        public <CMD extends Command<K, V, C>> NameSetter<K, V, C> add(CMD command) {
//            CMD checkedCommand = checkNotNullArgument( command, "Catalog does not accept null Command instances" );
//            return new DefaultNameSetter<K, V, C>(catalog, checkedCommand);
//        }
//
//    }
//
//    /**
//     * 默认的名称设置器实现，用于为命令设置名称。
//     *
//     * @param <K> 上下文键类型
//     * @param <V> 上下文值类型
//     * @param <C> 与此命令关联的上下文类型
//     */
//    private static final class DefaultNameSetter<K, V, C extends Map<K, V>> implements NameSetter<K, V, C> {
//
//        private final org.apache.commons.chain2.Catalog<K, V, C> catalog;
//
//        private final Command<K, V, C> command;
//
//        /**
//         * 构造一个默认名称设置器。
//         *
//         * @param catalog 要操作的目录实例
//         * @param command 要设置名称的命令实例
//         */
//        public DefaultNameSetter(org.apache.commons.chain2.Catalog<K, V, C> catalog, Command<K, V, C> command) {
//            this.catalog = catalog;
//            this.command = command;
//        }
//
//        /**
//         * 为命令指定一个名称并将其添加到目录中。
//         *
//         * @param name 命令的名称，不能为 null
//         * @return 新的命名命令设置器，用于继续添加更多命令
//         * @throws IllegalArgumentException 如果 name 为 null
//         */
//        public NamedCommandSetter<K, V, C> identifiedBy(String name) {
//            catalog.addCommand(checkNotNullArgument(name, "Command <%s> cannot be identified by a null name", command),
//                               command);
//            return new DefaultNamedCommandSetter<K, V, C>(catalog);
//        }
//
//    }
//
//    /**
//     * 检查参数是否为 null，如果是则抛出 IllegalArgumentException。
//     *
//     * @param <T> 参数类型
//     * @param reference 要检查的参数引用
//     * @param message 异常消息格式字符串
//     * @param args 消息格式参数
//     * @return 如果参数不为 null，则返回该参数
//     * @throws IllegalArgumentException 如果 reference 为 null
//     */
//    private static <T> T checkNotNullArgument(T reference, String message, Object...args) {
//        if (reference == null) {
//            throw new IllegalArgumentException(format(message, args));
//        }
//        return reference;
//    }
//
//}