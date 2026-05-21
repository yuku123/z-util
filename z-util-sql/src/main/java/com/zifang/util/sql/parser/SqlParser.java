package com.zifang.util.sql.parser;

import com.zifang.util.dsl.g4.DynamicLexer;
import com.zifang.util.dsl.g4.DynamicParser;
import com.zifang.util.dsl.core.ASTNode;
import com.zifang.util.dsl.token.Token;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL解析器
 * 使用DSL模块的DynamicLexer和DynamicParser进行SQL解析
 */
public class SqlParser {
    
    private static final String SQL_GRAMMAR = 
        "lexer\n" +
        "  Id:        [a-zA-Z_][a-zA-Z0-9_]*\n" +
        "  String:    '\"' (~[\"\\\\r\\\\n])* '\"'\n" +
        "  SQuote:    '\\'' (~['\\\\r\\\\n])* '\\''\n" +
        "  Int:       [0-9]+\n" +
        "  Float:     [0-9]+ '.' [0-9]+\n" +
        "  Op:        '<=' | '>=' | '!=' | '<>' | '=' | '<' | '>' \n" +
        "  KW:        'SELECT' | 'FROM' | 'WHERE' | 'AND' | 'OR' | 'INSERT' | 'INTO' \n" +
        "            | 'VALUES' | 'UPDATE' | 'SET' | 'DELETE' | 'CREATE' | 'TABLE' \n" +
        "            | 'DROP' | 'ORDER' | 'BY' | 'ASC' | 'DESC' | 'LIMIT' | 'OFFSET'\n" +
        "            | 'JOIN' | 'ON' | 'NULL' | 'AS' | 'IN' | 'NOT'\n" +
        "  LParen:    '('\n" +
        "  RParen:    ')'\n" +
        "  Comma:     ','\n" +
        "  Star:      '*'\n" +
        "  Dot:       '.'\n" +
        "  WS:        [ \\\\t\\\\r\\\\n]+\n" +
        "fragment NL: '\\\\n'\n" +
        "\n" +
        "parser\n" +
        "  selectStmt: 'SELECT' selectCols 'FROM' Id joinClause? whereClause? orderByClause? limitClause?\n" +
        "  selectCols: '*' | colList\n" +
        "  colList: Id (',' Id)*\n" +
        "  joinClause: ('JOIN' Id 'ON' Id Op Id)\n" +
        "  whereClause: 'WHERE' Id Op value\n" +
        "  orderByClause: 'ORDER' 'BY' Id ('ASC' | 'DESC')?\n" +
        "  limitClause: 'LIMIT' Int ('OFFSET' Int)?\n" +
        "  insertStmt: 'INSERT' 'INTO' Id '(' colList ')' 'VALUES' '(' valueList ')'\n" +
        "  valueList: value (',' value)*\n" +
        "  value: String | SQuote | Int | Float | 'NULL'\n" +
        "  updateStmt: 'UPDATE' Id 'SET' Id '=' value whereClause?\n" +
        "  deleteStmt: 'DELETE' 'FROM' Id whereClause?\n" +
        "  createStmt: 'CREATE' 'TABLE' Id '(' colTypeList ')'\n" +
        "  colTypeList: Id typeName (',' Id typeName)*\n" +
        "  typeName: 'INT' | 'STRING' | 'LONG' | 'DOUBLE' | 'BOOLEAN' | 'FLOAT'\n" +
        "  dropStmt: 'DROP' 'TABLE' Id\n";
    
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
        
        // SELECT * FROM table WHERE ...
        // SELECT col1, col2 FROM table WHERE ...
        
