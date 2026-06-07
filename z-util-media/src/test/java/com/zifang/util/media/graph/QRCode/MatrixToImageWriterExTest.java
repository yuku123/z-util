package com.zifang.util.media.graph.qrcode;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * MatrixToImageWriterExTest类。
 */
public class MatrixToImageWriterExTest {

    @Test
    /**
     * testMatrixToImageWriterExExists方法。
     */
    public void testMatrixToImageWriterExExists() {
        MatrixToImageWriterEx writer = new MatrixToImageWriterEx();
        assertNotNull(writer);
    }
}
