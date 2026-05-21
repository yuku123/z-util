package com.zifang.util.distributes.sequence;

import org.junit.Test;

/**
 * Sequence序列号生成器单元测试类
 * <p>
 * 提供Sequence生成器的功能测试，包括：
 * <ul>
 *   <li>基本ID生成测试</li>
 *   <li>ID序列单调性验证</li>
 * </ul>
 *
 * @author zifang
 * @see Sequence
 */
public class SequenceTest1 {

    @Test
    public void name() {
        Sequence sequence = new Sequence(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = sequence.nextId();
            System.out.println(id);
        }
    }

}
