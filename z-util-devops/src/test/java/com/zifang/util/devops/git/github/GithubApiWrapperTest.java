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
public class GithubApiWrapperTest {

    @Test
    public void testRepoFactory() {
        RepositoryApiWrapper wrapper = GithubApiWrapper.repo();
        assertNotNull(wrapper);
    }

    @Test
    public void testRepoFactoryWithOwnerAndRepo() {
        RepositoryApiWrapper wrapper = GithubApiWrapper.repo("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    public void testIssueFactory() {
        IssueApiWrapper wrapper = GithubApiWrapper.issue();
        assertNotNull(wrapper);
    }

    @Test
    public void testIssueFactoryWithOwnerAndRepo() {
        IssueApiWrapper wrapper = GithubApiWrapper.issue("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    public void testPrFactory() {
        PullRequestApiWrapper wrapper = GithubApiWrapper.pr();
        assertNotNull(wrapper);
    }

    @Test
    public void testPrFactoryWithOwnerAndRepo() {
        PullRequestApiWrapper wrapper = GithubApiWrapper.pr("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    public void testReleaseFactory() {
        ReleaseApiWrapper wrapper = GithubApiWrapper.release();
        assertNotNull(wrapper);
    }

    @Test
    public void testReleaseFactoryWithOwnerAndRepo() {
        ReleaseApiWrapper wrapper = GithubApiWrapper.release("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    public void testActionFactory() {
        ActionApiWrapper wrapper = GithubApiWrapper.action();
        assertNotNull(wrapper);
    }

    @Test
    public void testActionFactoryWithOwnerAndRepo() {
        ActionApiWrapper wrapper = GithubApiWrapper.action("owner", "repo");
        assertNotNull(wrapper);
    }

    @Test
    public void testUserFactory() {
        UserApiWrapper wrapper = GithubApiWrapper.user();
        assertNotNull(wrapper);
    }

    @Test
    public void testOrgFactory() {
        OrganizationApiWrapper wrapper = GithubApiWrapper.org();
        assertNotNull(wrapper);
    }

    @Test
    public void testOrgFactoryWithOrgName() {
        OrganizationApiWrapper wrapper = GithubApiWrapper.org("my-org");
        assertNotNull(wrapper);
    }
}
