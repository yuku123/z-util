package com.zifang.util.sql.parser;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL解析器
 * 使用正则表达式进行SQL解析
 */
public class SqlParser {
    
    public SqlParser() {
    }
    
    /**
     * 解析SQL语句
     */
    public SqlStatement parse(String sql) {
        // 去除多余空白
        sql = sql.trim();
        
        // 简单解析：先判断语句类型
        String upperSql = sql.toUpperCase();
        SqlStatement stmt = new SqlStatement();
        
        if (upperSql.startsWith("SELECT")) {
            parseSelect(sql, stmt);
        } else if (upperSql.startsWith("INSERT")) {
            parseInsert(sql, stmt);
        } else if (upperSql.startsWith("UPDATE")) {
            parseUpdate(sql, stmt);
        } else if (upperSql.startsWith("DELETE")) {
            parseDelete(sql, stmt);
        } else if (upperSql.startsWith("CREATE")) {
            parseCreate(sql, stmt);
        } else if (upperSql.startsWith("DROP")) {
            parseDrop(sql, stmt);
        } else {
            stmt.setType(SqlStatementType.UNKNOWN);
        }
        
        return stmt;
    }
    
    private void parseSelect(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.SELECT);
        
        // SELECT * FROM table WHERE ... JOIN table2 ON ...
        // SELECT col1, col2 FROM table WHERE ...
        
        // 分离JOIN子句（如果有的话）
        String joinCondition = null;
        String joinTable = null;
        boolean isLeftJoin = false;
        String mainSql = sql;
        
