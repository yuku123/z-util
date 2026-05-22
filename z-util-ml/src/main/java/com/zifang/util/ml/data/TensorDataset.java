package com.zifang.util.ml.data;

import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.Shape;
import com.zifang.util.numpy.Slice;

import java.util.ArrayList;
import java.util.List;

/**
 * Dataset wrapper around a list of NdArray tensors.
 * <p>
 * Each sample consists of one or more tensors (e.g., features and labels).
 * This provides a convenient way to create datasets from existing NdArray data.
 */
public class TensorDataset implements Dataset {

    private final List<NdArray[]> samples;
    private final int[] featureShape;
    private final int[] labelShape;

    /**
     * Create a TensorDataset from features and labels.
     *
     * @param features Features tensor of shape (n_samples, ...)
     * @param labels  Labels tensor of shape (n_samples, ...)
     */
    public TensorDataset(NdArray features, NdArray labels) {
        this.samples = new ArrayList<>();
        this.featureShape = features.getShape().getDimensions().clone();
        this.labelShape = labels.getShape().getDimensions().clone();

        int nSamples = features.getShape().get(0);
        for (int i = 0; i < nSamples; i++) {
            NdArray[] sample = new NdArray[2];
            sample[0] = features.slice(Slice.slice(i, i + 1));
            sample[1] = labels.slice(Slice.slice(i, i + 1));
            samples.add(sample);
        }
    }

    /**
     * Create a TensorDataset from a list of (features, labels) pairs.
     *
     * @param data List of NdArray pairs where each pair is [features, labels]
     */
    public TensorDataset(List<NdArray[]> data) {
        this.samples = new ArrayList<>(data);

        if (!data.isEmpty()) {
            NdArray[] first = data.get(0);
            this.featureShape = first[0].getShape().getDimensions().clone();
            this.labelShape = first[1].getShape().getDimensions().clone();
        } else {
            this.featureShape = new int[]{};
            this.labelShape = new int[]{};
        }
    }

    /**
     * Get the number of samples in the dataset.
     *
     * @return Number of samples
     */
    @Override
    public int size() {
        return samples.size();
    }

    /**
     * Get a sample from the dataset.
     *
     * @param index Sample index
     * @return Array of NdArrays representing [features, labels]
     */
    @Override
    public NdArray[] get(int index) {
        if (index < 0 || index >= samples.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + samples.size());
        }
        return samples.get(index);
    }

    /**
     * Get the shape of features.
     *
     * @return Shape of a single feature tensor
     */
    @Override
    public int[] getFeatureShape() {
        return featureShape;
    }

    /**
     * Get the shape of labels.
     *
     * @return Shape of a single label tensor
     */
    @Override
    public int[] getLabelShape() {
        return labelShape;
    }

    /**
     * Get all samples as a list.
     *
     * @return List of all samples
     */
    public List<NdArray[]> getAllSamples() {
        return new ArrayList<>(samples);
    }
}
