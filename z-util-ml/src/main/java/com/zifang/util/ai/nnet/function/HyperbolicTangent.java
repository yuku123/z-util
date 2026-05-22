package com.zifang.util.ml.nnet.function;

/**
 * 双曲正切激活函数
 * <p>
 * 将输入值映射到(-1, 1)区间，比Sigmoid函数更适合处理具有正负值的数据。
 *
 * <p>数学表达式：
 * <pre>
 * f(x) = tanh(x) = (e^x - e^(-x)) / (e^x + e^(-x))
 * </pre>
 *
 * <p>特点：
 * <ul>
 *   <li>输出范围：(-1, 1)，零中心化</li>
 *   <li>梯度在(0, 1)范围内，比Sigmoid更不容易饱和</li>
 *   <li>计算效率与Sigmoid相当</li>
 * </ul>
 *
 * <p>应用场景：
 * <ul>
 *   <li>隐藏层的激活函数</li>
 *   <li>需要零中心化输出的问题</li>
 *   <li>LSTM等循环神经网络的门控机制</li>
 * </ul>
 *
 * @author zifang
 * @see IActivationFunction
 * @see SigmoidActivation
 */
public class HyperbolicTangent implements IActivationFunction {

    /**
     * 计算双曲正切激活值
     *
     * @param x 输入值
     * @return 激活值，范围在(-1, 1)之间
     */
    @Override
    public Double calculate(Double x) {
        return (1D - Math.pow(E, -x)) / (1D + Math.pow(E, x));
    }
}
