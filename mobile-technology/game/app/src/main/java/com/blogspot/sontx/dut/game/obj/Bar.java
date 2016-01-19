package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.blogspot.sontx.dut.game.lib.InputManager;

/**
 * Created by Noem on 17/1/2016.
 */
public class Bar extends MovableObject {
    private boolean mLastTouchDown = false;
    private float mLeftBound = 0.0f;
    private float mRightBound = 0.0f;

    public void setBound(float left, float right) {
        mLeftBound = left;
        mRightBound = right;
    }

    public Bar(float x, float y, int color) {
        super(x, y, color);
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }

    private void horizontalMove(float dx) {
        mRect.left += dx;
        mRect.right += dx;
    }

    private void ensureInsideBound() {
        if (mRect.left < mLeftBound)
            horizontalMove(mLeftBound - mRect.left);
        if (mRect.right > mRightBound)
            horizontalMove(mRightBound - mRect.right);
    }

    @Override
    protected void update0() {
        super.update0();
        if (InputManager.hasTouch(InputManager.TOUCH_DOWN) && contains(InputManager.getTouchPoint()))  {
            mLastTouchDown = true;
            return;
        } else if (mLastTouchDown && InputManager.hasTouch(InputManager.TOUCH_DRAG)) {
            horizontalMove(InputManager.getTouchDelta().x);
            ensureInsideBound();
            return;
        }
        mLastTouchDown = false;
    }
}
