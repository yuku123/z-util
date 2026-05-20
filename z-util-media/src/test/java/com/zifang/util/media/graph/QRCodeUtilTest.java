package com.zifang.util.media.graph;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.*;

public class QRCodeUtilTest {

    @Test
    public void testQRCodeUtilExists() {
        QRCodeUtil util = new QRCodeUtil();
        assertNotNull(util);
    }

    @Test
    public void testMkdirs() {
        // Should not throw exception - mkdirs handles existing directories
        QRCodeUtil.mkdirs("/tmp/test_qr");
    }

    @Test
    public void testEncodeWithOutputStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        QRCodeUtil.encode("test content", out);
        // Should not throw exception
    }

    @Test
    public void testEncodeWithoutLogo() throws Exception {
        String content = "Test QR Code";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        QRCodeUtil.encode(content, out);
        assertTrue(out.size() > 0);
    }
}
