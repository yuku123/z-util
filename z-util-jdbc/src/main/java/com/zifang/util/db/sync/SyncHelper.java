package com.zifang.util.db.sync;

import com.zifang.util.db.context.DataSourceManager;

import javax.sql.DataSource;

/**
 * 数据源同步助手，提供数据源之间表结构同步功能
 * 用于在不同的数据源之间同步表结构信息
 */
public class SyncHelper {

    public static void main(String[] args) {

        DataSource dataSource = DataSourceManager.registerDataSource(
                "aaa",
                "rm-bp11g0550d7oq42p9.mysql.rds.aliyuncs.com",
                3306,
                "mdbdemo",
                "mdb",
                "Future1234"
        );

        SqlExecutor sqlExecutor = new SqlExecutor(dataSource);

        System.out.println(sqlExecutor.fetchTableInfo("mdb"));

    }

}
