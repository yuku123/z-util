package com.zifang.util.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 URL 安全编解码（无三方依赖）。
 */
final class Base64Url {

    private static final Base64.Encoder ENC = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DEC = Base64.getUrlDecoder();

    static String encode(byte[] bytes) {
        return ENC.encodeToString(bytes);
    }

    static byte[] decode(String s) {
        return DEC.decode(s);
    }

    static String encode(String s) {
        return encode(s.getBytes(StandardCharsets.UTF_8));
    }
}
