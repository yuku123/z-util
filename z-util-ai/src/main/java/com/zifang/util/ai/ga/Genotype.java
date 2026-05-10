package com.zifang.util.ai.ga;

/**
 * 基因型接口 - 代表个体的染色体编码
 *
 * @param <T> 基因类型
 */
public interface Genotype<T> {

    /**
     * 获取基因长度
     */
    int length();

    /**
     * 获取指定位置的基因
     */
    T getGene(int index);

    /**
     * 设置指定位置的基因
     */
    void setGene(int index, T gene);

    /**
     * 复制当前基因型
     */
    Genotype<T> copy();

    /**
     * 随机初始化基因
     */
    void randomize();
}