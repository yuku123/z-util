package com.zifang.util.devops.docker.dto;

import java.util.Map;

/**
 * Docker 网络信息
 */
public class NetworkDTO {

    private String id;
    private String name;
    private String driver;
    private String scope;
    private boolean internal;
    private boolean attachable;
    private String subnet;
    private String gateway;
    private Map<String, String> labels;
    private int containerCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public boolean isAttachable() {
        return attachable;
    }

    public void setAttachable(boolean attachable) {
        this.attachable = attachable;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public int getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(int containerCount) {
        this.containerCount = containerCount;
    }

    @Override
    public String toString() {
        return "NetworkDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", driver='" + driver + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
