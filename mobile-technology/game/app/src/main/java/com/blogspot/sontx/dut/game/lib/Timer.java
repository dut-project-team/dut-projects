package com.blogspot.sontx.dut.game.lib;

import android.os.Handler;

/**
 * Copyright by SONTX 2016. www.sontx.in
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
