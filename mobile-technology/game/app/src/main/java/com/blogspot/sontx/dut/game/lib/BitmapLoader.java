package com.blogspot.sontx.dut.game.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.blogspot.sontx.dut.game.App;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by noem on 3/10/16.
 */
public final class BitmapLoader {
    private static Context context = App.getInstance().getApplicationContext();

    public static Bitmap getBitmapById(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static Bitmap getBitmapById(int resId, float scale) {
        Bitmap bitmap = getBitmapById(resId);
        if (scale == 0.0f)
            return bitmap;
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), true);
        if (!scaledBitmap.equals(bitmap))
            bitmap.recycle();
        return scaledBitmap;
    }

    public static Bitmap stretchBitmap(Bitmap src, int width, int height) {
        if (width == src.getWidth() && height == src.getHeight())
            return src;
        return Bitmap.createScaledBitmap(src, width, height, true);
    }
}
