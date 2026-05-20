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