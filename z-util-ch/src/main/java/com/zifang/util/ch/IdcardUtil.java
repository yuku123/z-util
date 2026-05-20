package com.zifang.util.ch;

import java.util.regex.Pattern;

/**
 * 身份证号码工具类
 * <p>
 * 提供身份证号码的验证和解析功能，支持15位和18位身份证号码。
 * <p>
 * 15位身份证号码：第7、8位为出生年份（两位数），第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
 * <p>
 * 18位身份证号码：第7、8、9、10位为出生年份（四位数），第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
 *
 * @author zifang
 */
public class IdcardUtil {

    /**
     * 省，直辖市代码表
     */
    private static final String[] PROVINCE_CODES = {
            "11:北京", "12:天津", "13:河北", "14:山西", "15:内蒙古",
            "21:辽宁", "22:吉林", "23:黑龙江", "31:上海", "32:江苏",
            "33:浙江", "34:安徽", "35:福建", "36:江西", "37:山东", "41:河南",
            "42:湖北", "43:湖南", "44:广东", "45:广西", "46:海南", "50:重庆",
            "51:四川", "52:贵州", "53:云南", "54:西藏", "61:陕西", "62:甘肃",
            "63:青海", "64:宁夏", "65:新疆", "71:台湾", "81:香港", "82:澳门", "91:国外"
    };

    /**
     * 每位加权因子
     */
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 验证身份证是否合法
     * <p>
     * 该方法验证传入的身份证号码是否合法，目前仅支持18位身份证的验证。
     *
     * @param idcard 待验证的身份证号码
     * @return 如果身份证合法返回true，否则返回false
     */
    public static boolean isValidatedAllIdcard(String idcard) {
        return isValidate18Idcard(idcard);
    }

    /**
     * 判断18位身份证的合法性
     * <p>
     * 根据《中华人民共和国国家标准GB11643-1999》中有关公民身份号码的规定进行验证。
     * 公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * <p>
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * <p>
     * 顺序码：表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
     * 顺序码的奇数分配给男性，偶数分配给女性。
     * <p>
     * 第十八位数字（校验码）的计算方法：
     * <ol>
     *   <li>将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：
     *       7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2</li>
     *   <li>将这17位数字和系数相乘的结果相加</li>
     *   <li>用加出来和除以11，看余数是多少</li>
     *   <li>余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字，其分别对应的最后一位身份证的号码为
     *       1 0 X 9 8 7 6 5 4 3 2</li>
     *   <li>如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2</li>
     * </ol>
     *
     * @param idcard 待验证的18位身份证号码
     * @return 如果是合法的身份证返回true，否则返回false
     */
    public static boolean isValidate18Idcard(String idcard) {
        // 非18位为假
        if (idcard.length() != 18) {
            return false;
        }
        // 获取前17位
        String idcard17 = idcard.substring(0, 17);
        // 获取第18位
        String idcard18Code = idcard.substring(17, 18);
        char[] c = null;
        String checkCode = "";
        // 是否都为数字
        if (isDigital(idcard17)) {
            c = idcard17.toCharArray();
        } else {
            return false;
        }

        if (null != c) {
            int[] bit = new int[idcard17.length()];
            bit = converCharToInt(c);
            int sum17 = 0;
            sum17 = getPowerSum(bit);
            // 将和值与11取模得到余数进行校验码判断
            checkCode = getCheckCodeBySum(sum17);
            if (null == checkCode) {
                return false;
            }
            // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
            return idcard18Code.equalsIgnoreCase(checkCode);
        }

        return true;
    }

    /**
     * 验证18位身份证号码的基本数字和位数格式
     * <p>
     * 该方法仅验证身份证号码的格式是否符合18位身份证的标准格式要求，
     * 不验证校验位是否正确。如需完整验证，请使用 {@link #isValidate18Idcard(String)} 方法。
     *
     * @param idcard 待验证的身份证号码
     * @return 如果格式符合18位身份证标准返回true，否则返回false
     */
    public static boolean is18Idcard(String idcard) {
        return Pattern.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$", idcard);
    }

    /**
     * 判断字符串是否全部为数字
     *
     * @param str 待检测的字符串
     * @return 如果字符串全部为数字返回true，否则返回false
     */
    private static boolean isDigital(String str) {
        return str != null && !"".equals(str) && str.matches("^[0-9]*$");
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再计算总和
     *
     * @param bit 身份证各位数字组成的数组
     * @return 加权因子相乘后的总和
     */
    private static int getPowerSum(int[] bit) {
        int sum = 0;
        if (power.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }

        return sum;
    }

    /**
     * 根据加权和与11的模值获取对应的校验码
     *
     * @param sum17 加权因子相乘后的总和与11的模
     * @return 对应的校验码（0-9或x）
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sum17 % 11);
        }
        return checkCode;
    }

    /**
     * 将字符数组转换为整型数组
     *
     * @param c 字符数组
     * @return 对应的整型数组
     * @throws NumberFormatException 如果字符不是有效的数字字符
     */
    private static int[] converCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

    /**
     * 从身份证号码中获取性别标识
     * <p>
     * 根据身份证号码的第15位（15位身份证）或第17位（18位身份证）判断性别。
     * 奇数表示男性，偶数表示女性。
     *
     * @param idno 身份证号码（15位或18位）
     * @return 性别标识，1表示男性，0表示女性
     */
    public static int getUserSex(String idno) {
        String sex = "1";
        if (idno != null) {
            if (idno.length() > 15) {
                sex = idno.substring(16, 17);
            }
        }

        return Integer.parseInt(sex) % 2 == 0 ? 0 : 1;
    }
}
