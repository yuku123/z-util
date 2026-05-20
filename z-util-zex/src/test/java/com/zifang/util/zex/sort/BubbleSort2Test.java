package com.zifang.util.zex.sort;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class BubbleSort2Test {

    private BubbleSort2 bubbleSort2 = new BubbleSort2();

    @Test
    public void testSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        bubbleSort2.sort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testSortEmpty() {
        int[] arr = new int[]{};
        bubbleSort2.sort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        bubbleSort2.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        bubbleSort2.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortSingleElement() {
        int[] arr = new int[]{1};
        bubbleSort2.sort(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testSortTwoElements() {
        int[] arr = new int[]{2, 1};
        bubbleSort2.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    public void testSortWithDuplicates() {
        int[] arr = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        bubbleSort2.sort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, arr);
    }

    @Test
    public void testSortAdv() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        bubbleSort2.sortAdv(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testSortAdvEmpty() {
        int[] arr = new int[]{};
        bubbleSort2.sortAdv(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testSortAdvAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        bubbleSort2.sortAdv(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortAdvReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        bubbleSort2.sortAdv(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testSortAdvSingleElement() {
        int[] arr = new int[]{1};
        bubbleSort2.sortAdv(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testSortAdvTwoElements() {
        int[] arr = new int[]{2, 1};
        bubbleSort2.sortAdv(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }

    @Test
    public void testSortAdvWithDuplicates() {
        int[] arr = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        bubbleSort2.sortAdv(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, arr);
    }
}
