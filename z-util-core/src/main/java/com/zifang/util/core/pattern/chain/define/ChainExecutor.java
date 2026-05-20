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
// * 构建器，允许继续向目标链添加命令并执行它。
// *
// * @param <K> 上下文键类型
// * @param <V> 上下文值类型
// * @param <C> 与此链执行器关联的上下文类型
// * @version $Id$
// * @since 2.0
// */
//public interface ChainExecutor<K, V, C extends Map<K, V>> extends CommandSetter<K, V, C, ChainExecutor<K, V, C>> {
//
//    /**
//     * 执行目标链表示的处理。
//     *
//     * @param context 由目标链处理的上下文
//     * @return {@link Processing#FINISHED} 如果此上下文的处理已完成。
//     *  {@link Processing#CONTINUE} 如果此上下文的处理应该委托给 enclosing chain 中的后续命令。
//     *
//     * @throws IllegalArgumentException 如果 context 为 null
//     * @throws ChainException 如果执行过程中发生异常
//     * @see Chain#execute(Map)
//     */
//    Processing execute(C context);
//
//}