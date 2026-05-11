package com.zifang.util.devops.docker.dto;

import java.util.Map;

/**
 * Docker 卷信息
 */
public class VolumeDTO {

    private String name;
    private String driver;
    private String mountpoint;
    private String scope;
    private Map<String, String> labels;
    private String created;
    private boolean readonly;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public String toString() {
        return "VolumeDTO{" +
                "name='" + name + '\'' +
                ", driver='" + driver + '\'' +
                ", mountpoint='" + mountpoint + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
