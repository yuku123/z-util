package com.zifang.util.office.excel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ExcelUtils工具类的单元测试
 */
public class ExcelUtilsTest {

    @Test
    public void testClassExists() {
        // Verify the class can be instantiated
        ExcelUtils excelUtils = new ExcelUtils();
        assertNotNull(excelUtils);
    }
}