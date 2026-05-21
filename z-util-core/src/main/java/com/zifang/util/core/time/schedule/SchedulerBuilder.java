package com.zifang.util.core.time.schedule;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;

/**
 * 调度器构建器，用于配置和创建 {@link SchedulerManager} 实例。
 * <p>
 * 支持两种模式：
 * <ul>
 *   <li><b>RAM 模式（默认）</b>：任务数据存储在内存中，重启后丢失</li>
 *   <li><b>JDBC 模式</b>：任务数据持久化到数据库，支持集群和重启恢复</li>
 * </ul>
 * <p>
 * 示例：创建默认调度器
 * <pre>
 * SchedulerManager scheduler = SchedulerBuilder.newScheduler()
 *     .withName("my-scheduler")
 *     .build();
 * </pre>
 * <p>
 * 示例：配置集群调度器
 * <pre>
 * SchedulerManager scheduler = SchedulerBuilder.newScheduler()
 *     .withName("cluster-scheduler")
 *     .withJdbcStore()
 *     .withClusterEnabled(true)
 *     .withInstanceId("AUTO")
 *     .build();
 * </pre>
 *
 * @see SchedulerManager
 */
public class SchedulerBuilder {

    private String name = "Scheduler";
    private String instanceId = Scheduler.DEFAULT_INSTANCE_ID;
    private boolean embedded = false;
    private int threadCount = 10;
    private int threadPriority = Thread.NORM_PRIORITY;
    private boolean clusterEnabled = false;
    private Properties jdbcProperties = null;
    private boolean autoStart = false;

    private SchedulerBuilder() {
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建新的调度器构建器。
     */
    public static SchedulerBuilder newScheduler() {
        return new SchedulerBuilder();
    }

    // ==================== 基本配置 ====================

    /**
     * 设置调度器名称。
     */
    public SchedulerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 设置调度器实例 ID。
     * <p>
     * 集群环境下推荐使用 {@code "AUTO"} 自动生成唯一 ID。
     */
    public SchedulerBuilder withInstanceId(String instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    /**
     * 设置调度器实例 ID 为自动生成。
     */
    public SchedulerBuilder withAutoInstanceId() {
        this.instanceId = "AUTO";
        return this;
    }

    // ==================== 线程池配置 ====================

    /**
     * 设置线程池线程数量。
     * <p>
     * 默认值：10。
     *
     * @param count 线程数量
     */
    public SchedulerBuilder withThreadCount(int count) {
        this.threadCount = count;
        return this;
    }

    /**
     * 设置线程优先级。
     * <p>
     * 默认值：{@link Thread#NORM_PRIORITY}
     *
     * @param priority 线程优先级（1-10）
     */
    public SchedulerBuilder withThreadPriority(int priority) {
        this.threadPriority = priority;
        return this;
    }

    // ==================== JDBC 持久化配置 ====================

    /**
     * 启用 JDBC 存储（将任务数据持久化到数据库）。
     * <p>
     * 需要在 classpath 下提供 quartz.properties 或设置相关数据源。
     */
    public SchedulerBuilder withJdbcStore() {
        this.jdbcProperties = new Properties();
        return this;
    }

    /**
     * 设置 JDBC 存储的额外属性。
     *
     * @param props Quartz JDBC 配置属性
     */
    public SchedulerBuilder withJdbcProperties(Properties props) {
        this.jdbcProperties = props;
        return this;
    }

    // ==================== 集群配置 ====================

    /**
     * 启用集群模式。
     * <p>
     * 集群环境下，多个 Scheduler 实例共享数据库中的任务数据，
     * 保证任务只在一个节点上执行。
     * <p>
     * 必须配合 {@link #withJdbcStore()} 使用。
     */
    public SchedulerBuilder withClusterEnabled(boolean enabled) {
        this.clusterEnabled = enabled;
        return this;
    }

    // ==================== 构建 ====================

    /**
     * 构建调度器。
     *
     * @return 配置好的 SchedulerManager 实例
     * @throws SchedulerRuntimeException 构建失败时抛出
     */
    public SchedulerManager build() {
        try {
            Properties props = buildProperties();
            SchedulerFactory factory;

            if (jdbcProperties != null) {
                // 使用自定义属性（JDBC 模式）
                factory = new StdSchedulerFactory(props);
            } else {
                // 使用默认配置（RAM 模式）
                factory = new StdSchedulerFactory();
            }

            Scheduler quartzScheduler = factory.getScheduler();

            if (autoStart) {
                quartzScheduler.start();
            }

            return new SchedulerManager(quartzScheduler);
        } catch (org.quartz.SchedulerException e) {
            throw new SchedulerRuntimeException("Failed to build scheduler", e);
        }
    }

    private Properties buildProperties() {
        Properties props = new Properties();

        if (jdbcProperties != null) {
            // JDBC 模式
            props.putAll(jdbcProperties);
        }

        // 基本配置
        if (name != null) {
            props.setProperty("org.quartz.scheduler.instanceName", name);
        }
        if (instanceId != null) {
            props.setProperty("org.quartz.scheduler.instanceId", instanceId);
        }

        // 线程池配置
        props.setProperty("org.quartz.threadPool.class",
                "org.quartz.simpl.SimpleThreadPool");
        props.setProperty("org.quartz.threadPool.threadCount",
                String.valueOf(threadCount));
        props.setProperty("org.quartz.threadPool.threadPriority",
                String.valueOf(threadPriority));

        // 集群配置
        if (clusterEnabled) {
            props.setProperty("org.quartz.scheduler.clustered", "true");
            props.setProperty("org.quartz.jobStore.class",
                    "org.quartz.impl.jdbcjobstore.JobStoreTX");
            props.setProperty("org.quartz.jobStore.driverDelegateClass",
                    "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
            props.setProperty("org.quartz.jobStore.isClustered", "true");
            props.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
            // 数据源由外部 quartz.properties 或 JNDI 提供
        } else if (jdbcProperties == null) {
            // RAM 模式（不使用 JDBC）
            props.setProperty("org.quartz.jobStore.class",
                    "org.quartz.simpl.RAMJobStore");
        }

        return props;
    }

    /**
     * 构建完成后自动启动调度器。
     * <p>
     * 默认不自动启动，需要手动调用 {@link SchedulerManager#start()}。
     */
    public SchedulerBuilder withAutoStart() {
        this.autoStart = true;
        return this;
    }
}
