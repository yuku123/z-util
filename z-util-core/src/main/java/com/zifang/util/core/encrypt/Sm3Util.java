package com.zifang.util.core.encrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.Security;

/**
 * Sm3Util类。
 * 国密 SM3 密码杂凑（哈希）工具，基于 BouncyCastle 提供者。
 * <p>
 * 摘要输出 256 位（32 字节）；文本形态输出十六进制（提供大小写两版）；
 * 另提供 HMAC-SM3 消息认证码，适合国密合规场景的摘要计算与报文鉴权。
 */
public final class Sm3Util {

    /**
     * 算法名称
     */
    private static final String ALGORITHM = "SM3";

    /**
     * HMAC-SM3 算法名称
     */
    private static final String HMAC_ALGORITHM = "HmacSM3";

    /**
     * 十六进制字符表（小写）
     */
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Sm3Util() {
    }

    /**
     * digest方法。
     * 计算数据的 SM3 摘要。
     *
     * @param data 原始数据，null 视为空数据
     * @return byte[]类型返回值，长度固定 32 字节
     */
    public static byte[] digest(byte[] data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            return data == null ? messageDigest.digest() : messageDigest.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("SM3 digest error", e);
        }
    }

    /**
     * digestHex方法。
     * 计算 UTF-8 文本的 SM3 摘要，输出小写十六进制。
     *
     * @param utf8Plain 原始文本
     * @return String类型返回值，64 位十六进制摘要
     */
    public static String digestHex(String utf8Plain) {
        return encodeHex(digest(utf8Plain == null ? null : utf8Plain.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * digestHexUpper方法。
     * 计算 UTF-8 文本的 SM3 摘要，输出大写十六进制。
     *
     * @param utf8Plain 原始文本
     * @return String类型返回值，64 位十六进制摘要（大写）
     */
    public static String digestHexUpper(String utf8Plain) {
        return digestHex(utf8Plain).toUpperCase();
    }

    /**
     * hmacSm3方法。
     * 计算 HMAC-SM3 消息认证码。
     *
     * @param message 消息数据，null 视为空数据
     * @param key     密钥字节，不允许为 null
     * @return byte[]类型返回值，长度固定 32 字节
     */
    public static byte[] hmacSm3(byte[] message, byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("hmac key must not be null");
        }
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM, BouncyCastleProvider.PROVIDER_NAME);
            mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
            return message == null ? mac.doFinal() : mac.doFinal(message);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SM3 error", e);
        }
    }

    /**
     * hmacSm3HexUpper方法。
     * 计算 UTF-8 文本与密钥的 HMAC-SM3，输出大写十六进制。
     *
     * @param message 消息文本
     * @param keyUtf8 密钥文本（按 UTF-8 取字节）
     * @return String类型返回值，64 位十六进制认证码（大写）
     */
    public static String hmacSm3HexUpper(String message, String keyUtf8) {
        byte[] key = keyUtf8 == null ? new byte[0] : keyUtf8.getBytes(StandardCharsets.UTF_8);
        byte[] messageBytes = message == null ? null : message.getBytes(StandardCharsets.UTF_8);
        return encodeHex(hmacSm3(messageBytes, key)).toUpperCase();
    }

    /**
     * 字节数组转小写十六进制。
     */
    private static String encodeHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xFF;
            chars[i * 2] = HEX_DIGITS[value >>> 4];
            chars[i * 2 + 1] = HEX_DIGITS[value & 0x0F];
        }
        return new String(chars);
    }
}
