package com.zifang.util.zex.sort;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class SortUtilsTest {

    private SortUtils sortUtils = new SortUtils();

    @Test
    public void testDirectSelectionSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        sortUtils.directSelectionSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testDirectSelectionSortEmpty() {
        int[] arr = new int[]{};
        sortUtils.directSelectionSort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testDirectSelectionSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        sortUtils.directSelectionSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testDirectSelectionSortReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        sortUtils.directSelectionSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testBinaryInsertionSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        sortUtils.binaryInsertionSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testBinaryInsertionSortEmpty() {
        int[] arr = new int[]{};
        sortUtils.binaryInsertionSort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testBinaryInsertionSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        sortUtils.binaryInsertionSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testBinaryInsertionSortSingleElement() {
        int[] arr = new int[]{1};
        sortUtils.binaryInsertionSort(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testDirectInsertSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        sortUtils.directInsertSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testDirectInsertSortEmpty() {
        int[] arr = new int[]{};
        sortUtils.directInsertSort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testDirectInsertSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        sortUtils.directInsertSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testDirectInsertSortReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        sortUtils.directInsertSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testDirectInsertSortSingleElement() {
        int[] arr = new int[]{1};
        sortUtils.directInsertSort(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testShellSort() {
        int[] arr = new int[]{64, 25, 12, 22, 11};
        sortUtils.shellSort(arr);
        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    public void testShellSortEmpty() {
        int[] arr = new int[]{};
        sortUtils.shellSort(arr);
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    public void testShellSortAlreadySorted() {
        int[] arr = new int[]{1, 2, 3, 4, 5};
        sortUtils.shellSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testShellSortReverseSorted() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        sortUtils.shellSort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    public void testShellSortSingleElement() {
        int[] arr = new int[]{1};
        sortUtils.shellSort(arr);
        assertArrayEquals(new int[]{1}, arr);
    }

    @Test
    public void testShellSortTwoElements() {
        int[] arr = new int[]{2, 1};
        sortUtils.shellSort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
    }
}
