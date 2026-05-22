package com.zifang.util.ml.data;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.Shape;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

/**
 * Tests for TensorDataset and DataLoader.
 */
class DataLoaderTest {

    @Test
    void testTensorDataset() {
        int nSamples = 10;
        int nFeatures = 4;
        int nLabels = 3;

        // Create features
        float[] featureData = new float[nSamples * nFeatures];
        Random rand = new Random(42);
        for (int i = 0; i < featureData.length; i++) {
            featureData[i] = rand.nextFloat();
        }
        NdArray features = NdArray.array(featureData, DType.FLOAT32).reshape(nSamples, nFeatures);

        // Create labels (one-hot encoded for simplicity)
        float[] labelData = new float[nSamples * nLabels];
        for (int i = 0; i < nSamples; i++) {
            int label = rand.nextInt(nLabels);
            labelData[i * nLabels + label] = 1.0f;
        }
        NdArray labels = NdArray.array(labelData, DType.FLOAT32).reshape(nSamples, nLabels);

        // Create TensorDataset
        TensorDataset dataset = new TensorDataset(features, labels);

        // Verify size
        assertEquals(nSamples, dataset.size());

        // Verify feature shape
        int[] featureShape = dataset.getFeatureShape();
        assertEquals(nFeatures, featureShape[0]);

        // Verify label shape
        int[] labelShape = dataset.getLabelShape();
        assertEquals(nLabels, labelShape[0]);

        // Get a sample
        NdArray[] sample = dataset.get(0);
        assertEquals(2, sample.length);
        assertNotNull(sample[0]);
        assertNotNull(sample[1]);
    }

    @Test
    void testTensorDatasetGet() {
        int nSamples = 5;
        int nFeatures = 3;

        // Create simple features
        float[] featureData = new float[nSamples * nFeatures];
        for (int i = 0; i < featureData.length; i++) {
            featureData[i] = i;
        }
        NdArray features = NdArray.array(featureData, DType.FLOAT32).reshape(nSamples, nFeatures);

        // Create simple labels
        float[] labelData = new float[nSamples];
        for (int i = 0; i < nSamples; i++) {
            labelData[i] = i;
        }
        NdArray labels = NdArray.array(labelData, DType.FLOAT32).reshape(nSamples, 1);

        // Create TensorDataset
        TensorDataset dataset = new TensorDataset(features, labels);

        // Get sample 0
        NdArray[] sample0 = dataset.get(0);
        NdArray sampleFeatures = sample0[0];
        NdArray sampleLabels = sample0[1];

        // Verify sample 0 has feature values [0, 1, 2]
        Object featData = sampleFeatures.getData();
        assertEquals(0.0f, (float) com.zifang.util.numpy.Array.get(featData, 0), 0.001f);
        assertEquals(1.0f, (float) com.zifang.util.numpy.Array.get(featData, 1), 0.001f);
        assertEquals(2.0f, (float) com.zifang.util.numpy.Array.get(featData, 2), 0.001f);
    }

    @Test
    void testDataLoaderBatching() {
        int nSamples = 10;
        int nFeatures = 4;
        int batchSize = 3;

        // Create features
        float[] featureData = new float[nSamples * nFeatures];
        Random rand = new Random(42);
        for (int i = 0; i < featureData.length; i++) {
            featureData[i] = rand.nextFloat();
        }
        NdArray features = NdArray.array(featureData, DType.FLOAT32).reshape(nSamples, nFeatures);

        // Create labels
        float[] labelData = new float[nSamples];
        for (int i = 0; i < nSamples; i++) {
            labelData[i] = i;
        }
        NdArray labels = NdArray.array(labelData, DType.FLOAT32).reshape(nSamples, 1);

        // Create TensorDataset
        TensorDataset dataset = new TensorDataset(features, labels);

        // Create DataLoader without shuffle
        DataLoader dataLoader = new DataLoader(dataset, batchSize, false, false);

        // Verify number of batches
        // With 10 samples and batch size 3, without dropLast we get ceil(10/3) = 4 batches
        assertEquals(4, dataLoader.getNumBatches());

        // Iterate through batches
        int batchCount = 0;
        int totalSamples = 0;
        for (DataLoader.Batch batch : dataLoader) {
            batchCount++;
            totalSamples += batch.getSize();
        }

        assertEquals(4, batchCount);
        assertEquals(10, totalSamples);
    }

