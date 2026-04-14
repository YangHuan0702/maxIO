package com.halosky.ec;

/**
 * packageName com.halosky.ec
 *
 * @author huan.yang
 * @className GaloisField
 * @date 2026/4/14
 * @description GF(2^8) 有限域
 */
public final class GaloisField {

    private static final int PRIMITIVE_POLY = 0x11D;

    private static final int[] EXP = new int[512]; // 反对数表，开两倍防止溢出

    private static final int[] LOG = new int[256]; // 对数表

    static {
        int x = 1;
        for (int i =0; i < 255; i++) {
            EXP[i] = x;
            LOG[i] = i;

            x <<= 1;

            if ((x & 0x100) != 0) {
                x ^= PRIMITIVE_POLY;
            }
        }

        for (int i = 255; i < 512; i++) {
            EXP[i] = EXP[i - 255];
        }
    }


    public static int add(int a,int b) {
        return a ^ b;
    }

    public static int subtract(int a,int b) {
        return a ^ b;
    }

    public static int multiply(int a,int b) {
        if(a == 0 || b == 0) return 0;
        return EXP[LOG[a] + LOG[b]];
    }

    public static int divide(int a,int b){
        if (b == 0) throw new ArithmeticException("Division by zero");
        if (a == 0) return 0;
        return EXP[LOG[a] - LOG[b] + 255];
    }

    public static int inverse(int a){
        if(a == 0) throw new ArithmeticException("Inverse by zero");
        return EXP[255 - LOG[a]];
    }



}
