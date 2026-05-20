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