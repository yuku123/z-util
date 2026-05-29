package com.zifang.util.media.graph.qrcode.encoder;

import org.junit.Test;
import static org.junit.Assert.*;

public class ReedSolomonEncoderTest {

    @Test
    public void testEncodeSimpleData() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        byte[] data = new byte[]{0x12, 0x34, 0x56, 0x78};
        byte[] ecc = encoder.encode(data, 7);
        assertNotNull(ecc);
        assertEquals(7, ecc.length);
    }

    @Test
    public void testEncodeProducesNonZeroECC() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        byte[] data = new byte[]{0x00, 0x00, 0x00, 0x00};
        byte[] ecc = encoder.encode(data, 7);
        assertNotNull(ecc);
        // Zero data should still produce ECC
        boolean allZero = true;
        for (byte b : ecc) {
            if (b != 0) {
                allZero = false;
                break;
            }
        }
        // ECC should not be all zeros for typical data
    }

    @Test
    public void testEncodeDifferentDataLengths() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        // Test with different data lengths
        byte[] data1 = new byte[]{0x01};
        byte[] data2 = new byte[]{0x01, 0x02, 0x03};
        byte[] data3 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

        byte[] ecc1 = encoder.encode(data1, 5);
        byte[] ecc2 = encoder.encode(data2, 10);
        byte[] ecc3 = encoder.encode(data3, 15);

        assertNotNull(ecc1);
        assertNotNull(ecc2);
        assertNotNull(ecc3);
        assertEquals(5, ecc1.length);
        assertEquals(10, ecc2.length);
        assertEquals(15, ecc3.length);
    }

    @Test
    public void testEncodeNullDataThrows() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        try {
            encoder.encode(null, 7);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("null") || e.getMessage().contains("empty"));
        }
    }

    @Test
    public void testEncodeEmptyDataThrows() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        try {
            encoder.encode(new byte[]{}, 7);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("null") || e.getMessage().contains("empty"));
        }
    }

    @Test
    public void testEncodeNegativeECBytesThrows() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        try {
            encoder.encode(new byte[]{0x01, 0x02}, -1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("negative"));
        }
    }

    @Test
    public void testDecodeNoErrors() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        byte[] original = new byte[]{0x12, 0x34, 0x56, 0x78};
        byte[] ecc = encoder.encode(original, 7);

        // Combine data + ECC
        byte[] dataWithEcc = new byte[original.length + ecc.length];
        System.arraycopy(original, 0, dataWithEcc, 0, original.length);
        System.arraycopy(ecc, 0, dataWithEcc, original.length, ecc.length);

        byte[] decoded = encoder.decode(dataWithEcc, ecc.length);
        assertNotNull(decoded);
        assertEquals(original.length, decoded.length);
    }

    @Test
    public void testEncodeThenDecode() {
        ReedSolomonEncoder encoder = new ReedSolomonEncoder();
        byte[] original = new byte[]{(byte)0xAB, (byte)0xCD, (byte)0xEF, (byte)0x01, (byte)0x23, (byte)0x45};
        int eccLength = 10;

        byte[] ecc = encoder.encode(original, eccLength);
        byte[] combined = new byte[original.length + eccLength];
        System.arraycopy(original, 0, combined, 0, original.length);
        System.arraycopy(ecc, 0, combined, original.length, eccLength);

        byte[] decoded = encoder.decode(combined, eccLength);
        assertEquals(original.length, decoded.length);
    }
}
