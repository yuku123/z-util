package com.zifang.util.visuallization.swing.algrithm.lesson3.moneyExperimentBasics2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * 算法可视化帮助类
 * 提供绘图辅助方法
 */
/**
 * AlgoVisHelper类。
 */
/**
 * AlgoVisHelper类。
 */
public class AlgoVisHelper {

    private AlgoVisHelper() {
    }

    /**
     * 红色
     */
    /**
     * Color方法。
     *      * @param 0xF44336 Object类型参数
     * @return static final Color Red = new类型返回值
     */
    public static final Color Red = new Color(0xF44336);
    /**
     * 粉色
     */
    /**
     * Color方法。
     *      * @param 0xE91E63 Object类型参数
     * @return static final Color Pink = new类型返回值
     */
    public static final Color Pink = new Color(0xE91E63);
    /**
     * 紫色
     */
    /**
     * Color方法。
     *      * @param 0x9C27B0 Object类型参数
     * @return static final Color Purple = new类型返回值
     */
    public static final Color Purple = new Color(0x9C27B0);
    /**
     * 深紫色
     */
    /**
     * Color方法。
     *      * @param 0x673AB7 Object类型参数
     * @return static final Color DeepPurple = new类型返回值
     */
    public static final Color DeepPurple = new Color(0x673AB7);
    /**
     * 靖蓝色
     */
    /**
     * Color方法。
     *      * @param 0x3F51B5 Object类型参数
     * @return static final Color Indigo = new类型返回值
     */
    public static final Color Indigo = new Color(0x3F51B5);
    /**
     * 蓝色
     */
    /**
     * Color方法。
     *      * @param 0x2196F3 Object类型参数
     * @return static final Color Blue = new类型返回值
     */
    public static final Color Blue = new Color(0x2196F3);
    /**
     * 浅蓝色
     */
    /**
     * Color方法。
     *      * @param 0x03A9F4 Object类型参数
     * @return static final Color LightBlue = new类型返回值
     */
    public static final Color LightBlue = new Color(0x03A9F4);
    /**
     * 青色
     */
    /**
     * Color方法。
     *      * @param 0x00BCD4 Object类型参数
     * @return static final Color Cyan = new类型返回值
     */
    public static final Color Cyan = new Color(0x00BCD4);
    /**
     * 蓝绿色
     */
    /**
     * Color方法。
     *      * @param 0x009688 Object类型参数
     * @return static final Color Teal = new类型返回值
     */
    public static final Color Teal = new Color(0x009688);
    /**
     * 绿色
     */
    /**
     * Color方法。
     *      * @param 0x4CAF50 Object类型参数
     * @return static final Color Green = new类型返回值
     */
    public static final Color Green = new Color(0x4CAF50);
    /**
     * 浅绿色
     */
    /**
     * Color方法。
     *      * @param 0x8BC34A Object类型参数
     * @return static final Color LightGreen = new类型返回值
     */
    public static final Color LightGreen = new Color(0x8BC34A);
    /**
     * 酸橙色
     */
    /**
     * Color方法。
     *      * @param 0xCDDC39 Object类型参数
     * @return static final Color Lime = new类型返回值
     */
    public static final Color Lime = new Color(0xCDDC39);
    /**
     * 黄色
     */
    /**
     * Color方法。
     *      * @param 0xFFEB3B Object类型参数
     * @return static final Color Yellow = new类型返回值
     */
    public static final Color Yellow = new Color(0xFFEB3B);
    /**
     * 琥珀色
     */
    /**
     * Color方法。
     *      * @param 0xFFC107 Object类型参数
     * @return static final Color Amber = new类型返回值
     */
    public static final Color Amber = new Color(0xFFC107);
    /**
     * 橙色
     */
    /**
     * Color方法。
     *      * @param 0xFF9800 Object类型参数
     * @return static final Color Orange = new类型返回值
     */
    public static final Color Orange = new Color(0xFF9800);
    /**
     * 深橙色
     */
    /**
     * Color方法。
     *      * @param 0xFF5722 Object类型参数
     * @return static final Color DeepOrange = new类型返回值
     */
    public static final Color DeepOrange = new Color(0xFF5722);
    /**
     * 棕色
     */
    /**
     * Color方法。
     *      * @param 0x795548 Object类型参数
     * @return static final Color Brown = new类型返回值
     */
    public static final Color Brown = new Color(0x795548);
    /**
     * 灰色
     */
    /**
     * Color方法。
     *      * @param 0x9E9E9E Object类型参数
     * @return static final Color Grey = new类型返回值
     */
    public static final Color Grey = new Color(0x9E9E9E);
    /**
     * 蓝灰色
     */
    /**
     * Color方法。
     *      * @param 0x607D8B Object类型参数
     * @return static final Color BlueGrey = new类型返回值
     */
    public static final Color BlueGrey = new Color(0x607D8B);
    /**
     * 黑色
     */
    /**
     * Color方法。
     *      * @param 0x000000 Object类型参数
     * @return static final Color Black = new类型返回值
     */
    public static final Color Black = new Color(0x000000);
    /**
     * 白色
     */
    /**
     * Color方法。
     *      * @param 0xFFFFFF Object类型参数
     * @return static final Color White = new类型返回值
     */
    public static final Color White = new Color(0xFFFFFF);


