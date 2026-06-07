package com.zifang.util.db.transation;

import com.zifang.util.db.define.Isolation;
import com.zifang.util.db.define.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事务管理器
 *
 * @see com.zifang.util.db.transation.TransactionInterceptor
 * @see com.zifang.util.db.transation.TransactionTemplate
 */
/**
 * TranslationManager类。
 */
/**
 * TranslationManager类。
 */
public class TranslationManager {

    private static final Logger log = LoggerFactory.getLogger(TranslationManager.class);

    private final ThreadLocal<Integer> depthHolder = ThreadLocal.withInitial(() -> 0);

    /**
     * 开启事务
     *
     * @param propagation 传播行为
     * @param isolation   隔离级别
     * @param readOnly    是否只读
     */
    /**
     * begin方法。
     *      * @param propagation Propagation类型参数
     * @param isolation Isolation类型参数
     * @param readOnly boolean类型参数
     */
    /**
     * begin方法。
     *      * @param propagation Propagation类型参数
     * @param isolation Isolation类型参数
     * @param readOnly boolean类型参数
     */
    public void begin(Propagation propagation, Isolation isolation, boolean readOnly) {
        int depth = getDepth();
        if (depth > 0) {
            switch (propagation) {
                case MANDATORY:
                    throw new TransactionException("当前存在活动事务，但传播行为为 MANDATORY");
                case NEVER:
                    throw new TransactionException("当前存在活动事务，但传播行为为 NEVER");
                case NOT_SUPPORTED:
                    // 挂起当前事务，以非事务执行
                    depthHolder.set(0);
                    log.debug("事务传播行为 NOT_SUPPORTED，挂起当前事务");
                    break;
                case SUPPORTS:
                    // 加入已存在事务，不做任何操作
                    log.debug("事务传播行为 SUPPORTS，加入已存在事务，depth={}", depth);
                    break;
                case REQUIRES_NEW:
                    // 挂起当前事务，开启新事务
                    log.debug("事务传播行为 REQUIRES_NEW，挂起当前事务");
                    break;
                case NESTED:
                    // 创建嵌套事务（保存点）
                    log.debug("事务传播行为 NESTED，创建嵌套事务");
                    break;
                case REQUIRED:
                default:
                    // 加入已存在事务
                    log.debug("事务传播行为 REQUIRED，加入已存在事务，depth={}", depth);
                    break;
            }
        }
        depthHolder.set(depth + 1);
        log.debug("事务开启，depth={}, propagation={}, isolation={}, readOnly={}",
                depth + 1, propagation, isolation, readOnly);
    }

    /**
     * 检查是否存在活动事务
     */
    /**
     * isActive方法。
     * @return boolean类型返回值
     */
    /**
     * isActive方法。
     * @return boolean类型返回值
     */
    public boolean isActive() {
        return getDepth() > 0;
    }

    /**
     * 获取当前事务嵌套深度
     *
     * @return 深度，0 表示无活动事务
     */
    /**
     * getDepth方法。
     * @return int类型返回值
     */
    /**
     * getDepth方法。
     * @return int类型返回值
     */
    public int getDepth() {
        Integer depth = depthHolder.get();
        return depth == null ? 0 : depth;
    }

    /**
     * 提交当前事务
     *
     * @throws TransactionException 如果没有活动事务
     */
    /**
     * commit方法。
     */
    /**
     * commit方法。
     */
    public void commit() {
        int depth = getDepth();
        if (depth == 0) {
            throw new TransactionException("没有活动事务，无法提交");
        }
        depthHolder.set(depth - 1);
        log.debug("事务提交，剩余 depth={}", depth - 1);
    }

    /**
     * 回滚当前事务
     *
     * @throws TransactionException 如果没有活动事务
     */
    /**
     * rollback方法。
     */
    /**
     * rollback方法。
     */
    public void rollback() {
        int depth = getDepth();
        if (depth == 0) {
            throw new TransactionException("没有活动事务，无法回滚");
        }
        depthHolder.set(0);
        log.debug("事务回滚完成");
    }

    /**
     * 事务异常
     */
    public static class TransactionException extends RuntimeException {

    /**
     * TransactionException方法。
     *      * @param message String类型参数
     */
    /**
     * TransactionException方法。
     *      * @param message String类型参数
     */
        public TransactionException(String message) {
            super(message);
        }

    /**
     * TransactionException方法。
     *      * @param message String类型参数
     * @param cause Throwable类型参数
     */
    /**
     * TransactionException方法。
     *      * @param message String类型参数
     * @param cause Throwable类型参数
     */
        public TransactionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
