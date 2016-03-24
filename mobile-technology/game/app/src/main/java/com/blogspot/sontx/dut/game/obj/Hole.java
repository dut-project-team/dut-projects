package com.blogspot.sontx.dut.game.obj;

import com.blogspot.sontx.dut.game.lib.BitmapLoader;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 17/1/2016.
 */
public class Hole extends MovableObject {
    private final int mHoleBitmapResource;

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
        setBitmap(BitmapLoader.getBitmapById(mHoleBitmapResource));
        stretchBitmapByWidth();
    }

    public Hole(float x, float y, int holeBitmapResource) {
        super(x, y);
        mHoleBitmapResource = holeBitmapResource;
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
