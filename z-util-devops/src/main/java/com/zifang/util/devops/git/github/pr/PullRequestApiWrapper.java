package com.zifang.util.devops.git.github.pr;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GitHub Pull Request API 封装
 * <p>
 * 提供GitHub Pull Request相关操作的封装，
 * 包括PR的创建、更新、关闭、合并、Review管理、评论等功能。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * PullRequestApiWrapper类。
 */
/**
 * PullRequestApiWrapper类。
 */
public class PullRequestApiWrapper {

    private final GitHub github;
    private String owner;
    private String repo;

    /**
     * PullRequestApiWrapper方法。
     *      * @param github GitHub类型参数
     */
    /**
     * PullRequestApiWrapper方法。
     *      * @param github GitHub类型参数
     */
    public PullRequestApiWrapper(GitHub github) {
        this.github = github;
    }

    /**
     * PullRequestApiWrapper方法。
     *      * @param github GitHub类型参数
     * @param owner String类型参数
     * @param repo String类型参数
     */
    /**
     * PullRequestApiWrapper方法。
     *      * @param github GitHub类型参数
     * @param owner String类型参数
     * @param repo String类型参数
     */
    public PullRequestApiWrapper(GitHub github, String owner, String repo) {
        this.github = github;
        this.owner = owner;
        this.repo = repo;
    }

    /**
     * withRepo方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return PullRequestApiWrapper类型返回值
     */
    /**
     * withRepo方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return PullRequestApiWrapper类型返回值
     */
    public PullRequestApiWrapper withRepo(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
        return this;
    }

    private String fullName() {
        return owner + "/" + repo;
    }

    // ==================== CRUD ====================

    /**
     * 创建 Pull Request
     *
     * @param head 源分支（带前缀，如 "user:feature-branch"）
     * @param base 目标分支（如 "main"）
     */
    /**
     * create方法。
     *      * @param title String类型参数
     * @param head String类型参数
     * @param base String类型参数
     * @param body String类型参数
     * @return GHPullRequest类型返回值
     */
    /**
     * create方法。
     *      * @param title String类型参数
     * @param head String类型参数
     * @param base String类型参数
     * @param body String类型参数
     * @return GHPullRequest类型返回值
     */
    public GHPullRequest create(String title, String head, String base, String body) throws IOException {
        return github.getRepository(fullName()).createPullRequest(title, head, base, body);
    }

    /**
     * 获取 PR
     */
    /**
     * get方法。
     *      * @param number int类型参数
     * @return GHPullRequest类型返回值
     */
    /**
     * get方法。
     *      * @param number int类型参数
     * @return GHPullRequest类型返回值
     */
    public GHPullRequest get(int number) throws IOException {
        return github.getRepository(fullName()).getPullRequest(number);
    }

    /**
     * 更新 PR
     */
    /**
     * update方法。
     *      * @param number int类型参数
     * @param title String类型参数
     * @param body String类型参数
     * @return GHPullRequest类型返回值
     */
    /**
     * update方法。
     *      * @param number int类型参数
     * @param title String类型参数
     * @param body String类型参数
     * @return GHPullRequest类型返回值
     */
    public GHPullRequest update(int number, String title, String body) throws IOException {
        GHPullRequest pr = get(number);
        if (title != null) pr.setTitle(title);
        if (body != null) pr.setBody(body);
        return pr;
    }

    /**
     * 关闭 PR
     */
    /**
     * close方法。
     *      * @param number int类型参数
     */
    /**
     * close方法。
     *      * @param number int类型参数
     */
    public void close(int number) throws IOException {
        get(number).close();
    }

    /**
     * 合并 PR
     */
    /**
     * merge方法。
     *      * @param number int类型参数
     * @param commitMessage String类型参数
     */
    /**
     * merge方法。
     *      * @param number int类型参数
     * @param commitMessage String类型参数
     */
    public void merge(int number, String commitMessage) throws IOException {
        get(number).merge(commitMessage);
    }

    // ==================== List ====================

    /**
     * 列出所有 open PR
     */
    /**
     * listOpen方法。
     * @return List<GHPullRequest>类型返回值
     */
    /**
     * listOpen方法。
     * @return List<GHPullRequest>类型返回值
     */
    public List<GHPullRequest> listOpen() throws IOException {
        return github.getRepository(fullName()).getPullRequests(GHIssueState.OPEN);
    }

