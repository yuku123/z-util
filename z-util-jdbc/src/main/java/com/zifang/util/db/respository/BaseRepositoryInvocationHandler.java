package com.zifang.util.db.respository;

import com.zifang.util.db.context.DataSourceContext;
import com.zifang.util.db.context.DatasourceContextManager;
import com.zifang.util.db.define.Param;
import com.zifang.util.db.define.Select;
import com.zifang.util.db.sync.SqlExecutor;
import com.zifang.util.db.transation.TranslationManager;

import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 仓储调用处理器，负责处理Repository接口方法的调用
 */
public class BaseRepositoryInvocationHandler implements InvocationHandler {

    private final Class<?> targetClass;

    private SqlExecutor sqlExecutor;

    public BaseRepositoryInvocationHandler(Class<?> clazz) {
        this.targetClass = clazz;
        solve(targetClass);
    }

    private void solve(Class<?> clazz) {
        DataSourceContext dataSourceContext = DatasourceContextManager.fetchContext(DatasourceContextManager.DEFAULT);
        sqlExecutor = new SqlExecutor(dataSourceContext.getDatasourceFactory().getDatasource());
        // 将 datasourceFactory 设置到 transactionManager（如果存在）
        TranslationManager tm = dataSourceContext.getTransactionManager();
        if (tm != null && tm.getDatasourceFactory() == null) {
            tm.setDatasourceFactory(dataSourceContext.getDatasourceFactory());
        }
    }

    /**
     * 获取事务管理器中的连接（支持事务），无事务则返回 null
     */
    private Connection getTransactionConnection() {
        DataSourceContext ctx = DatasourceContextManager.fetchContext(DatasourceContextManager.DEFAULT);
        TranslationManager tm = ctx.getTransactionManager();
        if (tm != null && tm.isActive()) {
            return tm.getConnection();
        }
        return null;
    }

    /**
     * 获取新的数据库连接（不受事务管理）
     */
    private Connection getNewConnection() throws SQLException {
        DataSourceContext ctx = DatasourceContextManager.fetchContext(DatasourceContextManager.DEFAULT);
        return ctx.getDatasourceFactory().getDatasource().getConnection();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> clazz = method.getDeclaringClass();
        if (clazz == CrudRepository.class) {
            return invokeCrud(method, args);
        } else {
            return invokeCustom(method, args);
        }
    }

    private Object invokeCrud(Method method, Object[] args) throws Exception {
        String methodName = method.getName();

        if (methodName.startsWith("save")) {
            return invokeSave(method, args);
        } else if (methodName.startsWith("findById")) {
            return invokeFindById(method, args);
        } else if (methodName.startsWith("existsById")) {
            return invokeExistsById(method, args);
        } else if (methodName.startsWith("findAll")) {
            return invokeFindAll(method, args);
        } else if (methodName.startsWith("count")) {
            return invokeCount(method, args);
        } else if (methodName.startsWith("delete")) {
            return invokeDelete(method, args);
        }
        return null;
    }

    private Object invokeSave(Method method, Object[] args) throws Exception {
        if (args == null || args.length == 0) {
            return null;
        }
        Object entity = args[0];
        Table table = entity.getClass().getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalStateException("实体类未标注 @Table 注解: " + entity.getClass().getName());
        }
        String tableName = table.name();

