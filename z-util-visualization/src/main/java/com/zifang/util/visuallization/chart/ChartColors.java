package com.zifang.util.visuallization.chart;

import java.awt.*;

/**
 * 图表颜色定义
 */
public class ChartColors {

    private ChartColors() {}

    public static final Color[] PALETTE = {
            new Color(0x2196F3),  // Blue
            new Color(0x4CAF50),  // Green
            new Color(0xF44336),  // Red
            new Color(0xFF9800),  // Orange
            new Color(0x9C27B0),  // Purple
            new Color(0x00BCD4),  // Cyan
            new Color(0xFFEB3B),  // Yellow
            new Color(0x795548),  // Brown
            new Color(0xE91E63),  // Pink
            new Color(0x607D8B),  // BlueGrey
    };

    public static Color getColor(int index) {
        return PALETTE[index % PALETTE.length];
    }
}