package com.blogspot.sontx.dut.game.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.blogspot.sontx.dut.game.lib.InputManager;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 16/1/2016.
 */
public final class WindowSurface extends View {
    private static WindowSurface instance;
    private WindowEventListener mWindowEventListener = null;
    private boolean mInitialized = false;

    public static WindowSurface getInstance() {
        return instance;
    }

    public static void createInstance(Context context) {
        instance = new WindowSurface(context);
    }

    public void setWindowEventListener(WindowEventListener listener) {
        mWindowEventListener = listener;
    }

    public WindowSurface(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int motionAction = event.getAction();
        float x = event.getX();
        float y = event.getY();
        InputManager.onTouch(motionAction, x, y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWindowEventListener != null) {
            mWindowEventListener.onUpdate();
            mWindowEventListener.onDraw(canvas);
        }
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!mInitialized && mWindowEventListener != null)
            mWindowEventListener.onInit();
        mInitialized = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWindowEventListener != null)
            mWindowEventListener.onDestroy();
    }

    public interface WindowEventListener {
        void onDraw(Canvas canvas);
        void onUpdate();
        void onInit();
        void onDestroy();
    }
}
