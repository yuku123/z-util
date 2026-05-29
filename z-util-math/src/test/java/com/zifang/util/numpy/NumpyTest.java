package com.zifang.util.numpy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Numpy 类测试
 */
public class NumpyTest {

    @Test
    public void testArrayFromIntArray() {
        int[] data = {1, 2, 3, 4, 5};
        NdArray arr = Numpy.array(data);
        assertNotNull(arr);
        assertEquals(5, arr.size());
        assertEquals(1, arr.ndim());
    }

    @Test
    public void testArrayFromDoubleArray() {
        double[] data = {1.0, 2.0, 3.0, 4.0, 5.0};
        NdArray arr = Numpy.array(data);
        assertNotNull(arr);
        assertEquals(5, arr.size());
        assertEquals(DType.FLOAT64, arr.getDtype());
    }

    @Test
    public void testArrayWithDType() {
        int[] data = {1, 2, 3};
        NdArray arr = Numpy.array(data, DType.INT32);
        assertNotNull(arr);
        assertEquals(DType.INT32, arr.getDtype());
    }

    @Test
    public void testZeros() {
        NdArray arr = Numpy.zeros(3, 4);
        assertNotNull(arr);
        assertEquals(12, arr.size());
        assertEquals(2, arr.ndim());
    }

    @Test
    public void testZerosWithShape() {
        NdArray arr = Numpy.zeros(new Shape(2, 3), DType.FLOAT64);
        assertNotNull(arr);
        assertEquals(6, arr.size());
    }

    @Test
    public void testOnes() {
        NdArray arr = Numpy.ones(3, 4);
        assertNotNull(arr);
        assertEquals(12, arr.size());
    }

    @Test
    public void testArangeWithStop() {
        NdArray arr = Numpy.arange(5);
        assertNotNull(arr);
        assertEquals(5, arr.size());
    }

    @Test
    public void testArangeWithStartStop() {
        NdArray arr = Numpy.arange(2, 5);
        assertNotNull(arr);
        assertEquals(3, arr.size());
    }

    @Test
    public void testArangeWithStartStopStep() {
        NdArray arr = Numpy.arange(0, 10, 1, DType.INT32);
        assertNotNull(arr);
        assertEquals(10, arr.size());
    }

    @Test
    public void testArangeWithDType() {
        NdArray arr = Numpy.arange(0, 5, DType.FLOAT64);
        assertNotNull(arr);
        assertEquals(DType.FLOAT64, arr.getDtype());
    }

    @Test
    public void testEmpty() {
        NdArray arr = Numpy.empty(3, 3);
        assertNotNull(arr);
        assertEquals(9, arr.size());
    }

    @Test
    public void testReshape() {
        NdArray arr = Numpy.arange(12);
        NdArray reshaped = Numpy.reshape(arr, 3, 4);
        assertNotNull(reshaped);
        assertEquals(12, reshaped.size());
    }

    @Test
    public void testTranspose() {
        NdArray arr = Numpy.arange(6).reshape(2, 3);
        NdArray transposed = Numpy.transpose(arr);
        assertNotNull(transposed);
        assertEquals(2, transposed.getShape().get(1));
        assertEquals(3, transposed.getShape().get(0));
    }

    @Test
    public void testCopy() {
        NdArray arr = Numpy.arange(5);
        NdArray copied = Numpy.copy(arr);
        assertNotNull(copied);
        assertEquals(arr.size(), copied.size());
    }

    @Test
    public void testFill() {
        NdArray arr = Numpy.zeros(3, 3);
        Numpy.fill(arr, 5.0);
        for (int i = 0; i < arr.size(); i++) {
            assertEquals(5.0, ((double[]) arr.getData())[i], 1e-10);
        }
    }

    @Test
    public void testShape() {
        NdArray arr = Numpy.zeros(2, 3, 4);
        Shape shape = Numpy.shape(arr);
        assertEquals(2, shape.get(0));
        assertEquals(3, shape.get(1));
        assertEquals(4, shape.get(2));
    }

    @Test
    public void testDtype() {
        int[] data = {1, 2, 3};
        NdArray arr = Numpy.array(data, DType.INT32);
        assertEquals(DType.INT32, Numpy.dtype(arr));
    }

    @Test
    public void testSize() {
        NdArray arr = Numpy.zeros(3, 4, 2);
        assertEquals(24, Numpy.size(arr));
    }

    @Test
    public void testNdim() {
        NdArray arr1d = Numpy.arange(5);
        assertEquals(1, Numpy.ndim(arr1d));

        NdArray arr2d = Numpy.zeros(3, 4);
        assertEquals(2, Numpy.ndim(arr2d));

        NdArray arr3d = Numpy.zeros(2, 3, 4);
        assertEquals(3, Numpy.ndim(arr3d));
    }

    @Test
    public void testSum() {
        NdArray arr = Numpy.array(new int[]{1, 2, 3, 4, 5});
        NdArray result = Numpy.sum(arr);
        assertNotNull(result);
    }

    @Test
    public void testMean() {
        NdArray arr = Numpy.array(new int[]{2, 4, 6, 8, 10});
        NdArray result = Numpy.mean(arr);
        assertNotNull(result);
    }

    @Test
    public void testMax() {
        NdArray arr = Numpy.array(new int[]{3, 1, 4, 1, 5, 9, 2, 6});
        NdArray result = Numpy.max(arr);
        assertNotNull(result);
        Object data = result.getData();
        if (data instanceof double[]) {
            assertEquals(9.0, ((double[]) data)[0], 1e-10);
        } else if (data instanceof int[]) {
            assertEquals(9, ((int[]) data)[0]);
        } else if (data instanceof long[]) {
            assertEquals(9L, ((long[]) data)[0]);
        }
    }

    @Test
    public void testMin() {
        NdArray arr = Numpy.array(new int[]{3, 1, 4, 1, 5, 9, 2, 6});
        NdArray result = Numpy.min(arr);
        assertNotNull(result);
        Object data = result.getData();
        if (data instanceof double[]) {
            assertEquals(1.0, ((double[]) data)[0], 1e-10);
        } else if (data instanceof int[]) {
            assertEquals(1, ((int[]) data)[0]);
        } else if (data instanceof long[]) {
            assertEquals(1L, ((long[]) data)[0]);
        }
    }

    @Test
    public void testSumEmpty() {
        NdArray arr = Numpy.array(new int[]{});
        NdArray result = Numpy.sum(arr);
        assertNotNull(result);
    }

    @Test
    public void testInferDType() {
        assertEquals(DType.INT8, Numpy.array(new byte[]{1, 2}).getDtype());
        assertEquals(DType.INT16, Numpy.array(new short[]{1, 2}).getDtype());
        assertEquals(DType.INT32, Numpy.array(new int[]{1, 2}).getDtype());
        assertEquals(DType.INT64, Numpy.array(new long[]{1, 2}).getDtype());
        assertEquals(DType.FLOAT32, Numpy.array(new float[]{1f, 2f}).getDtype());
        assertEquals(DType.FLOAT64, Numpy.array(new double[]{1.0, 2.0}).getDtype());
    }
}