package com.zifang.util.db.transation;

import com.zifang.util.db.define.Isolation;
import com.zifang.util.db.define.Propagation;
import com.zifang.util.db.define.Transactional;
import com.zifang.util.proxy.aspects.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 事务拦截器，实现 {@link Aspect} 接口，
 * 拦截标注了 {@link Transactional} 的方法，自动管理事务。
 *
 * <p>该拦截器通过 AOP 机制工作，配合代理框架使用：
 * <ul>
 *   <li>方法执行前：开启事务（根据传播行为）</li>
 *   <li>方法正常返回：提交事务</li>
 *   <li>方法抛出异常：根据 rollbackFor / noRollbackFor 判断是否回滚</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>
 * // 注册拦截器到代理框架
 * TransactionInterceptor interceptor = new TransactionInterceptor(transactionManager);
 * proxyFactory.addAspect(interceptor);
 * </pre>
 */
/**
 * TransactionInterceptor类。
 */
public class TransactionInterceptor implements Aspect {

    private static final Logger log = LoggerFactory.getLogger(TransactionInterceptor.class);

    private final TranslationManager transactionManager;

    /**
     * TransactionInterceptor方法。
     *      * @param transactionManager TranslationManager类型参数
     */
    public TransactionInterceptor(TranslationManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    /**
     * before方法。
     *      * @param target Object类型参数
     * @param method Method类型参数
     * @param args Object[]类型参数
     * @return boolean类型返回值
     */
    public boolean before(Object target, Method method, Object[] args) {
        Transactional transactional = findTransactional(target.getClass(), method);
        if (transactional == null) {
            return true; // 不拦截无 @Transactional 标注的方法
        }

        // 检查是否已在新事务中
        boolean isNewTransaction = !transactionManager.isActive();
        if (isNewTransaction) {
            transactionManager.begin(
                    transactional.propagation(),
                    transactional.isolation(),
                    transactional.readOnly()
            );
            log.debug("事务拦截器开启事务: method={}, propagation={}, isolation={}",
                    method.getName(), transactional.propagation(), transactional.isolation());
        } else {
            log.debug("事务拦截器加入已存在事务: method={}", method.getName());
        }
        return true;
    }

    @Override
    /**
     * after方法。
     *      * @param target Object类型参数
     * @param method Method类型参数
     * @param args Object[]类型参数
     * @param returnVal Object类型参数
     * @return boolean类型返回值
     */
    public boolean after(Object target, Method method, Object[] args, Object returnVal) {
        Transactional transactional = findTransactional(target.getClass(), method);
        if (transactional == null) {
            return true;
        }

        boolean isNewTransaction = !transactionManager.isActive()
                || transactionManager.getDepth() == 0
                || (transactionManager.getDepth() == 1 && isTransactionalMethod(method));

        if (isNewTransaction && transactionManager.isActive()) {
            try {
                transactionManager.commit();
            } catch (Exception e) {
                log.error("事务提交失败: method={}", method.getName(), e);
                throw new TranslationManager.TransactionException("事务提交失败", e);
            }
        }
        return true;
    }

    @Override
    /**
     * afterException方法。
     *      * @param target Object类型参数
     * @param method Method类型参数
     * @param args Object[]类型参数
     * @param e Throwable类型参数
     * @return boolean类型返回值
     */
    public boolean afterException(Object target, Method method, Object[] args, Throwable e) {
        Transactional transactional = findTransactional(target.getClass(), method);
        if (transactional == null) {
            return true; // 未标注事务，让异常继续传播
        }

        boolean shouldRollback = shouldRollback(transactional, e);
        if (shouldRollback) {
            log.warn("事务回滚，method={}, exception={}", method.getName(), e.getMessage());
            if (transactionManager.isActive()) {
                transactionManager.rollback();
            }
        } else {
            log.debug("异常不在回滚范围内，尝试提交: method={}, exception={}",
                    method.getName(), e.getMessage());
            if (transactionManager.isActive()) {
                try {
                    transactionManager.commit();
                } catch (Exception commitEx) {
                    log.error("提交失败（异常后）: method={}", method.getName(), commitEx);
                    throw new TranslationManager.TransactionException("提交失败", commitEx);
                }
            }
        }
        // 返回 true 表示已处理（回滚了），不再传播异常；如需继续传播异常返回 false
        return true;
    }

    /**
     * 从类或方法上查找 @Transactional 注解
     * 方法级优先于类级
     */
    private Transactional findTransactional(Class<?> clazz, Method method) {
        // 1. 方法级注解
        Transactional methodTx = method.getAnnotation(Transactional.class);
        if (methodTx != null) {
            return methodTx;
        }

        // 2. 类级注解
        return clazz.getAnnotation(Transactional.class);
    }

    /**
     * 判断是否应该回滚
     */
    private boolean shouldRollback(Transactional transactional, Throwable e) {
        Class<? extends Throwable> exClass = e.getClass();

        // 检查 noRollbackFor
        for (Class<? extends Throwable> noRollback : transactional.noRollbackFor()) {
            if (noRollback.isAssignableFrom(exClass)) {
                return false;
            }
        }

        // 检查 noRollbackForClassName
        String exName = exClass.getName();
        for (String noRollbackName : transactional.noRollbackForClassName()) {
            if (noRollbackName.equals(exName)) {
                return false;
            }
        }

        // 检查 rollbackFor
        if (transactional.rollbackFor().length > 0) {
            for (Class<? extends Throwable> rollbackFor : transactional.rollbackFor()) {
                if (rollbackFor.isAssignableFrom(exClass)) {
                    return true;
                }
            }
            return false; // 有 rollbackFor 但不匹配，不回滚
        }

        // 检查 rollbackForClassName
        if (transactional.rollbackForClassName().length > 0) {
            for (String rollbackForName : transactional.rollbackForClassName()) {
                if (rollbackForName.equals(exName)) {
                    return true;
                }
                try {
                    if (Class.forName(rollbackForName).isAssignableFrom(exClass)) {
                        return true;
                    }
                } catch (ClassNotFoundException ignored) {
                    // 类不存在，跳过
                }
            }
            return false;
        }

        // 默认：RuntimeException 和 Error 回滚
        return (e instanceof RuntimeException) || (e instanceof Error);
    }

    private boolean isTransactionalMethod(Method method) {
        return method.getAnnotation(Transactional.class) != null;
    }
}
