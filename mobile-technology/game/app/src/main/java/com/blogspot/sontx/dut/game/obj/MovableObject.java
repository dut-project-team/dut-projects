package com.blogspot.sontx.dut.game.obj;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 16/1/2016.
 */
public abstract class MovableObject extends GameObject {
    protected float mSpeedX = 0.0f;
    protected float mSpeedY = 0.0f;
    protected RectF mMovableBound = new RectF(0.0f, 0.0f, 0.0f, 0.0f);

    public void setMovableBound(RectF rect) {
        mMovableBound = rect;
    }

    public float getSpeedX() {
        return mSpeedX;
    }

    public void setSpeedX(float speed) {
        mSpeedX = speed;
    }

    public float getSpeedY() {
        return mSpeedY;
    }

    public void setSpeedY(float speed) {
        mSpeedY = speed;
    }

    @Override
    public void update() {
        mRect.offset(mSpeedX, mSpeedY);
    }

    public MovableObject(float x, float y) {
        super(x, y);
    }

    public MovableObject(float x, float y, int color) {
        super(x, y, color);
    }
}
