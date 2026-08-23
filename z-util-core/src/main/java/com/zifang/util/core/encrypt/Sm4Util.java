package com.zifang.util.core.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;

/**
 * Sm4Util类。
 * 国密 SM4 对称加解密工具（ECB 模式，PKCS7Padding 填充），基于 BouncyCastle 提供者。
 * <p>
 * 密钥固定 128 位（16 字节）；文本形态以十六进制编码（兼容大小写），
 * 适合国密合规场景的对称加解密。
 */
public final class Sm4Util {

    /**
     * 算法名称
     */
    private static final String ALGORITHM = "SM4";

    /**
     * ECB 模式变换（SM4/ECB/PKCS7Padding）
     */
    private static final String ECB_TRANSFORMATION = "SM4/ECB/PKCS7Padding";

    /**
     * 密钥字节数（128 位）
     */
    private static final int KEY_LENGTH = 16;

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Sm4Util() {
    }

    /**
     * generateKey方法。
     * 生成 128 位随机 SM4 密钥。
     *
     * @return byte[]类型返回值，长度固定 16 字节
     */
    public static byte[] generateKey() {
        byte[] key = new byte[KEY_LENGTH];
        new SecureRandom().nextBytes(key);
        return key;
    }

    /**
     * encryptEcb方法。
     * SM4/ECB/PKCS7Padding 加密。
     *
     * @param data 明文字节
     * @param key  密钥，长度必须为 16 字节
     * @return byte[]类型返回值，密文字节
     */
    public static byte[] encryptEcb(byte[] data, byte[] key) {
        return doEcb(Cipher.ENCRYPT_MODE, data, key);
    }

    /**
     * decryptEcb方法。
     * SM4/ECB/PKCS7Padding 解密。
     *
     * @param data 密文字节
     * @param key  密钥，长度必须为 16 字节
     * @return byte[]类型返回值，明文字节
     */
    public static byte[] decryptEcb(byte[] data, byte[] key) {
        return doEcb(Cipher.DECRYPT_MODE, data, key);
    }

    /**
     * encryptEcbToHex方法。
     * 文本形态加密：明文按 UTF-8 编码加密，密文以十六进制小写输出。
     *
     * @param plainText 明文
     * @param hexKey    十六进制密钥串（32 位，兼容大小写），解码后须为 16 字节
     * @return String类型返回值，十六进制密文
     */
    public static String encryptEcbToHex(String plainText, String hexKey) {
        byte[] cipher = encryptEcb(plainText.getBytes(StandardCharsets.UTF_8), decodeHex(hexKey));
        return encodeHex(cipher);
    }

    /**
     * decryptEcbFromHex方法。
     * 文本形态解密：十六进制密文解码解密，明文按 UTF-8 还原。
     *
     * @param hexCipher 十六进制密文串（兼容大小写）
     * @param hexKey    十六进制密钥串（32 位，兼容大小写），解码后须为 16 字节
     * @return String类型返回值，明文
     */
    public static String decryptEcbFromHex(String hexCipher, String hexKey) {
        byte[] plain = decryptEcb(decodeHex(hexCipher), decodeHex(hexKey));
        return new String(plain, StandardCharsets.UTF_8);
    }

    /**
     * doEcb方法。
     * ECB 模式加解密共用实现。
     */
    private static byte[] doEcb(int cipherMode, byte[] data, byte[] key) {
        if (data == null || key == null) {
            throw new IllegalArgumentException("data and key must not be null");
        }
        if (key.length != KEY_LENGTH) {
            throw new IllegalArgumentException("key length must be 16 bytes");
        }
        try {
            Cipher cipher = Cipher.getInstance(ECB_TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(cipherMode, new SecretKeySpec(key, ALGORITHM));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("SM4 ecb cipher error", e);
        }
    }

    /**
     * encodeHex方法。
     * 字节数组编码为十六进制字符串（小写）。
     */
    private static String encodeHex(byte[] data) {
        char[] digits = "0123456789abcdef".toCharArray();
        char[] out = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            int value = data[i] & 0xFF;
            out[i * 2] = digits[value >>> 4];
            out[i * 2 + 1] = digits[value & 0x0F];
        }
        return new String(out);
    }

    /**
     * decodeHex方法。
     * 十六进制字符串解码为字节数组（兼容大小写）。
     */
    private static byte[] decodeHex(String hex) {
        if (hex == null || hex.length() == 0 || hex.length() % 2 != 0) {
            throw new IllegalArgumentException("illegal hex string: " + hex);
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
