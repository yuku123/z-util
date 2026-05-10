package com.zifang.util.core.pattern.cache;

public class CacheClientBuilder {

    private CacheProvider cacheProvider;
    private String dbNum;

    public CacheClientBuilder() {
    }

    public CacheClientBuilder(CacheProvider cacheProvider, String dbNum) {
        this.cacheProvider = cacheProvider;
        this.dbNum = dbNum;
    }

    public CacheProvider getCacheProvider() {
        return cacheProvider;
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public String getDbNum() {
        return dbNum;
    }

    public void setDbNum(String dbNum) {
        this.dbNum = dbNum;
    }

    public CacheClient build() {
        CacheClient cacheClient = new DefaultCacheClient();
        return cacheClient;
    }

    @Override
    public String toString() {
        return "CacheClientBuilder{cacheProvider=" + cacheProvider + ", dbNum=" + dbNum + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheClientBuilder that = (CacheClientBuilder) o;
        return java.util.Objects.equals(cacheProvider, that.cacheProvider) &&
                java.util.Objects.equals(dbNum, that.dbNum);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(cacheProvider, dbNum);
    }
}