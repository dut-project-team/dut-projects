package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
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

    public float getRight() {
        return mRect.right;
    }

    public float getBottom() {
        return mRect.bottom;
    }

    public float getWidth() {
        return mRect.width();
    }

    public float getHeight() {
        return mRect.height();
    }

    public void setTop(float top) {
        mRect.top = top;
    }

    public void setLeft(float left) {
        mRect.left = left;
    }

    public void setRight(float right) {
        mRect.right = right;
    }

    public void setBottom(float bottom) {
        mRect.bottom = bottom;
    }

    public void setWidth(float width) {
        mRect.right = mRect.left + width;
    }

    public void setHeight(float height) {
        mRect.bottom = mRect.top + height;
    }

    public boolean isVisible() {
        return mVisible;
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    public Paint getPaint() {
        return mPaint;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    protected abstract void draw0(Canvas canvas);

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
