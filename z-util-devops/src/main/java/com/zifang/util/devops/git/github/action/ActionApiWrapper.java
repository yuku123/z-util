package com.zifang.util.devops.git.github.action;

import org.kohsuke.github.GHArtifact;
import org.kohsuke.github.GHWorkflow;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;
import org.kohsuke.github.PagedIterable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GitHub Actions API 封装
 * <p>
 * 提供GitHub Actions相关操作的封装，
 * 包括Workflow管理、Workflow Run执行、Job查询、Artifact管理等。
 *
 * @author zifang
 * @version 1.0.0
 */
/**
 * ActionApiWrapper类。
 */
/**
 * ActionApiWrapper类。
 */
public class ActionApiWrapper {

    private final GitHub github;
    private String owner;
    private String repo;

    /**
     * ActionApiWrapper方法。
     *      * @param github GitHub类型参数
     */
    /**
     * ActionApiWrapper方法。
     *      * @param github GitHub类型参数
     */
    public ActionApiWrapper(GitHub github) {
        this.github = github;
    }

    /**
     * ActionApiWrapper方法。
     *      * @param github GitHub类型参数
     * @param owner String类型参数
     * @param repo String类型参数
     */
    /**
     * ActionApiWrapper方法。
     *      * @param github GitHub类型参数
     * @param owner String类型参数
     * @param repo String类型参数
     */
    public ActionApiWrapper(GitHub github, String owner, String repo) {
        this.github = github;
        this.owner = owner;
        this.repo = repo;
    }

    /**
     * withRepo方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return ActionApiWrapper类型返回值
     */
    /**
     * withRepo方法。
     *      * @param owner String类型参数
     * @param repo String类型参数
     * @return ActionApiWrapper类型返回值
     */
    public ActionApiWrapper withRepo(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
        return this;
    }

    private String fullName() {
        return owner + "/" + repo;
    }

    // ==================== Workflow ====================

    /**
     * 列出所有 Workflow
     */
    /**
     * listWorkflows方法。
     * @return List<GHWorkflow>类型返回值
     */
    /**
     * listWorkflows方法。
     * @return List<GHWorkflow>类型返回值
     */
    public List<GHWorkflow> listWorkflows() throws IOException {
        List<GHWorkflow> workflows = new ArrayList<>();
        PagedIterator<GHWorkflow> it = github.getRepository(fullName()).listWorkflows().iterator();
        while (it.hasNext()) {
            workflows.add(it.next());
        }
        return workflows;
    }

    /**
     * 获取 Workflow（通过文件名，如 "ci.yml"）
     */
    /**
     * getWorkflow方法。
     *      * @param workflowFileName String类型参数
     * @return GHWorkflow类型返回值
     */
    /**
     * getWorkflow方法。
     *      * @param workflowFileName String类型参数
     * @return GHWorkflow类型返回值
     */
    public GHWorkflow getWorkflow(String workflowFileName) throws IOException {
        return github.getRepository(fullName()).getWorkflow(workflowFileName);
    }

    /**
     * 触发 Workflow（workflow_dispatch）
     */
    /**
     * dispatch方法。
     *      * @param workflowFileName String类型参数
     * @param ref String类型参数
     */
    /**
     * dispatch方法。
     *      * @param workflowFileName String类型参数
     * @param ref String类型参数
     */
    public void dispatch(String workflowFileName, String ref) throws IOException {
        github.getRepository(fullName()).getWorkflow(workflowFileName).dispatch(ref);
    }

    /**
     * 触发 Workflow（带输入参数）
     */
    /**
     * dispatch方法。
     *      * @param workflowFileName String类型参数
     * @param ref String类型参数
     * @param inputs java.util.MapString,类型参数
     */
    /**
     * dispatch方法。
     *      * @param workflowFileName String类型参数
     * @param ref String类型参数
     * @param inputs java.util.MapString,类型参数
     */
    public void dispatch(String workflowFileName, String ref, java.util.Map<String, Object> inputs) throws IOException {
        github.getRepository(fullName()).getWorkflow(workflowFileName).dispatch(ref, inputs);
    }

