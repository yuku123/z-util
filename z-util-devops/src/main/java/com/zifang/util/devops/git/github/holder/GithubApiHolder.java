package com.zifang.util.devops.git.github.holder;

import com.zifang.util.devops.git.github.config.GithubConfig;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

/**
 * GitHub 客户端单例持有者
 */
public class GithubApiHolder {

    public static GithubApiHolder INSTANCE = new GithubApiHolder(null);

    private final GitHub github;
    private final GithubConfig config;

    private GithubApiHolder(GithubConfig config) {
        this.config = config;
        this.github = config == null ? null : build(config);
    }

    private GithubApiHolder() {
        this(GithubConfig.fromEnv());
    }

    private GitHub build(GithubConfig config) {
        try {
            GitHubBuilder builder = new GitHubBuilder()
                    .withOAuthToken(config.getToken());
            if (config.getApiUrl() != null && !config.getApiUrl().equals("https://api.github.com")) {
                builder.withEndpoint(config.getApiUrl());
            }
            return builder.build();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to build GitHub client", e);
        }
    }

    public GitHub getGithub() {
        return github;
    }

    public GithubConfig getConfig() {
        return config;
    }

    public static GithubApiHolder getInstance() {
        return INSTANCE;
    }

    public static void init(GithubConfig config) {
        if (INSTANCE.config != null) {
            throw new IllegalStateException("GithubApiHolder has already been initialized");
        }
        INSTANCE = new GithubApiHolder(config);
    }

    public static void reset() {
        INSTANCE = new GithubApiHolder(null);
    }
}
