package com.zifang.util.devops.git.operations.core;

/**
 * 单条 diff 记录
 *
 * @author zifang
 * @version 1.0.0
 */
public class GitDiffEntry {

    public enum ChangeType {
        ADD, DELETE, MODIFY, RENAME, COPY
    }

    private ChangeType changeType;
    private String oldPath;
    private String newPath;
    /** 旧 blob SHA（可空） */
    private String oldSha;
    /** 新 blob SHA（可空） */
    private String newSha;
    /** 是否为二进制 */
    private boolean binary;
    /** 完整 diff 文本（旧+新），可选 */
    private String patch;

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getOldSha() {
        return oldSha;
    }

    public void setOldSha(String oldSha) {
        this.oldSha = oldSha;
    }

    public String getNewSha() {
        return newSha;
    }

    public void setNewSha(String newSha) {
        this.newSha = newSha;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    @Override
    public String toString() {
        return changeType + " " + (oldPath != null ? oldPath : "") +
                (newPath != null && !newPath.equals(oldPath) ? " -> " + newPath : "");
    }
}
