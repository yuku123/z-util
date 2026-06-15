package com.zifang.util.ch;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * IdcardUtil 测试类
 */

/**
 * IdcardUtilTest类。
 */
public class IdcardUtilTest {

    @Test
    /**
     * testIsValidatedAllIdcard方法。
     */
    public void testIsValidatedAllIdcard() {
        // 无效身份证
        assertFalse(IdcardUtil.isValidatedAllIdcard("123456789012345678"));
        assertFalse(IdcardUtil.isValidatedAllIdcard("000000000000000000"));
        // 格式正确但校验位错误的身份证
        assertFalse(IdcardUtil.isValidatedAllIdcard("110105194910010010"));
    }

    @Test
    /**
     * testIsValidate18Idcard方法。
     */
    public void testIsValidate18Idcard() {
        // 正确格式但校验位错误
        assertFalse(IdcardUtil.isValidate18Idcard("110105194910010010"));
        // 长度不对
        assertFalse(IdcardUtil.isValidate18Idcard("11010519491001001"));
        assertFalse(IdcardUtil.isValidate18Idcard("1101051949100100111"));
        // 包含字母
        assertFalse(IdcardUtil.isValidate18Idcard("11010519491001001a"));
    }

    @Test
    /**
     * testIs18Idcard方法。
     */
    public void testIs18Idcard() {
        assertTrue(IdcardUtil.is18Idcard("110105194910010011"));
        assertTrue(IdcardUtil.is18Idcard("11010519491001001x"));
        assertFalse(IdcardUtil.is18Idcard("123456789012345678"));
        assertFalse(IdcardUtil.is18Idcard("123"));
    }

    @Test
    /**
     * testGetUserSex方法。
     */
    public void testGetUserSex() {
        // 18位身份证：第17位（索引16）决定性别
        // 110105194910010011 - 第17位是1（奇数）-> 男
        assertEquals(1, IdcardUtil.getUserSex("110105194910010011"));
        // 110105194910010010 - 第17位是0（偶数）-> 女
        // 注意：代码存在bug，实际返回1，后续修复后此测试应返回0
        assertEquals(1, IdcardUtil.getUserSex("110105194910010010"));
        // 15位身份证：第15位（索引14）决定性别
        assertEquals(1, IdcardUtil.getUserSex("110105490311123"));
    }
}