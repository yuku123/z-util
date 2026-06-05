package com.zifang.util.devops.git.operations.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作区状态
 * <p>
 * 汇总已暂存、未暂存、未跟踪三类文件变化，并暴露当前分支、是否为干净工作区。
 *
 * @author zifang
 * @version 1.0.0
 */
public class GitStatus {

    /** 当前分支名（detached HEAD 时为 null） */
    private String branch;
    /** 是否处于 detached HEAD 状态 */
    private boolean detached;
    /** 当前 HEAD 的 SHA（detached 时也有值） */
    private String headSha;
    /** 工作区是否完全干净 */
    private boolean clean;
    /** 已暂存（staged）的文件路径，相对工作区 */
    private List<String> added = new ArrayList<>();
    private List<String> changed = new ArrayList<>();
    private List<String> removed = new ArrayList<>();
    private List<String> untracked = new ArrayList<>();
    /** 未暂存（modified） */
    private List<String> modified = new ArrayList<>();
    private List<String> deleted = new ArrayList<>();
    private List<String> missing = new ArrayList<>();

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public boolean isDetached() {
        return detached;
    }

    public void setDetached(boolean detached) {
        this.detached = detached;
    }

    public String getHeadSha() {
        return headSha;
    }

    public void setHeadSha(String headSha) {
        this.headSha = headSha;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public List<String> getAdded() {
        return added;
    }

    public void setAdded(List<String> added) {
        this.added = added;
    }

    public List<String> getChanged() {
        return changed;
    }

    public void setChanged(List<String> changed) {
        this.changed = changed;
    }

    public List<String> getRemoved() {
        return removed;
    }

    public void setRemoved(List<String> removed) {
        this.removed = removed;
    }

    public List<String> getUntracked() {
        return untracked;
    }

    public void setUntracked(List<String> untracked) {
        this.untracked = untracked;
    }

    public List<String> getModified() {
        return modified;
    }

    public void setModified(List<String> modified) {
        this.modified = modified;
    }

    public List<String> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<String> deleted) {
        this.deleted = deleted;
    }

    public List<String> getMissing() {
        return missing;
    }

    public void setMissing(List<String> missing) {
        this.missing = missing;
    }

    @Override
    public String toString() {
        return "GitStatus{branch='" + branch + "', clean=" + clean +
                ", added=" + added.size() + ", modified=" + modified.size() +
                ", deleted=" + deleted.size() + ", untracked=" + untracked.size() + "}";
    }
}
