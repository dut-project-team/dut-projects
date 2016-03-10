package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.lib.BitmapLoader;
import com.blogspot.sontx.dut.game.lib.InputManager;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 17/1/2016.
 */
public class Bar extends MovableObject {
    private boolean mLastTouchDown = false;
    private float mExtendWidth = 0.0f;

    public Bar(float x, float y) {
        super(x, y);
    }

    public void setExtendWidth(float width) {
        mExtendWidth = width;
    }

    private void horizontalMove(float dx) {
        mRect.left += dx;
        mRect.right += dx;
        mSpeedX = dx;
    }

    private void ensureInsideBound() {
        if (mRect.left < mMovableBound.left)
            horizontalMove(mMovableBound.left - mRect.left);
        if (mRect.right > mMovableBound.right)
            horizontalMove(mMovableBound.right - mRect.right);
    }

    @Override
    protected boolean contains(PointF point) {
        return point.x >= (mRect.left - mExtendWidth) && point.x < (mRect.right + mExtendWidth) &&
                point.y >= (mRect.top - mExtendWidth) && point.y < (mRect.bottom + mExtendWidth);
    }

    @Override
    public void init() {
        setBitmap(BitmapLoader.getBitmapById(R.drawable.bar));
        stretchBitmapToRectangle();
    }

    @Override
    public void update() {
        if (InputManager.hasTouch(InputManager.TOUCH_DOWN) && contains(InputManager.getTouchPoint())) {
            mLastTouchDown = true;
            return;
        } else if (mLastTouchDown && InputManager.hasTouch(InputManager.TOUCH_DRAG)) {
            float dx = InputManager.getTouchDelta().x;
            horizontalMove(dx);
            ensureInsideBound();
            InputManager.refreshTouchPoint();
            return;
        }
        mLastTouchDown = false;
    }
}
