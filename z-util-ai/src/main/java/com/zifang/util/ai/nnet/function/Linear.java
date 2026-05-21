package com.zifang.util.ai.nnet.function;

/**
 * 线性激活函数
 * <p>
 * 恒等激活函数，输出等于输入，不进行任何非线性变换。
 *
 * <p>数学表达式：
 * <pre>
 * f(x) = x
 * </pre>
 *
 * <p>特点：
 * <ul>
 *   <li>输出范围：(-∞, +∞)</li>
 *   <li>不会改变输入值</li>
 *   <li>梯度恒为1，不会出现梯度消失</li>
 * </ul>
 *
 * <p>应用场景：
 * <ul>
 *   <li>回归问题的输出层</li>
 *   <li>近似线性可分的问题</li>
 *   <li>作为其他激活函数的组成部分</li>
 * </ul>
 *
 * @author zifang
 * @see IActivationFunction
 * @see HardLimitThreshold
 */
public class Linear implements IActivationFunction {

    /**
     * 计算线性激活值（恒等函数）
     *
     * @param x 输入值
     * @return 输入值本身
     */
    @Override
    public Double calculate(Double x) {
        return x;
    }
}
