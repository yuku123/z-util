package com.zifang.util.db.test0;

import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Table(name = "resource_item")
public class ResourceItem {
    private Long id;
    private String cmsId;
    private String type;
    private int ownerId;
    private Boolean shared;
    private Timestamp createTime;
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCmsId() {
        return cmsId;
    }

    public void setCmsId(String cmsId) {
        this.cmsId = cmsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ResourceItem{id=" + id + ", cmsId=" + cmsId + ", type=" + type + ", ownerId=" + ownerId + ", shared=" + shared + ", createTime=" + createTime + ", updateTime=" + updateTime + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceItem that = (ResourceItem) o;
        return ownerId == that.ownerId &&
                Objects.equals(id, that.id) &&
                Objects.equals(cmsId, that.cmsId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(shared, that.shared) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cmsId, type, ownerId, shared, createTime, updateTime);
    }
}
