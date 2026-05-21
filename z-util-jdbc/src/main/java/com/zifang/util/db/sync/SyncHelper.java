package com.zifang.util.db.sync;

import com.zifang.util.db.context.DataSourceManager;

import javax.sql.DataSource;

/**
 * 数据源同步助手
 * <p>
 * 提供数据源之间表结构同步功能，用于在不同的数据库之间
 * 同步表结构信息，包括表名、字段、索引、约束等元数据。
 *
 * <p>该类主要解决以下场景：
 * <ul>
 *   <li>将表结构从源数据库迁移到目标数据库</li>
 *   <li>在不同数据源之间保持表结构一致性</li>
 *   <li>多数据源环境下的元数据管理</li>
 *   <li>数据库 schema 的版本化管理</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>
 * DataSource sourceDs = DataSourceManager.registerDataSource("source", ...);
 * DataSource targetDs = DataSourceManager.registerDataSource("target", ...);
 * SyncHelper.syncTableStructures(sourceDs, targetDs, "mydb");
 * </pre>
 *
 * @author zifang
 * @see SqlExecutor
 * @see DataSourceManager
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
