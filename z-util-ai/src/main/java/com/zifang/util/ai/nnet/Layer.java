package com.zifang.util.ai.nnet;

/**
 * 神经网络层接口
 */
public interface Layer {

    /**
     * 前向传播
     *
     * @param inputs 输入数据
     * @return 输出数据
     */
    double[] forward(double[] inputs);

    /**
     * 反向传播
     *
     * @param gradients 输出层梯度
     * @return 输入层梯度
     */
    double[] backward(double[] gradients);

    /**
     * 获取层输出
     */
    double[] getOutput();

    /**
     * 获取层类型
     */
    LayerType getLayerType();

    enum LayerType {
        INPUT,
        HIDDEN,
        OUTPUT
    }
}