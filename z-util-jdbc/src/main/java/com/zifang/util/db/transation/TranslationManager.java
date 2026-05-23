package com.zifang.util.db.transation;

import com.zifang.util.db.context.DatasourceFactory;
import com.zifang.util.db.define.Isolation;
import com.zifang.util.db.define.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 事务管理器
 * <p>
 * 基于 ThreadLocal 的事务管理器，支持：
 * <ul>
 *   <li>编程式事务：begin / commit / rollback</li>
 *   <li>声明式事务：通过 {@link com.zifang.util.db.define.Transactional} 注解自动管理</li>
 *   <li>事务传播行为：REQUIRED, REQUIRES_NEW, MANDATORY, SUPPORTS 等</li>
 *   <li>事务隔离级别：READ_COMMITTED, REPEATABLE_READ 等</li>
 *   <li>嵌套事务：NESTED 通过 Savepoint 实现</li>
 * </ul>
 *
 * <p>事务状态存储在 ThreadLocal 中，每个线程独立管理自己的事务。
 *
 * <p>使用示例：
 * <pre>
 * // 编程式
 * transactionManager.begin();
 * try {
 *     repository.save(entity);
 *     transactionManager.commit();
 * } catch (Exception e) {
 *     transactionManager.rollback();
 * }
 *
 * // 声明式（配合 TransactionInterceptor）
 * &#64;Transactional
 * public void transfer(Account from, Account to, BigDecimal amount) {
 *     // 自动开启、提交或回滚
 * }
 * </pre>
 */
public class TranslationManager {

    private static final Logger log = LoggerFactory.getLogger(TranslationManager.class);

    /**
     * 事务状态上下文，线程安全（每个线程独立）
     */
    private static final ThreadLocal<TransactionContext> transactionContext = new ThreadLocal<>();

    private DatasourceFactory datasourceFactory;

    public TranslationManager() {
    }

