package com.zifang.util.zex.sort;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class QuickSortTest {

    private QuickSort quickSort = new QuickSort();

    @Test
    public void testSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testSortEmpty() {
        int[] arr = new int[]{};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortSingleElement() {
        int[] arr = new int[]{1};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testSortTwoElements() {
        int[] arr = new int[]{2, 1};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    public void testSortWithDuplicates() {
        int[] arr = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, arr);
    }

    @Test
    public void testSortAllSameElements() {
        int[] arr = new int[]{5, 5, 5, 5, 5};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{5, 5, 5, 5, 5}, arr);
    }

    @Test
    public void testSortNegativeNumbers() {
        int[] arr = new int[]{-5, 3, -2, 8, -1, 0, 7};
        quickSort.sort(arr);
        assertArrayEquals(new int[]{-5, -2, -1, 0, 3, 7, 8}, arr);
    }

    @Test
    public void testSortWithStartAndEnd() {
        int[] arr = new int[]{64, 25, 12, 22, 11, 30, 40};
        quickSort.sort(arr, 0, 4);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64, 30, 40}, arr);
    }

    @Test
    public void testSortStack() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        quickSort.sortStack(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testSortStackEmpty() {
        int[] arr = new int[]{};
        quickSort.sortStack(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testSortStackAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        quickSort.sortStack(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortStackReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        quickSort.sortStack(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortStackSingleElement() {
        int[] arr = new int[]{1};
        quickSort.sortStack(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testSortStackTwoElements() {
        int[] arr = new int[]{2, 1};
        quickSort.sortStack(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    public void testSortStackWithDuplicates() {
        int[] arr = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        quickSort.sortStack(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, arr);
    }
}
