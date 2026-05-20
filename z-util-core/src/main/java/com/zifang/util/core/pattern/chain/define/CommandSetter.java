//public interface CommandSetter<K, V, C extends Map<K, V>, R> {
//
//    /**
//     * 将给定命令添加到要执行的目标 {@link Chain}。
//     *
//     * @param <CMD> 要添加的命令类型
//     * @param command 要添加到目标链中的命令
//     * @return 下一个链构建器
//     * @throws IllegalArgumentException 如果 command 为 null
//     * @see Chain#addCommand(org.apache.commons.chain2.Command)
//     */
//    <CMD extends org.apache.commons.chain2.Command<K, V, C>> R add(CMD command);
//
//}