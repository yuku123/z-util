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
// * <p>{@link Command} 封装要执行的单个处理单元，
// * 其目的是检查和/或修改由 {@link org.apache.commons.chain2.Context} 表示的事务的状态。
// * 单个 {@link Command} 可以组装成 {@link Chain}，
// * 使其能够完成所需的处理或将进一步处理委托给 {@link Chain} 中的下一个 {@link Command}。</p>
// *
// * <p>{@link Command} 实现应该是线程安全的，
// * 适合同时由不同线程处理包含在多个 {@link Chain} 中。
// * 通常，这意味着 {@link Command} 类不应在实例变量中维护状态信息。
// * 相反，状态信息应该通过对传递给 <code>execute()</code> 命令的 {@link org.apache.commons.chain2.Context} 
// * 的属性进行适当的修改来维护。</p>
// *
// * <p>{@link Command} 实现通常通过调用
// * <code>Context.getAttributes()</code> 获取的 <code>Map</code>
// * 使用特定键来检索和存储传递给 <code>execute()</code> 方法的 {@link org.apache.commons.chain2.Context} 实例中的状态信息。
// * 为了提高 {@link Command} 实现的互操作性，
// * 一个有用的设计模式是将用作 {@link Command} 实现类本身的 JavaBeans 属性的键值暴露出来。
// * 例如，需要输入和输出键的 {@link Command} 可能实现以下属性：</p>
// *
// * <pre>
// *   private String inputKey = "input";
// *   public String getInputKey() {
// *     return (this.inputKey);
// *   }
// *   public void setInputKey(String inputKey) {
// *     this.inputKey = inputKey;
// *   }
// *
// *   private String outputKey = "output";
// *   public String getOutputKey() {
// *     return (this.outputKey);
// *   }
// *   public void setOutputKey(String outputKey) {
// *     this.outputKey = outputKey;
// *   }
// * </pre>
// *
// * <p>在上下文中访问 "input" 信息的操作将通过调用：</p>
// *
// * <pre>
// *   String input = (String) context.get(getInputKey());
// * </pre>
// *
// * <p>而不是硬编码属性名。
// * 在此类属性名上使用 "Key" 后缀是一个有用的约定，
// * 用于识别以这种方式使用的属性，
// * 而不是简单配置 {@link Command} 内部操作的 JavaBeans 属性。</p>
// *
// * @param <K> 与此命令关联的上下文维护的键类型
// * @param <V> 映射值的类型
// * @param <C> 与此命令关联的上下文类型
// *
// * @version $Id$
// */
//public interface Command<K, V, C extends Map<K, V>> {
//
//    /**
//     * 执行要执行的单个处理单元。
//     * <p>
//     * 命令可以完成所需的处理并返回 finished，
//     * 或者通过返回 continue 将剩余处理委托给 enclosing {@link Chain} 中的后续命令。
//     *
//     * @param context 要由此 {@link Command} 处理的 {@link org.apache.commons.chain2.Context}
//     *
//     * @throws ChainException 用于指示异常终止的通用异常返回
//     * @throws IllegalArgumentException 如果 <code>context</code> 为 <code>null</code>
//     *
//     * @return {@link Processing#FINISHED} 如果此上下文的处理已完成。
//     *  {@link Processing#CONTINUE} 如果此上下文的处理应该委托给 enclosing chain 中的后续命令。
//     */
//    Processing execute(C context);
//
//}