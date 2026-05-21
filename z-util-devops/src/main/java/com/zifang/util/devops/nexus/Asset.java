package com.zifang.util.devops.nexus;


/**
 * Nexus 仓库中的资产（Asset）实体类
 * <p>
 * 代表 Nexus 组件库中的一个文件资产，包含资产的路径、下载地址、校验和等信息。
 *
 * @author zifang
 * @version 1.0.0
 */
public class Asset {

    private String id;

    private String repository;

    private String path;

    private String downloadUrl;

    private Checksum checksum;

    private String format;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Checksum getChecksum() {
        return checksum;
    }

    public void setChecksum(Checksum checksum) {
        this.checksum = checksum;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return "Asset{id=" + id + ", repository=" + repository + ", path=" + path + ", downloadUrl=" + downloadUrl + ", checksum=" + checksum + ", format=" + format + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return java.util.Objects.equals(id, asset.id) && java.util.Objects.equals(repository, asset.repository) && java.util.Objects.equals(path, asset.path) && java.util.Objects.equals(downloadUrl, asset.downloadUrl) && java.util.Objects.equals(checksum, asset.checksum) && java.util.Objects.equals(format, asset.format);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, repository, path, downloadUrl, checksum, format);
    }
}
