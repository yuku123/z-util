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