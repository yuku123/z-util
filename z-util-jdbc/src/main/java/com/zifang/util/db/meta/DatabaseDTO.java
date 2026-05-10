package com.zifang.util.db.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据库DTO
 */
public class DatabaseDTO {

    private String databaseName;

    private List<TableDTO> tables = new ArrayList<>();

    public DatabaseDTO(String databaseName) {
        this.databaseName = Objects.requireNonNull(databaseName, "数据库名不能为空").trim();
    }

    public void addTable(TableDTO table) {
        if (table != null && !table.getColumns().isEmpty()) {
            tables.add(table);
        }
    }

    public List<TableDTO> getTables() {
        return new ArrayList<>(tables);
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setTables(List<TableDTO> tables) {
        this.tables = tables == null ? new ArrayList<>() : tables;
    }

    @Override
    public String toString() {
        return "DatabaseDTO{databaseName=" + databaseName + ", tables=" + tables + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseDTO that = (DatabaseDTO) o;
        return Objects.equals(databaseName, that.databaseName) &&
                Objects.equals(tables, that.tables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(databaseName, tables);
    }
}
