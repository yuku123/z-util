package com.zifang.util.visuallization.swing.algrithm.lesson2.a;

/**
 * 算法可视化客户端入口
 * 用于启动圆形动画可视化
 */
public class Client {
    public static void main(String[] args) {
        int sceneWidth = 800;
        int sceneHeight = 800;
        int N = 100;

        AlgoVisualizer visualizer = new AlgoVisualizer(sceneWidth, sceneHeight, N);
    }
}
