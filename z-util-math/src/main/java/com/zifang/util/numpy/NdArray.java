package com.zifang.util.numpy;

import java.util.Arrays;

/**
 * N-dimensional array implementation
 */
public class NdArray {
    private Object data;
    private final DType dtype;
    private final Shape shape;

    private NdArray(Object data, DType dtype, Shape shape) {
        this.data = data;
        this.dtype = dtype;
        this.shape = shape;
    }

    public static NdArray create(Object data, DType dtype, Shape shape) {
        return new NdArray(data, dtype, shape);
    }

    public static NdArray zeros(Shape shape, DType dtype) {
        int size = shape.size();
        Object data = Array.createZeroArray(dtype, size);
        return new NdArray(data, dtype, shape);
    }

    public static NdArray ones(Shape shape, DType dtype) {
        int size = shape.size();
        Object data = Array.createOneArray(dtype, size);
        return new NdArray(data, dtype, shape);
    }

    public static NdArray arange(int start, int stop, int step, DType dtype) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be zero");
        }
        int size = (int) Math.ceil((double) (stop - start) / step);
        Object data = Array.arange(dtype, start, stop, step, size);
        return new NdArray(data, dtype, new Shape(size));
    }

    public static NdArray arange(int start, int stop, DType dtype) {
        return arange(start, stop, 1, dtype);
    }

    public static NdArray arange(int stop, DType dtype) {
        return arange(0, stop, 1, dtype);
    }

    public static NdArray array(Object data, DType dtype) {
        if (data instanceof Object[]) {
            Object[] arr = (Object[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        if (data instanceof byte[]) {
            byte[] arr = (byte[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        if (data instanceof short[]) {
            short[] arr = (short[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        if (data instanceof int[]) {
            int[] arr = (int[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        if (data instanceof long[]) {
            long[] arr = (long[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        if (data instanceof float[]) {
            float[] arr = (float[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        if (data instanceof double[]) {
            double[] arr = (double[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        if (data instanceof boolean[]) {
            boolean[] arr = (boolean[]) data;
            return new NdArray(arr, dtype, new Shape(arr.length));
        }
        throw new IllegalArgumentException("Unsupported array type: " + data.getClass());
    }

    public Object getData() {
        return data;
    }

    public DType getDtype() {
        return dtype;
    }

    public Shape getShape() {
        return shape;
    }

    public int size() {
        return shape.size();
    }

    public int ndim() {
        return shape.ndim();
    }

    public Object get(int... indices) {
        if (indices.length != shape.ndim()) {
            throw new IllegalArgumentException("Number of indices must match dimensions");
        }
        int flatIndex = calculateFlatIndex(indices);
        return Array.get(data, flatIndex);
    }

    public void set(Object value, int... indices) {
        if (indices.length != shape.ndim()) {
            throw new IllegalArgumentException("Number of indices must match dimensions");
        }
        int flatIndex = calculateFlatIndex(indices);
        Array.set(data, flatIndex, value);
    }

    private int calculateFlatIndex(int... indices) {
        int flatIndex = 0;
        int multiplier = 1;
        for (int i = shape.ndim() - 1; i >= 0; i--) {
            flatIndex += indices[i] * multiplier;
            multiplier *= shape.get(i);
        }
        return flatIndex;
    }

    public NdArray reshape(int... newShape) {
        int size = shape.size();
        int expectedSize = 1;
        for (int dim : newShape) {
            expectedSize *= dim;
        }
        if (size != expectedSize) {
            throw new IllegalArgumentException("Total size must remain the same: " + size + " vs " + expectedSize);
        }
        return new NdArray(data, dtype, new Shape(newShape));
    }

    public NdArray transpose() {
        if (shape.ndim() < 2) {
            return this;
        }
        int ndim = shape.ndim();
        int[] perm = new int[ndim];
        for (int i = 0; i < ndim; i++) {
            perm[i] = ndim - 1 - i;
        }
        return transpose(perm);
    }

    public NdArray transpose(int... axes) {
        if (axes.length != shape.ndim()) {
            throw new IllegalArgumentException("Number of axes must match dimensions");
        }
        int[] newShape = new int[axes.length];
        for (int i = 0; i < axes.length; i++) {
            newShape[i] = shape.get(axes[i]);
        }
        Object transposedData = Array.transpose(data, shape.getDimensions(), axes);
        return new NdArray(transposedData, dtype, new Shape(newShape));
    }

    public NdArray slice(Slice... slices) {
        if (slices.length > shape.ndim()) {
            throw new IllegalArgumentException("Too many slices");
        }
        Slice[] fullSlices = new Slice[shape.ndim()];
        int sliceIdx = 0;
        for (int i = 0; i < shape.ndim(); i++) {
            if (sliceIdx < slices.length) {
                fullSlices[i] = slices[sliceIdx++];
            } else {
                fullSlices[i] = Slice.all();
            }
        }
        return sliceInternal(fullSlices);
    }

    private NdArray sliceInternal(Slice[] slices) {
        int[] newShape = new int[shape.ndim()];
        int[][] allIndices = new int[shape.ndim()][];
        int totalSize = 1;

        for (int i = 0; i < shape.ndim(); i++) {
            allIndices[i] = slices[i].resolve(shape.get(i));
            newShape[i] = allIndices[i].length;
            totalSize *= newShape[i];
        }

        Object newData = Array.createZeroArray(dtype, totalSize);
        int[] idx = new int[shape.ndim()];
        sliceRecursive(data, newData, allIndices, idx, 0);
        return new NdArray(newData, dtype, new Shape(newShape));
    }

    private void sliceRecursive(Object src, Object dst, int[][] indices, int[] idx, int dim) {
        if (dim == indices.length) {
            int flatSrcIdx = calculateFlatIndex(idx);
            int flatDstIdx = calculateFlatIndex(Arrays.copyOf(idx, idx.length));
            Array.copy(src, flatSrcIdx, dst, flatDstIdx);
            return;
        }
        for (int i = 0; i < indices[dim].length; i++) {
            idx[dim] = indices[dim][i];
            sliceRecursive(src, dst, indices, idx, dim + 1);
        }
    }

    public NdArray copy() {
        Object newData = Array.copy(data, shape.size(), dtype);
        return new NdArray(newData, dtype, shape);
    }

    public NdArray fill(Object value) {
        Array.fill(data, value, shape.size());
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NdArray(shape=").append(shape);
        sb.append(", dtype=").append(dtype);
        sb.append(", data=");
        sb.append(Array.toString(data, shape.size()));
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NdArray ndArray = (NdArray) o;
        if (!shape.equals(ndArray.shape) || dtype != ndArray.dtype) return false;
        return Array.equals(data, ndArray.data, shape.size());
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(shape.getDimensions());
        result = 31 * result + dtype.hashCode();
        result = 31 * result + Array.hashCode(data, shape.size());
        return result;
    }
}
