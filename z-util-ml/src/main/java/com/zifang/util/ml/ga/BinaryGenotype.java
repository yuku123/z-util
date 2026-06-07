package com.zifang.util.ml.ga;

import java.util.Random;

/**
 * 二进制基因型 - 染色体每个基因是0或1
 */
/**
 * BinaryGenotype类。
 */
/**
 * BinaryGenotype类。
 */
public class BinaryGenotype extends Individual {

    private static final Random RANDOM = new Random();

    /**
     * 构造一个具有指定染色体长度的二进制基因型个体
     *
     * @param length 染色体长度
     */
    /**
     * BinaryGenotype方法。
     *      * @param length int类型参数
     */
    /**
     * BinaryGenotype方法。
     *      * @param length int类型参数
     */
    public BinaryGenotype(int length) {
        super(length);
    }

    /**
     * 构造一个具有给定染色体的二进制基因型个体
     *
     * @param chromosome 染色体数组，每个元素为0或1
     */
    /**
     * BinaryGenotype方法。
     *      * @param chromosome Integer[]类型参数
     */
    /**
     * BinaryGenotype方法。
     *      * @param chromosome Integer[]类型参数
     */
    public BinaryGenotype(Integer[] chromosome) {
        super(chromosome);
    }

    /**
     * 随机初始化染色体的每个基因
     */
    @Override
    /**
     * randomize方法。
     */
    /**
     * randomize方法。
     */
    public void randomize() {
        for (int i = 0; i < length(); i++) {
            setGene(i, RANDOM.nextBoolean() ? 1 : 0);
        }
    }

    /**
     * 获取指定位置的基因值
     *
     * @param index 基因索引
     * @return 基因值（0或1）
     */
    /**
     * getGene方法。
     *      * @param index int类型参数
     * @return int类型返回值
     */
    /**
     * getGene方法。
     *      * @param index int类型参数
     * @return int类型返回值
     */
    public Integer getGene(int index) {
        return (Integer) super.getGene(index);
    }

    /**
     * 设置指定位置的基因值
     *
     * @param index 基因索引
     * @param gene 基因值（必须为0或1）
     */
    /**
     * setGene方法。
     *      * @param index int类型参数
     * @param gene int类型参数
     */
    /**
     * setGene方法。
     *      * @param index int类型参数
     * @param gene int类型参数
     */
    public void setGene(int index, Integer gene) {
        super.setGene(index, gene);
    }

    /**
     * 获取二进制字符串表示
     *
     * @return 由0和1组成的字符串
     */
    /**
     * toBinaryString方法。
     * @return String类型返回值
     */
    /**
     * toBinaryString方法。
     * @return String类型返回值
     */
    public String toBinaryString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            sb.append(getGene(i) == 1 ? "1" : "0");
        }
        return sb.toString();
    }
}