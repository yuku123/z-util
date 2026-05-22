package com.zifang.util.media.graph.qrcode.encoder;

/**
 * A 2D boolean matrix implementation for QR code encoding/decoding.
 * This replaces com.google.zxing.common.BitMatrix.
 */
public class BitMatrix {
    private final int width;
    private final int height;
    private final boolean[] bits;

    public BitMatrix(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Width and height must be positive");
        }
        this.width = width;
        this.height = height;
        this.bits = new boolean[width * height];
    }

    public boolean get(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        return bits[y * width + x];
    }

    public void set(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        bits[y * width + x] = true;
    }

    /**
     * Sets the bit at (x, y) to the given value.
     */
    public void set(int x, int y, boolean value) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        bits[y * width + x] = value;
    }

    public void unset(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        bits[y * width + x] = false;
    }

    public void flip(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        bits[y * width + x] = !bits[y * width + x];
    }

    public void clear() {
        for (int i = 0; i < bits.length; i++) {
            bits[i] = false;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Finds the top-left on bit in the matrix.
     * @return int[] array of [x, y] or null if no bits are set
     */
    public int[] getTopLeftOnBit() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bits[y * width + x]) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    /**
     * Finds the bottom-right on bit in the matrix.
     * @return int[] array of [x, y] or null if no bits are set
     */
    public int[] getBottomRightOnBit() {
        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                if (bits[y * width + x]) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    @Override
    public BitMatrix clone() {
        BitMatrix clone = new BitMatrix(width, height);
        System.arraycopy(this.bits, 0, clone.bits, 0, this.bits.length);
        return clone;
    }
}