    /**
     * 列出所有 closed PR
     */
    /**
     * listClosed方法。
     * @return List<GHPullRequest>类型返回值
     */
    /**
     * listClosed方法。
     * @return List<GHPullRequest>类型返回值
     */
    public List<GHPullRequest> listClosed() throws IOException {
        return github.getRepository(fullName()).getPullRequests(GHIssueState.CLOSED);
    }

    // ==================== Review ====================

    /**
     * 请求 Review（按用户名）
     */
    /**
     * requestReview方法。
     *      * @param number int类型参数
     * @param reviewers String...类型参数
     */
    /**
     * requestReview方法。
     *      * @param number int类型参数
     * @param reviewers String...类型参数
     */
    public void requestReview(int number, String... reviewers) throws IOException {
        List<GHUser> users = new ArrayList<>();
        for (String r : reviewers) {
            users.add(github.getUser(r));
        }
        get(number).requestReviewers(users);
    }

    /**
     * 添加 Reviewer
     */
    /**
     * addReviewers方法。
     *      * @param number int类型参数
     * @param reviewers ListString类型参数
     */
    /**
     * addReviewers方法。
     *      * @param number int类型参数
     * @param reviewers ListString类型参数
     */
    public void addReviewers(int number, List<String> reviewers) throws IOException {
        List<GHUser> users = new ArrayList<>();
        for (String r : reviewers) {
            users.add(github.getUser(r));
        }
        get(number).requestReviewers(users);
    }

    /**
     * 获取 PR 的 Review 列表
     */
    /**
     * listReviews方法。
     *      * @param number int类型参数
     * @return List<GHPullRequestReview>类型返回值
     */
    /**
     * listReviews方法。
     *      * @param number int类型参数
     * @return List<GHPullRequestReview>类型返回值
     */
    public List<GHPullRequestReview> listReviews(int number) throws IOException {
        List<GHPullRequestReview> reviews = new ArrayList<>();
        PagedIterable<GHPullRequestReview> iterable = get(number).listReviews();
        for (GHPullRequestReview r : iterable) {
            reviews.add(r);
        }
        return reviews;
    }

    // ==================== Comment ====================

    /**
     * 创建 PR 评论
     */
    /**
     * comment方法。
     *      * @param number int类型参数
     * @param body String类型参数
     */
    /**
     * comment方法。
     *      * @param number int类型参数
     * @param body String类型参数
     */
    public void comment(int number, String body) throws IOException {
        get(number).comment(body);
    }

    // ==================== Status ====================

    /**
     * 检查 PR 是否已合并
     */
    /**
     * isMerged方法。
     *      * @param number int类型参数
     * @return boolean类型返回值
     */
    /**
     * isMerged方法。
     *      * @param number int类型参数
     * @return boolean类型返回值
     */
    public boolean isMerged(int number) throws IOException {
        return get(number).isMerged();
    }

    /**
     * 获取 PR head SHA
     */
    /**
     * getHeadSha方法。
     *      * @param number int类型参数
     * @return String类型返回值
     */
    /**
     * getHeadSha方法。
     *      * @param number int类型参数
     * @return String类型返回值
     */
    public String getHeadSha(int number) throws IOException {
        return get(number).getHead().getSha();
    }

    // ==================== DTO ====================

    /**
     * Pull Request 信息 DTO
     * <p>
     * 用于封装 PR 的基本信息，包括编号、标题、正文、状态等
     */
    public static class PRInfo {
        public int number;
        public String title;
        public String body;
        public String state;
        public String author;
        public String headBranch;
        public String baseBranch;
        public boolean merged;

        /**
         * 从 GHPullRequest 对象构建 PRInfo
         *
         * @param pr Pull Request 对象
         * @return PRInfo 实例
         * @throws IOException IO异常
         */
    /**
     * from方法。
     *      * @param pr GHPullRequest类型参数
     * @return static PRInfo类型返回值
     */
    /**
     * from方法。
     *      * @param pr GHPullRequest类型参数
     * @return static PRInfo类型返回值
     */
        public static PRInfo from(GHPullRequest pr) throws IOException {
            PRInfo info = new PRInfo();
            info.number = pr.getNumber();
            info.title = pr.getTitle();
            info.body = pr.getBody();
            info.state = pr.getState().name();
            info.author = pr.getUser().getLogin();
            info.headBranch = pr.getHead().getRef();
            info.baseBranch = pr.getBase().getRef();
            info.merged = pr.isMerged();
            return info;
        }
    }
}
