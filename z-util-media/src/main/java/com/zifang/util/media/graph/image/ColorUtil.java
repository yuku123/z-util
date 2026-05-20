package com.zifang.util.media.graph.image;

import java.awt.*;

/**
 * 颜色转换工具类。
 * 提供 RGB 颜色与十六进制字符串之间的相互转换功能。
 */
public final class ColorUtil {

    /**
     * 将十六进制颜色字符串转换为 Color 对象。
     * 输入格式：如 "#FF0000" 表示红色。
     *
     * @param str 十六进制颜色字符串，必须以 # 开头
     * @return 对应的 Color 对象
     */
    public final static Color String2Color(String str) {
        int i = Integer.parseInt(str.substring(1), 16);
        return new Color(i);
    }

    /**
     * 将 Color 对象转换为十六进制颜色字符串。
     *
     * @param color Color 对象
     * @return 十六进制颜色字符串，格式如 "#FF0000"
     */
    public final static String Color2String(Color color) {
        String R = Integer.toHexString(color.getRed());
        R = R.length() < 2 ? ('0' + R) : R;
        String B = Integer.toHexString(color.getBlue());
        B = B.length() < 2 ? ('0' + B) : B;
        String G = Integer.toHexString(color.getGreen());
        G = G.length() < 2 ? ('0' + G) : G;
        return '#' + R + B + G;
    }
}
