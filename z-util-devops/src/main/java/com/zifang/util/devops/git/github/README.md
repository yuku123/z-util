# GitHub API 封装

基于 [github-api](https://github.com/kohsuke/github-api) (kohsuke) 的轻量封装，统一入口 `GithubApiWrapper`，告别每次 new
GitHub + Credential 的重复代码。

## 初始化

**方式一：环境变量（推荐）**

```bash
export GITHUB_TOKEN=ghp_xxxxxxxxxxxxxxxxxxxx
```

```java
GithubApiHolder.init(GithubConfig.fromEnv());
```

**方式二：手动传入 Token**

```java
GithubApiHolder.init(GithubConfig.of("ghp_xxxx", "https://api.github.com"));
```

**方式三：Enterprise（私有 GitHub）**

```java
GithubApiHolder.init(GithubConfig.of("ghp_xxxx", "https://github.mycompany.com/api/v3"));
```

## 模块总览

```
GithubApiWrapper          # 统一入口
├── repo()                # RepositoryApiWrapper
├── issue()               # IssueApiWrapper
├── pr()                  # PullRequestApiWrapper
├── release()             # ReleaseApiWrapper
├── action()              # ActionApiWrapper
├── user()                # UserApiWrapper
└── org()                 # OrganizationApiWrapper
```

## Repository 仓库

```java
// 创建仓库
GHRepository repo = GithubApiWrapper.repo()
    .create("my-repo", "Description", false);

// 获取仓库信息
RepositoryApiWrapper.RepositoryInfo info = GithubApiWrapper.repo("owner", "repo").info();

// 列出分支
List<String> branches = GithubApiWrapper.repo("owner", "repo").listBranches();

// 搜索仓库（关键字 + 语言过滤）
List<GHRepository> repos = GithubApiWrapper.repo().search("spring-boot", "Java");

// Fork
GHRepository fork = GithubApiWrapper.repo("owner", "repo").fork();

// Stargazers
List<GHUser> stars = GithubApiWrapper.repo("owner", "repo").listStargazers();
```

## Issue

```java
// 创建 Issue（带标签）
GHIssue issue = GithubApiWrapper.issue("owner", "repo")
    .create("Bug: 登录失败", "复现步骤...", "bug", "priority-high");

// 创建 Issue（指定 assignee）
GHIssue issue = GithubApiWrapper.issue("owner", "repo")
    .create("Task", "Body", "zifang", Arrays.asList("enhancement"));

// 更新 & 关闭
GithubApiWrapper.issue("owner", "repo").update(1, "新标题", "新内容");
GithubApiWrapper.issue("owner", "repo").close(1);

// 搜索 Issue（完整查询语法）
List<GHIssue> bugs = GithubApiWrapper.issue()
    .search("is:issue is:open label:bug repo:owner/repo");

// 添加/移除标签
GithubApiWrapper.issue("owner", "repo").addLabels(1, "bug", "confirmed");

// 添加 assignee
GithubApiWrapper.issue("owner", "repo").addAssignees(1, "zifang", "other");

// 评论
GithubApiWrapper.issue("owner", "repo").comment(1, "This is fixed in #2");

// 列出评论
List<GHIssueComment> comments = GithubApiWrapper.issue("owner", "repo").listComments(1);
```

## Pull Request

```java
// 创建 PR
GHPullRequest pr = GithubApiWrapper.pr("owner", "repo")
    .create("feat: new login", "user:feature-branch", "main", "详见文档");

// 合并 PR
GithubApiWrapper.pr("owner", "repo").merge(1, "Merge feat-login into main");

// 请求 Review
GithubApiWrapper.pr("owner", "repo").requestReview(1, "reviewer1", "reviewer2");

// 列出 Review
List<GHPullRequestReview> reviews = GithubApiWrapper.pr("owner", "repo").listReviews(1);

// 检查是否已合并
boolean merged = GithubApiWrapper.pr("owner", "repo").isMerged(1);
```

## Release

```java
// 创建 Release
GHRelease release = GithubApiWrapper.release("owner", "repo")
    .create("v1.0.0", "v1.0.0 Release", "## Changelog\n- First release");

// 上传 Asset
GHAsset asset = GithubApiWrapper.release("owner", "repo")
    .uploadAsset(release.getId(), new File("/path/to.jar"), "application/java-archive");

// 获取最新 Release
GHRelease latest = GithubApiWrapper.release("owner", "repo").getLatest();

// 列出所有 Release
List<GHRelease> releases = GithubApiWrapper.release("owner", "repo").list();

// 删除 Release
GithubApiWrapper.release("owner", "repo").delete(release.getId());
```

## GitHub Actions

```java
// 触发 workflow（workflow_dispatch）
GithubApiWrapper.action("owner", "repo")
    .dispatch("ci.yml", "main");

// 触发（带输入参数）
GithubApiWrapper.action("owner", "repo")
    .dispatch("deploy.yml", "main", Map.of("env", "production"));

// 列出最近的 Run
List<GHWorkflowRun> runs = GithubApiWrapper.action("owner", "repo").listRuns(10);

// 重跑 Run
GithubApiWrapper.action("owner", "repo").rerun(runId);

// 取消 Run
GithubApiWrapper.action("owner", "repo").cancelRun(runId);

// 获取 Jobs
List<GHWorkflowJob> jobs = GithubApiWrapper.action("owner", "repo").listJobs(runId);

// 列出 Artifacts
List<GHArtifact> artifacts = GithubApiWrapper.action("owner", "repo").listArtifacts(runId);
```

## User

```java
// 当前用户
GHUser me = GithubApiWrapper.user().me();
String login = GithubApiWrapper.user().myLogin();

// 查用户信息
GHUser user = GithubApiWrapper.user().get("kohsuke");

// 用户仓库
List<GHRepository> repos = GithubApiWrapper.user().listRepos("torvalds");

// 用户 Star 的仓库
List<GHRepository> starred = GithubApiWrapper.user().listStarred("kohsuke");

// followers / following
List<GHUser> followers = GithubApiWrapper.user().listFollowers("kohsuke");

// 关注用户
GithubApiWrapper.user().follow("kohsuke");
```

## Organization

```java
// 列出当前用户所属组织
List<GHOrganization> orgs = GithubApiWrapper.org().listMyOrgs();

// 组织成员
List<GHUser> members = GithubApiWrapper.org("my-org").listMembers();

// 检查成员
boolean isMember = GithubApiWrapper.org("my-org").isMember("zifang");

// 创建 Team（带仓库）
GHTeam team = GithubApiWrapper.org("my-org")
    .createTeam("backend", "owner/repo1", "owner/repo2");

// 给 Team 加人
GithubApiWrapper.org("my-org").addTeamMember(team.getId(), "zifang");

// 组织仓库
List<GHRepository> repos = GithubApiWrapper.org("my-org").listRepos();
```

## 注意事项

### 编译问题

`GroupApiManager.java`（GitLab 相关）依赖不存在的 `GitlabApiWrapper`，编译时会报错。临时排除：

```bash
# 方法：删除或移走 GroupApiManager.java
mv src/main/java/com/zifang/util/devops/git/GroupApiManager.java /tmp/

# 然后正常编译
mvn compile -pl z-util-devops
```

### Rate Limit

GitHub API 有速率限制（未认证 60req/hr，OAuth 5000req/hr）。使用 `GithubApiHolder` 单例复用连接，自动处理 Token 认证。

### 异常处理

所有 API 调用声明 `throws IOException`，调用方需自行 try-catch 或向上抛出。

## 依赖

```xml
<dependency>
    <groupId>org.kohsuke</groupId>
    <artifactId>github-api</artifactId>
    <version>1.135</version>
</dependency>
```
