package com.zifang.util.pandas.num;

/**
 * Maths 类 - 对标 numpy 的数学函数
 * 提供各种数学和统计函数
 */
public class Maths {

    // ==================== 三角函数 ====================

    public static Num sin(Num x) {
        return x.sin();
    }

    public static Num cos(Num x) {
        return x.cos();
    }

    public static Num tan(Num x) {
        return x.tan();
    }

    public static Num arcsin(Num x) {
        return x.apply(Math::asin);
    }

    public static Num arccos(Num x) {
        return x.apply(Math::acos);
    }

    public static Num arctan(Num x) {
        return x.apply(Math::atan);
    }

    public static Num arctan2(Num y, Num x) {
        return apply2(y, x, Math::atan2);
    }

    // ==================== 双曲函数 ====================

    public static Num sinh(Num x) {
        return x.apply(Math::sinh);
    }

    public static Num cosh(Num x) {
        return x.apply(Math::cosh);
    }

    public static Num tanh(Num x) {
        return x.apply(Math::tanh);
    }

    // ==================== 指数和对数 ====================

    public static Num exp(Num x) {
        return x.exp();
    }

    public static Num expm1(Num x) {
        return x.apply(Math::expm1);
    }

    public static Num log(Num x) {
        return x.log();
    }

    public static Num log10(Num x) {
        return x.log10();
    }

    public static Num log2(Num x) {
        return x.apply(v -> Math.log(v) / Math.log(2));
    }

    public static Num log1p(Num x) {
        return x.apply(Math::log1p);
    }

    // ==================== 幂函数 ====================

    public static Num sqrt(Num x) {
        return x.sqrt();
    }

    public static Num cbrt(Num x) {
        return x.apply(Math::cbrt);
    }

    public static Num pow(Num x, double exponent) {
        return x.pow(exponent);
    }

    public static Num pow(double base, Num exponent) {
        return exponent.apply(exp -> Math.pow(base, exp));
    }

    // ==================== 舍入函数 ====================

    public static Num round(Num x) {
        return x.apply(Math::round);
    }

    public static Num floor(Num x) {
        return x.apply(Math::floor);
    }

    public static Num ceil(Num x) {
        return x.apply(Math::ceil);
    }

    public static Num trunc(Num x) {
        return x.apply(Math::floor);
    }

    // ==================== 绝对值和符号 ====================

    public static Num abs(Num x) {
        return x.abs();
    }

    public static Num sign(Num x) {
        return x.apply(v -> {
            if (v > 0) return 1.0;
            if (v < 0) return -1.0;
            return 0.0;
        });
    }

    public static Num positive(Num x) {
        return x.apply(v -> +v);
    }

    public static Num negative(Num x) {
        return x.apply(v -> -v);
    }

    // ==================== 数值范围 ====================

    public static double max(Num x) {
        return x.max();
    }

    public static double min(Num x) {
        return x.min();
    }

    public static double sum(Num x) {
        return x.sum();
    }

    public static double mean(Num x) {
        return x.mean();
    }

    public static double std(Num x) {
        return x.std();
    }

    public static double var(Num x) {
        return x.var();
    }

    // ==================== 常量 ====================

    public static final double PI = Math.PI;
    public static final double E = Math.E;
    public static final double INF = Double.POSITIVE_INFINITY;
    public static final double NINF = Double.NEGATIVE_INFINITY;
    public static final double NAN = Double.NaN;

    public static final double EPSILON = Math.ulp(1.0);

    // ==================== 辅助方法 ====================

    private static Num apply2(Num x, Num y, java.util.function.DoubleBinaryOperator op) {
        // 简化实现，假设两个数组形状相同
        if (x.data() instanceof double[] && y.data() instanceof double[]) {
            double[] a = (double[]) x.data();
            double[] b = (double[]) y.data();
            double[] result = new double[a.length];
            for (int i = 0; i < a.length; i++) {
                result[i] = op.applyAsDouble(a[i], b[i]);
            }
            return new Num(result);
        }
        throw new UnsupportedOperationException("Two-argument operations for this shape not yet implemented");
    }
}
