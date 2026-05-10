package com.zifang.util.visuallization.swing.manager.subpanels;

import java.util.Objects;

public class UserObject {

    private Integer id;

    private String displayName;

    public UserObject() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserObject that = (UserObject) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName);
    }
}
