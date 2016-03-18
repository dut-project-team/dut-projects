package com.blogspot.sontx.dut.game.lib;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 18/1/2016.
 */
public final class Timer {
    private final int mTotalMillis;
    private int mRemainMillis = 0;
    private long mLastMillis = System.currentTimeMillis();

    public void updateTime() {
        if (mRemainMillis > 0) {
            long currentMillis = System.currentTimeMillis();
            int deltaMillis = (int) (currentMillis - mLastMillis);
            mLastMillis = currentMillis;
            mRemainMillis -= deltaMillis;
        }
    }

    public int getRemainMillis() {
        return mRemainMillis;
    }

    public Timer(int minutes, int seconds) {
        this.mTotalMillis = (minutes * 60 + seconds) * 1000;
        this.mRemainMillis = mTotalMillis;
    }

    @Override
    public String toString() {
        int remainSeconds = mRemainMillis / 1000;
        int minutes = remainSeconds / 60;
        int seconds = remainSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
