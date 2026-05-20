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
///**
// * <code>Processing</code> 封装了命令可以返回的状态。
// * <p>
// * {@link org.apache.commons.chain2.Command} 如果给定上下文的处理已完成应返回 <code>FINISHED</code>，
// * 或者如果给定 {@link org.apache.commons.chain2.Context} 的处理应该委托给 enclosing {@link Chain} 中的后续命令，
// * 则应返回 <code>CONTINUE</code>。
// *
// * @version $Id$
// */
//public enum Processing {
//
//    /**
//     * 如果给定上下文的处理应该委托给 enclosing chain 中的后续命令，
//     * 命令应返回 continue。
//     *
//     * @since Chain 2.0
//     */
//    CONTINUE,
//
//    /**
//     * 如果给定上下文的处理已完成，
//     * 命令应返回 finished。
//     *
//     * @since Chain 2.0
//     */
//    FINISHED;
//
//}