    /**
     * 禁用 Workflow
     */
    /**
     * disable方法。
     *      * @param workflowFileName String类型参数
     */
    /**
     * disable方法。
     *      * @param workflowFileName String类型参数
     */
    public void disable(String workflowFileName) throws IOException {
        github.getRepository(fullName()).getWorkflow(workflowFileName).disable();
    }

    /**
     * 启用 Workflow
     */
    /**
     * enable方法。
     *      * @param workflowFileName String类型参数
     */
    /**
     * enable方法。
     *      * @param workflowFileName String类型参数
     */
    public void enable(String workflowFileName) throws IOException {
        github.getRepository(fullName()).getWorkflow(workflowFileName).enable();
    }

    // ==================== Workflow Run ====================

    /**
     * 列出最近的 Workflow Run
     */
    /**
     * listRuns方法。
     *      * @param limit int类型参数
     * @return List<GHWorkflowRun>类型返回值
     */
    /**
     * listRuns方法。
     *      * @param limit int类型参数
     * @return List<GHWorkflowRun>类型返回值
     */
    public List<GHWorkflowRun> listRuns(int limit) throws IOException {
        List<GHWorkflowRun> result = new ArrayList<>();
        PagedIterator<GHWorkflowRun> it = github.getRepository(fullName()).queryWorkflowRuns().list().iterator();
        int count = 0;
        while (it.hasNext() && count < limit) {
            result.add(it.next());
            count++;
        }
        return result;
    }

    /**
     * 获取 Workflow Run
     */
    /**
     * getRun方法。
     *      * @param runId long类型参数
     * @return GHWorkflowRun类型返回值
     */
    /**
     * getRun方法。
     *      * @param runId long类型参数
     * @return GHWorkflowRun类型返回值
     */
    public GHWorkflowRun getRun(long runId) throws IOException {
        return github.getRepository(fullName()).getWorkflowRun(runId);
    }

    /**
     * 取消 Workflow Run
     */
    /**
     * cancelRun方法。
     *      * @param runId long类型参数
     */
    /**
     * cancelRun方法。
     *      * @param runId long类型参数
     */
    public void cancelRun(long runId) throws IOException {
        getRun(runId).cancel();
    }

    /**
     * 重跑 Workflow Run
     */
    /**
     * rerun方法。
     *      * @param runId long类型参数
     */
    /**
     * rerun方法。
     *      * @param runId long类型参数
     */
    public void rerun(long runId) throws IOException {
        getRun(runId).rerun();
    }

    /**
     * 删除 Workflow Run
     */
    /**
     * deleteRun方法。
     *      * @param runId long类型参数
     */
    /**
     * deleteRun方法。
     *      * @param runId long类型参数
     */
    public void deleteRun(long runId) throws IOException {
        getRun(runId).delete();
    }

    /**
     * 获取 Run 的 Jobs
     */
    /**
     * listJobs方法。
     *      * @param runId long类型参数
     * @return List<GHWorkflowJob>类型返回值
     */
    /**
     * listJobs方法。
     *      * @param runId long类型参数
     * @return List<GHWorkflowJob>类型返回值
     */
    public List<GHWorkflowJob> listJobs(long runId) throws IOException {
        List<GHWorkflowJob> jobs = new ArrayList<>();
        PagedIterable<GHWorkflowJob> iterable = getRun(runId).listJobs();
        for (GHWorkflowJob j : iterable) {
            jobs.add(j);
        }
        return jobs;
    }

    // ==================== Artifact ====================

    /**
     * 列出 Run 的 Artifacts
     */
    /**
     * listArtifacts方法。
     *      * @param runId long类型参数
     * @return List<GHArtifact>类型返回值
     */
    /**
     * listArtifacts方法。
     *      * @param runId long类型参数
     * @return List<GHArtifact>类型返回值
     */
    public List<GHArtifact> listArtifacts(long runId) throws IOException {
        List<GHArtifact> artifacts = new ArrayList<>();
        PagedIterable<GHArtifact> iterable = getRun(runId).listArtifacts();
        for (GHArtifact a : iterable) {
            artifacts.add(a);
        }
        return artifacts;
    }
}
