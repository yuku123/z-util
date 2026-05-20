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
///**
// * 通用构建器，允许将命令添加到要执行的目标 {@link Chain}。
// *
// * @param <K> 上下文键类型
// * @param <V> 上下文值类型
// * @param <C> 与此命令设置器关联的上下文类型
// * @param <R> 下一个链构建器类型
// * @since 2.0
// * @version $Id$
// */
//public interface CommandSetter<K, V, C extends Map<K, V>, R> {
//
//    /**
//     * 将给定命令添加到要执行的目标 {@link Chain}。
//     *
//     * @param <CMD> 要添加的命令类型
//     * @param command 要添加到目标链中的命令
//     * @return 下一个链构建器
//     * @throws IllegalArgumentException 如果 command 为 null
//     * @see Chain#addCommand(org.apache.commons.chain2.Command)
//     */
//    <CMD extends org.apache.commons.chain2.Command<K, V, C>> R add(CMD command);
//
//}