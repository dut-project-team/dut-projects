package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;

/**
 * Created by Noem on 17/1/2016.
 */
public class Hole extends MovableObject {
    public Hole(float x, float y, int color) {
        super(x, y, color);
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }
}
