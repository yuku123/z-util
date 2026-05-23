package com.zifang.util.db.respository;

import com.zifang.util.core.lang.converter.Converters;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 结果集处理器，负责将JDBC ResultSet转换为Java对象
 */
public class ResultSetHandler {

    private Type targetType;

    private ResultSet resultSet;

    private ResultSetMetaData resultSetMetaData;

    /**
     * 处理ResultSet并转换为目标类型
     *
     * @return 转换后的对象，可能是List、Map或单个实体
     * @throws InstantiationException 实例化实体失败
     * @throws IllegalAccessException 访问字段失败
     * @throws SQLException           数据库访问异常
     */
    public Object solve() throws InstantiationException, IllegalAccessException, SQLException {

        resultSetMetaData = resultSet.getMetaData();

        if (targetType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) targetType;
            if (pt.getRawType() == List.class) {
                Type elementType = pt.getActualTypeArguments()[0];
                if (elementType instanceof ParameterizedType) {
                    ParameterizedType elementPt = (ParameterizedType) elementType;
                    if (elementPt.getRawType() == Map.class) {
                        List<Map<String, Object>> list = fetch(resultSet);
                        return list;
                    }
                } else {
                    Class<?> clazz = (Class<?>) elementType;
                    List<Map<String, Object>> list = fetch(resultSet);
                    List<Object> o = transformBatch(list, clazz);
                    return o;
                }
            } else if (pt.getRawType() == Map.class) {
                List<Map<String, Object>> list = fetch(resultSet);
                if (list.size() > 1) {
                    throw new RuntimeException("rows > 1");
                } else if (list.size() == 0) {
                    return null;
                } else {
                    return list.get(0);
                }
            }
        } else {
            List<Map<String, Object>> list = fetch(resultSet);
            if (list.size() > 1) {
                throw new RuntimeException("rows > 1");
            } else if (list.size() == 0) {
                return null;
            } else {
                Class<?> clazz = (Class<?>) targetType;
                Map<String, Object> map = list.get(0);
                return transform(map, clazz);
            }
        }
        return null;
    }

    private List<Object> transformBatch(List<Map<String, Object>> list, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        List<Object> objects = new ArrayList<>();
        for (Map<String, Object> map : list) {
            Object o = transform(map, clazz);
            objects.add(o);
        }
        return objects;
    }

    private Object transform(Map<String, Object> map, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        Object o;
        try {
            o = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("无法实例化: " + clazz.getName(), e);
        }
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> columnMap = new LinkedHashMap<>();
        for (Field field : fields) {
            columnMap.put(handleColumn(field), field);
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (columnMap.get(entry.getKey()) != null) {
                Field field = columnMap.get(entry.getKey());
                field.setAccessible(true);

                Object target = Converters.to(entry.getValue(), field.getType());

                field.set(o, target);
            }
        }
        return o;
    }

    private static String handleColumn(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column == null) {
            StringBuilder convertName = new StringBuilder();
            for (int i = 0; i < field.getName().length(); i++) {
                if (isUpperCase(field.getName().charAt(i))) {
                    convertName.append("_");
                }
                convertName.append(field.getName().charAt(i));
            }
            return convertName.toString().toLowerCase();
        } else {
            return column.name();
        }
    }

    private static boolean isUpperCase(char c) {
        return c >= 'A' && c <= 'Z';
    }

    private List<Map<String, Object>> fetch(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> li = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> map = new LinkedHashMap<>();
            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                map.put(resultSetMetaData.getColumnName(i), resultSet.getObject(i));
            }
            li.add(map);
        }
        return li;
    }

    public Type getTargetType() {
        return targetType;
    }

    public void setTargetType(Type targetType) {
        this.targetType = targetType;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public ResultSetMetaData getResultSetMetaData() {
        return resultSetMetaData;
    }

    public void setResultSetMetaData(ResultSetMetaData resultSetMetaData) {
        this.resultSetMetaData = resultSetMetaData;
    }

    @Override
    public String toString() {
        return "ResultSetHandler{targetType=" + targetType + ", resultSet=" + resultSet + ", resultSetMetaData=" + resultSetMetaData + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultSetHandler that = (ResultSetHandler) o;
        return Objects.equals(targetType, that.targetType) &&
                Objects.equals(resultSet, that.resultSet) &&
                Objects.equals(resultSetMetaData, that.resultSetMetaData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType, resultSet, resultSetMetaData);
    }
}
