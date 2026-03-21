package com.zifang.util.pandas.num;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Nums 工具类测试
 */
public class NumsTest {

    @Test
    public void testArrayCreation() {
        // 测试基本数组创建
        int[] intArray = {1, 2, 3, 4, 5};
        Num num = Nums.array(intArray);
        assertNotNull(num);
        assertEquals(5, num.size());
    }

    @Test
    public void testArrayFromList() {
        // 测试从 List 创建数组
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Num num = Nums.array(list);
        assertNotNull(num);
        assertEquals(5, num.size());
    }

    @Test
    public void testNDim() {
        // 测试维度计算
        int[] oneDim = {1, 2, 3};
        Num num1 = Nums.array(oneDim);
        assertEquals(1, num1.nDim());

        // 二维数组
        int[][] twoDim = {{1, 2}, {3, 4}};
        Num num2 = Nums.array(twoDim);
        assertEquals(2, num2.nDim());
    }

    @Test
    public void testShape() {
        // 测试形状计算
        int[][] twoDim = {{1, 2, 3}, {4, 5, 6}};
        Num num = Nums.array(twoDim);
        int[] shape = num.shape();
        assertEquals(2, shape.length);
        assertEquals(2, shape[0]);
        assertEquals(3, shape[1]);
    }

    @Test
    public void testSize() {
        // 测试元素总数计算
        int[][] twoDim = {{1, 2, 3}, {4, 5, 6}};
        Num num = Nums.array(twoDim);
        assertEquals(6, num.size());
    }

    @Test
    public void testToString() {
        // 测试字符串表示
        int[] arr = {1, 2, 3};
        Num num = Nums.array(arr);
        String str = num.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
    }

    @Test(expected = RuntimeException.class)
    public void testArrayWithNonArray() {
        // 测试传入非数组时抛出异常
        Nums.array("not an array");
    }

    @Test
    public void testRandomAccess() {
        // 测试随机数生成器可访问
        assertNotNull(Nums.random);
    }
}
