# GitHub API 封装

基于 [github-api](https://github.com/kohsuke/github-api) 封装的 Java 工具库，提供简洁的链式调用接口。

## 快速开始

### 1. 配置 Token

**方式一：环境变量（推荐）**
```bash
export GITHUB_TOKEN=ghp_xxxxxxxxxxxx
```

**方式二：手动初始化**
```java
GithubConfig config = GithubConfig.of("ghp_xxxxxxxxxxxx");
GithubApiHolder.init(config);
```

### 2. 依赖

本模块已依赖 `org.kohsuke:github-api:1.135`，无需额外引入。

## 设计结构

```
git/github/
├── GithubApiWrapper.java       # 总入口，所有 API 统一由此获取
├── GithubApiHolder.java        # 单例，持有 GitHub 客户端
├── config/GithubConfig.java     # 配置（token / apiUrl）
├── repo/RepositoryApiWrapper    # 仓库 CRUD / 搜索 / Fork / Star
├── issue/IssueApiWrapper        # Issue CRUD / 标签 / 指派 / 评论
├── pr/PullRequestApiWrapper     # PR CRUD / Review / 合并
├── release/ReleaseApiWrapper    # Release CRUD / Asset 上传
├── action/ActionApiWrapper      # Workflow / Run / Job / Artifact
├── user/UserApiWrapper          # 用户信息 / 关注 / 仓库列表
└── org/OrganizationApiWrapper   # 组织成员 / Team / 仓库管理
```

## 使用示例

### Repository

```java
// 创建仓库
GHRepository repo = GithubApiWrapper.repo()
    .create("my-repo", "A description", false);

// 获取仓库信息
RepositoryApiWrapper.RepositoryInfo info = GithubApiWrapper.repo("owner", "repo")
    .info();

// 搜索仓库
List<GHRepository> repos = GithubApiWrapper.repo()
    .search("spring boot", "Java");

// Fork
GHRepository fork = GithubApiWrapper.repo("owner", "repo").fork();

// 加星
GithubApiWrapper.repo("owner", "repo").star();

// 列分支
List<String> branches = GithubApiWrapper.repo("owner", "repo").listBranches();
```

### Issue

```java
// 创建 Issue
GHIssue issue = GithubApiWrapper.issue("owner", "repo")
    .create("Bug: 登录失败", "## 描述\n步骤：...\n", "bug");

// 添加标签和 assignee
GithubApiWrapper.issue("owner", "repo").addLabels(issue.getNumber(), "priority:high", "bug");
GithubApiWrapper.issue("owner", "repo").addAssignees(issue.getNumber(), "zifang");

// 添加评论
GithubApiWrapper.issue("owner", "repo").comment(issue.getNumber(), "已确认，正在修复。");

// 关闭 Issue
GithubApiWrapper.issue("owner", "repo").close(issue.getNumber());

// 搜索 Issue
List<GHIssue> bugs = GithubApiWrapper.issue("owner", "repo")
    .search("is:issue is:open label:bug");
```

### Pull Request

```java
// 创建 PR
GHPullRequest pr = GithubApiWrapper.pr("owner", "repo")
    .create("feat: 新增用户模块", "user:feature-branch", "main", "## 功能说明\n...");

// 请求 Review
GithubApiWrapper.pr("owner", "repo").requestReview(pr.getNumber(), "reviewer1", "reviewer2");

// Squash 合并
GithubApiWrapper.pr("owner", "repo").squashMerge(pr.getNumber(), "feat: 新增用户模块 (#" + pr.getNumber() + ")");

// 查看 PR 是否已合并
boolean merged = GithubApiWrapper.pr("owner", "repo").isMerged(pr.getNumber());
```

### Release

```java
// 创建正式 Release
GHRelease release = GithubApiWrapper.release("owner", "repo")
    .create("v1.0.0", "v1.0.0 正式版", "## Changelog\n- Feature A\n- Feature B");

// 创建 Draft
GHRelease draft = GithubApiWrapper.release("owner", "repo")
    .createDraft("v1.1.0", "v1.1.0-beta", "## Changelog\n- ...");

// 上传 Asset
byte[] zipData = Files.readAllBytes(Paths.get("dist.zip"));
release.uploadAsset(release.getId(), "dist.zip", zipData, "application/zip");

// 获取最新正式版
GHRelease latest = GithubApiWrapper.release("owner", "repo").getLatest();
```

### GitHub Actions

```java
// 触发 Workflow（workflow_dispatch）
Map<String, Object> inputs = new HashMap<>();
inputs.put("environment", "staging");
GithubApiWrapper.action("owner", "repo").dispatch(workflowId, "main", inputs);

// 列出最近的 Run
List<GHWorkflowRun> runs = GithubApiWrapper.action("owner", "repo").listRuns(10);

// 重跑失败的 Run
GithubApiWrapper.action("owner", "repo").rerun(runId);

// 获取 Run 的 Jobs
List<GHWorkflowJob> jobs = GithubApiWrapper.action("owner", "repo").listJobs(runId);
```

### User

```java
// 当前用户信息
GHUser me = GithubApiWrapper.user().me();
System.out.println(me.getLogin() + " - " + me.getName());

// 关注用户
GithubApiWrapper.user().follow("someuser");

// 列出用户仓库
List<GHRepository> repos = GithubApiWrapper.user().listRepos("owner");
```

### Organization

```java
// 列出我所在的组织
List<GHOrganization> orgs = GithubApiWrapper.org().listMyOrgs();

// 列出组织下的仓库
List<GHRepository> repos = GithubApiWrapper.org("my-org").listRepos();

// 在组织下创建仓库
GHRepository repo = GithubApiWrapper.org("my-org")
    .createRepo("new-repo", "description", false);

// 列出组织成员
List<GHUser> members = GithubApiWrapper.org("my-org").listMembers();
```

## API 一览

| 模块 | 主要方法 |
|------|---------|
| **Repository** | `create` `get` `delete` `info` `listBranches` `search` `fork` `star` `unstar` `listForks` `listMyRepos` |
| **Issue** | `create` `get` `update` `close` `reopen` `listOpen` `listByLabel` `search` `addLabels` `addAssignees` `comment` |
| **PullRequest** | `create` `get` `update` `close` `merge` `squashMerge` `listOpen` `search` `requestReview` `isMerged` |
| **Release** | `create` `createDraft` `get` `getByTag` `update` `delete` `list` `uploadAsset` `getLatest` |
| **Action** | `listWorkflows` `dispatch` `listRuns` `getRun` `cancelRun` `rerun` `listJobs` `listArtifacts` |
| **User** | `me` `get` `follow` `unfollow` `listRepos` `listStarred` `listFollowers` `listFollowing` |
| **Organization** | `get` `listMyOrgs` `listMembers` `addMember` `removeMember` `listTeams` `listRepos` `createRepo` |
