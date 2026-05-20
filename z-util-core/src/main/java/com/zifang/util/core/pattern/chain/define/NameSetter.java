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
// * 允许在 {@link org.apache.commons.chain2.Catalog} 实例中为 {@link org.apache.commons.chain2.Command} 指定名称。
// *
// * @param <K> 上下文键类型
// * @param <V> 上下文值类型
// * @param <C> 与此名称设置器关联的上下文类型
// * @since 2.0
// * @version $Id$
// */
//public interface NameSetter<K, V, C extends Map<K, V>> {
//
//    /**
//     * 在 {@link org.apache.commons.chain2.Catalog} 实例中为 {@link org.apache.commons.chain2.Command} 指定名称。
//     *
//     * @param name 上一个设置的 {@link org.apache.commons.chain2.Command} 的名称
//     * @return 一个新的构建器，用于添加新的 {@link org.apache.commons.chain2.Command}
//     * @throws IllegalArgumentException 如果 name 为 null
//     */
//    NamedCommandSetter<K, V, C> identifiedBy(String name);
//
//}