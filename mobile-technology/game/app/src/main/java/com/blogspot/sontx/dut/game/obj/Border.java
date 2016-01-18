package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Noem on 17/1/2016.
 */
public class Border extends GameObject {

    public void setLineWidth(float width) {
        mPaint.setStrokeWidth(width);
        mRect.inset(width / 2.0f, width / 2.0f);
    }

    public Border(RectF rect, float lineWidth, int color) {
        super(rect.left, rect.top, color);
        mRect.right = rect.right;
        mRect.bottom = rect.bottom;
        mPaint.setStyle(Paint.Style.STROKE);
        setLineWidth(lineWidth);
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    protected void update0() {
    }
}
