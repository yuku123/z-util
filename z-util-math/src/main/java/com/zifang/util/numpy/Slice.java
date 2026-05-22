package com.zifang.util.numpy;

/**
 * Represents a slice operation for NdArray indexing
 */
public class Slice {
    private final int start;
    private final int stop;
    private final int step;

    public static final Slice ELLIPSIS = new Slice(null, null, null);

    private Slice(Integer start, Integer stop, Integer step) {
        this.start = start != null ? start : 0;
        this.stop = stop != null ? stop : Integer.MAX_VALUE;
        this.step = step != null ? step : 1;
    }

    public static Slice slice(int start, int stop) {
        return new Slice(start, stop, 1);
    }

    public static Slice slice(int start, int stop, int step) {
        return new Slice(start, stop, step);
    }

    public static Slice fromStart(int start) {
        return new Slice(start, null, 1);
    }

    public static Slice toStop(int stop) {
        return new Slice(null, stop, 1);
    }

    public static Slice all() {
        return new Slice(null, null, 1);
    }

    public Integer getStart() {
        return start;
    }

    public Integer getStop() {
        return stop;
    }

    public Integer getStep() {
        return step;
    }

    /**
     * Calculate the actual indices for this slice given a dimension size
     */
    public int[] resolve(int dimensionSize) {
        int actualStart = start < 0 ? Math.max(0, dimensionSize + start) : Math.min(start, dimensionSize);
        int actualStop = stop < 0 ? Math.max(0, dimensionSize + stop) : Math.min(stop, dimensionSize);
        
        if (actualStop <= actualStart && step > 0) {
            return new int[0];
        }

        int length = (int) Math.ceil((double) (actualStop - actualStart) / Math.abs(step));
        int[] indices = new int[length];

        int idx = 0;
        for (int i = actualStart; step > 0 ? i < actualStop : i > actualStop; i += step) {
            indices[idx++] = i;
        }

        return indices;
    }

    public int length(int dimensionSize) {
        return resolve(dimensionSize).length;
    }

    @Override
    public String toString() {
        if (this == ELLIPSIS) {
            return "...";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(start).append(":").append(stop);
        if (step != 1) {
            sb.append(":").append(step);
        }
        return sb.toString();
    }
}
