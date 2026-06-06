# git operations（JGit + shell 兜底）

位于 `com.zifang.util.devops.git.operations`，提供 Java 编程式调用 git 的统一入口。

## 设计

- **JGit 为主**：所有常规操作（init / clone / add / commit / push / pull / log / diff / branch / checkout / merge / rebase / tag / remote）走 Eclipse JGit 5.13.3（纯 Java，无外部依赖）
- **shell 兜底**：JGit 弱支持或不支持的操作（`stash` / `clean` / `blame` / `archive` / `worktree`）走系统 `git` CLI
- **统一返回**：所有方法返回 `GitResult<T>`，封装成功/失败/退出码/标准输出/标准错误；不直接抛异常
- **DTO 全部在 `core` 子包**，主类 `GitClient` 是纯静态 facade

## 快速上手

```java
import com.zifang.util.devops.git.operations.GitClient;
import com.zifang.util.devops.git.operations.GitResult;
import com.zifang.util.devops.git.operations.core.*;

// 克隆
GitResult<GitRepository> r = GitClient.clone(
        "https://github.com/x/y.git",
        new File("/tmp/y"),
        null, null);
if (!r.isSuccess()) { log(r.getStderr()); return; }
GitRepository repo = r.getData();

// 写文件并提交
new File(repo.getWorkDir(), "hello.txt").createNewFile();
GitClient.addAll(repo);
GitResult<String> cmt = GitClient.commit(
        repo,
        new GitAuthor("zifang", "z@x.com"),
        "first commit");
System.out.println("new commit: " + cmt.getData());

// 推送（带凭证）
GitClient.push(repo, "origin", "main", "user", "token");

// 查日志
for (GitCommit c : GitClient.log(repo, 20).getData()) {
    System.out.println(c.getShortSha() + " " + c.getShortMessage());
}

// 查工作区状态
GitStatus st = GitClient.status(repo).getData();
System.out.println("branch: " + st.getBranch() + ", clean: " + st.isClean());
```

## 支持的操作

### 仓库生命周期
- `init(path|File)`
- `open(path|File)`
- `clone(url, target, [user, pwd])` — JGit 失败时自动回退 shell

### 工作区
- `add(repo, patterns...)` / `addAll(repo)`
- `rm(repo, cached, patterns...)`
- `status(repo)`
- `diff(repo)` — 工作区 vs 索引
- `diffCached(repo)` — 索引 vs HEAD
- `diff(repo, oldRef, newRef)` — ref vs ref

### 提交
- `commit(repo, message)` — 用 git config 的 user.name/email
- `commit(repo, author, message)`
- `commit(repo, author, message, all)` — `all=true` 等同 `-a`
- `reset(repo, ref, mode)` — mode: soft/mixed/hard/keep/merge
- `log(repo[, maxCount])` / `show(repo, ref)`

### 分支
- `branchList(repo)` / `branchListLocal` / `branchListRemote`
- `branchCreate(repo, name)`
- `branchDelete(repo, name, force)`
- `checkout(repo, ref)` / `checkoutNewBranch(repo, name)`
- `currentBranch(repo)`

### 远程 & 同步
- `remoteList` / `remoteAdd` / `remoteRemove`
- `fetch(repo, remote[, user, pwd])`
- `pull(repo, remote, branch[, user, pwd])`
- `push(repo, remote, refspec[, user, pwd])`

### 合并 / 变基
- `merge(repo, ref)`
- `rebase(repo, upstream)`

### Tag
- `tagList(repo)`
- `tagCreate(repo, name[, ref])` — 轻量
- `tagCreateAnnotated(repo, name, message[, ref])`
- `tagDelete(repo, name)`

### Shell 兜底区
- `clean(repo, force, includeIgnored)`
- `stash(repo, includeUntracked, message)` / `stashPop` / `stashList`
- `blame(repo, path)`
- `archive(repo, ref, format, outputPath)`
- `worktreeAdd` / `worktreeList` / `worktreeRemove`
- `execRaw(...)` — 任意 git 命令逃生口

## 异常 & 错误

- 业务失败（仓库不存在、commit 冲突等）→ `GitResult.isSuccess() = false`，通过 `getStderr()` / `getMessage()` 查看原因
- 内部错误（IO 失败、参数严重非法）→ `GitException`（RuntimeException），通过 `getResult()` 拿到上下文

## 依赖

- `org.eclipse.jgit:org.eclipse.jgit:5.13.3.202401111512-r`（pom.xml 已加）
- 原有 z-util-core / github-api / gitlab4j-api / unirest 不受影响
