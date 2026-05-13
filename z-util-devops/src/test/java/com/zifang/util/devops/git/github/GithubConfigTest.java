package com.zifang.util.devops.git.github;

import com.zifang.util.devops.git.github.config.GithubConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class GithubConfigTest {

    @Test
    public void testOfWithTokenOnly() {
        GithubConfig config = GithubConfig.of("my-secret-token");
        assertEquals("my-secret-token", config.getToken());
        assertEquals("https://api.github.com", config.getApiUrl());
    }

    @Test
    public void testOfWithTokenAndApiUrl() {
        GithubConfig config = GithubConfig.of("token", "https://github.example.com/api/v3");
        assertEquals("token", config.getToken());
        assertEquals("https://github.example.com/api/v3", config.getApiUrl());
    }

    @Test
    public void testFromEnvThrowsWhenNotSet() {
        String originalToken = System.getenv("GITHUB_TOKEN");
        if (originalToken != null) {
            // Only test if GITHUB_TOKEN happens to be set in this environment
            return;
        }
        try {
            GithubConfig.fromEnv();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("GITHUB_TOKEN"));
        }
    }
}
