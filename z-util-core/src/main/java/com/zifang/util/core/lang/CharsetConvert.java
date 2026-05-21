package com.zifang.util.core.lang;

/**
 * 全角/半角字符转换工具类
 * <ul>
 *   <li>全角（ SBC ，Super Bitwise Computer）：中文标点及中文文字的宽度形式</li>
 *   <li>半角（ DBC ，Dot Bitwise Computer）：标准 ASCII 字符</li>
 * </ul>
 * <p>
 * 转换规则：以 ASCII 可显示字符 !（33）~ ~（126）为例，
 * 全角字符从 ！（65281）到 ～（65374），偏移量为 65248。
 */
public class CharsetConvert {

    private static final int DBC_CHAR_START = 33;   // '!'
    private static final int DBC_CHAR_END = 126;     // '~'
    private static final int SBC_SPACE = 12288;       // 全角空格
    private static final int DBC_SPACE = 32;          // 半角空格
    private static final int CONVERT_STEP = 65248;    // 全半角偏移量

    /**
     * 半角转全角
     *
     * @param src 半角字符串
     * @return 全角字符串
     */
    public static String bj2qj(String src) {
        if (src == null || src.isEmpty()) {
            return "";
        }
        char[] chars = src.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if (c == DBC_SPACE) {
                sb.append((char) SBC_SPACE);
            } else if (c >= DBC_CHAR_START && c <= DBC_CHAR_END) {
                sb.append((char) (c + CONVERT_STEP));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 全角转半角
     *
     * @param src 全角字符串
     * @return 半角字符串
     */
    public static String qj2bj(String src) {
        if (src == null || src.isEmpty()) {
            return "";
        }
        char[] chars = src.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if (c == SBC_SPACE) {
                sb.append((char) DBC_SPACE);
            } else if (c >= SBC_CHAR_START() && c <= SBC_CHAR_END()) {
                sb.append((char) (c - CONVERT_STEP));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int SBC_CHAR_START() {
        return DBC_CHAR_START + CONVERT_STEP; // 65281
    }

    private static int SBC_CHAR_END() {
        return DBC_CHAR_END + CONVERT_STEP; // 65374
    }
}
