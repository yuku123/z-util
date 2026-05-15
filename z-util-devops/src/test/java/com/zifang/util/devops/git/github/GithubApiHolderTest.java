package com.zifang.util.devops.git.github;

import com.zifang.util.devops.git.github.config.GithubConfig;
import com.zifang.util.devops.git.github.holder.GithubApiHolder;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * GithubApiHolder 单例持有者测试
 */
public class GithubApiHolderTest {

    @After
    public void tearDown() {
        // 恢复默认实例，避免影响其他测试
        try {
            GithubApiHolder.reset();
        } catch (Exception ignored) {
        }
    }

    @Test
    public void testGetInstance() {
        GithubApiHolder instance = GithubApiHolder.getInstance();
        assertNotNull(instance);
    }

    @Test
    public void testGetInstanceReturnsSameInstance() {
        GithubApiHolder a = GithubApiHolder.getInstance();
        GithubApiHolder b = GithubApiHolder.getInstance();
        assertSame(a, b);
    }

    @Test
    public void testInitWithConfig() {
        GithubConfig config = GithubConfig.of("test-token", "https://api.github.com");
        GithubApiHolder.init(config);

        GithubApiHolder holder = GithubApiHolder.getInstance();
        assertNotNull(holder.getGithub());
        assertNotNull(holder.getConfig());
        assertEquals("test-token", holder.getConfig().getToken());
        assertEquals("https://api.github.com", holder.getConfig().getApiUrl());
    }

    @Test
    public void testInitThrowsWhenAlreadyInitialized() {
        GithubConfig config = GithubConfig.of("test-token", "https://api.github.com");
        GithubApiHolder.init(config);

        try {
            GithubApiHolder.init(config);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("already been initialized"));
        }
    }

    @Test
    public void testReset() {
        GithubConfig config = GithubConfig.of("test-token", "https://api.github.com");
        GithubApiHolder.init(config);
        GithubApiHolder.reset();

        GithubApiHolder newInstance = GithubApiHolder.getInstance();
        assertNotNull(newInstance);
    }

    @Test
    public void testGetGithub() {
        GithubConfig config = GithubConfig.of("test-token");
        GithubApiHolder.init(config);
        assertNotNull(GithubApiHolder.getInstance().getGithub());
    }

    @Test
    public void testGetConfig() {
        GithubConfig config = GithubConfig.of("my-token", "https://github.example.com/api/v3");
        GithubApiHolder.init(config);
        assertEquals(config, GithubApiHolder.getInstance().getConfig());
    }

    @Test
    public void testInitWithEnterpriseUrl() {
        GithubConfig config = GithubConfig.of("enterprise-token", "https://github.mycompany.com/api/v3");
        GithubApiHolder.init(config);

        assertEquals("enterprise-token", holder().getConfig().getToken());
        assertEquals("https://github.mycompany.com/api/v3", holder().getConfig().getApiUrl());
    }

    private GithubApiHolder holder() {
        return GithubApiHolder.getInstance();
    }
}
