package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;

import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.lib.BitmapLoader;

/**
 * Copyright by SONTX 2016. www.sontx.in
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

    @Override
    public void init() {
        setBitmap(BitmapLoader.getBitmapById(R.drawable.hole));
        stretchBitmapToRectangle();
    }

    public Hole(float x, float y, int color) {
        super(x, y, color);
    }

    @Override
    public void update() {
        super.update();
        if (mRect.left < mMovableBound.left || mRect.right > mMovableBound.right)
            mSpeedX = -mSpeedX;
        if (mRect.top < mMovableBound.top || mRect.bottom > mMovableBound.bottom)
            mSpeedY = -mSpeedY;
    }
}
