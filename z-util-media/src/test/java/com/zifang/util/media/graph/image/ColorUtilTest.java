package com.zifang.util.media.graph.image;

import org.junit.Test;
import java.awt.Color;
import static org.junit.Assert.*;

public class ColorUtilTest {

    @Test
    public void testString2Color() {
        Color color = ColorUtil.String2Color("#FF0000");
        assertEquals(255, color.getRed());
        assertEquals(0, color.getGreen());
        assertEquals(0, color.getBlue());
    }

    @Test
    public void testString2ColorWithLowerCase() {
        Color color = ColorUtil.String2Color("#ff0000");
        assertEquals(255, color.getRed());
    }

    @Test
    public void testColor2String() {
        Color color = new Color(255, 0, 0);
        String hex = ColorUtil.Color2String(color);
        assertNotNull(hex);
        assertTrue(hex.startsWith("#"));
        assertEquals(7, hex.length());
    }

    @Test
    public void testColor2StringRoundTrip() {
        Color original = new Color(128, 64, 32);
        String hex = ColorUtil.Color2String(original);
        Color converted = ColorUtil.String2Color(hex);
        assertEquals(original.getRed(), converted.getRed());
        assertEquals(original.getGreen(), converted.getGreen());
        assertEquals(original.getBlue(), converted.getBlue());
    }

    @Test
    public void testColor2StringWhite() {
        Color white = Color.WHITE;
        String hex = ColorUtil.Color2String(white);
        assertEquals("#FFFFFF", hex);
    }

    @Test
    public void testColor2StringBlack() {
        Color black = Color.BLACK;
        String hex = ColorUtil.Color2String(black);
        assertEquals("#000000", hex);
    }
}
