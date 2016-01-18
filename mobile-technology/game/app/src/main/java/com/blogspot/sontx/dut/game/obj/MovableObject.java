package com.blogspot.sontx.dut.game.obj;

/**
 * Created by Noem on 16/1/2016.
 */
public abstract class MovableObject extends GameObject {
    protected float mSpeedX;
    protected float mSpeedY;

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
    protected void update0() {
        mRect.offset(mSpeedX, mSpeedY);
    }

    public MovableObject(float x, float y, int color) {
        super(x, y, color);
        mSpeedX = 0.0f;
        mSpeedY = 0.0f;
    }
}
