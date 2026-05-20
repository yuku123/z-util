package com.zifang.util.visuallization.swing.algrithm.lesson2.a;

import java.awt.*;

/**
 * 圆形数据类
 * 表示具有位置、半径和速度的圆形对象
 */
public class Circle {

    public int x, y;
    private int r;
    public int vx, vy;
    public boolean isFilled = false;

    /**
     * 创建圆形
     * @param x X坐标
     * @param y Y坐标
     * @param r 半径
     * @param vx X方向速度
     * @param vy Y方向速度
     */
    public Circle(int x, int y, int r, int vx, int vy) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.vx = vx;
        this.vy = vy;
    }

    /**
     * 获取半径
     * @return 半径
     */
    public int getR() {
        return r;
    }

    /**
     * 移动圆形（带边界碰撞检测）
     * @param minx 最小X坐标
     * @param miny 最小Y坐标
     * @param maxx 最大X坐标
     * @param maxy 最大Y坐标
     */
    public void move(int minx, int miny, int maxx, int maxy) {
        x += vx;
        y += vy;

        checkCollision(minx, miny, maxx, maxy);
    }

    /**
     * 检测是否包含指定点
     * @param p 坐标点
     * @return 如果点在圆内返回true
     */
    public boolean contain(Point p) {
        return (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y) <= r * r;
    }

}
