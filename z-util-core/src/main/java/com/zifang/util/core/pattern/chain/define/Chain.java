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
// * <p>{@link Chain} 表示一个配置好的 {@link org.apache.commons.chain2.Command} 列表，
// * 这些命令将按顺序执行以对指定的 {@link org.apache.commons.chain2.Context} 进行处理。
// * 每个包含的 {@link org.apache.commons.chain2.Command} 将依次执行，
// * 直到其中一个返回 <code>FINISHED</code>、
// * 其中一个执行的 {@link org.apache.commons.chain2.Command} 抛出异常，
// * 或到达链的末尾。{@link Chain} 本身将返回最后执行的 {@link org.apache.commons.chain2.Command} 的返回值
// * （如果没有抛出异常），或重新抛出抛出的异常。</p>
// *
// * <p>注意，{@link Chain} 扩展了 {@link org.apache.commons.chain2.Command}，
// * 因此两者可以在期望 {@link org.apache.commons.chain2.Command} 的地方互换使用。
// * 这使得通过将子链组合成整体处理链来轻松组装分层工作流变得容易。</p>
// *
// * <p>为保护应用程序免受此接口演化的影响，
// * {@link Chain} 的专用实现通常应该通过扩展提供的基类
// * ({@code org.apache.commons.chain2.impl.ChainBase}) 来创建，
// * 而不是直接实现此接口。</p>
// *
// * <p>{@link Chain} 实现应该是线程安全的，
// * 适合在多个线程上同时执行。
// * 通常，这意味着标识当前正在执行哪个 {@link org.apache.commons.chain2.Command} 的状态信息
// * 应该保持在 <code>execute()</code> 方法内的局部变量中，
// * 而不是实例变量中。
// * {@link Chain} 中的 {@link org.apache.commons.chain2.Command} 可以在
// * {@link Chain} 的 <code>execute()</code> 方法首次调用之前的任何时间配置
// * （通过调用 <code>addCommand()</code>）。
// * 此后，{@link Chain} 的配置将被冻结。</p>
// *
// * @param <K> 上下文键类型
// * @param <V> 上下文值类型
// * @param <C> 与此链关联的上下文类型
// *
// * @version $Id$
// */
//public interface Chain<K, V, C extends Map<K, V>> extends Command<K, V, C> {
//
//    /**
//     * <p>将 {@link org.apache.commons.chain2.Command} 添加到列表中，
//     * 这些命令将在调用此 {@link Chain} 的 <code>execute()</code> 方法时依次调用。
//     * 一旦 <code>execute()</code> 至少被调用一次，
//     * 就不再可能添加额外的 {@link org.apache.commons.chain2.Command}；
//     * 相反，将抛出异常。</p>
//     *
//     * @param <CMD> 要添加到 {@link Chain} 的 {@link org.apache.commons.chain2.Command} 类型
//     * @param command 要添加的 {@link org.apache.commons.chain2.Command}
//     *
//     * @throws IllegalArgumentException 如果 <code>command</code> 为 <code>null</code>
//     * @throws IllegalStateException 如果此 {@link Chain} 已经至少执行过一次，
//     *  因此不允许进一步配置
//     */
//    <CMD extends org.apache.commons.chain2.Command<K, V, C>> void addCommand(CMD command);
//
//    /**
//     * <p>根据以下算法执行此 {@link Chain} 表示的处理。</p>
//     * <ul>
//     * <li>如果 {@link Chain} 中没有配置 {@link org.apache.commons.chain2.Command}，
//     *     返回 <code>CONTINUE</code>。</li>
//     * <li>按通过调用 <code>addCommand()</code> 方法添加的顺序调用
//     *     配置在此链上的每个 {@link org.apache.commons.chain2.Command} 的 <code>execute()</code> 方法，
//     *     直到遇到配置的 {@link org.apache.commons.chain2.Command} 的末尾，
//     *     或者其中执行的 {@link org.apache.commons.chain2.Command} 返回 <code>FINISHED</code>
//     *     或抛出异常。</li>
//     * <li>向后遍历其 <code>execute()</code> 方法已执行的 {@link org.apache.commons.chain2.Command}，
//     *     从最后执行的一个开始。
//     *     如果此 {@link org.apache.commons.chain2.Command} 实例也是 {@link org.apache.commons.chain2.Filter}，
//     *     调用其 <code>postprocess()</code> 方法，
//     *     丢弃抛出的任何异常。</li>
//     * <li>如果最后调用其 <code>execute()</code> 方法的 {@link org.apache.commons.chain2.Command} 抛出了异常，
//     *     重新抛出该异常。</li>
//     * <li>否则，返回最后执行的 {@link org.apache.commons.chain2.Command} 的 <code>execute()</code> 方法返回的值。
//     *     如果最后一个 {@link org.apache.commons.chain2.Command} 指示此 {@link org.apache.commons.chain2.Context} 的处理已完成，
//     *     则返回 <code>FINISHED</code>；
//     *     如果没有调用的 {@link org.apache.commons.chain2.Command} 返回 <code>FINISHED</code>，
//     *     则返回 <code>CONTINUE</code>。</li>
//     * </ul>
//     *
//     * @param context 要由此 {@link Chain} 处理的 {@link org.apache.commons.chain2.Context}
//     *
//     * @throws IllegalArgumentException 如果 <code>context</code> 为 <code>null</code>
//     *
//     * @return {@link org.apache.commons.chain2.Processing#FINISHED} 如果此上下文的处理已完成。
//     *  {@link org.apache.commons.chain2.Processing#CONTINUE} 如果此上下文的处理应该委托给 enclosing chain 中的后续命令。
//     */
//    @Override
//    org.apache.commons.chain2.Processing execute(C context);
//
//}