package com.halosky.ec;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

/**
 * packageName com.halosky.ec
 *
 * @author huan.yang
 * @className VectorXOR
 * @date 2026/4/14
 * @description vector exec xor
 */
public final class VectorXOR {

    // SPECIES_PREFERRED: 自动选择当前CPU最宽的向量宽度
    private static final VectorSpecies<Byte> SPECIES = ByteVector.SPECIES_PREFERRED;


    /**
     * dst[i] ^= src[i]
     * Reed-Solomon里最核心的批量操作
     */
    public static void xorInPlace(byte[] dst, byte[] src) {
        int i = 0;
        int bound = SPECIES.loopBound(dst.length);

        for(; i < bound; i+= SPECIES.length()) {
            ByteVector vd = ByteVector.fromArray(SPECIES,dst,i);
            ByteVector vs = ByteVector.fromArray(SPECIES,src,i);
            vd.lanewise(VectorOperators.XOR,vs).intoArray(dst,i);
        }


        for(; i < dst.length; i++) {
            dst[i] ^= src[i];
        }
    }

    /**
     * dst[i] ^= src[i] * factor(GF乘法 + XOR)
     */
    public static void mulAndXor(byte[] dst,byte[] src,byte factor) {
        int f = factor &  0xFF;
        for (int i = 0; i < dst.length; i++) {
            dst[i] ^= (byte) GaloisField.multiply(src[i] & 0xFF,f);
        }
    }

}
