package com.zifang.util.core.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * AES加解密工具类。
 * <p>
 * 以口令作为 SHA1PRNG 随机源种子派生密钥，同一口令派生出确定的密钥，
 * 密文以十六进制字符串（大写）编码，适合口令对称加解密场景。
 */
public final class AesUtil {

    /**
     * 密钥位数
     */
    private static final int KEY_SIZE = 256;

    /**
     * 加密算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 密钥派生随机源算法
     */
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";

    /**
     * 十六进制字符表（大写）
     */
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private AesUtil() {
    }

    /**
     * encrypt方法。
     * AES 加密字节数组。
     *
     * @param data     byte[]类型参数，待加密数据
     * @param password String类型参数，派生密钥的口令
     * @return static byte[]类型返回值，密文字节数组
     * @throws RuntimeException 加密失败或口令为 null 时抛出
     */
    public static byte[] encrypt(byte[] data, String password) {
        if (data == null || password == null) {
            throw new IllegalArgumentException("data and password must not be null");
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, deriveKey(password));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("AES encrypt error", e);
        }
    }

    /**
     * decrypt方法。
     * AES 解密字节数组。
     *
     * @param data     byte[]类型参数，待解密密文
     * @param password String类型参数，派生密钥的口令
     * @return static byte[]类型返回值，明文字节数组
     * @throws RuntimeException 解密失败（口令不一致、密文损坏等）或口令为 null 时抛出
     */
    public static byte[] decrypt(byte[] data, String password) {
        if (data == null || password == null) {
            throw new IllegalArgumentException("data and password must not be null");
        }
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, deriveKey(password));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("AES decrypt error", e);
        }
    }

    /**
     * encryptToString方法。
     * AES 加密字符串，密文以十六进制（大写）编码。
     *
     * @param content  String类型参数，明文
     * @param password String类型参数，派生密钥的口令
     * @return static String类型返回值，十六进制密文字符串
     */
    public static String encryptToString(String content, String password) {
        return encodeHex(encrypt(content.getBytes(StandardCharsets.UTF_8), password));
    }

    /**
     * decryptToString方法。
     * AES 解密十六进制编码的密文字符串。
     *
     * @param content  String类型参数，十六进制密文字符串（大小写均可）
     * @param password String类型参数，派生密钥的口令
     * @return static String类型返回值，明文（UTF-8）
     */
    public static String decryptToString(String content, String password) {
        return new String(decrypt(decodeHex(content), password), StandardCharsets.UTF_8);
    }

    /**
     * deriveKey方法。
     * 以口令作为 SHA1PRNG 种子派生 AES 密钥。
     */
    private static SecretKeySpec deriveKey(String password) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance(RANDOM_ALGORITHM);
        secureRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
        keyGenerator.init(KEY_SIZE, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
    }

    /**
     * encodeHex方法。
     * 字节数组编码为十六进制字符串（大写）。
     */
    private static String encodeHex(byte[] data) {
        char[] out = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            int value = data[i] & 0xFF;
            out[i * 2] = HEX_CHARS[value >>> 4];
            out[i * 2 + 1] = HEX_CHARS[value & 0x0F];
        }
        return new String(out);
    }

    /**
     * decodeHex方法。
     * 十六进制字符串解码为字节数组（兼容大小写）。
     */
    private static byte[] decodeHex(String hex) {
        if (hex == null || hex.length() == 0) {
            throw new IllegalArgumentException("hex string must not be empty");
        }
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("hex string length must be even: " + hex);
        }
        byte[] out = new byte[hex.length() / 2];
        for (int i = 0; i < out.length; i++) {
            int high = Character.digit(hex.charAt(i * 2), 16);
            int low = Character.digit(hex.charAt(i * 2 + 1), 16);
            if (high < 0 || low < 0) {
                throw new IllegalArgumentException("illegal hex string: " + hex);
            }
            out[i] = (byte) ((high << 4) | low);
        }
        return out;
    }
}
