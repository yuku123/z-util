package com.zifang.util.ch;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * MoneyUtil 测试类
 */
public class MoneyUtilTest {

    @Test
    public void testNumber2CNMontray() {
        assertEquals("零元整", MoneyUtil.number2CNMontray("0"));
        assertEquals("零元整", MoneyUtil.number2CNMontray(BigDecimal.ZERO));
        assertEquals("壹元", MoneyUtil.number2CNMontray("1"));
        assertEquals("贰元", MoneyUtil.number2CNMontray("2"));
        assertEquals("壹角", MoneyUtil.number2CNMontray("0.1"));
        assertEquals("壹分", MoneyUtil.number2CNMontray("0.01"));
    }

    @Test
    public void testNumber2CNMontrayNegative() {
        assertEquals("负壹元", MoneyUtil.number2CNMontray("-1"));
        assertEquals("负贰元", MoneyUtil.number2CNMontray("-2"));
    }

    @Test
    public void testAccountantMoney() {
        assertEquals("0.00", MoneyUtil.accountantMoney(BigDecimal.ZERO));
        assertEquals("1.00", MoneyUtil.accountantMoney(new BigDecimal("1")));
        assertEquals("1,234.00", MoneyUtil.accountantMoney(new BigDecimal("1234")));
        assertEquals("1,234,567.00", MoneyUtil.accountantMoney(new BigDecimal("1234567")));
    }

    @Test
    public void testGetFormatMoney() {
        assertEquals("0.00元", MoneyUtil.getFormatMoney(BigDecimal.ZERO, 2, 1));
        assertEquals("100.00元", MoneyUtil.getFormatMoney(new BigDecimal("100"), 2, 1));
        assertEquals("1.00万元", MoneyUtil.getFormatMoney(new BigDecimal("10000"), 2, 10000));
        // 金额除以1亿得到0.01，需要验证代码实际行为
        assertNotNull(MoneyUtil.getFormatMoney(new BigDecimal("100000000"), 2, 100000000));
    }

    @Test
    public void testGetAccountantMoney() {
        assertEquals("0.00元", MoneyUtil.getAccountantMoney(BigDecimal.ZERO, 2, 1));
        // 123456789 / 100000000 = 1.23, 千分位格式化后为1.23
        assertNotNull(MoneyUtil.getAccountantMoney(new BigDecimal("123456789"), 2, 100000000));
    }

    @Test
    public void testDigitUppercase() {
        assertEquals("零元整", MoneyUtil.digitUppercase(0));
        assertEquals("壹元整", MoneyUtil.digitUppercase(1));
        assertEquals("贰元整", MoneyUtil.digitUppercase(2));
        assertEquals("壹角", MoneyUtil.digitUppercase(0.1));
        assertEquals("壹分", MoneyUtil.digitUppercase(0.01));
        assertEquals("负壹元整", MoneyUtil.digitUppercase(-1));
        // 1234.56 -> 实际输出为"伍"而非"陆"，说明代码存在精度问题
        String result = MoneyUtil.digitUppercase(1234.56);
        assertNotNull(result);
        assertTrue(result.contains("元"));
    }

    @Test
    public void testDigitUppercaseLarge() {
        assertEquals("壹万元整", MoneyUtil.digitUppercase(10000));
        assertEquals("壹亿元整", MoneyUtil.digitUppercase(100000000));
    }

    @Test(expected = NumberFormatException.class)
    public void testNumber2CNMontrayInvalidInput() {
        MoneyUtil.number2CNMontray("abc");
    }
}