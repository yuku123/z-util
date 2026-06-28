# z-util-devops

运维工具箱：Git（JGit + Shell 双实现）、GitHub API 封装、Nexus 组件管理、Docker 客户端、通用 GAV 坐标。

> 包路径：`com.zifang.util.devops.*`

## Features

### 通用工具 (`common`)

- `GavInfo` — Maven GAV（groupId / artifactId / version）封装

### Docker (`docker`)

- `DockerClient` — Docker 操作门面（容器/镜像/网络/卷）
- `DockerCommandClient` — 基于命令行的 Docker 客户端实现
- `DockerCommandResult` — 命令执行返回（stdout / stderr / exitCode）
- **DTO**：`ContainerDTO`、`ImageDTO`、`NetworkDTO`、`VolumeDTO`

### Git 操作 (`git.operations`)

- `GitClient` — 统一门面：clone / pull / push / commit / branch / tag / status / diff / log
- **双实现**：
  - `jgit.JGitExecutor` — 基于 Eclipse JGit（无需本机 git）
  - `shell.ShellExecutor` — 基于本机 `git` 命令（功能最全）
- `GitResult` — 执行结果（成功/失败 + 消息）
- `GitException` — 统一异常
- **领域对象 (`core`)**：
  - `GitRepository` — 仓库表示
  - `GitAuthor` — 提交作者
  - `GitCommit` — 提交
  - `GitBranch` — 分支
  - `GitTag` — 标签
  - `GitRemote` — 远端
  - `GitDiffEntry` — Diff 行
  - `GitStatus` — 工作区状态

### GitHub API (`git.github`)

- `GithubConfig` — 配置（token / baseUrl / org）
- `GithubApiHolder` — HTTP 客户端持有
- `GithubApiWrapper` — 总门面，分发到子模块：
  - `user.UserApiWrapper` — 用户
  - `org.OrganizationApiWrapper` — 组织
  - `repo.RepositoryApiWrapper` — 仓库
  - `pr.PullRequestApiWrapper` — PR
  - `issue.IssueApiWrapper` — Issue
  - `release.ReleaseApiWrapper` — Release
  - `action.ActionApiWrapper` — Actions 工作流

### Nexus (`nexus`)

- `NexusConfig` — Nexus 连接配置（url / user / pass）
- `NexusComponentManager` — 组件上传 / 查询 / 删除
- **领域对象**：
  - `Component` — 组件（对应 GAV 级别）
  - `Asset` — 资产（对应 jar / pom 单文件）
  - `Checksum` — SHA1 / MD5 校验和

## Quick Start

### 1. 克隆一个仓库 + 提交 + 推送

```java
GitClient git = GitClient.jgit()                         // 或 GitClient.shell()
    .auth("user", "token-or-password");

git.clone("https://github.com/me/demo.git", Paths.get("./demo"));

git.commit(Paths.get("./demo"), new GitAuthor("me", "me@x.com"), "feat: xxx");
git.push (Paths.get("./demo"));
```

### 2. 列出一个仓库的所有 PR

```java
GithubConfig cfg = new GithubConfig()
    .token("ghp_xxx")
    .baseUrl("https://api.github.com");

GithubApiWrapper gh = new GithubApiWrapper(cfg);
List<PullRequest> prs = gh.pulls().listOpen("yuku123", "z-util");
for (PullRequest pr : prs) {
    System.out.println(pr.getNumber() + " " + pr.getTitle());
}
```

### 3. 上传一个 jar 到 Nexus

```java
NexusConfig cfg = new NexusConfig()
    .url("https://nexus.example.com")
    .repository("maven-releases")
    .username("deploy")
    .password("***");

NexusComponentManager mgr = new NexusComponentManager(cfg);
mgr.uploadMavenArtifact(
    new GavInfo("com.example", "demo", "1.0.0"),
    Paths.get("./target/demo-1.0.0.jar"),
    Paths.get("./pom.xml")
);
```

### 4. Docker：拉镜像 + 起容器

```java
DockerClient docker = new DockerCommandClient();

docker.pull("openjdk:17-slim");
String cid = docker.run("openjdk:17-slim", Map.of(
    "name", "java-runner",
    "ports", "8080:8080",
    "detach", "true"
));

List<ContainerDTO> running = docker.ps();
```

## Maven

```xml
<dependency>
    <groupId>com.zifang.util</groupId>
    <artifactId>z-util-devops</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```