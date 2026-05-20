package com.zifang.util.zex.sort;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class BubbleSortTest {

    @Test
    public void testBubbleSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        int[] result = BubbleSort.bubbleSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, result);
    }

    @Test
    public void testBubbleSortEmpty() {
        int[] arr = new int[]{};
        int[] result = BubbleSort.bubbleSort(arr);
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    public void testBubbleSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        int[] result = BubbleSort.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testBubbleSortReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        int[] result = BubbleSort.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testBubbleSortSingleElement() {
        int[] arr = new int[]{1};
        int[] result = BubbleSort.bubbleSort(arr);
        assertArrayEquals(new int[]{1}, result);
    }

    @Test
    public void testBubbleSortTwoElements() {
        int[] arr = new int[]{2, 1};
        int[] result = BubbleSort.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    @Test
    public void testBubbleSortWithDuplicates() {
        int[] arr = new int[]{3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        int[] result = BubbleSort.bubbleSort(arr);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 4, 5, 5, 6, 9}, result);
    }
}
