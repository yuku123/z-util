package com.zifang.util.zex.sort;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class DualPivotQuicksortTest {

    @Test
    public void testSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testSortEmpty() {
        int[] arr = new int[]{};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortSingleElement() {
        int[] arr = new int[]{1};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testSortTwoElements() {
        int[] arr = new int[]{2, 1};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    public void testSortWithDuplicates() {
        int[] arr = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, arr);
    }

    @Test
    public void testSortAllSameElements() {
        int[] arr = new int[]{5, 5, 5, 5, 5};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{5, 5, 5, 5, 5}, arr);
    }

    @Test
    public void testSortNegativeNumbers() {
        int[] arr = new int[]{-5, 3, -2, 8, -1, 0, 7};
        DualPivotQuicksort.sort(arr);
        assertArrayEquals(new int[]{-5, -2, -1, 0, 3, 7, 8}, arr);
    }

    @Test
    public void testSortWithLeftAndRight() {
        int[] arr = new int[]{64, 25, 12, 22, 11, 30, 40};
        DualPivotQuicksort.sort(arr, 0, 4);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64, 30, 40}, arr);
    }

    @Test
    public void testSortWithLeftAndRightFullRange() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        DualPivotQuicksort.sort(arr, 0, 4);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }
}
