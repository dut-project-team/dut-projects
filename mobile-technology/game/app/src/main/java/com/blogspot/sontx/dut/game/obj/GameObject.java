package com.blogspot.sontx.dut.game.obj;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.lib.BitmapLoader;
import com.blogspot.sontx.dut.game.lib.InputManager;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 16/1/2016.
 */
public abstract class GameObject {
    protected final RectF mRect;
    private Bitmap mBitmap = null;
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

    protected boolean contains(PointF point) {
        return mRect.contains(point.x, point.y);
    }

    protected void recomputeRectangleFromBitmap() {
        if (mBitmap != null) {
            mRect.right = mRect.left + mBitmap.getWidth();
            mRect.bottom = mRect.top + mBitmap.getHeight();
        }
    }

    protected void stretchBitmapToRectangle() {
        if (mBitmap != null) {
            Bitmap stretchBitmap = BitmapLoader.stretchBitmap(mBitmap, (int) mRect.width(), (int) mRect.height());
            if (!stretchBitmap.equals(mBitmap)) {
                mBitmap.recycle();
                mBitmap = stretchBitmap;
            }
        }
    }

    protected void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public abstract void init();

    public void draw(Canvas canvas) {
        if (mBitmap != null)
            canvas.drawBitmap(mBitmap, mRect.left, mRect.top, mPaint);
    }

    public void update() {
    }

    public void destroy() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public GameObject(float x, float y) {
        this(x, y, Color.BLACK);
    }

    public GameObject(float x, float y, int color) {
        mRect = new RectF(x, y, 100.0f, 100.0f);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mTag = null;
    }

    public boolean isCollision(GameObject obj) {
        return mRect.left <= obj.mRect.right && obj.mRect.left <= mRect.right
                && mRect.top <= obj.mRect.bottom && obj.mRect.top <= mRect.bottom;
    }
}
