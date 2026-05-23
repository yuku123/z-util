package com.zifang.util.db.sync;

import com.zifang.util.core.lang.exception.BusinessException;
import com.zifang.util.core.meta.BaseStatusCode;
import com.zifang.util.db.meta.DataSourceTableColumnDTO;
import com.zifang.util.db.meta.DataSourceTableDTO;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SQL执行器，提供数据库表的创建、修改、查询等操作
 *
 * @author zifang
 */
public class SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);

    private final DataSource dataSource;

    /**
     * 使用指定数据源构造SqlExecutor
     *
     * @param dataSource 数据源，不能为null
     */
    public SqlExecutor(DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "dataSource不能为null");
    }

    /**
     * 获取指定schema下的所有表信息
     *
     * @param schemaMark 数据库schema名称，用于过滤表
     * @return 表信息列表，包含表名和备注
     * @throws BusinessException 获取表信息失败时抛出
     */
    public List<DataSourceTableDTO> fetchTableInfo(String schemaMark) {
        List<DataSourceTableDTO> tableList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            ResultSet resultSet = conn.getMetaData().getTables(schemaMark, "%", "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                DataSourceTableDTO dto = new DataSourceTableDTO();
                dto.setTableName(resultSet.getString("TABLE_NAME"));
                dto.setDescriptions(resultSet.getString("REMARKS"));
                tableList.add(dto);
            }
            return tableList;
        } catch (SQLException e) {
            log.error("获取表信息出错, schema={}", schemaMark, e);
            throw new BusinessException(BaseStatusCode.FAIL, "获取表信息出错");
        }
    }

    /**
     * 获取指定表的字段信息
     *
     * @param schemaMark 数据库schema名称
     * @param tableName  表名称
     * @return 字段信息列表，包含字段名、类型、备注等
     * @throws BusinessException 获取字段信息失败时抛出
     */
    public List<DataSourceTableColumnDTO> fetchTableColumnInfo(String schemaMark, String tableName) {
        List<DataSourceTableColumnDTO> columnList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            ResultSet resultSet = conn.getMetaData().getColumns(schemaMark, "%", tableName, null);
            while (resultSet.next()) {
                DataSourceTableColumnDTO dto = new DataSourceTableColumnDTO();
                dto.setColumnName(resultSet.getString("COLUMN_NAME"));
                dto.setColumnType(resultSet.getString("TYPE_NAME").toLowerCase());
                dto.setColumnComment(resultSet.getString("REMARKS"));
                columnList.add(dto);
            }
            return columnList;
        } catch (SQLException e) {
            log.error("获取表字段信息出错, schema={}, table={}", schemaMark, tableName, e);
            throw new BusinessException(BaseStatusCode.FAIL, "获取表字段信息出错");
        }
    }

    /**
     * 创建表，默认包含一个id主键字段
     *
     * @param tableName    表名，不能为空
     * @param descriptions 表描述/备注
     * @throws BusinessException 创建失败时抛出
     */
    public void createTable(String tableName, String descriptions) {
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s (id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键') COMMENT='%s' ENGINE=InnoDB DEFAULT CHARSET=utf8",
                tableName,
                descriptions
        );
        executeDML(sql);
    }

    /**
     * 为表添加新字段
     *
     * @param tableName     表名
     * @param columnName    新字段名
     * @param columnType    字段类型，如varchar(255)、int等
     * @param columnComment 字段备注/注释
     * @throws BusinessException 添加失败时抛出
     */
    public void createTableColumn(String tableName, String columnName, String columnType, String columnComment) {
        String sql = String.format("ALTER TABLE %s ADD COLUMN %s %s COMMENT '%s'",
                tableName,
                columnName,
                columnType,
                columnComment
        );
        executeDML(sql);
    }

    /**
     * 修改表字段（可修改字段名、类型、备注）
     *
     * @param tableName            表名
     * @param columnName           原字段名
     * @param targetColumnName     目标字段名
     * @param targetColumnType     目标字段类型
     * @param targetColumnComment  目标字段备注
     * @throws BusinessException 修改失败时抛出
     */
    public void updateTableColumn(String tableName, String columnName, String targetColumnName, String targetColumnType, String targetColumnComment) {
        String sql = String.format("ALTER TABLE %s CHANGE %s %s %s COMMENT '%s'",
                tableName,
                columnName,
                targetColumnName,
                targetColumnType,
                targetColumnComment
        );
        executeDML(sql);
    }

    /**
     * 删除表字段
     *
     * @param tableName  表名
     * @param columnName 要删除的字段名
     * @throws BusinessException 删除失败时抛出
     */
    public void removeTableColumn(String tableName, String columnName) {
        String sql = String.format("ALTER TABLE %s DROP COLUMN %s", tableName, columnName);
        executeDML(sql);
    }

    /**
     * 修改表名和表备注
     *
     * @param tableName           原表名
     * @param targetTableName     目标表名
     * @param targetTableComments 目标表备注
     * @throws BusinessException 修改失败时抛出
     */
    public void updateTable(String tableName, String targetTableName, String targetTableComments) {
        executeDML(String.format("ALTER TABLE %s RENAME TO %s", tableName, targetTableName));
        executeDML(String.format("ALTER TABLE %s COMMENT '%s'", targetTableName, targetTableComments));
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     * @throws BusinessException 删除失败时抛出
     */
    public void removeTable(String tableName) {
        String sql = String.format("DROP TABLE IF EXISTS %s", tableName);
        executeDML(sql);
    }

    /**
     * 获取指定schema下所有表的详细信息，包含每个表的字段列表
     *
     * @param schemaMark 数据库schema名称
     * @return 表信息列表，每条表信息包含字段详情
     * @throws BusinessException 获取表信息失败时抛出
     */
    public List<DataSourceTableDTO> deepFetchTableInfo(String schemaMark) {
        List<DataSourceTableDTO> tableList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            ResultSet tableResultSet = conn.getMetaData().getTables(schemaMark, "%", "%", new String[]{"TABLE"});
            while (tableResultSet.next()) {
                DataSourceTableDTO dto = new DataSourceTableDTO();
                dto.setTableName(tableResultSet.getString("TABLE_NAME"));
                dto.setDescriptions(tableResultSet.getString("REMARKS"));
                tableList.add(dto);
            }

            ResultSet columnResultSet = conn.getMetaData().getColumns(schemaMark, "%", "%", null);
            List<DataSourceTableColumnDTO> columnList = new ArrayList<>();
            while (columnResultSet.next()) {
                DataSourceTableColumnDTO dto = new DataSourceTableColumnDTO();
                dto.setTableName(columnResultSet.getString("TABLE_NAME"));
                dto.setColumnName(columnResultSet.getString("COLUMN_NAME"));
                dto.setColumnType(columnResultSet.getString("TYPE_NAME"));
                dto.setColumnComment(columnResultSet.getString("REMARKS"));
                columnList.add(dto);
            }

            Map<String, List<DataSourceTableColumnDTO>> columnMap = new java.util.LinkedHashMap<>();
            for (DataSourceTableColumnDTO col : columnList) {
                columnMap.computeIfAbsent(col.getTableName(), k -> new ArrayList<>()).add(col);
            }
            for (DataSourceTableDTO table : tableList) {
                table.setColumns(columnMap.get(table.getTableName()));
            }
            return tableList;
        } catch (SQLException e) {
            log.error("获取表详细信息出错, schema={}", schemaMark, e);
            throw new BusinessException(BaseStatusCode.FAIL, "获取表详细信息出错");
        }
    }

    /**
     * 执行DML语句（INSERT、UPDATE、DELETE）
     *
     * @param sql 要执行的DML语句
     * @throws BusinessException 执行失败时抛出
     */
    public void executeDML(String sql) {
        try (Connection connection = dataSource.getConnection();
             Statement smt = connection.createStatement()) {
            log.info("execute dml sql: {}", sql);
            smt.executeUpdate(sql);
        } catch (SQLException e) {
            log.error("执行DML失败: sql={}, error={}", sql, e.getMessage(), e);
            throw new BusinessException(BaseStatusCode.FAIL, "执行数据库操作失败");
        }
    }

    /**
     * 执行SELECT查询语句，返回多条记录
     *
     * @param sql SELECT查询语句
     * @return 查询结果列表，每条记录为Map结构
     * @throws BusinessException 查询失败时抛出
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectList(String sql) {
        try (Connection connection = dataSource.getConnection()) {
            QueryRunner qr = new QueryRunner();
            List<Map<String, Object>> result = (List<Map<String, Object>>) (List<?>) qr.query(connection, sql, new MapListHandler());
            return result == null ? new ArrayList<>() : result;
        } catch (SQLException e) {
            log.error("数据库查询异常: sql={}, error={}", sql, e.getMessage(), e);
            throw new BusinessException(BaseStatusCode.FAIL, "数据库查询异常");
        }
    }

    /**
     * 执行COUNT查询，返回记录数
     *
     * @param sqlCnt COUNT查询语句
     * @return 记录数，查询失败返回null
     */
    public Long count(String sqlCnt) {
        try (Connection connection = dataSource.getConnection()) {
            QueryRunner qr = new QueryRunner();
            Object result = qr.query(connection, sqlCnt, new ScalarHandler());
            if (result == null) {
                return 0L;
            }
            if (result instanceof Number) {
                return ((Number) result).longValue();
            }
            return Long.parseLong(result.toString());
        } catch (SQLException e) {
            log.error("COUNT查询异常: sql={}, error={}", sqlCnt, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取当前SqlExecutor使用的数据源
     *
     * @return 数据源对象
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String toString() {
        return "SqlExecutor{dataSource=" + dataSource + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlExecutor that = (SqlExecutor) o;
        return Objects.equals(dataSource, that.dataSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataSource);
    }
}
