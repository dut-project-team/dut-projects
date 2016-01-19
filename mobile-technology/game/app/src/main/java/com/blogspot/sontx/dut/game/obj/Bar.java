package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.blogspot.sontx.dut.game.lib.InputManager;

/**
 * Created by Noem on 17/1/2016.
 */
public class Bar extends MovableObject {
    private boolean mLastTouchDown = false;
    private float mExtendWidth = 0.0f;

    public Bar(float x, float y, int color) {
        super(x, y, color);
    }

    public void setExtendWidth(float width) {
        mExtendWidth = width;
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
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
    protected void update0() {
        //super.update0();
        if (InputManager.hasTouch(InputManager.TOUCH_DOWN) && contains(InputManager.getTouchPoint())) {
            mLastTouchDown = true;
            return;
        } else if (mLastTouchDown && InputManager.hasTouch(InputManager.TOUCH_DRAG)) {
            float dx = InputManager.getTouchDelta().x;
            horizontalMove(dx);
            ensureInsideBound();
            return;
        }
        mLastTouchDown = false;
    }
}
