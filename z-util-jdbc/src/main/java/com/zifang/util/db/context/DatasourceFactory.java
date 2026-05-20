package com.zifang.util.db.context;

import javax.sql.DataSource;

/**
 * 数据源工厂接口，用于创建和获取数据源
 */
public interface DatasourceFactory {

    /**
     * 获取数据源实例
     *
     * @return 数据源对象
     */
    DataSource getDatasource();

}
