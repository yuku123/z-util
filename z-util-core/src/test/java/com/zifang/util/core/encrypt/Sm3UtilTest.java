package com.zifang.util.core.encrypt;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Sm3UtilTest类。
 * 国密 SM3 摘要与 HMAC-SM3 测试。
 */
public class Sm3UtilTest {

    /**
     * GB/T 32905-2016 标准向量：SM3("abc")
     */
    private static final String SM3_ABC = "66c7f0f462eeedd9d1f2d46bdc10e4e24167c4875cf2f7a2297da02b8f4ba8e0";

    /**
     * testDigestKnownVector方法：标准向量 SM3("abc") 与摘要长度。
     */
    @Test
    public void testDigestKnownVector() {
        assertEquals(SM3_ABC, Sm3Util.digestHex("abc"));
        assertEquals(32, Sm3Util.digest("abc".getBytes(StandardCharsets.UTF_8)).length);
        // UTF-8 中文参与摘要
        assertEquals(64, Sm3Util.digestHex("国密摘要测试").length());
    }

    /**
     * testDigestDeterministic方法：同输入摘要一致，异输入摘要不同。
     */
    @Test
    public void testDigestDeterministic() {
        byte[] data = "sm3-deterministic-测试".getBytes(StandardCharsets.UTF_8);
        assertTrue(Arrays.equals(Sm3Util.digest(data), Sm3Util.digest(data)));
        assertFalse(Arrays.equals(Sm3Util.digest(data), Sm3Util.digest("other".getBytes(StandardCharsets.UTF_8))));
        // null 视为空数据，与空数组等价
        assertTrue(Arrays.equals(Sm3Util.digest(null), Sm3Util.digest(new byte[0])));
    }

    /**
     * testDigestHexCase方法：小写与大写十六进制形态。
     */
    @Test
    public void testDigestHexCase() {
        String lower = Sm3Util.digestHex("abc");
        String upper = Sm3Util.digestHexUpper("abc");
        assertTrue(lower.matches("[0-9a-f]{64}"));
        assertEquals(lower.toUpperCase(), upper);
        assertTrue(upper.matches("[0-9A-F]{64}"));
    }

    /**
     * testHmacSm3方法：认证码长度、确定性与密钥区分性。
     */
    @Test
    public void testHmacSm3() {
        byte[] message = "hmac-sm3-message".getBytes(StandardCharsets.UTF_8);
        byte[] key1 = "secret-key-001".getBytes(StandardCharsets.UTF_8);
        byte[] key2 = "secret-key-002".getBytes(StandardCharsets.UTF_8);
        // 长度 32 字节
        assertEquals(32, Sm3Util.hmacSm3(message, key1).length);
        // 同密钥确定
        assertTrue(Arrays.equals(Sm3Util.hmacSm3(message, key1), Sm3Util.hmacSm3(message, key1)));
        // 不同密钥结果不同
        assertFalse(Arrays.equals(Sm3Util.hmacSm3(message, key1), Sm3Util.hmacSm3(message, key2)));
        // 与裸摘要不同
        assertFalse(Arrays.equals(Sm3Util.hmacSm3(message, key1), Sm3Util.digest(message)));
    }

    /**
     * testHmacSm3HexUpper方法：文本形态认证码为大写十六进制。
     */
    @Test
    public void testHmacSm3HexUpper() {
        String mac = Sm3Util.hmacSm3HexUpper("data=1&ts=2", "platform-secret");
        assertTrue(mac.matches("[0-9A-F]{64}"));
        // 同输入确定
        assertEquals(mac, Sm3Util.hmacSm3HexUpper("data=1&ts=2", "platform-secret"));
        // 不同密钥不同结果
        assertFalse(mac.equals(Sm3Util.hmacSm3HexUpper("data=1&ts=2", "other-secret")));
    }

    /**
     * testHmacNullKey方法：null 密钥抛出 IllegalArgumentException。
     */
    @Test
    public void testHmacNullKey() {
        try {
            Sm3Util.hmacSm3("m".getBytes(StandardCharsets.UTF_8), null);
            fail("expected IllegalArgumentException for null key");
        } catch (IllegalArgumentException ignored) {
            // 预期
        }
    }
}
