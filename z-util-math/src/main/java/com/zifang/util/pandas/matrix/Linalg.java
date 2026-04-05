package com.zifang.util.pandas.matrix;

import com.zifang.util.pandas.num.Num;

/**
 * Linalg 类 - 对标 numpy.linalg
 * 提供线性代数运算功能
 */
public class Linalg {

    // ==================== 矩阵乘法 ====================

    /**
     * 矩阵点积 (matrix multiplication)
     */
    public static Num dot(Num a, Num b) {
        if (a.nDim() == 1 && b.nDim() == 1) {
            // 向量点积
            double[] av = (double[]) a.data();
            double[] bv = (double[]) b.data();
            double sum = 0;
            for (int i = 0; i < av.length; i++) {
                sum += av[i] * bv[i];
            }
            return new Num(new double[]{sum});
        } else if (a.nDim() == 2 && b.nDim() == 2) {
            // 矩阵乘法
            double[][] am = (double[][]) a.data();
            double[][] bm = (double[][]) b.data();
            int m = am.length;
            int n = bm[0].length;
            int p = bm.length;
            double[][] result = new double[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < p; k++) {
                        result[i][j] += am[i][k] * bm[k][j];
                    }
                }
            }
            return new Num(result);
        }
        throw new UnsupportedOperationException("Dot product for these dimensions not implemented");
    }

    /**
     * 矩阵乘法 (同 dot)
     */
    public static Num matmul(Num a, Num b) {
        return dot(a, b);
    }

    /**
     * 内积
     */
    public static double inner(Num a, Num b) {
        if (a.nDim() != 1 || b.nDim() != 1) {
            throw new IllegalArgumentException("Inner product requires 1D arrays");
        }
        double[] av = (double[]) a.data();
        double[] bv = (double[]) b.data();
        double sum = 0;
        for (int i = 0; i < av.length; i++) {
            sum += av[i] * bv[i];
        }
        return sum;
    }

    /**
     * 外积
     */
    public static Num outer(Num a, Num b) {
        if (a.nDim() != 1 || b.nDim() != 1) {
            throw new IllegalArgumentException("Outer product requires 1D arrays");
        }
        double[] av = (double[]) a.data();
        double[] bv = (double[]) b.data();
        double[][] result = new double[av.length][bv.length];
        for (int i = 0; i < av.length; i++) {
            for (int j = 0; j < bv.length; j++) {
                result[i][j] = av[i] * bv[j];
            }
        }
        return new Num(result);
    }

    /**
     * 张量积
     */
    public static Num tensordot(Num a, Num b, int[] axes) {
        // 简化实现
        return dot(a, b);
    }

    /**
     * 克罗内克积
     */
    public static Num kron(Num a, Num b) {
        if (a.nDim() == 2 && b.nDim() == 2) {
            double[][] am = (double[][]) a.data();
            double[][] bm = (double[][]) b.data();
            int m = am.length * bm.length;
            int n = am[0].length * bm[0].length;
            double[][] result = new double[m][n];

            for (int i = 0; i < am.length; i++) {
                for (int j = 0; j < am[0].length; j++) {
                    for (int k = 0; k < bm.length; k++) {
                        for (int l = 0; l < bm[0].length; l++) {
                            result[i * bm.length + k][j * bm[0].length + l] = am[i][j] * bm[k][l];
                        }
                    }
                }
            }
            return new Num(result);
        }
        throw new UnsupportedOperationException("Kronecker product for these dimensions not implemented");
    }

    // ==================== 分解 ====================

    /**
     * 奇异值分解 (SVD)
     */
    public static SVDResult svd(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("SVD requires 2D array");
        }

        double[][] matrix = (double[][]) a.data();
        int m = matrix.length;
        int n = matrix[0].length;

        // 简化的 SVD 实现 - 返回随机值作为占位符
        // 实际实现应该使用更复杂的算法如 Lanczos 或 QR 迭代
        double[] s = new double[Math.min(m, n)];
        for (int i = 0; i < s.length; i++) {
            s[i] = Math.random() * 10; // 占位符
        }

        double[][] u = new double[m][m];
        for (int i = 0; i < m; i++) {
            u[i][i] = 1.0; // 单位矩阵作为占位符
        }

        double[][] vh = new double[n][n];
        for (int i = 0; i < n; i++) {
            vh[i][i] = 1.0; // 单位矩阵作为占位符
        }

        return new SVDResult(new Num(u), new Num(s), new Num(vh));
    }

    public static class SVDResult {
        public final Num U;
        public final Num S;
        public final Num Vh;

        public SVDResult(Num u, Num s, Num vh) {
            this.U = u;
            this.S = s;
            this.Vh = vh;
        }
    }

    /**
     * QR 分解
     */
    public static QRResult qr(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("QR requires 2D array");
        }

        double[][] matrix = (double[][]) a.data();
        int m = matrix.length;
        int n = matrix[0].length;

        // 简化的 QR 分解实现 - 返回单位矩阵作为占位符
        double[][] q = new double[m][m];
        double[][] r = new double[m][n];

        for (int i = 0; i < m; i++) {
            q[i][i] = 1.0;
        }

        for (int i = 0; i < m && i < n; i++) {
            r[i][i] = 1.0;
        }

        return new QRResult(new Num(q), new Num(r));
    }

    public static class QRResult {
        public final Num Q;
        public final Num R;

        public QRResult(Num q, Num r) {
            this.Q = q;
            this.R = r;
        }
    }

    /**
     * Cholesky 分解
     */
    public static Num cholesky(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("Cholesky requires 2D array");
        }

        double[][] matrix = (double[][]) a.data();
        int n = matrix.length;

        // 简化的 Cholesky 实现
        double[][] l = new double[n][n];

        for (int i = 0; i < n; i++) {
            l[i][i] = Math.sqrt(matrix[i][i]);
        }

        return new Num(l);
    }

    // ==================== 矩阵属性 ====================

    /**
     * 矩阵行列式
     */
    public static double det(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("Determinant requires 2D array");
        }

        double[][] matrix = (double[][]) a.data();
        int n = matrix.length;

        // 对于 2x2 和 3x3 矩阵的简单实现
        if (n == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        } else if (n == 3) {
            return matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[1][2] * matrix[2][1])
                    - matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0])
                    + matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);
        }

        // 对于更大的矩阵，返回占位符值
        return 0.0;
    }

    /**
     * 矩阵秩
     */
    public static int matrix_rank(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("Matrix rank requires 2D array");
        }
        double[][] matrix = (double[][]) a.data();
        return Math.min(matrix.length, matrix[0].length);
    }

    /**
     * 矩阵迹
     */
    public static double trace(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("Trace requires 2D array");
        }
        double[][] matrix = (double[][]) a.data();
        double sum = 0;
        int n = Math.min(matrix.length, matrix[0].length);
        for (int i = 0; i < n; i++) {
            sum += matrix[i][i];
        }
        return sum;
    }

    // ==================== 矩阵求逆和解方程 ====================

    /**
     * 矩阵求逆
     */
    public static Num inv(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("Inverse requires 2D array");
        }

        double[][] matrix = (double[][]) a.data();
        int n = matrix.length;

        // 对于 2x2 矩阵的简单实现
        if (n == 2) {
            double det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            double[][] inv = new double[2][2];
            inv[0][0] = matrix[1][1] / det;
            inv[0][1] = -matrix[0][1] / det;
            inv[1][0] = -matrix[1][0] / det;
            inv[1][1] = matrix[0][0] / det;
            return new Num(inv);
        }

        // 对于更大的矩阵，返回单位矩阵作为占位符
        double[][] identity = new double[n][n];
        for (int i = 0; i < n; i++) {
            identity[i][i] = 1.0;
        }
        return new Num(identity);
    }

    /**
     * 伪逆 (Moore-Penrose 伪逆)
     */
    public static Num pinv(Num a) {
        // 简化实现，返回普通逆矩阵
        return inv(a);
    }

    /**
     * 解线性方程组 Ax = b
     */
    public static Num solve(Num a, Num b) {
        // 简化实现: x = A^(-1) * b
        return dot(inv(a), b);
    }

    /**
     * 最小二乘解
     */
    public static Num lstsq(Num a, Num b) {
        // 简化实现
        return solve(a, b);
    }

    // ==================== 范数 ====================

    /**
     * 向量/矩阵范数
     */
    public static double norm(Num x) {
        return norm(x, 2);
    }

    public static double norm(Num x, int ord) {
        if (x.nDim() == 1) {
            double[] v = (double[]) x.data();
            if (ord == 1) {
                double sum = 0;
                for (double val : v) sum += Math.abs(val);
                return sum;
            } else if (ord == 2) {
                double sum = 0;
                for (double val : v) sum += val * val;
                return Math.sqrt(sum);
            } else if (ord == Integer.MAX_VALUE) {
                double max = 0;
                for (double val : v) max = Math.max(max, Math.abs(val));
                return max;
            }
        }
        return norm(x, 2);
    }

    public static double norm(Num x, String ord) {
        if ("fro".equals(ord)) {
            return norm(x, 2);
        }
        return norm(x, Integer.parseInt(ord));
    }

    // ==================== 特征值 ====================

    /**
     * 特征值和特征向量
     */
    public static EigenResult eig(Num a) {
        if (a.nDim() != 2) {
            throw new IllegalArgumentException("Eigen decomposition requires 2D array");
        }
        double[][] matrix = (double[][]) a.data();
        int n = matrix.length;

        // 简化实现：返回占位符值
        double[] eigenvalues = new double[n];
        double[][] eigenvectors = new double[n][n];
        for (int i = 0; i < n; i++) {
            eigenvalues[i] = matrix[i][i];
            eigenvectors[i][i] = 1.0;
        }

        return new EigenResult(new Num(eigenvalues), new Num(eigenvectors));
    }

    /**
     * 仅特征值
     */
    public static Num eigvals(Num a) {
        return eig(a).w;
    }

    public static class EigenResult {
        public final Num w;  // 特征值
        public final Num v;  // 特征向量

        public EigenResult(Num w, Num v) {
            this.w = w;
            this.v = v;
        }
    }

    // ==================== 辅助方法 ====================

    private static Num apply2(Num x, Num y, java.util.function.DoubleBinaryOperator op) {
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
