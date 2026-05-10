package com.zifang.util.workflow.config;

/**
 * 缓存引擎配置
 */
public class CacheEngine {

    private String engineType;

    private String cacheEngineService;

    public CacheEngine() {
    }

    public CacheEngine(String engineType, String cacheEngineService) {
        this.engineType = engineType;
        this.cacheEngineService = cacheEngineService;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getCacheEngineService() {
        return cacheEngineService;
    }

    public void setCacheEngineService(String cacheEngineService) {
        this.cacheEngineService = cacheEngineService;
    }

    @Override
    public String toString() {
        return "CacheEngine{engineType=" + engineType + ", cacheEngineService=" + cacheEngineService + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheEngine that = (CacheEngine) o;
        if (engineType != null ? !engineType.equals(that.engineType) : that.engineType != null) return false;
        return cacheEngineService != null ? cacheEngineService.equals(that.cacheEngineService) : that.cacheEngineService == null;
    }

    @Override
    public int hashCode() {
        int result = engineType != null ? engineType.hashCode() : 0;
        result = 31 * result + (cacheEngineService != null ? cacheEngineService.hashCode() : 0);
        return result;
    }
}
