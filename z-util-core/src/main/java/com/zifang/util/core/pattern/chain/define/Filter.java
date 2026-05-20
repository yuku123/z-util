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
// * <p>{@link Filter} 是一个专用的 {@link org.apache.commons.chain2.Command}，
// * 它还期望执行它的 {@link Chain} 在调用其 <code>execute()</code> 方法时调用
// * <code>postprocess()</code> 方法。
// * 即使此 {@link org.apache.commons.chain2.Command} 的 <code>execute()</code> 方法
// * 或后续调用其 <code>execute()</code> 方法的 {@link org.apache.commons.chain2.Command}
// * 可能抛出任何可能的异常，也必须履行此承诺。
// * 所属的 {@link Chain} 必须按调用其 <code>execute()</code> 方法的反向顺序调用
// * {@link Chain} 中每个 {@link Filter} 的 <code>postprocess()</code> 方法。</p>
// *
// * <p>与 {@link org.apache.commons.chain2.Command} 相比，{@link Filter} 最常见的用例是：
// * 必须获取并保存可能昂贵的资源，直到特定请求的处理完成，
// * 即使通过 <code>execute()</code> 返回 <code>CONTINUE</code>
// * 将执行委托给后续 {@link org.apache.commons.chain2.Command}。
// * {@link Filter} 可以可靠地在 <code>postprocess()</code> 方法中释放此类资源，
// * 而该方法保证由所属的 {@link Chain} 调用。</p>
// *
// * @param <K> 与此命令关联的上下文维护的键类型
// * @param <V> 映射值的类型
// * @param <C> 与此命令关联的上下文类型
// *
// * @version $Id$
// */
//public interface Filter<K, V, C extends Map<K, V>> extends org.apache.commons.chain2.Command<K, V, C> {
//
//    /**
//     * <p>执行任何清理活动，例如释放在此 {@link Filter} 实例的
//     * <code>execute()</code> 方法期间获取的资源。</p>
//     *
//     * @param context 要由此 {@link Filter} 处理的 {@link org.apache.commons.chain2.Context}
//     * @param exception 最后执行的 {@link org.apache.commons.chain2.Command} 抛出的
//     *  <code>Exception</code>（如果有）；否则为 <code>null</code>
//     *
//     * @throws IllegalArgumentException 如果 <code>context</code> 为 <code>null</code>
//     *
//     * @return 如果此方法"处理"了非空 <code>exception</code>
//     *  （因此不需要重新抛出），则返回 <code>FINISHED</code>；
//     *  否则返回 <code>CONTINUE</code>
//     */
//   boolean postprocess(C context, Exception exception);
//
//}