    @Test
    void testDataLoaderShuffle() {
        int nSamples = 10;
        int nFeatures = 4;
        int batchSize = 2;

        // Create features with sequential values
        float[] featureData = new float[nSamples * nFeatures];
        for (int i = 0; i < featureData.length; i++) {
            featureData[i] = i;
        }
        NdArray features = NdArray.array(featureData, DType.FLOAT32).reshape(nSamples, nFeatures);

        // Create labels with sequential values
        float[] labelData = new float[nSamples];
        for (int i = 0; i < nSamples; i++) {
            labelData[i] = i;
        }
        NdArray labels = NdArray.array(labelData, DType.FLOAT32).reshape(nSamples, 1);

        // Create TensorDataset
        TensorDataset dataset = new TensorDataset(features, labels);

        // Create DataLoader with shuffle
        DataLoader dataLoader = new DataLoader(dataset, batchSize, true, false);

        // With shuffle, batches should be different from sequential
        // Just verify we can iterate and get correct number of batches
        int batchCount = 0;
        for (DataLoader.Batch batch : dataLoader) {
            batchCount++;
            assertTrue(batch.getSize() <= batchSize);
        }

        // 10 samples / 2 batch size = 5 batches
        assertEquals(5, batchCount);
    }

    @Test
    void testDataLoaderDropLast() {
        int nSamples = 10;
        int nFeatures = 4;
        int batchSize = 3;

        // Create features
        float[] featureData = new float[nSamples * nFeatures];
        Random rand = new Random(42);
        for (int i = 0; i < featureData.length; i++) {
            featureData[i] = rand.nextFloat();
        }
        NdArray features = NdArray.array(featureData, DType.FLOAT32).reshape(nSamples, nFeatures);

        // Create labels
        float[] labelData = new float[nSamples];
        for (int i = 0; i < nSamples; i++) {
            labelData[i] = i;
        }
        NdArray labels = NdArray.array(labelData, DType.FLOAT32).reshape(nSamples, 1);

        // Create TensorDataset
        TensorDataset dataset = new TensorDataset(features, labels);

        // Create DataLoader with dropLast
        DataLoader dataLoader = new DataLoader(dataset, batchSize, false, true);

        // With dropLast: 10 / 3 = 3 full batches
        assertEquals(3, dataLoader.getNumBatches());

        // Iterate and count
        int batchCount = 0;
        int totalSamples = 0;
        for (DataLoader.Batch batch : dataLoader) {
            batchCount++;
            totalSamples += batch.getSize();
        }

        assertEquals(3, batchCount);
        assertEquals(9, totalSamples); // Last sample dropped
    }

    @Test
    void testDataLoaderBatchSize() {
        int nSamples = 8;
        int nFeatures = 4;

        // Create features
        float[] featureData = new float[nSamples * nFeatures];
        Random rand = new Random(42);
        for (int i = 0; i < featureData.length; i++) {
            featureData[i] = rand.nextFloat();
        }
        NdArray features = NdArray.array(featureData, DType.FLOAT32).reshape(nSamples, nFeatures);

        // Create labels
        float[] labelData = new float[nSamples];
        for (int i = 0; i < nSamples; i++) {
            labelData[i] = i;
        }
        NdArray labels = NdArray.array(labelData, DType.FLOAT32).reshape(nSamples, 1);

        // Create TensorDataset
        TensorDataset dataset = new TensorDataset(features, labels);

        // Create DataLoader with batch size 4
        DataLoader dataLoader = new DataLoader(dataset, 4, false, false);

        // Iterate through batches
        for (DataLoader.Batch batch : dataLoader) {
            // Each batch should have 4 samples (except possibly last)
            assertEquals(4, batch.getSize());

            // Verify features and labels are accessible
            NdArray batchFeatures = batch.getFeatures();
            NdArray batchLabels = batch.getLabels();
            assertNotNull(batchFeatures);
            assertNotNull(batchLabels);
        }
    }
}