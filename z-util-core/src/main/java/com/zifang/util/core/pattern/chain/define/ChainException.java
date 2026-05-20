//public class ChainException extends RuntimeException {
//
//    /**
//     * 序列化版本UID
//     */
//    private static final long serialVersionUID = 20120724L;
//
//    /**
//     * 异常发生时的上下文
//     */
//    private final Map<?, ?> context;
//
//    /**
//     * 发生异常时失败的命令
//     */
//    private final Command<?, ?, ?> failedCommand;
//
//    /**
//     * 使用消息创建异常对象。
//     *
//     * @param message 与异常关联的消息
//     */
//    public ChainException(String message) {
//        super(message);
//        this.context = null;
//        this.failedCommand = null;
//    }
//
//    /**
//     * 使用消息创建异常对象，并将其链接到另一个异常。
//     *
//     * @param message 与异常关联的消息
//     * @param cause 要链接到此异常的异常
//     */
//    public ChainException(String message, Throwable cause) {
//        super(message, cause);
//        this.context = null;
//        this.failedCommand = null;
//    }
//
//    /**
//     * 构造一个新的 ChainException，并引用与被包装的异常关联的
//     * {@link org.apache.commons.chain2.Context} 和 {@link org.apache.commons.chain2.Command}。
//     *
//     * @param <K> 上下文键类型
//     * @param <V> 上下文值类型
//     * @param <C> 与此命令关联的上下文类型
//     * @param message 详细消息。详细消息被保存，以便以后通过
//     *          {@link #getMessage()} 方法检索。
//     * @param cause 原因（保存以供以后通过
//     *         {@link #getCause()} 方法检索）。(允许 <tt>null</tt> 值，
//     *         表示原因不存在或未知。)
//     * @param context 传递给发生异常的 {@link org.apache.commons.chain2.Command} 的 Context 对象。
//     * @param failedCommand 抛出异常的 Command 对象。
//     */
//    public <K, V, C extends Map<K, V>> ChainException(String message, Throwable cause,
//                                                      C context, org.apache.commons.chain2.Command<K, V, C> failedCommand) {
//        super(message, cause);
//        this.context = context;
//        this.failedCommand = failedCommand;
//    }
//
//    /**
//     * 返回抛出异常的 {@link org.apache.commons.chain2.Command} 时传递的上下文对象。
//     *
//     * @return 上下文对象
//     */
//    public Map<?, ?> getContext() {
//        return context;
//    }
//
//    /**
//     * 返回原始异常被抛出的 {@link org.apache.commons.chain2.Command} 对象。
//     *
//     * @return 失败的命令对象
//     */
//    public org.apache.commons.chain2.Command<?, ?, ?> getFailedCommand() {
//        return failedCommand;
//    }
//
//}