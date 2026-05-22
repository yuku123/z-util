package com.zifang.util.numpy;

import java.util.Arrays;

/**
 * Represents the shape of an NdArray
 */
public class Shape {
    private final int[] dimensions;

    public Shape(int... dimensions) {
        if (dimensions == null || dimensions.length == 0) {
            this.dimensions = new int[0];
        } else {
            this.dimensions = Arrays.copyOf(dimensions, dimensions.length);
        }
    }

    public Shape(long... dimensions) {
        if (dimensions == null || dimensions.length == 0) {
            this.dimensions = new int[0];
        } else {
            this.dimensions = new int[dimensions.length];
            for (int i = 0; i < dimensions.length; i++) {
                this.dimensions[i] = (int) dimensions[i];
            }
        }
    }

    public int[] getDimensions() {
        return Arrays.copyOf(dimensions, dimensions.length);
    }

    public int ndim() {
        return dimensions.length;
    }

    public int size() {
        int result = 1;
        for (int dim : dimensions) {
            result *= dim;
        }
        return result;
    }

    public int get(int index) {
        if (index < 0 || index >= dimensions.length) {
            throw new IndexOutOfBoundsException("Dimension index out of bounds: " + index);
        }
        return dimensions[index];
    }

    public Shape reshape(int... newShape) {
        return new Shape(newShape);
    }

    public Shape transpose() {
        int[] transposed = new int[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            transposed[i] = dimensions[dimensions.length - 1 - i];
        }
        return new Shape(transposed);
    }

    public boolean isScalar() {
        return dimensions.length == 0;
    }

    public boolean isVector() {
        return dimensions.length == 1;
    }

    public boolean isMatrix() {
        return dimensions.length == 2;
    }

    @Override
    public String toString() {
        if (dimensions.length == 0) {
            return "()";
        }
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < dimensions.length; i++) {
            sb.append(dimensions[i]);
            if (i < dimensions.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Arrays.equals(dimensions, shape.dimensions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dimensions);
    }
}
