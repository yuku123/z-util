package com.zifang.util.sql.parser;

/**
 * SQL语句类型
 */
public enum SqlStatementType {
    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    CREATE_TABLE,
    DROP_TABLE,
    UNKNOWN
}
