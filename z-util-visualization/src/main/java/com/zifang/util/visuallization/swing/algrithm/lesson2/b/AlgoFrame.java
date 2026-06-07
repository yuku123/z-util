package com.zifang.util.visuallization.swing.algrithm.lesson2.b;

import javax.swing.*;
import java.awt.*;

/**
 * 算法可视化框架（模板类）
 * 提供算法可视化所需的绘图基础组件
 */
/**
 * AlgoFrame类。
 */
/**
 * AlgoFrame类。
 */
public class AlgoFrame extends JFrame {

    private int canvasWidth;
    private int canvasHeight;

    /**
     * 创建算法可视化框架
     * @param title 窗口标题
     * @param canvasWidth 画布宽度
     * @param canvasHeight 画布高度
     */
    /**
     * AlgoFrame方法。
     *      * @param title String类型参数
     * @param canvasWidth int类型参数
     * @param canvasHeight int类型参数
     */
    public AlgoFrame(String title, int canvasWidth, int canvasHeight) {

        super(title);

        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        AlgoCanvas canvas = new AlgoCanvas();
        setContentPane(canvas);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setVisible(true);
    }

    /**
     * 创建算法可视化框架（使用默认尺寸1024x768）
     * @param title 窗口标题
     */
    /**
     * AlgoFrame方法。
     *      * @param title String类型参数
     */
    public AlgoFrame(String title) {

        this(title, 1024, 768);
    }

    /**
     * 获取画布宽度
     * @return 画布宽度
     */
    /**
     * getCanvasWidth方法。
     * @return int类型返回值
     */
    public int getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * 获取画布高度
     * @return 画布高度
     */
    /**
     * getCanvasHeight方法。
     * @return int类型返回值
     */
    public int getCanvasHeight() {
        return canvasHeight;
    }

    private Object data;

    /**
     * 渲染数据
     * @param data 要渲染的数据对象
     */
    /**
     * render方法。
     *      * @param data Object类型参数
     */
    public void render(Object data) {
        this.data = data;
        repaint();
    }

    /**
     * 算法画布内部类
     * 继承自JPanel，负责绘制算法可视化内容
     * 使用双缓冲技术提升渲染性能
     */
    private class AlgoCanvas extends JPanel {

        /**
         * 创建算法画布
         * 启用双缓冲以减少闪烁
         */
    /**
     * AlgoCanvas方法。
     */
        public AlgoCanvas() {
            // 双缓存
            super(true);
        }

        @Override
    /**
     * paintComponent方法。
     *      * @param g Graphics类型参数
     */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            // 抗锯齿
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.addRenderingHints(hints);

            // 具体绘制
            // TODO： 绘制自己的数据data
        }

        @Override
    /**
     * getPreferredSize方法。
     * @return Dimension类型返回值
     */
        public Dimension getPreferredSize() {
            return new Dimension(canvasWidth, canvasHeight);
        }
    }
}


