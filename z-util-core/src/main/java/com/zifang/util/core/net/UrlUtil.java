package com.zifang.util.core.net;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URL相关的工具类
 * <p>
 * 提供URL编码解码、参数解析等功能
 */
/**
 * UrlUtil类。
 */
public class UrlUtil {

    private static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * URL编码
     *
     * @param url 需要编码的URL
     * @return 编码后的URL
     */
    /**
     * encode方法。
     *      * @param url String类型参数
     * @return static String类型返回值
     */
    public static String encode(String url) {
        return encode(url, CHARSET_UTF_8);
    }

    /**
     * URL编码
     *
     * @param url      需要编码的URL
     * @param encoding 编码方式
     * @return 编码后的URL
     */
    /**
     * encode方法。
     *      * @param url String类型参数
     * @param encoding String类型参数
     * @return static String类型返回值
     */
    public static String encode(String url, String encoding) {
        try {
            return URLEncoder.encode(url, encoding);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    /**
     * URL解码
     *
     * @param url 需要解码的URL
     * @return 解码后的URL
     */
    /**
     * decode方法。
     *      * @param url String类型参数
     * @return static String类型返回值
     */
    public static String decode(String url) {
        return decode(url, CHARSET_UTF_8);
    }

    /**
     * URL解码
     *
     * @param url      需要解码的URL
     * @param encoding 解码方式
     * @return 解码后的URL
     */
    /**
     * decode方法。
     *      * @param url String类型参数
     * @param encoding String类型参数
     * @return static String类型返回值
     */
    public static String decode(String url, String encoding) {
        try {
            return URLDecoder.decode(url, encoding);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    /**
     * 设置URL参数
     *
     * @param url        原始URL
     * @param paramName  参数名
     * @param paramValue 参数值
     * @return 更新后的URL
     */
    /**
     * setParam方法。
     *      * @param url String类型参数
     * @param paramName String类型参数
     * @param paramValue String类型参数
     * @return static String类型返回值
     */
    public static String setParam(String url, String paramName, String paramValue) {
        int tempIndex = url.indexOf("?");
        if (tempIndex != -1) {
            int paramIndex = url.indexOf(paramName + "=", tempIndex + 1);
            if (paramIndex != -1) {
                tempIndex = url.indexOf("&", paramIndex + paramName.length() + 1);
                if (tempIndex != -1) {
                    return url.substring(0, paramIndex) + paramName + "=" + paramValue + url.substring(tempIndex);
                }
                return url.substring(0, paramIndex) + paramName + "=" + paramValue;
            } else {
                if (url.lastIndexOf("&") == url.length() - 1) {
                    return url + paramName + "=" + paramValue;
                }
                return url + "&" + paramName + "=" + paramValue;
            }
        } else {
            return url + "?" + paramName + "=" + paramValue;
        }
    }

    /**
     * 获取URL参数值
     *
     * @param url       URL
     * @param paramName 参数名
     * @return 参数值，不存在返回null
     */
    /**
     * getParamValue方法。
     *      * @param url String类型参数
     * @param paramName String类型参数
     * @return static String类型返回值
     */
    public static String getParamValue(String url, String paramName) {
        int tempIndex = url.indexOf("?");
        if (tempIndex != -1) {
            int paramIndex = url.indexOf(paramName + "=", tempIndex + 1);
            if (paramIndex != -1) {
                tempIndex = url.indexOf("&", paramIndex + paramName.length() + 1);
                if (tempIndex != -1) {
                    return url.substring(paramIndex + paramName.length() + 1, tempIndex);
                }
                return url.substring(paramIndex + paramName.length() + 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 移除URL参数
     *
     * @param url        URL
     * @param paramNames 参数名数组
     * @return 移除参数后的URL
     */
    /**
     * removeParam方法。
     *      * @param url String类型参数
     * @param paramNames String...类型参数
     * @return static String类型返回值
     */
    public static String removeParam(String url, String... paramNames) {
        for (String paramName : paramNames) {
            url = removeParam(url, paramName);
        }
        return url;
    }

    /**
     * 移除URL参数
     *
     * @param url       URL
     * @param paramName 参数名
     * @return 移除参数后的URL
     */
    /**
     * removeParam方法。
     *      * @param url String类型参数
     * @param paramName String类型参数
     * @return static String类型返回值
     */
    public static String removeParam(String url, String paramName) {
        int tempIndex = url.indexOf("?");
        if (tempIndex != -1) {
            int paramIndex = url.indexOf(paramName + "=", tempIndex + 1);
            if (paramIndex != -1) {
                tempIndex = url.indexOf("&", paramIndex + paramName.length() + 1);
                if (tempIndex != -1) {
                    return url.substring(0, paramIndex) + url.substring(tempIndex + 1);
                }
                return url.substring(0, paramIndex - 1);
            } else {
                return url;
            }
        } else {
            return url;
        }
    }

    /**
     * 拼接url方法
     * <p>
     * Example:
     * <pre>
     * url: http://tt.se/                 Location: /start              =>  http://tt.se/start
     * url: http://localhost/moved_perm   Location: /                   =>  http://localhost/
     * url: http://github.com/            Location: http://github.com/  =>  https://github.com/
     * }
     *
     * @param url            原始URL
     * @param locationHeader Location头
     * @return 拼接后的URL
     */
    /**
     * urlJoin方法。
     *      * @param url URL类型参数
     * @param locationHeader String类型参数
     * @return static String类型返回值
     */
    public static String urlJoin(URL url, String locationHeader) {
        try {
            if (locationHeader.startsWith("http")) {
                return new URL(locationHeader).toString();
            }
            return new URL(url.getProtocol() + "://" + url.getAuthority() + locationHeader).toString();
        } catch (MalformedURLException e) {
            return url.toString();
        }
    }

    /**
     * 获取HttpServletRequest的请求参数
     *
     * @param request http请求
     * @return 参数Map
     */
    /**
     * getRequestParams方法。
     *      * @param request HttpServletRequest类型参数
     * @return static Map<String, String>类型返回值
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        java.util.Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }

    /**
     * 解析字符串返回map键值对(例：a=1&b=2 => a=1,b=2)
     *
     * @param query   源参数字符串
     * @param split1  键值对之间的分隔符（例：&）
     * @param split2  key与value之间的分隔符（例：=）
     * @param dupLink 重复参数名的参数值之间的连接符，连接后的字符串作为该参数的参数值，可为null
     *                null：不允许重复参数名出现，则靠后的参数值会覆盖掉靠前的参数值。
     * @return map
     */
    /**
     * parseQuery方法。
     *      * @param query String类型参数
     * @param split1 char类型参数
     * @param split2 char类型参数
     * @param dupLink String类型参数
     * @return static Map<String, String>类型返回值
     */
    public static Map<String, String> parseQuery(String query, char split1, char split2, String dupLink) {
        if (query == null || query.isEmpty() || query.indexOf(split2) <= 0) {
            return null;
        }

        Map<String, String> result = new HashMap<>();

        String name = null;
        String value = null;
        String tempValue;
        int len = query.length();
        for (int i = 0; i < len; i++) {
            char c = query.charAt(i);
            if (c == split2) {
                value = "";
            } else if (c == split1) {
                if (name != null && value != null) {
                    if (dupLink != null) {
                        tempValue = result.get(name);
                        if (tempValue != null) {
                            value += dupLink + tempValue;
                        }
                    }
                    result.put(name, value);
                }
                name = null;
                value = null;
            } else if (value != null) {
                value += c;
            } else {
                name = (name != null) ? (name + c) : "" + c;
            }
        }

        if (name != null && value != null) {
            if (dupLink != null) {
                tempValue = result.get(name);
                if (tempValue != null) {
                    value += dupLink + tempValue;
                }
            }
            result.put(name, value);
        }

        return result;
    }

    /**
     * 解析http请求URI
     *
     * @param queryUri http请求的uri
     * @return 参数字符串Map
     */
    /**
     * httpParseQuery方法。
     *      * @param queryUri String类型参数
     * @return static Map<String, String>类型返回值
     */
    public static Map<String, String> httpParseQuery(String queryUri) {
        Map<String, String> result = new HashMap<>();
        if (queryUri != null && !queryUri.isEmpty()) {
            result = parseQuery(queryUri, '&', '=', ",");
        }
        return result;
    }

    private static class URLEncoder {
        private static final Pattern CHARSET_PATTERN = Pattern.compile("%([0-9A-Fa-f]{2})");

    /**
     * encode方法。
     *      * @param url String类型参数
     * @param encoding String类型参数
     * @return static String类型返回值
     */
        public static String encode(String url, String encoding) throws UnsupportedEncodingException {
            if (url == null || url.isEmpty()) {
                return url;
            }
            byte[] bytes = url.getBytes(encoding);
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                if (isSafeChar((char) (b & 0xFF))) {
                    result.append((char) (b & 0xFF));
                } else {
                    result.append('%');
                    result.append(String.format("%02X", b));
                }
            }
            return result.toString();
        }

        private static boolean isSafeChar(char c) {
            return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9'
                    || c == '-' || c == '_' || c == '.' || c == '~';
        }
    }

    /**
     * 解码URL查询字符串
     *
     * @param source 查询字符串
     * @return 解码后的字符串
     */
    /**
     * decodeQuery方法。
     *      * @param source String类型参数
     * @return static String类型返回值
     */
    public static String decodeQuery(String source) {
        return decodeQuery(source, CHARSET_UTF_8);
    }

    /**
     * 解码URL查询字符串，将 + 视为空格
     *
     * @param source   查询字符串
     * @param encoding 编码方式
     * @return 解码后的字符串
     */
    /**
     * decodeQuery方法。
     *      * @param source String类型参数
     * @param encoding String类型参数
     * @return static String类型返回值
     */
    public static String decodeQuery(String source, String encoding) {
        if (source == null) {
            return null;
        }
        int length = source.length();
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream(length);
        boolean changed = false;
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);
            switch (ch) {
                case '%':
                    if ((i + 2) < length) {
                        char hex1 = source.charAt(i + 1);
                        char hex2 = source.charAt(i + 2);
                        int u = Character.digit(hex1, 16);
                        int l = Character.digit(hex2, 16);
                        if (u == -1 || l == -1) {
                            throw new IllegalArgumentException("Invalid sequence: " + source.substring(i));
                        }
                        bos.write((u << 4) + l);
                        i += 2;
                        changed = true;
                    } else {
                        throw new IllegalArgumentException("Invalid sequence: " + source.substring(i));
                    }
                    break;
                case '+':
                    ch = ' ';
                    changed = true;
                default:
                    bos.write(ch);
            }
        }
        try {
            return changed ? new String(bos.toByteArray(), encoding) : source;
        } catch (java.io.UnsupportedEncodingException e) {
            return source;
        }
    }

    /**
     * URL构建器，用于链式拼接查询参数
     * <p>Example:
     * <pre>
     * String url = UrlUtil.build("/api/user")
     *     .queryParam("name", "张三")
     *     .queryParam("age", "20")
     *     .toString();
     * // /api/user?name=%E5%BC%A0%E4%B8%89&amp;age=20
     * </pre>
     */
    public static class Builder {
        private final StringBuilder url;
        private final String encoding;
        private boolean hasParams;

    /**
     * Builder方法。
     *      * @param path String类型参数
     * @param encodePath boolean类型参数
     * @param encoding String类型参数
     */
        public Builder(String path, boolean encodePath, String encoding) {
            this.encoding = encoding;
            this.url = new StringBuilder();
            if (encodePath) {
                this.url.append(encode(path, encoding));
            } else {
                this.url.append(path);
            }
            this.hasParams = this.url.indexOf("?") != -1;
        }

        /**
         * 创建默认UTF-8编码的URL构建器
         *
         * @param path 初始路径
         * @return Builder实例
         */
    /**
     * build方法。
     *      * @param path String类型参数
     * @return static Builder类型返回值
     */
        public static Builder build(String path) {
            return new Builder(path, true, CHARSET_UTF_8);
        }

        /**
         * 追加查询参数
         *
         * @param name  参数名
         * @param value 参数值
         * @return this
         */
    /**
     * queryParam方法。
     *      * @param name String类型参数
     * @param value String类型参数
     * @return Builder类型返回值
     */
        public Builder queryParam(String name, String value) {
            url.append(hasParams ? '&' : '?');
            hasParams = true;
            try {
                url.append(URLEncoder.encode(name, encoding));
            } catch (UnsupportedEncodingException e) {
                url.append(name);
            }
            if (value != null && !value.isEmpty()) {
                url.append('=');
                try {
                    url.append(URLEncoder.encode(value, encoding));
                } catch (UnsupportedEncodingException e) {
                    url.append(value);
                }
            }
            return this;
        }

        @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
        public String toString() {
            return url.toString();
        }
    }
}