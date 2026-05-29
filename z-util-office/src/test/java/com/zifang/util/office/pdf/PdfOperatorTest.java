package com.zifang.util.office.pdf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PdfOperator PDF操作类的单元测试
 */
public class PdfOperatorTest {

    @Test
    public void testClassExists() {
        PdfOperator pdfOperator = new PdfOperator();
        assertNotNull(pdfOperator);
    }
}