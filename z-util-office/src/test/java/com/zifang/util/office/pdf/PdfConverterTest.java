package com.zifang.util.office.pdf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * PdfConverter PDF转换器类的单元测试
 */
public class PdfConverterTest {

    @Test
    public void testClassExists() {
        PdfConverter pdfConverter = new PdfConverter();
        assertNotNull(pdfConverter);
    }
}