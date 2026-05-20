package com.zifang.util.db.context;

import com.zifang.util.db.transation.TranslationManager;

/**
 * 数据源上下文配置类，用于配置数据源工厂、扫描包和事务管理器
 */
public class DataSourceContext {

    private DatasourceFactory datasourceFactory;

    private String scanPackageName;

    private TranslationManager transactionManager;

    /**
     * 设置数据源工厂
     *
     * @param datasourceFactory 数据源工厂实例
     * @return 当前实例，支持链式调用
     */
    public DataSourceContext dataSourceFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
        return this;
    }

    /**
     * 设置要扫描的包名
     *
     * @param scanPackageName 包名，用于Repository接口扫描
     * @return 当前实例，支持链式调用
     */
    public DataSourceContext scanPackage(String scanPackageName) {
        this.scanPackageName = scanPackageName;
        return this;
    }

    /**
     * 设置事务管理器
     *
     * @param transationManager 事务管理器实例
     * @return 当前实例，支持链式调用
     */
    public DataSourceContext transationManager(TranslationManager transationManager) {
        this.transactionManager = transationManager;
        return this;
    }

    /**
     * 获取数据源工厂
     *
     * @return 数据源工厂
     */
    public DatasourceFactory getDatasourceFactory() {
        return datasourceFactory;
    }

    /**
     * 设置数据源工厂
     *
     * @param datasourceFactory 数据源工厂
     */
    public void setDatasourceFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    /**
     * 获取扫描包名
     *
     * @return 扫描包名
     */
    public String getScanPackageName() {
        return scanPackageName;
    }

    /**
     * 设置扫描包名
     *
     * @param scanPackageName 扫描包名
     */
    public void setScanPackageName(String scanPackageName) {
        this.scanPackageName = scanPackageName;
    }

    /**
     * 获取事务管理器
     *
     * @return 事务管理器
     */
    public TranslationManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * 设置事务管理器
     *
     * @param transactionManager 事务管理器
     */
    public void setTransactionManager(TranslationManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public String toString() {
        return "DataSourceContext{datasourceFactory=" + datasourceFactory + ", scanPackageName=" + scanPackageName + ", transactionManager=" + transactionManager + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourceContext that = (DataSourceContext) o;
        return java.util.Objects.equals(datasourceFactory, that.datasourceFactory) &&
                java.util.Objects.equals(scanPackageName, that.scanPackageName) &&
                java.util.Objects.equals(transactionManager, that.transactionManager);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(datasourceFactory, scanPackageName, transactionManager);
    }
}
