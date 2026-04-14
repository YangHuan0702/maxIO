package com.halosky.ec;

import jdk.incubator.vector.ByteVector;
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



}
