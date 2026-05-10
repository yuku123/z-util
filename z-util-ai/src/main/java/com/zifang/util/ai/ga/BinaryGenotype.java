package com.zifang.util.ai.ga;

import java.util.Random;

/**
 * 二进制基因型 - 染色体每个基因是0或1
 */
public class BinaryGenotype extends Individual {

    private static final Random RANDOM = new Random();

    public BinaryGenotype(int length) {
        super(length);
    }

    public BinaryGenotype(Integer[] chromosome) {
        super(chromosome);
    }

    @Override
    public void randomize() {
        for (int i = 0; i < length(); i++) {
            setGene(i, RANDOM.nextBoolean() ? 1 : 0);
        }
    }

    public Integer getGene(int index) {
        return (Integer) super.getGene(index);
    }

    public void setGene(int index, Integer gene) {
        super.setGene(index, gene);
    }

    /**
     * 获取二进制字符串表示
     */
    public String toBinaryString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            sb.append(getGene(i) == 1 ? "1" : "0");
        }
        return sb.toString();
    }
}