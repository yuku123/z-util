package com.zifang.util.devops.git.github.repo;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GitHub Repository API 封装
 */
public class RepositoryApiWrapper {

    private final GitHub github;
    private String owner;
    private String repo;

    public RepositoryApiWrapper(GitHub github) {
        this.github = github;
    }

    public RepositoryApiWrapper(GitHub github, String owner, String repo) {
        this.github = github;
        this.owner = owner;
        this.repo = repo;
    }

    public RepositoryApiWrapper withRepo(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
        return this;
    }

    private String fullName() {
        return owner + "/" + repo;
    }

    private GHRepository getRepo() throws IOException {
        return github.getRepository(fullName());
    }

    // ==================== CRUD ====================

    /**
     * 创建仓库
     */
    public GHRepository create(String name, String description, boolean isPrivate) throws IOException {
        return github.createRepository(name)
                .description(description)
                .private_(isPrivate)
                .create();
    }

    /**
     * 创建组织仓库
     */
    public GHRepository createOrgRepo(String org, String name, String description, boolean isPrivate) throws IOException {
        return github.createRepository(name)
                .description(description)
                .private_(isPrivate)
                .owner(org)
                .create();
    }

    /**
     * 获取仓库
     */
    public GHRepository get(String owner, String repo) throws IOException {
        return github.getRepository(owner + "/" + repo);
    }

    public GHRepository get() throws IOException {
        return getRepo();
    }

    /**
     * 删除仓库（慎用）
     */
    public void delete(String owner, String repo) throws IOException {
        github.getRepository(owner + "/" + repo).delete();
    }

    public void delete() throws IOException {
        getRepo().delete();
    }

    // ==================== Repository Info ====================

    /**
     * 获取仓库基本信息
     */
    public RepositoryInfo info() throws IOException {
        return RepositoryInfo.from(getRepo());
    }

    public String getDescription() throws IOException {
        return getRepo().getDescription();
    }

    public String getDefaultBranch() throws IOException {
        return getRepo().getDefaultBranch();
    }

    public String getLanguage() throws IOException {
        return getRepo().getLanguage();
    }

    public int getStargazersCount() throws IOException {
        return getRepo().getStargazersCount();
    }

    public int getForksCount() throws IOException {
        return getRepo().getForksCount();
    }

    // ==================== Branch ====================

    /**
     * 获取分支列表
     */
    public List<String> listBranches() throws IOException {
        Map<String, GHBranch> branches = getRepo().getBranches();
        return new ArrayList<>(branches.keySet());
    }

    /**
     * 获取分支详情
     */
    public GHBranch getBranch(String branch) throws IOException {
        return getRepo().getBranch(branch);
    }

    // ==================== Search ====================

    /**
     * 搜索仓库
     */
    public List<GHRepository> search(String keyword) throws IOException {
        return search(keyword, null);
    }

    /**
     * 搜索仓库（带语言过滤）
     */
    public List<GHRepository> search(String keyword, String language) throws IOException {
        GHRepositorySearchBuilder builder = github.searchRepositories().q(keyword);
        if (language != null) {
            builder.language(language);
        }
        List<GHRepository> list = new ArrayList<>();
        for (GHRepository r : builder.list()) {
            list.add(r);
        }
        return list;
    }

    // ==================== User Repos ====================

    /**
     * 列出指定用户的仓库
     */
    public List<GHRepository> listUserRepos(String username) throws IOException {
        List<GHRepository> list = new ArrayList<>();
        for (GHRepository r : github.getUser(username).listRepositories(100)) {
            list.add(r);
        }
        return list;
    }

    /**
     * 列出当前认证用户的仓库
     */
    public List<GHRepository> listMyRepos() throws IOException {
        List<GHRepository> list = new ArrayList<>();
        for (GHRepository r : github.getMyself().listRepositories(100)) {
            list.add(r);
        }
        return list;
    }

    // ==================== Fork ====================

    /**
     * Fork 仓库
     */
    public GHRepository fork() throws IOException {
        return getRepo().fork();
    }

    /**
     * 列出仓库的 Fork
     */
    public List<GHRepository> listForks() throws IOException {
        List<GHRepository> list = new ArrayList<>();
        for (GHRepository r : getRepo().listForks()) {
            list.add(r);
        }
        return list;
    }

    // ==================== Star ====================

    /**
     * 列出 Stargazers
     */
    public List<org.kohsuke.github.GHUser> listStargazers() throws IOException {
        List<org.kohsuke.github.GHUser> list = new ArrayList<>();
        for (org.kohsuke.github.GHUser u : getRepo().listStargazers()) {
            list.add(u);
        }
        return list;
    }

    // ==================== DTO ====================

    public static class RepositoryInfo {
        private String fullName;
        private String description;
        private String defaultBranch;
        private String language;
        private int stargazersCount;
        private int forksCount;
        private boolean isPrivate;
        private String htmlUrl;

        public String getFullName() { return fullName; }
        public String getDescription() { return description; }
        public String getDefaultBranch() { return defaultBranch; }
        public String getLanguage() { return language; }
        public int getStargazersCount() { return stargazersCount; }
        public int getForksCount() { return forksCount; }
        public boolean isPrivate() { return isPrivate; }
        public String getHtmlUrl() { return htmlUrl; }

        public static RepositoryInfo from(GHRepository r) {
            RepositoryInfo info = new RepositoryInfo();
            info.fullName = r.getFullName();
            info.description = r.getDescription();
            info.defaultBranch = r.getDefaultBranch();
            info.language = r.getLanguage();
            info.stargazersCount = r.getStargazersCount();
            info.forksCount = r.getForksCount();
            info.isPrivate = r.isPrivate();
            info.htmlUrl = r.getHtmlUrl().toString();
            return info;
        }
    }
}
