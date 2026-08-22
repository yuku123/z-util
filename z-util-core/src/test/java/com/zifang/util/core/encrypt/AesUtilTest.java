package com.zifang.util.core.encrypt;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * AesUtil工具类的单元测试
 */
public class AesUtilTest {

    private static final String PASSWORD = "z-util-test-password";

    /**
     * testEncryptDecryptToString方法：字符串加解密往返一致（含中文与特殊字符）。
     */
    @Test
    public void testEncryptDecryptToString() {
        String[] samples = {"hello world", "中文内容测试", "a=b&c=d%20e", "line1\nline2\t"};
        for (String sample : samples) {
            String encrypted = AesUtil.encryptToString(sample, PASSWORD);
            assertEquals(sample, AesUtil.decryptToString(encrypted, PASSWORD));
        }
    }

    /**
     * testEncryptDecryptEmptyString方法：空字符串可加解密往返。
     */
    @Test
    public void testEncryptDecryptEmptyString() {
        String encrypted = AesUtil.encryptToString("", PASSWORD);
        assertEquals("", AesUtil.decryptToString(encrypted, PASSWORD));
    }

    /**
     * testEncryptDecryptBytes方法：字节数组加解密往返一致。
     */
    @Test
    public void testEncryptDecryptBytes() {
        byte[] data = new byte[]{0x00, 0x01, 0x7F, (byte) 0x80, (byte) 0xFF};
        byte[] encrypted = AesUtil.encrypt(data, PASSWORD);
        byte[] decrypted = AesUtil.decrypt(encrypted, PASSWORD);
        assertTrue(java.util.Arrays.equals(data, decrypted));
    }

    /**
     * testEncryptDeterministic方法：同一口令对同一明文的加密结果确定。
     */
    @Test
    public void testEncryptDeterministic() {
        String first = AesUtil.encryptToString("deterministic", PASSWORD);
        String second = AesUtil.encryptToString("deterministic", PASSWORD);
        assertEquals(first, second);
    }

    /**
     * testDifferentPassword方法：不同口令派生不同密钥，密文不同。
     */
    @Test
    public void testDifferentPassword() {
        String first = AesUtil.encryptToString("content", PASSWORD);
        String second = AesUtil.encryptToString("content", "another-password");
        assertNotEquals(first, second);
    }

    /**
     * testDecryptWithWrongPassword方法：口令不一致时解密失败。
     */
    @Test(expected = RuntimeException.class)
    public void testDecryptWithWrongPassword() {
        String encrypted = AesUtil.encryptToString("secret", PASSWORD);
        AesUtil.decryptToString(encrypted, "wrong-password");
    }

    /**
     * testDecryptWithInvalidHex方法：非法十六进制密文抛出 IllegalArgumentException。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecryptWithInvalidHex() {
        AesUtil.decryptToString("not-hex", PASSWORD);
    }

    /**
     * testDecryptWithOddLengthHex方法：奇数长度十六进制串抛出 IllegalArgumentException。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecryptWithOddLengthHex() {
        AesUtil.decryptToString("ABC", PASSWORD);
    }

    /**
     * testEncryptWithNullPassword方法：口令为 null 抛出 IllegalArgumentException。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncryptWithNullPassword() {
        AesUtil.encryptToString("content", null);
    }
}
