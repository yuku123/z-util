package com.zifang.util.devops.docker.dto;

import java.util.List;
import java.util.Map;

/**
 * Docker 容器信息
 */
public class ContainerDTO {

    private String id;
    private String name;
    private String image;
    private String imageId;
    private String command;
    private String created;
    private String state;
    private String status;
    private String port;
    private Map<String, String> labels;
    private List<PortMapping> ports;
    private String mount;
    private String network;
    private List<String> networks;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public List<PortMapping> getPorts() {
        return ports;
    }

    public void setPorts(List<PortMapping> ports) {
        this.ports = ports;
    }

    public String getMount() {
        return mount;
    }

    public void setMount(String mount) {
        this.mount = mount;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public List<String> getNetworks() {
        return networks;
    }

    public void setNetworks(List<String> networks) {
        this.networks = networks;
    }

    /**
     * 端口映射
     * <p>
     * 用于表示容器与宿主机之间的端口映射关系
     */
    public static class PortMapping {
        private String hostIp;
        private int hostPort;
        private int containerPort;
        private String protocol;

        public String getHostIp() {
            return hostIp;
        }

        public void setHostIp(String hostIp) {
            this.hostIp = hostIp;
        }

        public int getHostPort() {
            return hostPort;
        }

        public void setHostPort(int hostPort) {
            this.hostPort = hostPort;
        }

        public int getContainerPort() {
            return containerPort;
        }

        public void setContainerPort(int containerPort) {
            this.containerPort = containerPort;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        @Override
        public String toString() {
            return hostPort + ":" + containerPort + "/" + protocol;
        }
    }

    @Override
    public String toString() {
        return "ContainerDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", state='" + state + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
