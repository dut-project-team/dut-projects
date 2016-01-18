package com.blogspot.sontx.dut.game.lib;

/**
 * Created by Noem on 17/1/2016.
 */
public final class Calc {

    public static boolean withProbability(float probability) {
        return Math.random() * 100.0f <= probability;
    }

    public static float nextFloat(float from, float to) {
        return (float) (Math.random() * (to - from) + from);
    }

    public static int nextInt(int from, int to) {
        return Math.round(nextFloat(from, to));
    }

    private Calc() {}
}
