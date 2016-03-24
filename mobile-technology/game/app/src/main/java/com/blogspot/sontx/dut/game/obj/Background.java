package com.blogspot.sontx.dut.game.obj;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.lib.BitmapLoader;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/03/2016.
 */
public class Background extends GameObject {
    public Background() {
        super(0.0f, 0.0f);
    }

    public void setBackgroundRectangle(RectF rectangle) {
        mRect.left = rectangle.left;
        mRect.top = rectangle.top;
        mRect.right = rectangle.right;
        mRect.bottom = rectangle.bottom;
    }

    @Override
    public void init() {
    }

    public void setBitmap(int bitmapRes) {
        Bitmap bitmap = BitmapLoader.getBitmapById(bitmapRes);
        super.setBitmap(bitmap);
        stretchBitmapToRectangle();
    }
}
