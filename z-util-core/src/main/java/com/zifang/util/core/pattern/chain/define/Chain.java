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