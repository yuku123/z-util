package com.zifang.util.ml.nnet.function;

/**
 * 激活函数接口
 * <p>
 * 定义神经网络激活函数的标准接口。所有激活函数实现类都需要实现此接口。
 * 激活函数用于对神经元的加权输入进行非线性变换，是神经网络能够表达
 * 复杂非线性关系的关键组成部分。
 *
 * <p>常用的激活函数包括：
 * <ul>
 *   <li>Sigmoid函数：输出范围(0, 1)，常用于二分类问题</li>
 *   <li>Tanh函数：输出范围(-1, 1)，零中心化</li>
 *   <li>ReLU函数：计算高效，用于深度网络</li>
 *   <li>Softmax函数：多分类问题的输出层</li>
 *   <li>线性激活函数：恒等变换，用于回归输出</li>
 * </ul>
 *
 * @author zifang
 * @see SigmoidActivation
 * @see HyperbolicTangent
 * @see Linear
 * @see HardLimitThreshold
 */
public interface IActivationFunction {

    /**
     * 欧拉常数（自然对数的底）
     */
    Double E = Math.E;

    /**
     * 计算激活值
     *
     * @param x 输入值，可以是任意实数
     * @return 激活后的值，具体范围取决于实现
     */
    Double calculate(Double x);
}
