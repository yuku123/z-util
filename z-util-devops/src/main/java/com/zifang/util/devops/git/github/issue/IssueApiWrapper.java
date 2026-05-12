package com.zifang.util.devops.git.github.issue;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GitHub Issue API 封装
 */
public class IssueApiWrapper {

    private final GitHub github;
    private String owner;
    private String repo;

    public IssueApiWrapper(GitHub github) {
        this.github = github;
    }

    public IssueApiWrapper(GitHub github, String owner, String repo) {
        this.github = github;
        this.owner = owner;
        this.repo = repo;
    }

    public IssueApiWrapper withRepo(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
        return this;
    }

    private String fullName() {
        return owner + "/" + repo;
    }

    // ==================== CRUD ====================

    /**
     * 创建 Issue
     */
    public GHIssue create(String title, String body, String... labels) throws IOException {
        GHIssueBuilder builder = github.getRepository(fullName()).createIssue(title).body(body);
        for (String l : labels) {
            builder.label(l);
        }
        return builder.create();
    }

    /**
     * 创建 Issue 并指定 assignee
     */
    public GHIssue create(String title, String body, String assignee, List<String> labels) throws IOException {
        GHIssueBuilder builder = github.getRepository(fullName()).createIssue(title).body(body);
        if (assignee != null) {
            builder.assignee(assignee);
        }
        if (labels != null && !labels.isEmpty()) {
            for (String l : labels) {
                builder.label(l);
            }
        }
        return builder.create();
    }

    /**
     * 获取 Issue
     */
    public GHIssue get(int number) throws IOException {
        return github.getRepository(fullName()).getIssue(number);
    }

    /**
     * 更新 Issue
     */
    public GHIssue update(int number, String title, String body) throws IOException {
        GHIssue issue = get(number);
        if (title != null) issue.setTitle(title);
        if (body != null) issue.setBody(body);
        return issue;
    }

    /**
     * 关闭 Issue
     */
    public void close(int number) throws IOException {
        get(number).close();
    }

    /**
     * 重新打开 Issue
     */
    public void reopen(int number) throws IOException {
        get(number).reopen();
    }

    // ==================== List ====================

    /**
     * 列出所有 open 的 Issue
     */
    public List<GHIssue> listOpen() throws IOException {
        return github.getRepository(fullName()).getIssues(GHIssueState.OPEN);
    }

    /**
     * 列出所有 closed 的 Issue
     */
    public List<GHIssue> listClosed() throws IOException {
        return github.getRepository(fullName()).getIssues(GHIssueState.CLOSED);
    }

    /**
     * 搜索 Issue（传入完整查询字符串，如 "is:issue is:open label:bug"）
     */
    public List<GHIssue> search(String query) throws IOException {
        List<GHIssue> result = new ArrayList<>();
        for (GHIssue i : github.searchIssues().q(query).list()) {
            result.add(i);
        }
        return result;
    }

    // ==================== Label ====================

    /**
     * 添加标签
     */
    public List<GHLabel> addLabels(int number, String... labels) throws IOException {
        return get(number).addLabels(labels);
    }

    /**
     * 移除标签
     */
    public List<GHLabel> removeLabel(int number, String label) throws IOException {
        return get(number).removeLabel(label);
    }

    /**
     * 获取当前仓库的所有标签
     */
    public List<GHLabel> listLabels() throws IOException {
        List<GHLabel> labels = new ArrayList<>();
        PagedIterator<GHLabel> it = github.getRepository(fullName()).listLabels().iterator();
        while (it.hasNext()) {
            labels.add(it.next());
        }
        return labels;
    }

    /**
     * 创建标签
     */
    public GHLabel createLabel(String name, String color, String description) throws IOException {
        return github.getRepository(fullName()).createLabel(name, color, description);
    }

    // ==================== Assignee ====================

    /**
     * 添加 assignee
     */
    public void addAssignees(int number, String... assignees) throws IOException {
        GHUser[] users = new GHUser[assignees.length];
        for (int i = 0; i < assignees.length; i++) {
            users[i] = github.getUser(assignees[i]);
        }
        get(number).addAssignees(Arrays.asList(users));
    }

    /**
     * 移除 assignee
     */
    public void removeAssignee(int number, String assignee) throws IOException {
        get(number).removeAssignees(github.getUser(assignee));
    }

    // ==================== Comment ====================

    /**
     * 添加评论
     */
    public GHIssueComment comment(int number, String body) throws IOException {
        return get(number).comment(body);
    }

    /**
     * 列出 Issue 的所有评论
     */
    public List<GHIssueComment> listComments(int number) throws IOException {
        List<GHIssueComment> comments = new ArrayList<>();
        PagedIterable<GHIssueComment> iterable = get(number).listComments();
        for (GHIssueComment c : iterable) {
            comments.add(c);
        }
        return comments;
    }

    /**
     * 删除评论
     */
    public void deleteComment(GHIssueComment comment) throws IOException {
        comment.delete();
    }

    // ==================== DTO ====================

    public static class IssueInfo {
        public int number;
        public String title;
        public String body;
        public String state;
        public String author;
        public List<String> labels;
        public String createdAt;
        public String updatedAt;

        public static IssueInfo from(GHIssue issue) throws IOException {
            IssueInfo info = new IssueInfo();
            info.number = issue.getNumber();
            info.title = issue.getTitle();
            info.body = issue.getBody();
            info.state = issue.getState().name();
            info.author = issue.getUser().getLogin();
            info.labels = new ArrayList<>();
            for (GHLabel l : issue.getLabels()) {
                info.labels.add(l.getName());
            }
            info.createdAt = issue.getCreatedAt().toString();
            info.updatedAt = issue.getUpdatedAt().toString();
            return info;
        }
    }
}
