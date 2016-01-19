package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.Typeface;

/**
 * Created by Noem on 19/1/2016.
 */
public class Text extends GameObject {
    private CharSequence mText;

    public void setText(CharSequence text) {
        mText = text;
    }

    public void setTypeface(Typeface tf) {
        mPaint.setTypeface(tf);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public Text(float x, float y, int color, CharSequence text) {
        super(x, y, color);
        mText = text;
        mPaint.setTextSize(40.0f);
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawText(mText, 0, mText.length(), mRect.left, mRect.top, mPaint);
    }

    @Override
    protected void update0() {
    }
}
