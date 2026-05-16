package com.zifang.util.devops.git.github.config;

import org.apache.commons.lang3.StringUtils;

/**
 * GitHub API 配置
 */
public class GithubConfig {

    /**
     * GitHub Personal Access Token
     */
    private final String token;

    /**
     * GitHub Enterprise API URL（可选，默认为 https://api.github.com）
     */
    private final String apiUrl;

    private GithubConfig(String token, String apiUrl) {
        this.token = token;
        this.apiUrl = apiUrl;
    }

    public String getToken() {
        return token;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public static GithubConfig of(String token) {
        return new GithubConfig(token, "https://api.github.com");
    }

    public static GithubConfig of(String token, String apiUrl) {
        return new GithubConfig(token, apiUrl);
    }

    /**
     * 从环境变量 GITHUB_TOKEN 构建
     */
    public static GithubConfig fromEnv() {
        String token = System.getenv("GITHUB_TOKEN");
        if (token == null || StringUtils.isBlank(token)) {
            throw new IllegalStateException("GITHUB_TOKEN environment variable is not set");
        }
        return of(token);
    }
}
