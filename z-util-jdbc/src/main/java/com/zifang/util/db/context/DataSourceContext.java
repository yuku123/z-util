package com.zifang.util.db.context;

import com.zifang.util.db.transation.TranslationManager;

public class DataSourceContext {

    private DatasourceFactory datasourceFactory;

    private String scanPackageName;

    private TranslationManager transactionManager;

    public DataSourceContext dataSourceFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
        return this;
    }

    public DataSourceContext scanPackage(String scanPackageName) {
        this.scanPackageName = scanPackageName;
        return this;
    }

    public DataSourceContext transationManager(TranslationManager transationManager) {
        this.transactionManager = transationManager;
        return this;
    }

    public DatasourceFactory getDatasourceFactory() {
        return datasourceFactory;
    }

    public void setDatasourceFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    public String getScanPackageName() {
        return scanPackageName;
    }

    public void setScanPackageName(String scanPackageName) {
        this.scanPackageName = scanPackageName;
    }

    public TranslationManager getTransactionManager() {
        return transactionManager;
    }

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
