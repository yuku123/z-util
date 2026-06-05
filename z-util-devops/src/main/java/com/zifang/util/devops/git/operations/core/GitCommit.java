package com.zifang.util.devops.git.operations.core;

import java.util.List;

/**
 * 单条提交信息
 *
 * @author zifang
 * @version 1.0.0
 */
public class GitCommit {

    /** 完整 SHA-1 */
    private String sha;
    /** 短 SHA（前 7-12 位） */
    private String shortSha;
    /** 提交信息（完整，多行可能） */
    private String message;
    /** 提交信息首行（subject） */
    private String shortMessage;
    /** 作者 */
    private GitAuthor author;
    /** 提交者 */
    private GitAuthor committer;
    /** 父提交 SHA 列表（merge commit 通常有 2 个） */
    private List<String> parentShas;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getShortSha() {
        return shortSha;
    }

    public void setShortSha(String shortSha) {
        this.shortSha = shortSha;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public GitAuthor getAuthor() {
        return author;
    }

    public void setAuthor(GitAuthor author) {
        this.author = author;
    }

    public GitAuthor getCommitter() {
        return committer;
    }

    public void setCommitter(GitAuthor committer) {
        this.committer = committer;
    }

    public List<String> getParentShas() {
        return parentShas;
    }

    public void setParentShas(List<String> parentShas) {
        this.parentShas = parentShas;
    }

    @Override
    public String toString() {
        return "GitCommit{" + shortSha + " " + shortMessage + "}";
    }
}
