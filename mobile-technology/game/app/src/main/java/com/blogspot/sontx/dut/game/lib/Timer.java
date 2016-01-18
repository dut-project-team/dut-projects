package com.blogspot.sontx.dut.game.lib;

import android.os.Handler;

/**
 * Created by Noem on 18/1/2016.
 */
public final class Timer {
    private static Handler mHandler = new Handler();

    public static void postDelay(Runnable work, long delay) {
        mHandler.postDelayed(work, delay);
    }

    private Timer() {
    }
}
