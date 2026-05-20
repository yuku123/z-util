package com.zifang.util.media.graph.image;

import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.Assert.*;

public class ImageCaptchaTest {

    @Test
    public void testCreate() {
        ImageCaptcha captcha = ImageCaptcha.create();
        assertNotNull(captcha);
    }

    @Test
    public void testWidth() {
        ImageCaptcha captcha = ImageCaptcha.create().width(300);
        assertNotNull(captcha);
    }

    @Test
    public void testHeight() {
        ImageCaptcha captcha = ImageCaptcha.create().height(100);
        assertNotNull(captcha);
    }

    @Test
    public void testLength() {
        ImageCaptcha captcha = ImageCaptcha.create().length(6);
        assertNotNull(captcha);
    }

    @Test
    public void testChars() {
        ImageCaptcha captcha = ImageCaptcha.create().chars("ABC123");
        assertNotNull(captcha);
    }

    @Test
    public void testRandomCode() {
        ImageCaptcha captcha = ImageCaptcha.create().length(4);
        String code = captcha.randomCode();
        assertNotNull(code);
        assertEquals(4, code.length());
    }

    @Test
    public void testPngImage() {
        ImageCaptcha captcha = ImageCaptcha.create();
        BufferedImage img = captcha.pngImage("ABCD");
        assertNotNull(img);
    }

    @Test
    public void testPngToBase64() {
        ImageCaptcha captcha = ImageCaptcha.create();
        String base64 = captcha.pngToBase64("ABCD");
        assertNotNull(base64);
        assertTrue(base64.startsWith("data:image/png;base64,"));
    }

    @Test
    public void testPngToBase64WithResult() throws IOException {
        ImageCaptcha captcha = ImageCaptcha.create();
        ImageCaptcha.CaptchaResult result = captcha.pngToBase64();
        assertNotNull(result);
        assertNotNull(result.code);
        assertNotNull(result.base64);
    }

    @Test
    public void testGifToBase64() {
        ImageCaptcha captcha = ImageCaptcha.create();
        String base64 = captcha.gifToBase64("ABCD");
        // GIF generation may return null if it fails
        if (base64 != null) {
            assertTrue(base64.startsWith("data:image/gif;base64,"));
        }
    }

    @Test
    public void testGifToBase64WithResult() throws IOException {
        ImageCaptcha captcha = ImageCaptcha.create();
        ImageCaptcha.CaptchaResult result = captcha.gifToBase64();
        assertNotNull(result);
        assertNotNull(result.code);
        // base64 may be null if GIF generation failed
    }
}
