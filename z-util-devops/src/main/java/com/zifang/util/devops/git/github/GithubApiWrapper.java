package com.zifang.util.devops.git.github;

import com.zifang.util.devops.git.github.holder.GithubApiHolder;
import com.zifang.util.devops.git.github.repo.RepositoryApiWrapper;
import com.zifang.util.devops.git.github.issue.IssueApiWrapper;
import com.zifang.util.devops.git.github.pr.PullRequestApiWrapper;
import com.zifang.util.devops.git.github.release.ReleaseApiWrapper;
import com.zifang.util.devops.git.github.action.ActionApiWrapper;
import com.zifang.util.devops.git.github.user.UserApiWrapper;
import com.zifang.util.devops.git.github.org.OrganizationApiWrapper;
import org.kohsuke.github.GitHub;

/**
 * GitHub API 总入口，统一提供各子模块的封装接口。
 * <p>
 * 入口类，提供获取各种GitHub API封装对象的静态方法，
 * 包括RepositoryApiWrapper、IssueApiWrapper、PullRequestApiWrapper等。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * GithubApiWrapper类。
 */
/**
 * GithubApiWrapper类。
 */
public class GithubApiWrapper {

    private GithubApiWrapper() {
    }

    private static GitHub github() {
        return GithubApiHolder.getInstance().getGithub();
    }

    // ---- Repository ----

    /**
     * 获取仓库 API 封装（不指定仓库）
     *
     * @return RepositoryApiWrapper 实例
     */
    /**
     * repo方法。
     * @return static RepositoryApiWrapper类型返回值
     */
    /**
     * repo方法。
     * @return static RepositoryApiWrapper类型返回值
     */
    public static RepositoryApiWrapper repo() {
        return new RepositoryApiWrapper(github());
    }

    /**
     * 获取仓库 API 封装（指定仓库）
     *
     * @param owner 仓库所有者
     * @param repo  仓库名称
     * @return RepositoryApiWrapper 实例
     */
    /**
     * repo方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static RepositoryApiWrapper类型返回值
     */
    /**
     * repo方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static RepositoryApiWrapper类型返回值
     */
    public static RepositoryApiWrapper repo(String owner, String repo) {
        return new RepositoryApiWrapper(github(), owner, repo);
    }

    // ---- Issue ----

    /**
     * 获取 Issue API 封装（不指定仓库）
     *
     * @return IssueApiWrapper 实例
     */
    /**
     * issue方法。
     * @return static IssueApiWrapper类型返回值
     */
    /**
     * issue方法。
     * @return static IssueApiWrapper类型返回值
     */
    public static IssueApiWrapper issue() {
        return new IssueApiWrapper(github());
    }

    /**
     * 获取 Issue API 封装（指定仓库）
     *
     * @param owner 仓库所有者
     * @param repo  仓库名称
     * @return IssueApiWrapper 实例
     */
    /**
     * issue方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static IssueApiWrapper类型返回值
     */
    /**
     * issue方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static IssueApiWrapper类型返回值
     */
    public static IssueApiWrapper issue(String owner, String repo) {
        return new IssueApiWrapper(github(), owner, repo);
    }

    // ---- Pull Request ----

    /**
     * 获取 Pull Request API 封装（不指定仓库）
     *
     * @return PullRequestApiWrapper 实例
     */
    /**
     * pr方法。
     * @return static PullRequestApiWrapper类型返回值
     */
    /**
     * pr方法。
     * @return static PullRequestApiWrapper类型返回值
     */
    public static PullRequestApiWrapper pr() {
        return new PullRequestApiWrapper(github());
    }

    /**
     * 获取 Pull Request API 封装（指定仓库）
     *
     * @param owner 仓库所有者
     * @param repo  仓库名称
     * @return PullRequestApiWrapper 实例
     */
    /**
     * pr方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static PullRequestApiWrapper类型返回值
     */
    /**
     * pr方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static PullRequestApiWrapper类型返回值
     */
    public static PullRequestApiWrapper pr(String owner, String repo) {
        return new PullRequestApiWrapper(github(), owner, repo);
    }

    // ---- Release ----

    /**
     * 获取 Release API 封装（不指定仓库）
     *
     * @return ReleaseApiWrapper 实例
     */
    /**
     * release方法。
     * @return static ReleaseApiWrapper类型返回值
     */
    /**
     * release方法。
     * @return static ReleaseApiWrapper类型返回值
     */
    public static ReleaseApiWrapper release() {
        return new ReleaseApiWrapper(github());
    }

    /**
     * 获取 Release API 封装（指定仓库）
     *
     * @param owner 仓库所有者
     * @param repo  仓库名称
     * @return ReleaseApiWrapper 实例
     */
    /**
     * release方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static ReleaseApiWrapper类型返回值
     */
    /**
     * release方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static ReleaseApiWrapper类型返回值
     */
    public static ReleaseApiWrapper release(String owner, String repo) {
        return new ReleaseApiWrapper(github(), owner, repo);
    }

    // ---- GitHub Actions ----

    /**
     * 获取 GitHub Actions API 封装（不指定仓库）
     *
     * @return ActionApiWrapper 实例
     */
    /**
     * action方法。
     * @return static ActionApiWrapper类型返回值
     */
    /**
     * action方法。
     * @return static ActionApiWrapper类型返回值
     */
    public static ActionApiWrapper action() {
        return new ActionApiWrapper(github());
    }

    /**
     * 获取 GitHub Actions API 封装（指定仓库）
     *
     * @param owner 仓库所有者
     * @param repo  仓库名称
     * @return ActionApiWrapper 实例
     */
    /**
     * action方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static ActionApiWrapper类型返回值
     */
    /**
     * action方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return static ActionApiWrapper类型返回值
     */
    public static ActionApiWrapper action(String owner, String repo) {
        return new ActionApiWrapper(github(), owner, repo);
    }

    // ---- User ----

    /**
     * 获取用户 API 封装
     *
     * @return UserApiWrapper 实例
     */
    /**
     * user方法。
     * @return static UserApiWrapper类型返回值
     */
    /**
     * user方法。
     * @return static UserApiWrapper类型返回值
     */
    public static UserApiWrapper user() {
        return new UserApiWrapper(github());
    }

    // ---- Organization ----

    /**
     * 获取组织 API 封装（不指定组织）
     *
     * @return OrganizationApiWrapper 实例
     */
    /**
     * org方法。
     * @return static OrganizationApiWrapper类型返回值
     */
    /**
     * org方法。
     * @return static OrganizationApiWrapper类型返回值
     */
    public static OrganizationApiWrapper org() {
        return new OrganizationApiWrapper(github());
    }

    /**
     * 获取组织 API 封装（指定组织）
     *
     * @param orgName 组织名称
     * @return OrganizationApiWrapper 实例
     */
    /**
     * org方法。
     *      * @param orgName String类型参数
     * @return static OrganizationApiWrapper类型返回值
     */
    /**
     * org方法。
     *      * @param orgName String类型参数
     * @return static OrganizationApiWrapper类型返回值
     */
    public static OrganizationApiWrapper org(String orgName) {
        return new OrganizationApiWrapper(github(), orgName);
    }
}
