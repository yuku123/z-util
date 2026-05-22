package com.zifang.util.ml.data;

import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.Shape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * DataLoader for batching, shuffling, and iterating over datasets.
 * <p>
 * Provides an iterable over batches of data with configurable batch size,
 * shuffling, and drop-last option for incomplete batches.
 */
public class DataLoader implements Iterable<DataLoader.Batch> {

    private final Dataset dataset;
    private final int batchSize;
    private final boolean shuffle;
    private final boolean dropLast;
    private final Random random;

    /**
     * Create a DataLoader.
     *
     * @param dataset    Source dataset
     * @param batchSize  Number of samples per batch
     * @param shuffle    Whether to shuffle data before each epoch
     * @param dropLast   Whether to drop the last incomplete batch
     */
    public DataLoader(Dataset dataset, int batchSize, boolean shuffle, boolean dropLast) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("Batch size must be positive: " + batchSize);
        }

        this.dataset = dataset;
        this.batchSize = batchSize;
        this.shuffle = shuffle;
        this.dropLast = dropLast;
        this.random = new Random();
    }

    /**
     * Create a DataLoader with default settings (no shuffle, no drop-last).
     *
     * @param dataset   Source dataset
     * @param batchSize Number of samples per batch
     */
    public DataLoader(Dataset dataset, int batchSize) {
        this(dataset, batchSize, false, false);
    }

    /**
     * Get the number of batches per epoch.
     *
     * @return Number of batches
     */
    public int getNumBatches() {
        int nSamples = dataset.size();
        if (dropLast) {
            return nSamples / batchSize;
        } else {
            return (nSamples + batchSize - 1) / batchSize;
        }
    }

    /**
     * Get the dataset size.
     *
     * @return Number of samples
     */
    public int size() {
        return dataset.size();
    }

    @Override
    public Iterator<Batch> iterator() {
        return new DataLoaderIterator();
    }

    /**
     * Represents a single batch of data.
     */
    public static class Batch {
        private final NdArray features;
        private final NdArray labels;

        public Batch(NdArray features, NdArray labels) {
            this.features = features;
            this.labels = labels;
        }

        public NdArray getFeatures() {
            return features;
        }

        public NdArray getLabels() {
            return labels;
        }

        public int getSize() {
            return features.getShape().get(0);
        }
    }

    /**
     * Iterator over batches.
     */
    private class DataLoaderIterator implements Iterator<Batch> {
        private final List<Integer> indices;
        private int currentIndex;

        DataLoaderIterator() {
            this.currentIndex = 0;

            // Initialize indices
            int nSamples = dataset.size();
            this.indices = new ArrayList<>(nSamples);
            for (int i = 0; i < nSamples; i++) {
                indices.add(i);
            }

            // Shuffle if requested
            if (shuffle) {
                for (int i = indices.size() - 1; i > 0; i--) {
                    int j = random.nextInt(i + 1);
                    int temp = indices.get(i);
                    indices.set(i, indices.get(j));
                    indices.set(j, temp);
                }
            }
        }

        @Override
        public boolean hasNext() {
            int nSamples = dataset.size();
            int maxIndex = dropLast
                    ? (nSamples / batchSize) * batchSize
                    : nSamples;

            return currentIndex < maxIndex;
        }

        @Override
        public Batch next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            int start = currentIndex;
            int end = Math.min(start + batchSize, dataset.size());

            List<NdArray> featureList = new ArrayList<>();
            List<NdArray> labelList = new ArrayList<>();

            for (int i = start; i < end; i++) {
                NdArray[] sample = dataset.get(indices.get(i));
                featureList.add(sample[0]);
                labelList.add(sample[1]);
            }

            currentIndex = end;

            // Concatenate tensors along batch dimension (axis 0)
            NdArray batchFeatures = concatenate(featureList, 0);
            NdArray batchLabels = concatenate(labelList, 0);

            return new Batch(batchFeatures, batchLabels);
        }

        /**
         * Concatenate a list of NdArrays along a specified axis.
         */
        private NdArray concatenate(List<NdArray> tensors, int axis) {
            if (tensors.isEmpty()) {
                throw new IllegalArgumentException("Cannot concatenate empty list");
            }

            if (tensors.size() == 1) {
                return tensors.get(0);
            }

            NdArray first = tensors.get(0);
            DType dtype = first.getDtype();
            int[] firstShape = first.getShape().getDimensions();

            // Calculate output shape
            int batchSize = tensors.size();
            int[] outputShape = firstShape.clone();
            outputShape[0] = batchSize;

            // For simplicity, concatenate along axis 0 (batch dimension)
            // Create output array
            int totalSize = 1;
            for (int dim : outputShape) {
                totalSize *= dim;
            }

            double[] outputData = new double[totalSize];
            int outputIdx = 0;

            for (NdArray tensor : tensors) {
                double[] tensorData = toDoubleArray(tensor);
                for (double val : tensorData) {
                    outputData[outputIdx++] = val;
                }
            }

            return NdArray.array(outputData, dtype).reshape(outputShape);
        }

        private double[] toDoubleArray(NdArray arr) {
            Object data = arr.getData();
            int size = arr.size();

            if (data instanceof double[]) {
                return (double[]) data;
            } else if (data instanceof float[]) {
                float[] fData = (float[]) data;
                double[] result = new double[size];
                for (int i = 0; i < size; i++) {
                    result[i] = fData[i];
                }
                return result;
            } else if (data instanceof int[]) {
                int[] iData = (int[]) data;
                double[] result = new double[size];
                for (int i = 0; i < size; i++) {
                    result[i] = iData[i];
                }
                return result;
            } else {
                double[] result = new double[size];
                for (int i = 0; i < size; i++) {
                    result[i] = ((Number) arr.get(i)).doubleValue();
                }
                return result;
            }
        }
    }
}