    public TranslationManager(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    public void setDatasourceFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    public DatasourceFactory getDatasourceFactory() {
        return datasourceFactory;
    }

    // ==================== 公开 API ====================

    /**
     * 开启一个新事务或加入已存在事务（根据传播行为）
     *
     * @param propagation 传播行为
     * @param isolation  隔离级别
     * @param readOnly  是否只读
     */
    public void begin(Propagation propagation, Isolation isolation, boolean readOnly) {
        TransactionContext ctx = transactionContext.get();
        if (ctx != null && ctx.isActive()) {
            handlePropagation(propagation, ctx, isolation, readOnly);
        } else {
            doBegin(isolation, readOnly);
        }
    }

    /**
     * 以默认传播行为（REQUIRED）和隔离级别开启事务
     */
    public void begin() {
        begin(Propagation.REQUIRED, Isolation.DEFAULT, false);
    }

    /**
     * 提交当前事务
     */
    public void commit() {
        TransactionContext ctx = transactionContext.get();
        if (ctx == null || !ctx.isActive()) {
            log.warn("尝试提交一个不存在或已结束的事务");
            return;
        }
        if (ctx.getDepth() > 1) {
            ctx.decrementDepth();
            log.debug("嵌套事务，深度减为 {}，不真正提交", ctx.getDepth());
            return;
        }
        try {
            if (ctx.isReadOnly()) {
                log.debug("只读事务，直接释放连接");
            } else {
                ctx.getConnection().commit();
                log.info("事务提交成功");
            }
        } catch (Exception e) {
            log.error("事务提交失败", e);
            throw new TransactionException("事务提交失败", e);
        } finally {
            releaseConnection(ctx);
            transactionContext.remove();
        }
    }

    /**
     * 回滚当前事务
     */
    public void rollback() {
        rollback(null);
    }

    /**
     * 回滚当前事务到指定保存点
     *
     * @param savepoint 保存点，为 null 则回滚整个事务
     */
    public void rollback(Savepoint savepoint) {
        TransactionContext ctx = transactionContext.get();
        if (ctx == null || !ctx.isActive()) {
            log.warn("尝试回滚一个不存在或已结束的事务");
            return;
        }
        try {
            if (savepoint != null) {
                ctx.getConnection().rollback(savepoint);
                log.info("事务回滚到保存点: {}", savepoint);
            } else {
                ctx.getConnection().rollback();
                log.info("事务回滚成功");
            }
        } catch (Exception e) {
            log.error("事务回滚失败", e);
            throw new TransactionException("事务回滚失败", e);
        } finally {
            releaseConnection(ctx);
            transactionContext.remove();
        }
    }

    /**
     * 创建嵌套事务保存点
     *
     * @param name 保存点名称
     * @return 保存点对象
     */
    public Savepoint createSavepoint(String name) {
        TransactionContext ctx = transactionContext.get();
        if (ctx == null || !ctx.isActive()) {
            throw new TransactionException("当前无活跃事务，无法创建保存点");
        }
        try {
            Savepoint sp = ctx.getConnection().setSavepoint(name);
            log.debug("创建保存点: {}", name);
            return sp;
        } catch (Exception e) {
            throw new TransactionException("创建保存点失败: " + name, e);
        }
    }

    /**
     * 释放保存点
     *
     * @param savepoint 保存点
     */
    public void releaseSavepoint(Savepoint savepoint) {
        TransactionContext ctx = transactionContext.get();
        if (ctx == null) {
            return;
        }
        try {
            ctx.getConnection().releaseSavepoint(savepoint);
        } catch (Exception e) {
            log.warn("释放保存点失败: {}", savepoint, e);
        }
    }

    /**
     * 判断当前线程是否存在活跃事务
     *
     * @return true if active transaction exists
     */
    public boolean isActive() {
        TransactionContext ctx = transactionContext.get();
        return ctx != null && ctx.isActive();
    }

    /**
     * 获取当前线程的数据库连接（从事务上下文获取，无事务则返回 null）
     *
     * @return Connection 或 null
     */
    public Connection getConnection() {
        TransactionContext ctx = transactionContext.get();
        return ctx != null ? ctx.getConnection() : null;
    }

    /**
     * 获取当前线程事务深度（用于嵌套事务计数）
     *
     * @return 事务深度，0 表示无事务
     */
    public int getDepth() {
        TransactionContext ctx = transactionContext.get();
        return ctx != null ? ctx.getDepth() : 0;
    }

    /**
     * 清理当前线程的事务上下文（通常无需主动调用）
     */
    public void clear() {
        TransactionContext ctx = transactionContext.get();
        if (ctx != null) {
            releaseConnection(ctx);
        }
        transactionContext.remove();
    }

    // ==================== 内部方法 ====================

    private void doBegin(Isolation isolation, boolean readOnly) {
        Objects.requireNonNull(datasourceFactory, "DatasourceFactory 未设置，请先调用 setDatasourceFactory");
        try {
            DataSource ds = datasourceFactory.getDatasource();
            Connection conn = ds.getConnection();
            if (isolation != Isolation.DEFAULT) {
                conn.setTransactionIsolation(isolation.getLevel());
            }
            conn.setAutoCommit(false);
            if (readOnly) {
                conn.setReadOnly(true);
            }
            TransactionContext ctx = new TransactionContext(conn, isolation, readOnly);
            transactionContext.set(ctx);
            log.info("事务开启，隔离级别={}, 只读={}", isolation, readOnly);
        } catch (Exception e) {
            throw new TransactionException("开启事务失败", e);
        }
    }

    private void handlePropagation(Propagation propagation, TransactionContext ctx,
                                   Isolation isolation, boolean readOnly) {
        switch (propagation) {
            case REQUIRED:
                // 加入当前事务，什么都不做
                ctx.incrementDepth();
                log.debug("加入已存在事务，深度加为 {}", ctx.getDepth());
                break;
            case REQUIRES_NEW:
                // 挂起当前事务，开启新事务
                ctx.suspend();
                doBegin(isolation, readOnly);
                break;
            case MANDATORY:
                if (!ctx.isActive()) {
                    throw new TransactionException("当前无活跃事务，但传播行为为 MANDATORY");
                }
                ctx.incrementDepth();
                break;
            case SUPPORTS:
                if (ctx.isActive()) {
                    ctx.incrementDepth();
                }
                break;
            case NOT_SUPPORTED:
                if (ctx.isActive()) {
                    ctx.suspend();
                }
                break;
            case NEVER:
                if (ctx.isActive()) {
                    throw new TransactionException("当前存在活跃事务，但传播行为为 NEVER");
                }
                break;
            case NESTED:
                // NESTED 使用 Savepoint 实现嵌套
                if (!ctx.isActive()) {
                    doBegin(isolation, readOnly);
                } else {
                    ctx.incrementDepth();
                    ctx.createSavepoint();
                }
                break;
            default:
                throw new TransactionException("不支持的传播行为: " + propagation);
        }
    }

    private void releaseConnection(TransactionContext ctx) {
        if (ctx == null) {
            return;
        }
        try {
            ctx.getConnection().setAutoCommit(true);
            ctx.getConnection().setReadOnly(false);
            ctx.getConnection().close();
        } catch (Exception e) {
            log.warn("关闭数据库连接时出错", e);
        }
    }

    // ==================== 内部类 ====================

    /**
     * 事务上下文，存储在 ThreadLocal 中
     */
    private static class TransactionContext {
        private final Connection connection;
        private final Isolation isolation;
        private final boolean readOnly;
        private final boolean isNew;
        private int depth = 1;
        private boolean active = true;
        private Connection suspendedConnection; // 用于 REQUIRES_NEW 时保存挂起的连接

        TransactionContext(Connection connection, Isolation isolation, boolean readOnly) {
            this.connection = connection;
            this.isolation = isolation;
            this.readOnly = readOnly;
            this.isNew = true;
        }

        Connection getConnection() {
            return suspendedConnection != null ? suspendedConnection : connection;
        }

        boolean isActive() {
            return active;
        }

        void setActive(boolean active) {
            this.active = active;
        }

        int getDepth() {
            return depth;
        }

        void incrementDepth() {
            this.depth++;
        }

        void decrementDepth() {
            this.depth--;
        }

        boolean isReadOnly() {
            return readOnly;
        }

        boolean isNew() {
            return isNew;
        }

        void suspend() {
            // REQUIRES_NEW 时挂起旧事务（这里简化处理：设置非活跃但不关闭连接）
            this.active = false;
        }

        void resume() {
            this.active = true;
        }

        void setSuspendedConnection(Connection suspendedConnection) {
            this.suspendedConnection = suspendedConnection;
        }

        void createSavepoint() {
            try {
                connection.setSavepoint("NESTED_" + depth);
            } catch (Exception e) {
                throw new TransactionException("创建保存点失败", e);
            }
        }
    }

    // ==================== 内部异常 ====================

    /**
     * 事务异常
     */
    public static class TransactionException extends RuntimeException {
        public TransactionException(String message) {
            super(message);
        }

        public TransactionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
