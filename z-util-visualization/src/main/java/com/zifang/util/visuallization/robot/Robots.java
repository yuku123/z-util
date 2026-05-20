package com.zifang.util.visuallization.robot;

import java.awt.*;

/**
 * Robot测试类
 * 演示Java Robot类移动鼠标的基本功能
 */
public class Robots {
    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();
        robot.mouseMove(500, 501);
    }
}
