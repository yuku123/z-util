package com.zifang.util.devops.docker.dto;

import java.util.List;
import java.util.Map;

/**
 * Docker 镜像信息
 * <p>
 * 用于封装Docker镜像（Image）的完整元数据信息，
 * 包括镜像ID、仓库名、标签、创建时间、大小、标签列表等属性。
 *
 * @author zifang
 * @version 1.0.0
 */
public class ImageDTO {

    private String id;
    private String repository;
    private String tag;
    private String created;
    private String size;
    private long sizeBytes;
    private Map<String, String> labels;
    private List<String> repoTags;
    private List<String> ports;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public List<String> getRepoTags() {
        return repoTags;
    }

    public void setRepoTags(List<String> repoTags) {
        this.repoTags = repoTags;
    }

    public List<String> getPorts() {
        return ports;
    }

    public void setPorts(List<String> ports) {
        this.ports = ports;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "id='" + id + '\'' +
                ", repository='" + repository + '\'' +
                ", tag='" + tag + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
