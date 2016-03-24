package com.blogspot.sontx.dut.game.obj;

import android.graphics.Bitmap;

import com.blogspot.sontx.dut.game.lib.BitmapLoader;
import com.blogspot.sontx.dut.game.lib.InputManager;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/03/2016.
 */
public class Button extends GameObject {
    private OnClickListener mOnClickListener = null;

    public void setOnClickListener(OnClickListener listener) {
        mOnClickListener = listener;
    }

    public Button(float x, float y) {
        super(x, y);
    }

    @Override
    public void init() {
    }

    private Bitmap stretchBitmapByWidth(Bitmap bitmap) {
        float ratio = bitmap.getWidth() / (float) bitmap.getHeight();
        int newHeight = (int) (mRect.width() / ratio);
        Bitmap stretchBitmap = BitmapLoader.stretchBitmap(bitmap, (int) mRect.width(), newHeight);
        if (!stretchBitmap.equals(bitmap))
            bitmap.recycle();
        return stretchBitmap;
    }

    public void setBitmap(int bitmapRes) {
        Bitmap bitmap = BitmapLoader.getBitmapById(bitmapRes);
        super.setBitmap(stretchBitmapByWidth(bitmap));
        recomputeRectangleFromBitmap();
    }

    @Override
    public void update() {
        super.update();
        if (InputManager.hasTouch(InputManager.TOUCH_DOWN) && contains(InputManager.getTouchPoint()) && mOnClickListener != null)
            mOnClickListener.onClick(this);
    }

    public interface OnClickListener {
        void onClick(Button button);
    }
}
