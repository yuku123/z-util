package com.zifang.util.ai.nnet.function;

/**
 * 阈值函数（硬限制）
 * <p>
 * f(x) = 0 当 x < 0<br>
 * f(x) = 1 当 x >= 0
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
