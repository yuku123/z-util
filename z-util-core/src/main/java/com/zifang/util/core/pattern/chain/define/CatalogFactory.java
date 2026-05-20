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
//import java.util.Iterator;
//import java.util.Map;
//
///**
// * <code>CatalogFactory</code> 存储和检索目录。
// * <p>
// * 工厂允许使用默认 {@link Catalog} 以及使用名称键存储的 Catalog。
// * 它遵循工厂模式（参见 GoF）。
// * <p>
// * 基本实现基于单个 String 提供命令查找。
// * 此 String 对编码目录和命令名称。
// *
// * @param <K> 与此 {@link Command} 关联的上下文维护的键类型
// * @param <V> 映射值的类型
// * @param <C> 与此命令关联的上下文类型
// *
// * @version $Id: CatalogFactory.java 1486528 2013-05-27 07:38:38Z simonetripoda $
// */
//public interface CatalogFactory<K, V, C extends Map<K, V>> {
//
//    /**
//     * <p>传递给 <code>getCommand(String)</code> 方法的值应使用
//     * "catalog" 名称和 "command" 名称之间的分隔符。</p>
//     */
//    public static final String DELIMITER = ":";
//
//    /**
//     * <p>获取与工厂关联的默认 Catalog 实例（如果有）；
//     * 否则返回 <code>null</code>。</p>
//     *
//     * @return 默认 Catalog 实例
//     */
//    public abstract Catalog<K, V, C> getCatalog();
//
//    /**
//     * <p>设置与工厂关联的默认 Catalog 实例。</p>
//     *
//     * @param catalog 默认 Catalog 实例
//     */
//    public abstract void setCatalog(Catalog<K, V, C> catalog);
//
//    /**
//     * <p>按名称检索 Catalog 实例（如果有）；
//     * 否则返回 <code>null</code>。</p>
//     *
//     * @param name 要检索的 Catalog 名称
//     * @return 指定的 Catalog
//     * @throws IllegalArgumentException 如果 name 为 null
//     */
//    public abstract Catalog<K, V, C> getCatalog(String name);
//
//    /**
//     * <p>将命名的 Catalog 实例添加到工厂（以便后续检索）。</p>
//     *
//     * @param name 要添加的 Catalog 名称
//     * @param catalog 要添加的 Catalog
//     * @throws IllegalArgumentException 如果 name 或 catalog 为 null
//     */
//    public abstract void addCatalog(String name, Catalog<K, V, C> catalog);
//
//    /**
//     * <p>返回此实例已知命名 {@link Catalog} 集上的
//     * <code>Iterator</code>。
//     * 如果没有已知的目录，则返回空迭代器。</p>
//     *
//     * @return 此工厂已知的 Catalog 名称的迭代器
//     */
//    public abstract Iterator<String> getNames();
//
//    /**
//     * <p>根据给定的 commandID 返回 <code>Command</code>。</p>
//     *
//     * <p>目前，commandID 的结构比较简单：
//     * 如果 commandID 包含 DELIMITER，
//     * 则将 commandID 中不包括 DELIMITER 的部分作为目录名称，
//     * 将 DELIMITER 后的部分作为该目录中的命令名称。
//     * 如果 commandID 不包含 DELIMITER，
//     * 则将 commandID 作为默认目录中命令的名称。</p>
//     *
//     * <p>为了保留将来扩展此查找机制的可能性，
//     * DELIMITER 字符串应被视为保留的，
//     * 不应在命令名称中使用。
//     * 包含多个 DELIMITER 的 commandID 值将导致抛出
//     * <code>IllegalArgumentException</code>。</p>
//     *
//     * @param <CMD> 期望返回的 {@link Command} 类型
//     * @param commandID 要返回的命令的标识符
//     * @return 用 commandID 定位的命令，
//     *  如果命令名称或目录名称无法解析则为 <code>null</code>
//     * @throws IllegalArgumentException 如果 commandID 包含多个 DELIMITER
//     *
//     * @since Chain 1.1
//     */
//    public abstract <CMD extends Command<K, V, C>> CMD getCommand(
//            String commandID);
//
//}