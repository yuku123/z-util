package com.zifang.util.ml.nnet.function;

/**
 * 阈值函数（硬限制激活函数）
 * <p>
 * 阶跃函数，将输入值映射为0或1。
 * 当输入小于0时输出0，当输入大于等于0时输出1。
 *
 * <p>数学表达式：
 * <pre>
 * f(x) = 0  当 x < 0
 * f(x) = 1  当 x >= 0
 * </pre>
 *
 * <p>应用场景：
 * <ul>
 *   <li>早期的感知机模型</li>
 *   <li>二分类问题的决策边界</li>
 *   <li>需要离散输出的逻辑控制系统</li>
 * </ul>
 *
 * @author zifang
 * @see IActivationFunction
 * @see Linear
 */
public class HardLimitThreshold implements IActivationFunction {

    /**
     * 计算阈值激活值
     *
     * @param x 输入值
     * @return 当x小于0时返回0，否则返回1
     */
    @Override
    public Double calculate(Double x) {
        return x < 0D ? 0D : 1D;
    }
}
