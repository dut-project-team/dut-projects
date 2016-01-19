package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;

/**
 * Created by Noem on 17/1/2016.
 */
public class Hole extends MovableObject {
    @Override
    public void setSpeedX(float speed) {
        super.setSpeedX(speed);
        mSpeedY = 0.0f;
    }

    @Override
    public void setSpeedY(float speed) {
        super.setSpeedY(speed);
        mSpeedX = 0.0f;
    }

    public Hole(float x, float y, int color) {
        super(x, y, color);
    }

    @Override
    protected void update0() {
        super.update0();
        if (mRect.left < mMovableBound.left || mRect.right > mMovableBound.right)
            mSpeedX = -mSpeedX;
        if (mRect.top < mMovableBound.top || mRect.bottom > mMovableBound.bottom)
            mSpeedY = -mSpeedY;
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }
}
