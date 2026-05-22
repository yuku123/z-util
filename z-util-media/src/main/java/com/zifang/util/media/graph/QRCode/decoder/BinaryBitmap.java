package com.zifang.util.media.graph.qrcode.decoder;

import com.zifang.util.media.graph.qrcode.encoder.BitMatrix;

/**
 * Binary bitmap wrapper for QR code decoding.
 * Represents a binarized image.
 */
public class BinaryBitmap {
    private final BitMatrix matrix;
    
    public BinaryBitmap(BitMatrix matrix) {
        this.matrix = matrix;
    }
    
    public BitMatrix getBlackMatrix() {
        return matrix;
    }
    
    public int getWidth() {
        return matrix.getWidth();
    }
    
    public int getHeight() {
        return matrix.getHeight();
    }
}
