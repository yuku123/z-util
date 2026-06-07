package com.zifang.util.numpy;

/**
 * Represents a slice operation for NdArray indexing
 */
/**
 * Slice类。
 */
/**
 * Slice类。
 */
public class Slice {
    private final int start;
    private final int stop;
    private final int step;

    /**
     * Slice方法。
     *      * @param null Object类型参数
     * @param null Object类型参数
     * @param null Object类型参数
     * @return static final Slice ELLIPSIS = new类型返回值
     */
    /**
     * Slice方法。
     *      * @param null Object类型参数
     * @param null Object类型参数
     * @param null Object类型参数
     * @return static final Slice ELLIPSIS = new类型返回值
     */
    public static final Slice ELLIPSIS = new Slice(null, null, null);

    private Slice(Integer start, Integer stop, Integer step) {
        this.start = start != null ? start : 0;
        this.stop = stop != null ? stop : Integer.MAX_VALUE;
        this.step = step != null ? step : 1;
    }

    /**
     * slice方法。
     *      * @param start int类型参数
     * @param stop int类型参数
     * @return static Slice类型返回值
     */
    /**
     * slice方法。
     *      * @param start int类型参数
     * @param stop int类型参数
     * @return static Slice类型返回值
     */
    public static Slice slice(int start, int stop) {
        return new Slice(start, stop, 1);
    }

    /**
     * slice方法。
     *      * @param start int类型参数
     * @param stop int类型参数
     * @param step int类型参数
     * @return static Slice类型返回值
     */
    /**
     * slice方法。
     *      * @param start int类型参数
     * @param stop int类型参数
     * @param step int类型参数
     * @return static Slice类型返回值
     */
    public static Slice slice(int start, int stop, int step) {
        return new Slice(start, stop, step);
    }

    /**
     * fromStart方法。
     *      * @param start int类型参数
     * @return static Slice类型返回值
     */
    /**
     * fromStart方法。
     *      * @param start int类型参数
     * @return static Slice类型返回值
     */
    public static Slice fromStart(int start) {
        return new Slice(start, null, 1);
    }

    /**
     * toStop方法。
     *      * @param stop int类型参数
     * @return static Slice类型返回值
     */
    /**
     * toStop方法。
     *      * @param stop int类型参数
     * @return static Slice类型返回值
     */
    public static Slice toStop(int stop) {
        return new Slice(null, stop, 1);
    }

    /**
     * all方法。
     * @return static Slice类型返回值
     */
    /**
     * all方法。
     * @return static Slice类型返回值
     */
    public static Slice all() {
        return new Slice(null, null, 1);
    }

    /**
     * getStart方法。
     * @return int类型返回值
     */
    /**
     * getStart方法。
     * @return int类型返回值
     */
    public Integer getStart() {
        return start;
    }

    /**
     * getStop方法。
     * @return int类型返回值
     */
    /**
     * getStop方法。
     * @return int类型返回值
     */
    public Integer getStop() {
        return stop;
    }

    /**
     * getStep方法。
     * @return int类型返回值
     */
    /**
     * getStep方法。
     * @return int类型返回值
     */
    public Integer getStep() {
        return step;
    }

    /**
     * Calculate the actual indices for this slice given a dimension size
     */
    /**
     * resolve方法。
     *      * @param dimensionSize int类型参数
     * @return int[]类型返回值
     */
    /**
     * resolve方法。
     *      * @param dimensionSize int类型参数
     * @return int[]类型返回值
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

    /**
     * length方法。
     *      * @param dimensionSize int类型参数
     * @return int类型返回值
     */
    /**
     * length方法。
     *      * @param dimensionSize int类型参数
     * @return int类型返回值
     */
    public int length(int dimensionSize) {
        return resolve(dimensionSize).length;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
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