    /**
     * 绘制空心圆
     * @param g 图形上下文
     * @param x 圆心X坐标
     * @param y 圆心Y坐标
     * @param r 半径
     */
    /**
     * strokeCircle方法。
     *      * @param g Graphics2D类型参数
     * @param x int类型参数
     * @param y int类型参数
     * @param r int类型参数
     * @return static void类型返回值
     */
    public static void strokeCircle(Graphics2D g, int x, int y, int r) {

        Ellipse2D circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        g.draw(circle);
    }

    /**
     * 绘制实心圆
     * @param g 图形上下文
     * @param x 圆心X坐标
     * @param y 圆心Y坐标
     * @param r 半径
     */
    /**
     * fillCircle方法。
     *      * @param g Graphics2D类型参数
     * @param x int类型参数
     * @param y int类型参数
     * @param r int类型参数
     * @return static void类型返回值
     */
    public static void fillCircle(Graphics2D g, int x, int y, int r) {

        Ellipse2D circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        g.fill(circle);
    }

    /**
     * 绘制空心矩形
     * @param g 图形上下文
     * @param x 左上角X坐标
     * @param y 左上角Y坐标
     * @param w 宽度
     * @param h 高度
     */
    /**
     * strokeRectangle方法。
     *      * @param g Graphics2D类型参数
     * @param x int类型参数
     * @param y int类型参数
     * @param w int类型参数
     * @param h int类型参数
     * @return static void类型返回值
     */
    public static void strokeRectangle(Graphics2D g, int x, int y, int w, int h) {

        Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
        g.draw(rectangle);
    }

    /**
     * 绘制实心矩形
     * @param g 图形上下文
     * @param x 左上角X坐标
     * @param y 左上角Y坐标
     * @param w 宽度
     * @param h 高度
     */
    /**
     * fillRectangle方法。
     *      * @param g Graphics2D类型参数
     * @param x int类型参数
     * @param y int类型参数
     * @param w int类型参数
     * @param h int类型参数
     * @return static void类型返回值
     */
    public static void fillRectangle(Graphics2D g, int x, int y, int w, int h) {

        Rectangle2D rectangle = new Rectangle2D.Double(x, y, w, h);
        g.fill(rectangle);
    }

    /**
     * 设置绘图颜色
     * @param g 图形上下文
     * @param color 颜色
     */
    /**
     * setColor方法。
     *      * @param g Graphics2D类型参数
     * @param color Color类型参数
     * @return static void类型返回值
     */
    public static void setColor(Graphics2D g, Color color) {
        g.setColor(color);
    }

    /**
     * 设置线条宽度
     * @param g 图形上下文
     * @param w 宽度
     */
    /**
     * setStrokeWidth方法。
     *      * @param g Graphics2D类型参数
     * @param w int类型参数
     * @return static void类型返回值
     */
    public static void setStrokeWidth(Graphics2D g, int w) {
        int strokeWidth = w;
        g.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    /**
     * 暂停指定时间
     * @param t 暂停时间（毫秒）
     */
    /**
     * pause方法。
     *      * @param t int类型参数
     * @return static void类型返回值
     */
    public static void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

    /**
     * 绘制图片
     * @param g 图形上下文
     * @param x X坐标
     * @param y Y坐标
     * @param imageURL 图片路径
     */
    /**
     * putImage方法。
     *      * @param g Graphics2D类型参数
     * @param x int类型参数
     * @param y int类型参数
     * @param imageURL String类型参数
     * @return static void类型返回值
     */
    public static void putImage(Graphics2D g, int x, int y, String imageURL) {

        ImageIcon icon = new ImageIcon(imageURL);
        Image image = icon.getImage();

        g.drawImage(image, x, y, null);
    }

    /**
     * 绘制居中文字
     * @param g 图形上下文
     * @param text 文本内容
     * @param centerx 中心X坐标
     * @param centery 中心Y坐标
     * @throws IllegalArgumentException 如果文本为null
     */
    /**
     * drawText方法。
     *      * @param g Graphics2D类型参数
     * @param text String类型参数
     * @param centerx int类型参数
     * @param centery int类型参数
     * @return static void类型返回值
     */
    public static void drawText(Graphics2D g, String text, int centerx, int centery) {

        if (text == null)
            throw new IllegalArgumentException("Text is null in drawText function!");

        FontMetrics metrics = g.getFontMetrics();
        int w = metrics.stringWidth(text);
        int h = metrics.getDescent();
        g.drawString(text, centerx - w / 2, centery + h);
    }

}