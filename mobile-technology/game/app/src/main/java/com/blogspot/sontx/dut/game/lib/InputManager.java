package com.blogspot.sontx.dut.game.lib;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by Noem on 19/1/2016.
 */
public final class InputManager {
    public static final int TOUCH_DOWN = MotionEvent.ACTION_DOWN;
    public static final int TOUCH_UP = MotionEvent.ACTION_UP;
    public static final int TOUCH_DRAG = MotionEvent.ACTION_MOVE;
    public static final int TOUCH_NONE = MotionEvent.ACTION_CANCEL;

    private static float mLastX = 0.0f;
    private static float mLastY = 0.0f;
    private static float mX = 0.0f;
    private static float mY = 0.0f;
    private static int mTouchState = TOUCH_NONE;

    public static void onTouch(int motionAction, float x, float y) {
        mLastX = mX;
        mLastY = mY;
        mX = x;
        mY = y;
        mTouchState = motionAction;
    }

    public static boolean hasTouch(int touchEvent) {
        return touchEvent == mTouchState;
    }

    public static PointF getTouchDelta() {
        return new PointF(mX - mLastX, mY - mLastY);
    }

    public static PointF getTouchPoint() {
        return new PointF(mX, mY);
    }

    public static void refreshTouchPoint() {
        mLastX = mX;
        mLastY = mY;
    }

    private InputManager() {}
}
