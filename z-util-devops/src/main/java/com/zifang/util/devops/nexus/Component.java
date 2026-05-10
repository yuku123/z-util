package com.zifang.util.devops.nexus;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Maven组件
 */
public class Component implements Comparable<Component> {

    private String id;

    private String repository;

    private String group;

    private String name;

    private String version;

    private String format;

    private List<Asset> assets = new ArrayList<>();

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public int compareTo(Component o) {
        return this.version.compareTo(o.getVersion());
    }

    @Override
    public String toString() {
        return "Component{id=" + id + ", repository=" + repository + ", group=" + group + ", name=" + name + ", version=" + version + ", format=" + format + ", assets=" + assets + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(id, component.id) && Objects.equals(repository, component.repository) && Objects.equals(group, component.group) && Objects.equals(name, component.name) && Objects.equals(version, component.version) && Objects.equals(format, component.format) && Objects.equals(assets, component.assets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, repository, group, name, version, format, assets);
    }
}