        // 尝试匹配 LEFT JOIN
        Pattern leftJoinPattern = Pattern.compile(
            "(.+?)\\s+LEFT\\s+JOIN\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+ON\\s+(.+)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher leftJoinMatcher = leftJoinPattern.matcher(sql);
        if (leftJoinMatcher.find()) {
            mainSql = leftJoinMatcher.group(1).trim();
            joinTable = leftJoinMatcher.group(2);
            joinCondition = leftJoinMatcher.group(3).trim();
            isLeftJoin = true;
        } else {
            // 尝试匹配普通 JOIN
            Pattern joinPattern = Pattern.compile(
                "(.+?)\\s+JOIN\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+ON\\s+(.+)",
                Pattern.CASE_INSENSITIVE
            );
            Matcher joinMatcher = joinPattern.matcher(sql);
            if (joinMatcher.find()) {
                mainSql = joinMatcher.group(1).trim();
                joinTable = joinMatcher.group(2);
                joinCondition = joinMatcher.group(3).trim();
                isLeftJoin = false;
            }
        }
        
        // 解析主查询部分
        // 首先尝试提取列
        Pattern colPattern = Pattern.compile(
            "SELECT\\s+(\\*|[a-zA-Z_][a-zA-Z0-9_]*(?:\\.[a-zA-Z_][a-zA-Z0-9_]*)?(?:\\s*,\\s*[a-zA-Z_][a-zA-Z0-9_]*(?:\\.[a-zA-Z_][a-zA-Z0-9_]*)?)*|COUNT\\s*\\(\\s*\\*\\s*\\))\\s+FROM\\s+([a-zA-Z_][a-zA-Z0-9_]*)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher colMatcher = colPattern.matcher(mainSql);
        if (colMatcher.find()) {
            String cols = colMatcher.group(1).trim();
            if ("*".equals(cols) || "COUNT(*)".equalsIgnoreCase(cols)) {
                stmt.addSelectColumn("*");
            } else {
                for (String col : cols.split("\\s*,\\s*")) {
                    stmt.addSelectColumn(col.trim());
                }
            }
            stmt.setTableName(colMatcher.group(2));
        }
        
        // 解析WHERE、ORDER BY、LIMIT
        String wherePart = null;
        String orderPart = null;
        String limitPart = null;
        
        // 提取WHERE
        Pattern wherePattern = Pattern.compile(
            "WHERE\\s+(.+?)(?:\\s+ORDER\\s+BY|\\s+LIMIT|\\s+OFFSET|$)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher whereMatcher = wherePattern.matcher(mainSql);
        if (whereMatcher.find()) {
            wherePart = whereMatcher.group(1).trim();
            if (wherePart != null && wherePart.length() == 0) wherePart = null;
        }
        
        // 提取ORDER BY
        Pattern orderPattern = Pattern.compile(
            "ORDER\\s+BY\\s+([a-zA-Z_][a-zA-Z0-9_]*)(?:\\s+(ASC|DESC))?",
            Pattern.CASE_INSENSITIVE
        );
        Matcher orderMatcher = orderPattern.matcher(mainSql);
        if (orderMatcher.find()) {
            stmt.setOrderByColumn(orderMatcher.group(1));
            stmt.setOrderByDesc("DESC".equalsIgnoreCase(orderMatcher.group(2)));
        }
        
        // 提取LIMIT
        Pattern limitPattern = Pattern.compile(
            "LIMIT\\s+(\\d+)(?:\\s+OFFSET\\s+(\\d+))?",
            Pattern.CASE_INSENSITIVE
        );
        Matcher limitMatcher = limitPattern.matcher(mainSql);
        if (limitMatcher.find()) {
            stmt.setLimit(Integer.parseInt(limitMatcher.group(1)));
            if (limitMatcher.group(2) != null) {
                stmt.setOffset(Integer.parseInt(limitMatcher.group(2)));
            }
        }
        
        // 解析WHERE条件
        if (wherePart != null && !wherePart.isEmpty()) {
            parseWhereCondition(wherePart, stmt);
        }
        
        // 解析JOIN
        if (joinTable != null && joinCondition != null) {
            stmt.setJoinTable(joinTable);
            stmt.setLeftJoin(isLeftJoin);
            parseJoinCondition(joinCondition, stmt);
        }
    }
    
    private void parseWhereCondition(String wherePart, SqlStatement stmt) {
        // 匹配: column op value
        // op 可以是: =, !=, <>, <, >, <=, >=
        Pattern pattern = Pattern.compile(
            "([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(wherePart.trim());
        if (matcher.find()) {
            stmt.setWhereColumn(matcher.group(1));
            stmt.setWhereOp(normalizeOp(matcher.group(2)));
            stmt.setWhereValue(parseValue(matcher.group(3), matcher.group(2)));
        }
    }
    
    private void parseJoinCondition(String joinCondition, SqlStatement stmt) {
        // 解析 ON a.x = b.y 格式
        // 支持: table1.column = table2.column 或者 column = column
        Pattern pattern = Pattern.compile(
            "([a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*([a-zA-Z_][a-zA-Z0-9_]*\\.[a-zA-Z_][a-zA-Z0-9_]*)",
            Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(joinCondition.trim());
        if (matcher.find()) {
            String leftCol = matcher.group(1);
            stmt.setJoinOp(normalizeOp(matcher.group(2)));
            String rightCol = matcher.group(3);
            
            // 分割 table.column
            String[] leftParts = leftCol.split("\\.");
            String[] rightParts = rightCol.split("\\.");
            
            // 根据左右表确定哪边是主表的列
            String mainTableName = stmt.getTableName().toLowerCase();
            String joinTableName = stmt.getJoinTable().toLowerCase();
            
            if (leftParts[0].toLowerCase().equals(mainTableName)) {
                stmt.setJoinLeftColumn(leftParts[1]);
                stmt.setJoinRightColumn(rightParts[1]);
            } else {
                stmt.setJoinLeftColumn(rightParts[1]);
                stmt.setJoinRightColumn(leftParts[1]);
            }
        } else {
            // 简化格式: ON column = column
            Pattern simplePattern = Pattern.compile(
                "([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*([a-zA-Z_][a-zA-Z0-9_]*)",
                Pattern.CASE_INSENSITIVE
            );
            Matcher simpleMatcher = simplePattern.matcher(joinCondition.trim());
            if (simpleMatcher.find()) {
                stmt.setJoinLeftColumn(simpleMatcher.group(1));
                stmt.setJoinOp(normalizeOp(simpleMatcher.group(2)));
                stmt.setJoinRightColumn(simpleMatcher.group(3));
            }
        }
    }
    
    private String normalizeOp(String op) {
        if ("<>".equals(op)) return "!=";
        return op;
    }
    
    private void parseInsert(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.INSERT);
        
        // INSERT INTO table (col1, col2) VALUES (val1, val2)
        Pattern pattern = Pattern.compile(
            "INSERT\\s+INTO\\s+([a-zA-Z_][a-zA-Z0-_]*)\\s*\\(([^)]+)\\)\\s*VALUES\\s*\\(([^)]+)\\)",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            stmt.setTableName(matcher.group(1));
            
            String cols = matcher.group(2);
            for (String col : cols.split("\\s*,\\s*")) {
                stmt.addColumn(col.trim());
            }
            
            String vals = matcher.group(3);
            for (String val : vals.split("\\s*,\\s*")) {
                stmt.addValue(val.trim());
            }
        }
    }
    
    private void parseUpdate(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.UPDATE);
        
        // UPDATE table SET col = value WHERE ...
        Pattern pattern = Pattern.compile(
            "UPDATE\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+SET\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+)",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            stmt.setTableName(matcher.group(1));
            stmt.setUpdateColumn(matcher.group(2));
            stmt.setUpdateValue(parseValue(matcher.group(3), "="));
            
            // 查找WHERE子句
            Pattern wherePattern = Pattern.compile(
                "WHERE\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+)",
                Pattern.CASE_INSENSITIVE
            );
            Matcher whereMatcher = wherePattern.matcher(sql);
            if (whereMatcher.find()) {
                stmt.setWhereColumn(whereMatcher.group(1));
                stmt.setWhereOp(normalizeOp(whereMatcher.group(2)));
                stmt.setWhereValue(parseValue(whereMatcher.group(3), whereMatcher.group(2)));
            }
        }
    }
    
    private void parseDelete(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.DELETE);
        
        // DELETE FROM table WHERE ...
        Pattern pattern = Pattern.compile(
            "DELETE\\s+FROM\\s+([a-zA-Z_][a-zA-Z0-9_]*)",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            stmt.setTableName(matcher.group(1));
            
            // 查找WHERE子句
            Pattern wherePattern = Pattern.compile(
                "WHERE\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+)",
                Pattern.CASE_INSENSITIVE
            );
            Matcher whereMatcher = wherePattern.matcher(sql);
            if (whereMatcher.find()) {
                stmt.setWhereColumn(whereMatcher.group(1));
                stmt.setWhereOp(normalizeOp(whereMatcher.group(2)));
                stmt.setWhereValue(parseValue(whereMatcher.group(3), whereMatcher.group(2)));
            }
        }
    }
    
    private void parseCreate(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.CREATE_TABLE);
        
        // CREATE TABLE table (col1 INT, col2 STRING, ...)
        Pattern pattern = Pattern.compile(
            "CREATE\\s+TABLE\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(([^)]+)\\)",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            stmt.setTableName(matcher.group(1));
            
            String cols = matcher.group(2);
            Pattern colPattern = Pattern.compile(
                "([a-zA-Z_][a-zA-Z0-9_]*)\\s+(INT|STRING|LONG|DOUBLE|BOOLEAN|FLOAT)",
                Pattern.CASE_INSENSITIVE
            );
            
            Matcher colMatcher = colPattern.matcher(cols);
            while (colMatcher.find()) {
                String colName = colMatcher.group(1);
                String typeName = colMatcher.group(2).toUpperCase();
                Class<?> type = parseType(typeName);
                stmt.addColumnType(colName, type);
            }
        }
    }
    
