package com.zifang.util.visuallization.swing.algrithm.lesson2.a;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 圆形动画可视化器
 * 演示带碰撞检测的圆形动画效果
 */
public class AlgoVisualizer {

    private Circle[] circles;
    private AlgoFrame frame;
    private boolean isAnimated = true;

    /**
     * 创建可视化器
     * @param sceneWidth 场景宽度
     * @param sceneHeight 场景高度
     * @param N 圆形数量
     */
    public AlgoVisualizer(int sceneWidth, int sceneHeight, int N) {

        circles = new Circle[N];
        int R = 50;
        for (int i = 0; i < N; i++) {
            int x = (int) (Math.random() * (sceneWidth - 2 * R)) + R;
            int y = (int) (Math.random() * (sceneHeight - 2 * R)) + R;
            int vx = (int) (Math.random() * 11) - 5;
            int vy = (int) (Math.random() * 11) - 5;
            circles[i] = new Circle(x, y, R, vx, vy);
        }

        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("Welcome", sceneWidth, sceneHeight);
            frame.addKeyListener(new AlgoKeyListener());
            frame.addMouseListener(new AlgoMouseListener());
            new Thread(() -> {
                run();
            }).start();
        });
    }

    /**
     * 动画循环
     */
    private void run() {

        while (true) {
            frame.render(circles);
            AlgoVisHelper.pause(20);

            if (isAnimated)
                for (Circle circle : circles)
                    circle.move(0, 0, frame.getCanvasWidth(), frame.getCanvasHeight());
        }
    }

    /**
     * 键盘事件监听器内部类
     * 处理键盘按键释放事件
     */
    private class AlgoKeyListener extends KeyAdapter {

        /**
         * 按键释放事件处理
         * 空格键用于切换动画播放/暂停状态
         *
         * @param event 键盘事件对象
         */
        @Override
        public void keyReleased(KeyEvent event) {
            if (event.getKeyChar() == ' ')
                isAnimated = !isAnimated;
        }
    }

    /**
     * 鼠标事件监听器内部类
     * 处理鼠标释放事件，用于切换圆形填充状态
     */
    private class AlgoMouseListener extends MouseAdapter {

        /**
         * 鼠标释放事件处理
         * 如果点击位置在某个圆内，则切换该圆的填充状态
         *
         * @param event 鼠标事件对象，包含点击位置信息
         */
        @Override
        public void mouseReleased(MouseEvent event) {

            event.translatePoint(
                    0,
                    -(frame.getBounds().height - frame.getCanvasHeight())
            );
            //System.out.println(event.getPoint());

            for (Circle circle : circles)
                if (circle.contain(event.getPoint()))
                    circle.isFilled = !circle.isFilled;
        }
    }

    public static void main(String[] args) {

        int sceneWidth = 800;
        int sceneHeight = 800;
        int N = 10;

        AlgoVisualizer visualizer = new AlgoVisualizer(sceneWidth, sceneHeight, N);
    }
}
