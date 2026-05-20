//public interface Catalog<K, V, C extends Map<K, V>> {
//
//    /**
//     * <p>用于存储默认 {@link Catalog} 的默认上下文属性，
//     * 仅作为便利提供。</p>
//     */
//    String CATALOG_KEY = "org.apache.commons.chain2.CATALOG";
//
//    /**
//     * <p>将新名称和关联的 {@link Command} 或 {@link Chain} 添加到此
//     * {@link Catalog} 的命名命令集中，
//     * 替换该名称的任何先前命令。</p>
//     *
//     * @param <CMD> 要添加到 {@link Catalog} 的 {@link Command} 类型
//     * @param name 新命令的名称
//     * @param command 以后对此名称进行查找时要返回的 {@link Command} 或 {@link Chain}
//     * @throws IllegalArgumentException 如果 name 或 command 为 null
//     */
//    <CMD extends Command<K, V, C>> void addCommand(String name, CMD command);
//
//    /**
//     * <p>返回与指定名称关联的 {@link Command} 或 {@link Chain}（如果有）；
//     * 否则返回 <code>null</code>。</p>
//     *
//     * @param <CMD> 期望返回的 {@link Command} 类型
//     * @param name 要检索其 {@link Command} 或 {@link Chain} 的名称
//     * @return 与指定名称关联的命令
//     * @throws IllegalArgumentException 如果 name 为 null
//     */
//    <CMD extends Command<K, V, C>> CMD getCommand(String name);
//
//    /**
//     * <p>返回此 {@link Catalog} 已知命名命令集上的
//     * <code>Iterator</code>。
//     * 如果没有已知命令，则返回空迭代器。</p>
//     *
//     * @return 目录中名称的迭代器
//     */
//    Iterator<String> getNames();
//
//}