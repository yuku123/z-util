package com.zifang.util.db.transation;

import com.zifang.util.db.define.Isolation;
import com.zifang.util.db.define.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编程式事务模板，简化事务代码
 * <p>
 * 通过回调模式，将事务边界管理封装在模板中，业务代码只需关注业务逻辑。
 *
 * <p>使用示例：
 * <pre>
 * transactionTemplate.execute(() -> {
 *     repository.save(order);
 *     repository.save(orderItem);
 *     return order.getId();
 * });
 * </pre>
 *
 * <pre>
 * Long id = transactionTemplate.executeWithResult(() -> {
 *     repository.save(order);
 *     return order.getId();
 * });
 * </pre>
 *
 * @see TranslationManager
 */
public class TransactionTemplate {

    private static final Logger log = LoggerFactory.getLogger(TransactionTemplate.class);

    private TranslationManager transactionManager;
    private Propagation propagation = Propagation.REQUIRED;
    private Isolation isolation = Isolation.DEFAULT;
    private boolean readOnly = false;

    public TransactionTemplate() {
    }

    public TransactionTemplate(TranslationManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setTransactionManager(TranslationManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public void setIsolation(Isolation isolation) {
        this.isolation = isolation;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * 执行事务，无返回值
     *
     * @param action 业务逻辑
     * @throws TransactionException 事务执行失败时抛出
     */
    public void execute(TransactionAction action) {
        executeWithResult(() -> {
            action.execute();
            return null;
        });
    }

    /**
     * 执行事务，有返回值
     *
     * @param action 业务逻辑
     * @param <T>   返回值类型
     * @return 业务逻辑的返回值
     * @throws TransactionException 事务执行失败时抛出
     */
    public <T> T executeWithResult(TransactionFunction<T> action) {
        TranslationManager tm = getTransactionManager();
        boolean isNewTransaction = !tm.isActive();

        if (isNewTransaction) {
            tm.begin(propagation, isolation, readOnly);
        }
        try {
            T result = action.execute();
            if (isNewTransaction) {
                tm.commit();
            }
            return result;
        } catch (Throwable e) {
            if (isNewTransaction) {
                tm.rollback();
            }
            throw e instanceof TranslationManager.TransactionException ? (TranslationManager.TransactionException) e
                    : new TranslationManager.TransactionException("事务执行失败: " + e.getMessage(), e);
        }
    }

    private TranslationManager getTransactionManager() {
        if (transactionManager == null) {
            throw new TranslationManager.TransactionException("TransactionManager 未设置");
        }
        return transactionManager;
    }

    // ==================== 回调接口 ====================

    @FunctionalInterface
    public interface TransactionAction {
        void execute();
    }

    @FunctionalInterface
    public interface TransactionFunction<T> {
        T execute();
    }
}
