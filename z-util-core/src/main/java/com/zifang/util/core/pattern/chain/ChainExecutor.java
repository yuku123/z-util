package com.zifang.util.core.pattern.chain;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;

/**
 * 链执行器
 * <p>
 * 提供链的执行、管理和监控功能
 *
 * @param <C> 上下文类型
 * @author zifang
 */
public class ChainExecutor<C extends ChainContext<?, ?>> {

    private final Map<String, Chain<C>> chainCatalog;
    private final List<ChainListener<C>> listeners;
    private final ExecutorService executor;

    public ChainExecutor() {
        this.chainCatalog = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.executor = null;
    }

    public ChainExecutor(ExecutorService executor) {
        this.chainCatalog = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.executor = executor;
    }

    /**
     * 注册链到目录
     */
    public ChainExecutor<C> register(String name, Chain<C> chain) {
        chainCatalog.put(name, chain);
        return this;
    }

    /**
     * 注册链
     */
    public ChainExecutor<C> register(Chain<C> chain) {
        chainCatalog.put(chain.getName(), chain);
        return this;
    }

    /**
     * 获取注册的链
     */
    public Chain<C> getChain(String name) {
        return chainCatalog.get(name);
    }

    /**
     * 移除注册的链
     */
    public Chain<C> removeChain(String name) {
        return chainCatalog.remove(name);
    }

    /**
     * 是否包含指定名称的链
     */
    public boolean contains(String name) {
        return chainCatalog.containsKey(name);
    }

    /**
     * 获取所有链名称
     */
    public Set<String> getChainNames() {
        return Collections.unmodifiableSet(chainCatalog.keySet());
    }

    /**
     * 获取链数量
     */
    public int getChainCount() {
        return chainCatalog.size();
    }

    /**
     * 添加监听器
     */
    public ChainExecutor<C> addListener(ChainListener<C> listener) {
        if (listener != null) {
            listeners.add(listener);
        }
        return this;
    }

    /**
     * 移除监听器
     */
    public ChainExecutor<C> removeListener(ChainListener<C> listener) {
        listeners.remove(listener);
        return this;
    }

    /**
     * 执行链
     */
    public ProcessorResult execute(Chain<C> chain, C context) {
        notifyBeforeExecution(chain, context);
        long startTime = System.currentTimeMillis();

        try {
            ProcessorResult result = chain.process(context);
            long duration = System.currentTimeMillis() - startTime;
            notifyAfterExecution(chain, context, result, duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            notifyOnError(chain, context, e, duration);
            throw e;
        }
    }

    /**
     * 执行命名链
     */
    public ProcessorResult execute(String chainName, C context) {
        Chain<C> chain = chainCatalog.get(chainName);
        if (chain == null) {
            throw new IllegalArgumentException("Chain not found: " + chainName);
        }
        return execute(chain, context);
    }

    /**
     * 异步执行链
     */
    public CompletableFuture<ProcessorResult> executeAsync(Chain<C> chain, C context) {
        if (executor == null) {
            throw new IllegalStateException("No ExecutorService configured for async execution");
        }
        return CompletableFuture.supplyAsync(() -> execute(chain, context), executor);
    }

    /**
     * 异步执行命名链
     */
    public CompletableFuture<ProcessorResult> executeAsync(String chainName, C context) {
        Chain<C> chain = chainCatalog.get(chainName);
        if (chain == null) {
            throw new IllegalArgumentException("Chain not found: " + chainName);
        }
        return executeAsync(chain, context);
    }

    private void notifyBeforeExecution(Chain<C> chain, C context) {
        for (ChainListener<C> listener : listeners) {
            try {
                listener.onBeforeExecution(chain, context);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    private void notifyAfterExecution(Chain<C> chain, C context, ProcessorResult result, long duration) {
        for (ChainListener<C> listener : listeners) {
            try {
                listener.onAfterExecution(chain, context, result, duration);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    private void notifyOnError(Chain<C> chain, C context, Exception error, long duration) {
        for (ChainListener<C> listener : listeners) {
            try {
                listener.onError(chain, context, error, duration);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    /**
     * 链监听器接口
     */
    public interface ChainListener<C extends ChainContext<?, ?>> {
        default void onBeforeExecution(Chain<C> chain, C context) {}
        default void onAfterExecution(Chain<C> chain, C context, ProcessorResult result, long duration) {}
        default void onError(Chain<C> chain, C context, Exception error, long duration) {}
    }

    /**
     * 简单监听器适配器
     */
    public abstract static class ChainListenerAdapter<C extends ChainContext<?, ?>> implements ChainListener<C> {
        @Override
        public void onBeforeExecution(Chain<C> chain, C context) {}

        @Override
        public void onAfterExecution(Chain<C> chain, C context, ProcessorResult result, long duration) {}

        @Override
        public void onError(Chain<C> chain, C context, Exception error, long duration) {}
    }

    /**
     * 日志监听器
     */
    public static class LoggingChainListener<C extends ChainContext<?, ?>> extends ChainListenerAdapter<C> {
        private final java.util.logging.Logger logger;

        public LoggingChainListener() {
            this.logger = java.util.logging.Logger.getLogger(ChainExecutor.class.getName());
        }

        public LoggingChainListener(java.util.logging.Logger logger) {
            this.logger = logger;
        }

        @Override
        public void onBeforeExecution(Chain<C> chain, C context) {
            logger.info("Executing chain: " + chain.getName());
        }

        @Override
        public void onAfterExecution(Chain<C> chain, C context, ProcessorResult result, long duration) {
            logger.info("Chain '" + chain.getName() + "' completed with result: " + result + " in " + duration + "ms");
        }

        @Override
        public void onError(Chain<C> chain, C context, Exception error, long duration) {
            logger.severe("Chain '" + chain.getName() + "' failed after " + duration + "ms: " + error.getMessage());
        }
    }

    /**
     * 指标监听器
     */
    public static class MetricsChainListener<C extends ChainContext<?, ?>> extends ChainListenerAdapter<C> {
        private final Map<String, Long> executionCount = new ConcurrentHashMap<>();
        private final Map<String, Long> totalDuration = new ConcurrentHashMap<>();
        private final Map<String, AtomicLong> failureCount = new ConcurrentHashMap<>();

        @Override
        public void onAfterExecution(Chain<C> chain, C context, ProcessorResult result, long duration) {
            String name = chain.getName();
            executionCount.merge(name, 1L, Long::sum);
            totalDuration.merge(name, duration, Long::sum);
        }

        @Override
        public void onError(Chain<C> chain, C context, Exception error, long duration) {
            String name = chain.getName();
            executionCount.merge(name, 1L, Long::sum);
            totalDuration.merge(name, duration, Long::sum);
            failureCount.computeIfAbsent(name, k -> new AtomicLong()).incrementAndGet();
        }

        public long getExecutionCount(String chainName) {
            return executionCount.getOrDefault(chainName, 0L);
        }

        public long getTotalDuration(String chainName) {
            return totalDuration.getOrDefault(chainName, 0L);
        }

        public long getFailureCount(String chainName) {
            AtomicLong count = failureCount.get(chainName);
            return count != null ? count.get() : 0;
        }

        public double getAverageDuration(String chainName) {
            long count = executionCount.getOrDefault(chainName, 0L);
            long total = totalDuration.getOrDefault(chainName, 0L);
            return count > 0 ? (double) total / count : 0;
        }
    }
}
