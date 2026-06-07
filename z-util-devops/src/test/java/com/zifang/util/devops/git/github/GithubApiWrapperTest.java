package com.zifang.util.devops.git.github;

import com.zifang.util.devops.git.github.action.ActionApiWrapper;
import com.zifang.util.devops.git.github.issue.IssueApiWrapper;
import com.zifang.util.devops.git.github.org.OrganizationApiWrapper;
import com.zifang.util.devops.git.github.pr.PullRequestApiWrapper;
import com.zifang.util.devops.git.github.release.ReleaseApiWrapper;
import com.zifang.util.devops.git.github.repo.RepositoryApiWrapper;
import com.zifang.util.devops.git.github.user.UserApiWrapper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * GithubApiWrapper 静态工厂方法测试
 * <p>
 * 这些方法只是构造 wrapper 实例，不涉及网络调用。
 */
/**
 * GithubApiWrapperTest类。
 */
public class GithubApiWrapperTest {

    @Test
    /**
     * testRepoFactory方法。
     */
    public void testRepoFactory() {
        RepositoryApiWrapper wrapper = GithubApiWrapper.repo();
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testRepoFactoryWithOwnerAndRepo方法。
     */
    public void testRepoFactoryWithOwnerAndRepo() {
        RepositoryApiWrapper wrapper = GithubApiWrapper.repo("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testIssueFactory方法。
     */
    public void testIssueFactory() {
        IssueApiWrapper wrapper = GithubApiWrapper.issue();
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testIssueFactoryWithOwnerAndRepo方法。
     */
    public void testIssueFactoryWithOwnerAndRepo() {
        IssueApiWrapper wrapper = GithubApiWrapper.issue("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testPrFactory方法。
     */
    public void testPrFactory() {
        PullRequestApiWrapper wrapper = GithubApiWrapper.pr();
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testPrFactoryWithOwnerAndRepo方法。
     */
    public void testPrFactoryWithOwnerAndRepo() {
        PullRequestApiWrapper wrapper = GithubApiWrapper.pr("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testReleaseFactory方法。
     */
    public void testReleaseFactory() {
        ReleaseApiWrapper wrapper = GithubApiWrapper.release();
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testReleaseFactoryWithOwnerAndRepo方法。
     */
    public void testReleaseFactoryWithOwnerAndRepo() {
        ReleaseApiWrapper wrapper = GithubApiWrapper.release("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testActionFactory方法。
     */
    public void testActionFactory() {
        ActionApiWrapper wrapper = GithubApiWrapper.action();
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testActionFactoryWithOwnerAndRepo方法。
     */
    public void testActionFactoryWithOwnerAndRepo() {
        ActionApiWrapper wrapper = GithubApiWrapper.action("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testUserFactory方法。
     */
    public void testUserFactory() {
        UserApiWrapper wrapper = GithubApiWrapper.user();
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testOrgFactory方法。
     */
    public void testOrgFactory() {
        OrganizationApiWrapper wrapper = GithubApiWrapper.org();
        assertNotNull(wrapper);
    }

    @Test
    /**
     * testOrgFactoryWithOrgName方法。
     */
    public void testOrgFactoryWithOrgName() {
        OrganizationApiWrapper wrapper = GithubApiWrapper.org("my-org");
        assertNotNull(wrapper);
    }
}
