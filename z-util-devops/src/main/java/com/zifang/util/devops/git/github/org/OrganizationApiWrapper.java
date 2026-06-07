package com.zifang.util.devops.git.github.org;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHOrganization.Permission;
import org.kohsuke.github.GHOrganization.Role;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GHTeamBuilder;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GitHub Organization API 封装
 * <p>
 * 提供GitHub组织（Organization）相关操作的封装，
 * 包括组织信息查询、成员管理、团队管理、仓库管理等。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * OrganizationApiWrapper类。
 */
/**
 * OrganizationApiWrapper类。
 */
public class OrganizationApiWrapper {

    private final GitHub github;
    private String org;

    /**
     * OrganizationApiWrapper方法。
     *      * @param github GitHub类型参数
     */
    /**
     * OrganizationApiWrapper方法。
     *      * @param github GitHub类型参数
     */
    public OrganizationApiWrapper(GitHub github) {
        this.github = github;
    }

    /**
     * OrganizationApiWrapper方法。
     *      * @param github GitHub类型参数
     * @param org String类型参数
     */
    /**
     * OrganizationApiWrapper方法。
     *      * @param github GitHub类型参数
     * @param org String类型参数
     */
    public OrganizationApiWrapper(GitHub github, String org) {
        this.github = github;
        this.org = org;
    }

    /**
     * withOrg方法。
     *      * @param org String类型参数
     * @return OrganizationApiWrapper类型返回值
     */
    /**
     * withOrg方法。
     *      * @param org String类型参数
     * @return OrganizationApiWrapper类型返回值
     */
    public OrganizationApiWrapper withOrg(String org) {
        this.org = org;
        return this;
    }

    // ==================== Organization Info ====================

    /**
     * 获取组织信息
     *
     * @return GHOrganization 组织对象
     * @throws IOException IO异常
     */
    /**
     * get方法。
     * @return GHOrganization类型返回值
     */
    /**
     * get方法。
     * @return GHOrganization类型返回值
     */
    public GHOrganization get() throws IOException {
        return github.getOrganization(org);
    }

    /**
     * 获取指定组织信息
     *
     * @param org 组织名称
     * @return GHOrganization 组织对象
     * @throws IOException IO异常
     */
    /**
     * get方法。
     *      * @param org String类型参数
     * @return GHOrganization类型返回值
     */
    /**
     * get方法。
     *      * @param org String类型参数
     * @return GHOrganization类型返回值
     */
    public GHOrganization get(String org) throws IOException {
        return github.getOrganization(org);
    }

    /**
     * 获取当前用户所属的所有组织
     */
    /**
     * listMyOrgs方法。
     * @return List<GHOrganization>类型返回值
     */
    /**
     * listMyOrgs方法。
     * @return List<GHOrganization>类型返回值
     */
    public List<GHOrganization> listMyOrgs() throws IOException {
        Map<String, GHOrganization> orgs = github.getMyOrganizations();
        return new ArrayList<>(orgs.values());
    }

    // ==================== Member ====================

    /**
     * 列出组织成员
     */
    /**
     * listMembers方法。
     * @return List<GHUser>类型返回值
     */
    /**
     * listMembers方法。
     * @return List<GHUser>类型返回值
     */
    public List<GHUser> listMembers() throws IOException {
        List<GHUser> users = new ArrayList<>();
        PagedIterator<GHUser> it = get().listMembers().iterator();
        while (it.hasNext()) {
            users.add(it.next());
        }
        return users;
    }

    /**
     * 列出组织的 public 成员
     */
    /**
     * listPublicMembers方法。
     * @return List<GHUser>类型返回值
     */
    /**
     * listPublicMembers方法。
     * @return List<GHUser>类型返回值
     */
    public List<GHUser> listPublicMembers() throws IOException {
        List<GHUser> users = new ArrayList<>();
        PagedIterator<GHUser> it = get().listPublicMembers().iterator();
        while (it.hasNext()) {
            users.add(it.next());
        }
        return users;
    }

    /**
     * 检查用户是否为组织成员
     */
    /**
     * isMember方法。
     *      * @param username String类型参数
     * @return boolean类型返回值
     */
    /**
     * isMember方法。
     *      * @param username String类型参数
     * @return boolean类型返回值
     */
    public boolean isMember(String username) throws IOException {
        return get().hasMember(github.getUser(username));
    }

    /**
     * 添加组织成员
     */
    /**
     * addMember方法。
     *      * @param username String类型参数
     */
    /**
     * addMember方法。
     *      * @param username String类型参数
     */
    public void addMember(String username) throws IOException {
        get().add(github.getUser(username), Role.MEMBER);
    }

    /**
     * 移除组织成员
     */
    /**
     * removeMember方法。
     *      * @param username String类型参数
     */
    /**
     * removeMember方法。
     *      * @param username String类型参数
     */
    public void removeMember(String username) throws IOException {
        get().remove(github.getUser(username));
    }

    // ==================== Team ====================

