package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.lib.InputManager;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 16/1/2016.
 */
public abstract class GameObject {
    protected final RectF mRect;
    protected boolean mVisible;
    protected final Paint mPaint;
    private Object mTag;

    public float getTop() {
        return mRect.top;
    }

    public float getLeft() {
        return mRect.left;
    }

    public float getWidth() {
        return mRect.width();
    }

    public void setWidth(float width) {
        mRect.right = mRect.left + width;
    }

    public void setHeight(float height) {
        mRect.bottom = mRect.top + height;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    protected abstract void draw0(Canvas canvas);

    protected boolean contains(PointF point) {
        return mRect.contains(point.x, point.y);
    }

    protected abstract void update0();

    public final void draw(Canvas canvas) {
        if (mVisible)
            draw0(canvas);
    }

    public final void update() {
        if (mVisible)
            update0();
    }

    public void destroy() {
    }

    public GameObject(float x, float y, int color) {
        mRect = new RectF(x, y, 100.0f, 100.0f);
        mVisible = true;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mTag = null;
    }

    public boolean isCollision(GameObject obj) {
        return mRect.left <= obj.mRect.right && obj.mRect.left <= mRect.right
                && mRect.top <= obj.mRect.bottom && obj.mRect.top <= mRect.bottom;
    }
}
