package com.zifang.util.db.respository;

import java.util.Map;
import java.util.Objects;

/**
 * @author zifang
 */
public class BoundSql {

    private String originSql;

    private String transformSql;

    private Map<Integer, String> indexName;

    private Map<Integer, Object> indexValue;

    private Map<Integer, Object> indexValueInsert;

    public String getOriginSql() {
        return originSql;
    }

    public void setOriginSql(String originSql) {
        this.originSql = originSql;
    }

    public String getTransformSql() {
        return transformSql;
    }

    public void setTransformSql(String transformSql) {
        this.transformSql = transformSql;
    }

    public Map<Integer, String> getIndexName() {
        return indexName;
    }

    public void setIndexName(Map<Integer, String> indexName) {
        this.indexName = indexName;
    }

    public Map<Integer, Object> getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(Map<Integer, Object> indexValue) {
        this.indexValue = indexValue;
    }

    public Map<Integer, Object> getIndexValueInsert() {
        return indexValueInsert;
    }

    public void setIndexValueInsert(Map<Integer, Object> indexValueInsert) {
        this.indexValueInsert = indexValueInsert;
    }

    @Override
    public String toString() {
        return "BoundSql{originSql=" + originSql + ", transformSql=" + transformSql + ", indexName=" + indexName + ", indexValue=" + indexValue + ", indexValueInsert=" + indexValueInsert + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundSql boundSql = (BoundSql) o;
        return Objects.equals(originSql, boundSql.originSql) &&
                Objects.equals(transformSql, boundSql.transformSql) &&
                Objects.equals(indexName, boundSql.indexName) &&
                Objects.equals(indexValue, boundSql.indexValue) &&
                Objects.equals(indexValueInsert, boundSql.indexValueInsert);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originSql, transformSql, indexName, indexValue, indexValueInsert);
    }
}