    /**
     * 列出组织下的所有 Team
     */
    /**
     * listTeams方法。
     * @return List<GHTeam>类型返回值
     */
    /**
     * listTeams方法。
     * @return List<GHTeam>类型返回值
     */
    public List<GHTeam> listTeams() throws IOException {
        List<GHTeam> teams = new ArrayList<>();
        PagedIterator<GHTeam> it = get().listTeams().iterator();
        while (it.hasNext()) {
            teams.add(it.next());
        }
        return teams;
    }

    /**
     * 获取 Team
     */
    /**
     * getTeam方法。
     *      * @param teamId long类型参数
     * @return GHTeam类型返回值
     */
    /**
     * getTeam方法。
     *      * @param teamId long类型参数
     * @return GHTeam类型返回值
     */
    public GHTeam getTeam(long teamId) throws IOException {
        return get().getTeam(teamId);
    }

    /**
     * 创建 Team（无仓库）
     */
    /**
     * createTeam方法。
     *      * @param name String类型参数
     * @return GHTeam类型返回值
     */
    /**
     * createTeam方法。
     *      * @param name String类型参数
     * @return GHTeam类型返回值
     */
    public GHTeam createTeam(String name) throws IOException {
        GHTeamBuilder builder = get().createTeam(name);
        builder.privacy(GHTeam.Privacy.CLOSED);
        return builder.create();
    }

    /**
     * 创建 Team（带仓库）
     */
    /**
     * createTeam方法。
     *      * @param name String类型参数
     * @param repoNames String...类型参数
     * @return GHTeam类型返回值
     */
    /**
     * createTeam方法。
     *      * @param name String类型参数
     * @param repoNames String...类型参数
     * @return GHTeam类型返回值
     */
    public GHTeam createTeam(String name, String... repoNames) throws IOException {
        GHTeamBuilder builder = get().createTeam(name);
        builder.privacy(GHTeam.Privacy.CLOSED);
        if (repoNames != null && repoNames.length > 0) {
            builder.repositories(repoNames);
        }
        return builder.create();
    }

    /**
     * 给 Team 添加成员
     */
    /**
     * addTeamMember方法。
     *      * @param teamId long类型参数
     * @param username String类型参数
     */
    /**
     * addTeamMember方法。
     *      * @param teamId long类型参数
     * @param username String类型参数
     */
    public void addTeamMember(long teamId, String username) throws IOException {
        getTeam(teamId).add(github.getUser(username));
    }

    /**
     * 从 Team 移除成员
     */
    /**
     * removeTeamMember方法。
     *      * @param teamId long类型参数
     * @param username String类型参数
     */
    /**
     * removeTeamMember方法。
     *      * @param teamId long类型参数
     * @param username String类型参数
     */
    public void removeTeamMember(long teamId, String username) throws IOException {
        getTeam(teamId).remove(github.getUser(username));
    }

    /**
     * 将仓库添加到 Team（带权限）
     */
    /**
     * addTeamRepository方法。
     *      * @param teamId long类型参数
     * @param repoOwner String类型参数
     * @param repoName String类型参数
     * @param permission Permission类型参数
     */
    /**
     * addTeamRepository方法。
     *      * @param teamId long类型参数
     * @param repoOwner String类型参数
     * @param repoName String类型参数
     * @param permission Permission类型参数
     */
    public void addTeamRepository(long teamId, String repoOwner, String repoName, Permission permission) throws IOException {
        GHRepository repo = github.getRepository(repoOwner + "/" + repoName);
        getTeam(teamId).add(repo, permission);
    }

    /**
     * 从 Team 移除仓库
     */
    /**
     * removeTeamRepository方法。
     *      * @param teamId long类型参数
     * @param repoOwner String类型参数
     * @param repoName String类型参数
     */
    /**
     * removeTeamRepository方法。
     *      * @param teamId long类型参数
     * @param repoOwner String类型参数
     * @param repoName String类型参数
     */
    public void removeTeamRepository(long teamId, String repoOwner, String repoName) throws IOException {
        GHRepository repo = github.getRepository(repoOwner + "/" + repoName);
        getTeam(teamId).remove(repo);
    }

    // ==================== Repository ====================

    /**
     * 列出组织下的所有仓库
     */
    /**
     * listRepos方法。
     * @return List<GHRepository>类型返回值
     */
    /**
     * listRepos方法。
     * @return List<GHRepository>类型返回值
     */
    public List<GHRepository> listRepos() throws IOException {
        List<GHRepository> repos = new ArrayList<>();
        PagedIterator<GHRepository> it = get().listRepositories(100).iterator();
        while (it.hasNext()) {
            repos.add(it.next());
        }
        return repos;
    }

    /**
     * 在组织下创建仓库
     */
    /**
     * createRepo方法。
     *      * @param name String类型参数
     * @param description String类型参数
     * @param isPrivate boolean类型参数
     * @return GHRepository类型返回值
     */
    /**
     * createRepo方法。
     *      * @param name String类型参数
     * @param description String类型参数
     * @param isPrivate boolean类型参数
     * @return GHRepository类型返回值
     */
    public GHRepository createRepo(String name, String description, boolean isPrivate) throws IOException {
        return get().createRepository(name)
                .description(description)
                .private_(isPrivate)
                .create();
    }
}
