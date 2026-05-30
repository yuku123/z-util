package com.zifang.util.media.graph.qrcode;

import java.awt.*;

/**
 * 二维码 Logo 配置类。
 * 用于配置二维码中间 Logo 图标的样式和大小。
 */
/**
 * MatrixToLogoImageConfig类。
 */
public class MatrixToLogoImageConfig {
    /** 默认 Logo 边框颜色 */
    public static final Color DEFAULT_BORDERCOLOR = Color.RED;
    /** 默认 Logo 边框宽度 */
    public static final int DEFAULT_BORDER = 2;
    /** 默认 Logo 大小为二维码的 1/5 */
    public static final int DEFAULT_LOGOPART = 5;

    private final int border = DEFAULT_BORDER;
    private final Color borderColor;
    private final int logoPart;

    /**
     * 创建默认 Logo 配置。
     * 边框颜色为红色，边框宽度为 2，Logo 大小为二维码的 1/5。
     */
    /**
     * MatrixToLogoImageConfig方法。
     */
    public MatrixToLogoImageConfig() {
        this(DEFAULT_BORDERCOLOR, DEFAULT_LOGOPART);
    }

    /**
     * 创建自定义 Logo 配置。
     *
     * @param borderColor 边框颜色
     * @param logoPart    Logo 大小分母，如为 5 则 Logo 大小为二维码的 1/5
     */
    /**
     * MatrixToLogoImageConfig方法。
     *      * @param borderColor Color类型参数
     * @param logoPart int类型参数
     */
    public MatrixToLogoImageConfig(Color borderColor, int logoPart) {
        this.borderColor = borderColor;
        this.logoPart = logoPart;
    }

    /**
     * 获取边框颜色。
     *
     * @return 边框颜色
     */
    /**
     * getBorderColor方法。
     * @return Color类型返回值
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * 获取边框宽度。
     *
     * @return 边框宽度
     */
    /**
     * getBorder方法。
     * @return int类型返回值
     */
    public int getBorder() {
        return border;
    }

    /**
     * 获取 Logo 大小分母。
     *
     * @return Logo 大小分母
     */
    /**
     * getLogoPart方法。
     * @return int类型返回值
     */
    public int getLogoPart() {
        return logoPart;
    }
}
