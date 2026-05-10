package com.zifang.util.workflow.config;

import java.util.List;

/**
 * 描述此节点与其他节点的关联情况
 */
public class Connector {

    /**
     * 前置节点列表
     */
    private List<String> pre;

    /**
     * 后置节点列表
     */
    private List<String> post;

    public Connector() {
    }

    public Connector(List<String> pre, List<String> post) {
        this.pre = pre;
        this.post = post;
    }

    public List<String> getPre() {
        return pre;
    }

    public void setPre(List<String> pre) {
        this.pre = pre;
    }

    public List<String> getPost() {
        return post;
    }

    public void setPost(List<String> post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Connector{pre=" + pre + ", post=" + post + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connector connector = (Connector) o;
        if (pre != null ? !pre.equals(connector.pre) : connector.pre != null) return false;
        return post != null ? post.equals(connector.post) : connector.post == null;
    }

    @Override
    public int hashCode() {
        int result = pre != null ? pre.hashCode() : 0;
        result = 31 * result + (post != null ? post.hashCode() : 0);
        return result;
    }
}