        Pattern pattern = Pattern.compile(
            "SELECT\\s+(\\*|[a-zA-Z_][a-zA-Z0-9_]*(?:\\s*,\\s*[a-zA-Z_][a-zA-Z0-9_]*)*)" +
            "\\s+FROM\\s+([a-zA-Z_][a-zA-Z0-9_]*)" +
            "(?:\\s+JOIN\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+ON\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*([a-zA-Z_][a-zA-Z0-9_]*))?" +
            "(?:\\s+WHERE\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+))?" +
            "(?:\\s+ORDER\\s+BY\\s+([a-zA-Z_][a-zA-Z0-9_]*)(?:\\s+(ASC|DESC))?)?" +
            "(?:\\s+LIMIT\\s+(\\d+)(?:\\s+OFFSET\\s+(\\d+))?)?",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            String cols = matcher.group(1).trim();
            if ("*".equals(cols)) {
                stmt.addSelectColumn("*");
            } else {
                for (String col : cols.split("\\s*,\\s*")) {
                    stmt.addSelectColumn(col.trim());
                }
            }
            stmt.setTableName(matcher.group(2));
            
            if (matcher.group(3) != null) {
                stmt.setJoinTable(matcher.group(3));
                stmt.setJoinCondition(matcher.group(4) + " " + matcher.group(5) + " " + matcher.group(6));
            }
            
            if (matcher.group(7) != null) {
                stmt.setWhereColumn(matcher.group(7));
                String op = matcher.group(8);
                String val = matcher.group(9);
                stmt.setWhereValue(parseValue(val, op));
            }
            
            if (matcher.group(10) != null) {
                stmt.setOrderByColumn(matcher.group(10));
                String dir = matcher.group(11);
                stmt.setOrderByDesc("DESC".equalsIgnoreCase(dir));
            }
            
            if (matcher.group(12) != null) {
                stmt.setLimit(Integer.parseInt(matcher.group(12)));
            }
            if (matcher.group(13) != null) {
                stmt.setOffset(Integer.parseInt(matcher.group(13)));
            }
        }
    }
    
    private void parseInsert(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.INSERT);
        
        // INSERT INTO table (col1, col2) VALUES (val1, val2)
        Pattern pattern = Pattern.compile(
            "INSERT\\s+INTO\\s+([a-zA-Z_][a-zA-Z0-9_]*)" +
            "\\s*\\(([^)]+)\\)\\s*VALUES\\s*\\(([^)]+)\\)",
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
            "UPDATE\\s+([a-zA-Z_][a-zA-Z0-9_]*)" +
            "\\s+SET\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+)" +
            "(?:\\s+WHERE\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+))?",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            stmt.setTableName(matcher.group(1));
            stmt.setUpdateColumn(matcher.group(2));
            stmt.setUpdateValue(parseValue(matcher.group(3), "="));
            
            if (matcher.group(4) != null) {
                stmt.setWhereColumn(matcher.group(4));
                stmt.setWhereValue(parseValue(matcher.group(6), matcher.group(5)));
            }
        }
    }
    
    private void parseDelete(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.DELETE);
        
        // DELETE FROM table WHERE ...
        Pattern pattern = Pattern.compile(
            "DELETE\\s+FROM\\s+([a-zA-Z_][a-zA-Z0-9_]*)" +
            "(?:\\s+WHERE\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*([<>=!]+)\\s*('(?:[^'\\\\]|\\\\.)*'|\"(?:[^\"\\\\]|\\\\.)*\"|[^\\s,]+))?",
            Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            stmt.setTableName(matcher.group(1));
            
            if (matcher.group(2) != null) {
                stmt.setWhereColumn(matcher.group(2));
                stmt.setWhereValue(parseValue(matcher.group(4), matcher.group(3)));
            }
        }
    }
    
    private void parseCreate(String sql, SqlStatement stmt) {
        stmt.setType(SqlStatementType.CREATE_TABLE);
        
        // CREATE TABLE table (col1 INT, col2 STRING, ...)
        Pattern pattern = Pattern.compile(
            "CREATE\\s+TABLE\\s+([a-zA-Z_][a-zA-Z0-9_]*)" +
            "\\s*\\(([^)]+)\\)",
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
