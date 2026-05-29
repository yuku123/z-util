package com.zifang.util.office.pdf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PoiUtils POI工具类的单元测试
 */
public class PoiUtilsTest {

    @Test
    public void testClassExists() {
        PoiUtils poiUtils = new PoiUtils();
        assertNotNull(poiUtils);
    }
}