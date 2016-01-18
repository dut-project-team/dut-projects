package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Noem on 17/1/2016.
 */
public class Brick extends GameObject {
    public static final int LEVEL0 = 0;
    public static final int LEVEL1 = 1;
    public static final int LEVEL2 = 2;

    private long mTimeout = 0;
    private long mValue = 0;
    private long mStartTime = -1L;
    private boolean mDisappear = false;

    public boolean isDisappear() {
        return mDisappear;
    }

    public int getValue() {
        return (int) (mValue / 1000000000);
    }

    private void setup(int color, int timeout, int value) {
        mPaint.setColor(color);
        mTimeout = timeout * 1000000000L;
        mValue = value * 1000000000L;
    }

    public Brick(float x, float y, int level) {
        super(x, y, Color.WHITE);
        switch (level) {
            case LEVEL0:
                setup(Color.GRAY, 15000, 60000);
                break;
            case LEVEL1:
                setup(Color.GREEN, 30000, 40000);
                break;
            case LEVEL2:
                setup(Color.DKGRAY, 50000, 20000);
                break;
        }
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    protected void update0() {
        if (mStartTime < 0) {
            mStartTime = System.nanoTime();
        } else {
            if (System.nanoTime() - mStartTime > mTimeout)
                mDisappear = true;
        }
    }
}
