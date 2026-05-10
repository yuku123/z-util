package com.zifang.util.visuallization.chart;

import javax.swing.*;
import java.awt.*;

/**
 * 图表窗口框架
 */
public abstract class ChartFrame extends JFrame {

    protected int canvasWidth;
    protected int canvasHeight;
    protected ChartCanvas canvas;

    public ChartFrame(String title, int canvasWidth, int canvasHeight) {
        super(title);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        initCanvas();
    }

    public ChartFrame(String title) {
        this(title, 800, 600);
    }

    protected void initCanvas() {
        canvas = createCanvas();
        setContentPane(canvas);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    protected abstract ChartCanvas createCanvas();

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public void render() {
        canvas.repaint();
    }

    protected class ChartCanvas extends JPanel {

        public ChartCanvas() {
            super(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            enableAntialiasing(g2d);
        }

        protected void enableAntialiasing(Graphics2D g2d) {
            RenderingHints hints = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.addRenderingHints(hints);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(canvasWidth, canvasHeight);
        }
    }

    public static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}