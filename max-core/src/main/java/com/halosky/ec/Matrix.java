package com.halosky.ec;

/**
 * packageName com.halosky.ec
 *
 * @author huan.yang
 * @className Matrix
 * @date 2026/4/15
 */
public final class Matrix {

    private final int rows;
    private final int cols;

    public final int[][] data;

    public Matrix(final int rows, final int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new int[rows][cols];
    }


    public static Matrix identity(int size) {
        Matrix m = new Matrix(size, size);
        for (int r = 0; r < size; r++) {
            m.data[r][r] = 1;
        }
        return m;
    }


    public static Matrix vandermonde(int rows, int cols) {
        Matrix m = new Matrix(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                m.data[r][c] = GaloisField.exp(r, c);
            }
        }
        return m;
    }


    public Matrix multiply(Matrix m) {
        if (this.cols != m.cols) {
            throw new IllegalArgumentException("Matrix has different number of columns");
        }

        Matrix result = new Matrix(this.rows, m.cols);
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < m.cols; c++) {
                int val = 0;
                for (int k = 0; k < this.cols; k++) {
                    val ^= GaloisField.multiply(this.data[r][k], m.data[k][c]);
                }
                result.data[r][c] = val;
            }
        }
        return result;
    }

    public Matrix inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Matrix has different number of rows");
        }
        int n = rows;
        Matrix work = new Matrix(n, n * 2);
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                work.data[r][c] = this.data[r][c];
            }
            work.data[r][n + r] = 1;
        }

        for (int col = 0; col < n; col++) {

            int pivotRow = -1;

            for (int r = col; r < n; r++) {
                if (work.data[r][col] != 0) {
                    pivotRow = r;
                    break;
                }
            }

            if (pivotRow == -1) {
                throw new IllegalArgumentException("矩阵不可逆");
            }


            int[] tmp = work.data[col];
            work.data[col] = work.data[pivotRow];
            work.data[pivotRow] = tmp;

            int pivotVal = work.data[col][col];
            int pivotInverse = GaloisField.inverse(pivotVal);
            for (int c = 0; c < n * 2; c++) {
                work.data[col][c] = GaloisField.multiply(work.data[col][c], pivotInverse);
            }

            for (int r = 0; r < n; r++) {
                if (r == col) continue;
                int factor = work.data[r][col];
                if (factor == 0) continue;
                for (int c = 0; c < n * 2; c++) {
                    work.data[r][c] ^= GaloisField.multiply(factor, work.data[col][c]);
                }
            }
        }

        Matrix inv = new Matrix(n, n);
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                inv.data[r][c] = work.data[r][n + c];
            }
        }
        return inv;
    }


    public Matrix subMatrix(int[] rowIndices) {
        Matrix result = new Matrix(rowIndices.length,cols);
        for (int i = 0; i < rowIndices.length; i++) {
            result.data[i] = this.data[rowIndices[i]].clone();
        }
        return result;
    }


    public int get(int r, int c) {
        return data[r][c];
    }
    public int rows() {
        return rows;
    }
    public int cols() {
        return cols;
    }

}
