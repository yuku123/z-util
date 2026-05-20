package com.zifang.util.visuallization.swing.algrithm.lesson2.a;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * 算法可视化帮助类
 * 提供绘图辅助方法
 */
public class AlgoVisHelper {

    private AlgoVisHelper() {
    }

    /**
     * 绘制空心圆
     * @param g 图形上下文
     * @param x 圆心X坐标
     * @param y 圆心Y坐标
     * @param r 半径
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
    public static void fillCircle(Graphics2D g, int x, int y, int r) {

        Ellipse2D circle = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
        g.fill(circle);
    }

    /**
     * 设置绘图颜色
     * @param g 图形上下文
     * @param color 颜色
     */
    public static void setColor(Graphics2D g, Color color) {
        g.setColor(color);
    }

    /**
     * 设置线条宽度
     * @param g 图形上下文
     * @param w 宽度
     */
    public static void setStrokeWidth(Graphics2D g, int w) {
        int strokeWidth = w;
        g.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    /**
     * 暂停指定时间
     * @param t 暂停时间（毫秒）
     */
    public static void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

}
