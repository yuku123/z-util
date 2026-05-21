package com.zifang.util.core.lang;

/**
 * 全角/半角字符转换工具类
 * <p>
 * 全角（ SBC ，Super Bitwise Computer）：中文标点及中文文字的宽度形式。
 * 半角（ DBC ，Dot Bitwise Computer）：标准 ASCII 字符。
 * </p>
 * <p>
 * 转换规则：以 ASCII 可显示字符 !（33）~ ~（126）为例，
 * 全角字符从 ！（65281）到 ～（65374），偏移量为 65248。
 * </p>
 * <p>
 * 该工具类主要用于：
 * <ul>
 *   <li>中文排版中全角半角转换</li>
 *   <li>网页内容（通常使用半角）的全角化处理</li>
 *   <li>旧系统数据兼容处理</li>
 * </ul>
 * </p>
 *
 * @author zifang
 */
public class CharsetConvert {

    /**
     * 半角字符起始ASCII码：33（'!'）
     */
    private static final int DBC_CHAR_START = 33;   // '!'

    /**
     * 半角字符结束ASCII码：126（'~'）
     */
    private static final int DBC_CHAR_END = 126;     // '~'

    /**
     * 全角空格ASCII码：12288
     */
    private static final int SBC_SPACE = 12288;       // 全角空格

    /**
     * 半角空格ASCII码：32
     */
    private static final int DBC_SPACE = 32;          // 半角空格

    /**
     * 全半角字符偏移量：65248
     */
    private static final int CONVERT_STEP = 65248;    // 全半角偏移量

    /**
     * 将半角字符串转换为全角字符串
     * <p>
     * 转换规则：
     * <ul>
     *   <li>半角空格（ASCII 32）转换为全角空格（ASCII 12288）</li>
     *   <li>半角可显示字符（ASCII 33-126）转换为对应的全角字符（ASCII 65281-65374）</li>
     *   <li>其他字符保持不变</li>
     * </ul>
     * </p>
     * <p>
     * 示例：
     * <pre>
     * bj2qj("Hello World!")  // 返回 "Ｈｅｌｌｏ　Ｗｏｒｌｄ！"
     * bj2qj("123")           // 返回 "１２３"
     * </pre>
     * </p>
     *
     * @param src 半角字符串，可以为null或空字符串
     * @return 全角字符串，如果输入为空则返回空字符串
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
     * 将全角字符串转换为半角字符串
     * <p>
     * 转换规则：
     * <ul>
     *   <li>全角空格（ASCII 12288）转换为半角空格（ASCII 32）</li>
     *   <li>全角可显示字符（ASCII 65281-65374）转换为对应的半角字符（ASCII 33-126）</li>
     *   <li>其他字符保持不变</li>
     * </ul>
     * </p>
     * <p>
     * 示例：
     * <pre>
     * qj2bj("Ｈｅｌｌｏ　Ｗｏｒｌｄ！")  // 返回 "Hello World!"
     * qj2bj("１２３")                     // 返回 "123"
     * </pre>
     * </p>
     *
     * @param src 全角字符串，可以为null或空字符串
     * @return 半角字符串，如果输入为空则返回空字符串
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

    /**
     * 计算全角字符起始ASCII码
     *
     * @return 全角字符起始ASCII码（65281）
     */
    private static int SBC_CHAR_START() {
        return DBC_CHAR_START + CONVERT_STEP; // 65281
    }

    /**
     * 计算全角字符结束ASCII码
     *
     * @return 全角字符结束ASCII码（65374）
     */
    private static int SBC_CHAR_END() {
        return DBC_CHAR_END + CONVERT_STEP; // 65374
    }
}
