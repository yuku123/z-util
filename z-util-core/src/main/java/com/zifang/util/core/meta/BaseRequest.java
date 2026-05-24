package com.zifang.util.core.meta;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base request class with common fields for all request objects.
 *
 * @author zifang
 */
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = -4216068195245037766L;

    /**
     * Unique identifier for the request.
     */
    private String id;

    /**
     * Creation timestamp.
     */
    private LocalDateTime createTime;

    /**
     * Last update timestamp.
     */
    private LocalDateTime updateTime;

    /**
     * Default constructor.
     */
    public BaseRequest() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}