    private void parseDrop(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.DROP_TABLE);
        
        // DROP TABLE table
        Pattern pattern = Pattern.compile(
            "DROP\\s+TABLE\\s+([a-zA-Z_][a-zA-Z0-9_]*)",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            stmt.setTableName(matcher.group(1));
        }
    }
    
    /**
     * 解析值
     */
    private Object parseValue(String val, String op) {
        if (val == null) return null;
        val = val.trim();
        
        // NULL
        if ("NULL".equalsIgnoreCase(val)) {
            return null;
        }
        
        // 字符串
        if ((val.startsWith("'") && val.endsWith("'")) || 
            (val.startsWith("\"") && val.endsWith("\""))) {
            String str = val.substring(1, val.length() - 1);
            // 处理转义字符
            str = str.replace("\\'", "'");
            str = str.replace("\\\"", "\"");
            str = str.replace("\\\\", "\\");
            return str;
        }
        
        // 数字
        if (val.matches("-?\\d+\\.\\d+")) {
            return Double.parseDouble(val);
        }
        if (val.matches("-?\\d+")) {
            long l = Long.parseLong(val);
            if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
                return (int) l;
            }
            return l;
        }
        
        // 布尔
        if ("TRUE".equalsIgnoreCase(val)) return true;
        if ("FALSE".equalsIgnoreCase(val)) return false;
        
        // 尝试作为数字解析
        try {
            if (val.contains(".")) {
                return Double.parseDouble(val);
            } else {
                return Long.parseLong(val);
            }
        } catch (NumberFormatException e) {
            // 返回原字符串
            return val;
        }
    }
    
    private Class<?> parseType(String typeName) {
        switch (typeName) {
            case "INT":
                return Integer.class;
            case "LONG":
                return Long.class;
            case "DOUBLE":
                return Double.class;
            case "FLOAT":
                return Float.class;
            case "BOOLEAN":
                return Boolean.class;
            case "STRING":
            default:
                return String.class;
        }
    }
}