        Field[] fields = entity.getClass().getDeclaredFields();
        Map<String, Object> fieldMap = new LinkedHashMap<>();
        Field idField = null;
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(entity);
            if ("id".equalsIgnoreCase(field.getName())) {
                idField = field;
            } else {
                fieldMap.put(toUnderline(field.getName()), value);
            }
        }

        Connection connection = getTransactionConnection();
        boolean isOwnConnection = (connection == null);
        if (isOwnConnection) {
            connection = getNewConnection();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            StringBuilder sql = new StringBuilder("INSERT INTO ");
            sql.append(tableName).append(" (");
            StringBuilder placeholders = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                if (i > 0) {
                    sql.append(", ");
                    placeholders.append(", ");
                }
                sql.append(entry.getKey());
                placeholders.append("?");
                i++;
            }
            sql.append(") VALUES (").append(placeholders).append(")");

            ps = connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
            i = 1;
            for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
                ps.setObject(i++, entry.getValue());
            }
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next() && idField != null) {
                idField.setAccessible(true);
                idField.set(entity, rs.getObject(1));
            }
            return entity;
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            if (isOwnConnection) {
                closeQuietly(connection);
            }
        }
    }

    private Object invokeFindById(Method method, Object[] args) throws Exception {
        if (args == null || args.length == 0) {
            return null;
        }
        Object id = args[0];
        Table table = targetClass.getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalStateException("Repository接口未标注 @Table 注解: " + targetClass.getName());
        }
        String tableName = table.name();

        Connection connection = getTransactionConnection();
        boolean isOwnConnection = (connection == null);
        if (isOwnConnection) {
            connection = getNewConnection();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, id);
            rs = ps.executeQuery();

            ResultSetHandler handler = new ResultSetHandler();
            handler.setTargetType(method.getGenericReturnType());
            handler.setResultSet(rs);
            Object result = handler.solve();
            return result;
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            if (isOwnConnection) {
                closeQuietly(connection);
            }
        }
    }

    private Object invokeExistsById(Method method, Object[] args) throws Exception {
        if (args == null || args.length == 0) {
            return false;
        }
        Object id = args[0];
        Table table = targetClass.getAnnotation(Table.class);
        if (table == null) {
            return false;
        }
        String tableName = table.name();

        Connection connection = getTransactionConnection();
        boolean isOwnConnection = (connection == null);
        if (isOwnConnection) {
            connection = getNewConnection();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT 1 FROM " + tableName + " WHERE id = ? LIMIT 1";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, id);
            rs = ps.executeQuery();
            boolean exists = rs.next();
            return exists;
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            if (isOwnConnection) {
                closeQuietly(connection);
            }
        }
    }

    private Object invokeFindAll(Method method, Object[] args) throws Exception {
        Table table = targetClass.getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalStateException("Repository接口未标注 @Table 注解: " + targetClass.getName());
        }
        String tableName = table.name();

        Connection connection = getTransactionConnection();
        boolean isOwnConnection = (connection == null);
        if (isOwnConnection) {
            connection = getNewConnection();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM " + tableName;
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetHandler handler = new ResultSetHandler();
            handler.setTargetType(method.getGenericReturnType());
            handler.setResultSet(rs);
            Object result = handler.solve();
            return result;
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            if (isOwnConnection) {
                closeQuietly(connection);
            }
        }
    }

    private Object invokeCount(Method method, Object[] args) throws Exception {
        Table table = targetClass.getAnnotation(Table.class);
        if (table == null) {
            return 0L;
        }
        String tableName = table.name();

        Connection connection = getTransactionConnection();
        boolean isOwnConnection = (connection == null);
        if (isOwnConnection) {
            connection = getNewConnection();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) FROM " + tableName;
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            long count = 0;
            if (rs.next()) {
                count = rs.getLong(1);
            }
            return count;
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            if (isOwnConnection) {
                closeQuietly(connection);
            }
        }
    }

    private Object invokeDelete(Method method, Object[] args) throws Exception {
        if (args == null || args.length == 0) {
            return null;
        }
        Table table = targetClass.getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalStateException("Repository接口未标注 @Table 注解: " + targetClass.getName());
        }
        String tableName = table.name();

        String methodName = method.getName();
        Connection connection = getTransactionConnection();
        boolean isOwnConnection = (connection == null);
        if (isOwnConnection) {
            connection = getNewConnection();
        }
        PreparedStatement ps = null;
        try {
            if (methodName.equals("deleteById") && args.length == 1) {
                String sql = "DELETE FROM " + tableName + " WHERE id = ?";
                ps = connection.prepareStatement(sql);
                ps.setObject(1, args[0]);
                ps.executeUpdate();
            } else if (methodName.equals("delete") && args.length == 1) {
                Object entity = args[0];
                Field idField = findIdField(entity.getClass());
                if (idField == null) {
                    throw new IllegalStateException("实体类没有id字段: " + entity.getClass().getName());
                }
                idField.setAccessible(true);
                Object id = idField.get(entity);
                String sql = "DELETE FROM " + tableName + " WHERE id = ?";
                ps = connection.prepareStatement(sql);
                ps.setObject(1, id);
                ps.executeUpdate();
            } else if (methodName.equals("deleteAll")) {
                String sql = "DELETE FROM " + tableName;
                ps = connection.prepareStatement(sql);
                ps.executeUpdate();
            }
            return null;
        } finally {
            closeQuietly(ps);
            if (isOwnConnection) {
                closeQuietly(connection);
            }
        }
    }

    private Object invokeCustom(Method method, Object[] args) throws Exception {
        Select select = method.getAnnotation(Select.class);
        if (select == null) {
            throw new IllegalStateException("方法未标注 @Select 注解: " + method.getName());
        }
        String sql = select.value();

        BoundSql boundSql = boundSql(method, args, sql);

        Connection connection = getTransactionConnection();
        boolean isOwnConnection = (connection == null);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (isOwnConnection) {
                connection = getNewConnection();
            }
            ps = connection.prepareStatement(boundSql.getTransformSql());
            for (Map.Entry<Integer, Object> entry : boundSql.getIndexValueInsert().entrySet()) {
                ps.setObject(entry.getKey(), entry.getValue());
            }
            rs = ps.executeQuery();

            ResultSetHandler resultSetHandler = new ResultSetHandler();
            resultSetHandler.setTargetType(method.getGenericReturnType());
            resultSetHandler.setResultSet(rs);

            Object result = resultSetHandler.solve();
            return result;
        } finally {
            closeQuietly(rs);
            closeQuietly(ps);
            if (isOwnConnection) {
                closeQuietly(connection);
            }
        }
    }

    private BoundSql boundSql(Method method, Object[] args, String sql) {
        BoundSql boundSql = new BoundSql();

        String transformSQL = sql;

        Map<Integer, String> indexName = new LinkedHashMap<>();
        Map<Integer, Object> indexValue = new LinkedHashMap<>();

        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j].annotationType() == Param.class) {
                    indexName.put(i, ((Param) annotations[i][j]).value());
                }
            }
        }

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                indexValue.put(i, args[i]);
            }
        }

        Map<Integer, Object> indexValueInsert = new LinkedHashMap<>();
        int paramIndex = 1;
        for (Map.Entry<Integer, String> entry : indexName.entrySet()) {
            if (transformSQL.contains(":" + entry.getValue())) {
                indexValueInsert.put(paramIndex++, indexValue.get(entry.getKey()));
                transformSQL = transformSQL.replace(":" + entry.getValue(), "?");
            }
        }

        boundSql.setOriginSql(sql);
        boundSql.setTransformSql(transformSQL);
        boundSql.setIndexName(indexName);
        boundSql.setIndexValue(indexValue);
        boundSql.setIndexValueInsert(indexValueInsert);

        return boundSql;
    }

    private void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private Field findIdField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if ("id".equalsIgnoreCase(field.getName())) {
                return field;
            }
        }
        return null;
    }

    private String toUnderline(String camel) {
        if (camel == null || camel.isEmpty()) {
            return camel;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camel.length(); i++) {
            char c = camel.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
