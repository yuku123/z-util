package com.zifang.util.core.encrypt;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Sm4UtilTest类。
 * 国密 SM4 ECB 模式加解密测试。
 */
public class Sm4UtilTest {

    /**
     * testGenerateKey方法：生成 16 字节随机密钥，两次生成内容不同。
     */
    @Test
    public void testGenerateKey() {
        assertEquals(16, Sm4Util.generateKey().length);
        assertFalse(Arrays.equals(Sm4Util.generateKey(), Sm4Util.generateKey()));
    }

    /**
     * testEncryptDecryptEcb方法：字节形态往返加解密，同密钥加密结果确定。
     */
    @Test
    public void testEncryptDecryptEcb() {
        byte[] key = Sm4Util.generateKey();
        byte[] data = "sm4-ecb-round-trip-测试".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] encrypted = Sm4Util.encryptEcb(data, key);
        // 同 key 加密结果确定
        assertTrue(Arrays.equals(encrypted, Sm4Util.encryptEcb(data, key)));
        // 解密得到原文
        assertTrue(Arrays.equals(data, Sm4Util.decryptEcb(encrypted, key)));
    }

    /**
     * testEncryptDecryptEcbHex方法：十六进制文本形态往返加解密，中文与混合内容。
     */
    @Test
    public void testEncryptDecryptEcbHex() {
        byte[] key = Sm4Util.generateKey();
        // byte[] 转 32 位十六进制密钥串
        StringBuilder hexKey = new StringBuilder();
        for (byte b : key) {
            hexKey.append(String.format("%02x", b));
        }
        String plain = "{\"name\":\"测试\",\"id\":123}";
        String cipher = Sm4Util.encryptEcbToHex(plain, hexKey.toString());
        // 密文为十六进制字符
        assertTrue(cipher.matches("[0-9a-f]+"));
        assertEquals(plain, Sm4Util.decryptEcbFromHex(cipher, hexKey.toString()));
        // 大写密钥兼容
        assertEquals(plain, Sm4Util.decryptEcbFromHex(cipher, hexKey.toString().toUpperCase()));
    }

    /**
     * testEcbDifferentKey方法：不同密钥产生不同密文。
     */
    @Test
    public void testEcbDifferentKey() {
        byte[] key1 = Sm4Util.generateKey();
        byte[] key2 = Sm4Util.generateKey();
        byte[] data = "same-plain".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        assertFalse(Arrays.equals(Sm4Util.encryptEcb(data, key1), Sm4Util.encryptEcb(data, key2)));
    }

    /**
     * testEcbInvalidKeyOrHex方法：非法密钥长度与非法十六进制串抛出 IllegalArgumentException。
     */
    @Test
    public void testEcbInvalidKeyOrHex() {
        byte[] key = Sm4Util.generateKey();
        byte[] data = new byte[]{1, 2, 3};
        try {
            Sm4Util.encryptEcb(data, new byte[10]);
            fail("expected IllegalArgumentException for bad key length");
        } catch (IllegalArgumentException ignored) {
            // 预期
        }
        try {
            Sm4Util.encryptEcbToHex("plain", "abc");
            fail("expected IllegalArgumentException for odd hex key");
        } catch (IllegalArgumentException ignored) {
            // 预期
        }
        try {
            Sm4Util.encryptEcbToHex("plain", "zz" + String.format("%030d", 0));
            fail("expected IllegalArgumentException for illegal hex chars");
        } catch (IllegalArgumentException ignored) {
            // 预期
        }
    }
}
