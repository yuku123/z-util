package com.zifang.util.pandas.num;

import java.util.Random;

/**
 * NumRandom 类 - 对标 numpy.random
 * 提供随机数生成功能
 */
public class NumRandom {

    private Random random;
    private long seed;

    public NumRandom() {
        this.random = new Random();
    }

    public NumRandom(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    public void seed(long seed) {
        this.seed = seed;
        this.random.setSeed(seed);
    }

    // ==================== 简单随机数据 ====================

    /**
     * 生成 [0, 1) 之间的随机浮点数
     */
    public Num rand(int... shape) {
        if (shape.length == 1) {
            double[] array = new double[shape[0]];
            for (int i = 0; i < shape[0]; i++) {
                array[i] = random.nextDouble();
            }
            return new Num(array);
        } else if (shape.length == 2) {
            double[][] array = new double[shape[0]][shape[1]];
            for (int i = 0; i < shape[0]; i++) {
                for (int j = 0; j < shape[1]; j++) {
                    array[i][j] = random.nextDouble();
                }
            }
            return new Num(array);
        }
        throw new UnsupportedOperationException("Shape dimensions > 2 not yet supported");
    }

    /**
     * 生成标准正态分布的随机数
     */
    public Num randn(int... shape) {
        if (shape.length == 1) {
            double[] array = new double[shape[0]];
            for (int i = 0; i < shape[0]; i++) {
                array[i] = random.nextGaussian();
            }
            return new Num(array);
        } else if (shape.length == 2) {
            double[][] array = new double[shape[0]][shape[1]];
            for (int i = 0; i < shape[0]; i++) {
                for (int j = 0; j < shape[1]; j++) {
                    array[i][j] = random.nextGaussian();
                }
            }
            return new Num(array);
        }
        throw new UnsupportedOperationException("Shape dimensions > 2 not yet supported");
    }

    /**
     * 生成 [low, high) 之间的随机整数
     */
    public Num randint(int low, int high, int... shape) {
        if (shape.length == 0) {
            return new Num(new int[]{random.nextInt(high - low) + low});
        } else if (shape.length == 1) {
            int[] array = new int[shape[0]];
            for (int i = 0; i < shape[0]; i++) {
                array[i] = random.nextInt(high - low) + low;
            }
            return new Num(array);
        } else if (shape.length == 2) {
            int[][] array = new int[shape[0]][shape[1]];
            for (int i = 0; i < shape[0]; i++) {
                for (int j = 0; j < shape[1]; j++) {
                    array[i][j] = random.nextInt(high - low) + low;
                }
            }
            return new Num(array);
        }
        throw new UnsupportedOperationException("Shape dimensions > 2 not yet supported");
    }

    /**
     * 生成随机浮点数 [0.0, 1.0)
     */
    public double random() {
        return random.nextDouble();
    }

    // ==================== 分布 ====================

    /**
     * 正态分布
     */
    public Num normal(double loc, double scale, int... shape) {
        if (shape.length == 0) {
            return new Num(new double[]{random.nextGaussian() * scale + loc});
        } else if (shape.length == 1) {
            double[] array = new double[shape[0]];
            for (int i = 0; i < shape[0]; i++) {
                array[i] = random.nextGaussian() * scale + loc;
            }
            return new Num(array);
        } else if (shape.length == 2) {
            double[][] array = new double[shape[0]][shape[1]];
            for (int i = 0; i < shape[0]; i++) {
                for (int j = 0; j < shape[1]; j++) {
                    array[i][j] = random.nextGaussian() * scale + loc;
                }
            }
            return new Num(array);
        }
        throw new UnsupportedOperationException("Shape dimensions > 2 not yet supported");
    }

    public Num normal(int... shape) {
        return normal(0.0, 1.0, shape);
    }

    /**
     * 均匀分布
     */
    public Num uniform(double low, double high, int... shape) {
        if (shape.length == 0) {
            return new Num(new double[]{random.nextDouble() * (high - low) + low});
        } else if (shape.length == 1) {
            double[] array = new double[shape[0]];
            for (int i = 0; i < shape[0]; i++) {
                array[i] = random.nextDouble() * (high - low) + low;
            }
            return new Num(array);
        } else if (shape.length == 2) {
            double[][] array = new double[shape[0]][shape[1]];
            for (int i = 0; i < shape[0]; i++) {
                for (int j = 0; j < shape[1]; j++) {
                    array[i][j] = random.nextDouble() * (high - low) + low;
                }
            }
            return new Num(array);
        }
        throw new UnsupportedOperationException("Shape dimensions > 2 not yet supported");
    }

    public Num uniform(int... shape) {
        return uniform(0.0, 1.0, shape);
    }

    // ==================== 排列 ====================

    /**
     * 随机打乱数组
     */
    public void shuffle(Num array) {
        if (array.data() instanceof double[]) {
            double[] arr = (double[]) array.data();
            for (int i = arr.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                double temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }

    /**
     * 随机排列
     */
    public Num permutation(int n) {
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        Num result = new Num(arr);
        shuffle(result);
        return result;
    }

    public Num permutation(Num array) {
        Num result = new Num(array.data());
        shuffle(result);
        return result;
    }

    // ==================== 采样 ====================

    /**
     * 随机选择
     */
    public Num choice(double[] array, int size, boolean replace) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            int idx = random.nextInt(array.length);
            result[i] = array[idx];
            if (!replace && i < size - 1) {
                // 简单处理，实际应该更复杂
            }
        }
        return new Num(result);
    }

    public double choice(double[] array) {
        return array[random.nextInt(array.length)];
    }

    // ==================== 静态方法 ====================

    public static NumRandom getDefault() {
        return Nums.random;
    }

    public static void setSeed(long seed) {
        Nums.random.seed(seed);
    }
}
