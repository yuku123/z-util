package com.zifang.util.devops.git.operations.core;

/**
 * 分支信息
 *
 * @author zifang
 * @version 1.0.0
 */
public class GitBranch {

    public enum Type {
        LOCAL, REMOTE, ALL
    }

    /** 分支简称（如 main, origin/main） */
    private String name;
    /** 完整 ref 名（refs/heads/main, refs/remotes/origin/main） */
    private String fullName;
    /** 分支指向的提交 SHA */
    private String sha;
    /** 是否为当前 HEAD */
    private boolean current;
    /** 是否为远程分支 */
    private boolean remote;
    /** 跟踪的上游分支简称（可能为 null） */
    private String upstream;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public String getUpstream() {
        return upstream;
    }

    public void setUpstream(String upstream) {
        this.upstream = upstream;
    }

    @Override
    public String toString() {
        return (current ? "* " : "  ") + name + (upstream != null ? " -> " + upstream : "");
    }
}
