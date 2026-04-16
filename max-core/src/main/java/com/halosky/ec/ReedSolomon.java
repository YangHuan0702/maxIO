package com.halosky.ec;

import java.util.Arrays;

/**
 * packageName com.halosky.ec
 *
 * @author huan.yang
 * @className ReedSolomon
 * @date 2026/4/14
 * @description 编码/解码入口
 */
public class ReedSolomon {

    // data blocks
    private final int dataShards;
    // validator blocks
    private final int parityShards;

    private final Matrix encoderMatrix;

    public ReedSolomon(int dataShards, int parityShards) {
        this.dataShards = dataShards;
        this.parityShards = parityShards;

        // 拼接成（k+m） * k的矩阵
        this.encoderMatrix = buildEncodeMatrix(dataShards, parityShards);

    }

    // 构建编码矩阵： 上半单位矩阵 + 下半范德蒙德
    private static Matrix buildEncodeMatrix(int k, int m) {
        Matrix top = Matrix.identity(k);
        Matrix bottom = Matrix.vandermonde(m, k);

        // 拼接为 (k + m) * k的矩阵
        Matrix result = new Matrix(k + m, k);

        for (int r = 0; r < k; r++) {
            for (int c = 0; c < k; c++) {
                result.data[r][c] = top.get(r, c);
            }
        }
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < k; c++) {
                result.data[k + r][c] = bottom.get(r, c);
            }
        }
        return result;
    }


    /**
     * 编码： 输入k个数据库，输出m个校验块
     *
     * @param shards shards[0..k-1] = 数据块（已有）
     *               shards[k..k+m-1] = 校验块（本方法填充）
     */
    public void encode(byte[][] shards) {
        int shardSize = shards[0].length;

        for (int r = dataShards; r < dataShards + parityShards; r++) {
            byte[] output = shards[r];
            Arrays.fill(output, (byte) 0);
            for (int c = 0; c < dataShards; c++) {
                int factor = encoderMatrix.get(r, c);
                VectorXOR.mulAndXor(output, shards[c], (byte) factor);
            }
        }
    }


    /**
     * 解码：有块损坏时恢复
     *
     * @param shards shards中损坏的块用null表示
     */
    public void decode(byte[][] shards) {
        int[] presentIndices = new int[dataShards];
        int found = 0;
        for (int i = 0; i < dataShards + parityShards && found < dataShards; i++) {
            if (shards[i] != null) {
                presentIndices[found++] = i;
            }
        }
        if (found < dataShards) {
            throw new IllegalArgumentException("存活块不足，无法恢复");
        }
        // 取编码矩阵中对应行，组成恢复矩阵
        Matrix subMatrix = encoderMatrix.subMatrix(presentIndices);

        // 求逆
        Matrix decodeMatrix = subMatrix.inverse();

        int shardSize = shards[presentIndices[0]].length;
        byte[][] present = new byte[dataShards][];
        for (int i = 0; i < dataShards; i++) {
            present[i] = shards[presentIndices[i]];
        }

        byte[][] recovered = new byte[dataShards][shardSize];
        for (int r = 0; r < dataShards; r++) {
            Arrays.fill(present[r], (byte) 0);
            for (int c = 0; c < dataShards; c++) {
                int factor = decodeMatrix.get(r, c);
                VectorXOR.mulAndXor(recovered[r],present[c], (byte) factor);
            }
            shards[r] = recovered[r];
        }


    }


}
