package com.zifang.util.core.pattern.pool;

/**
 * 对象池配置
 */
public class PoolConfig {

    /**
     * 最大池对象数量
     */
    private int maxTotal = 8;

    /**
     * 最大空闲对象数量
     */
    private int maxIdle = 8;

    /**
     * 最小空闲对象数量
     */
    private int minIdle = 0;

    /**
     * 借出前是否验证
     */
    private boolean testOnBorrow = false;

    /**
     * 归还后是否验证
     */
    private boolean testOnReturn = false;

    /**
     * 空闲时是否验证
     */
    private boolean testWhileIdle = false;

    /**
     * 空闲对象验证器调用的时间间隔（毫秒）
     */
    private long durationBetweenEvictionRuns = -1;

    /**
     * 空闲对象最大数量
     */
    private int maxWaitMillis = -1;

    /**
     * 最小空闲对象数量检查
     */
    private boolean blockWhenExhausted = true;

    /**
     * 软引用缓冲类型
     */
    private boolean softMinIdle = false;

    public PoolConfig() {
    }

    public PoolConfig(int maxTotal, int maxIdle, int minIdle) {
        this.maxTotal = maxTotal;
        this.maxIdle = maxIdle;
        this.minIdle = minIdle;
    }

    // Getters and Setters

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public long getDurationBetweenEvictionRuns() {
        return durationBetweenEvictionRuns;
    }

    public void setDurationBetweenEvictionRuns(long durationBetweenEvictionRuns) {
        this.durationBetweenEvictionRuns = durationBetweenEvictionRuns;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public boolean isSoftMinIdle() {
        return softMinIdle;
    }

    public void setSoftMinIdle(boolean softMinIdle) {
        this.softMinIdle = softMinIdle;
    }

    /**
     * 创建默认配置
     */
    public static PoolConfig createDefault() {
        return new PoolConfig();
    }

    /**
     * 创建配置Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PoolConfig config = new PoolConfig();

        public Builder maxTotal(int maxTotal) {
            config.setMaxTotal(maxTotal);
            return this;
        }

        public Builder maxIdle(int maxIdle) {
            config.setMaxIdle(maxIdle);
            return this;
        }

        public Builder minIdle(int minIdle) {
            config.setMinIdle(minIdle);
            return this;
        }

        public Builder testOnBorrow(boolean testOnBorrow) {
            config.setTestOnBorrow(testOnBorrow);
            return this;
        }

        public Builder testOnReturn(boolean testOnReturn) {
            config.setTestOnReturn(testOnReturn);
            return this;
        }

        public Builder testWhileIdle(boolean testWhileIdle) {
            config.setTestWhileIdle(testWhileIdle);
            return this;
        }

        public Builder durationBetweenEvictionRuns(long durationBetweenEvictionRuns) {
            config.setDurationBetweenEvictionRuns(durationBetweenEvictionRuns);
            return this;
        }

        public Builder maxWaitMillis(int maxWaitMillis) {
            config.setMaxWaitMillis(maxWaitMillis);
            return this;
        }

        public Builder blockWhenExhausted(boolean blockWhenExhausted) {
            config.setBlockWhenExhausted(blockWhenExhausted);
            return this;
        }

        public PoolConfig build() {
            return config;
        }
    }
}
