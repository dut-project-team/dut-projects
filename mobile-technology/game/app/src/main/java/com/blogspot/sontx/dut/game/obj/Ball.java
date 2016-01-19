package com.blogspot.sontx.dut.game.obj;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Noem on 16/1/2016.
 */
public class Ball extends MovableObject {
    public static final int NONE_COLLISION = 0;
    public static final int VERTICAL_COLLISION = 1;
    public static final int HORIZONTAL_COLLISION = 2;

    private float mR;

    public void setR(float r) {
        mR = r;
        float width = r * 2.0f;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + width;
    }

    public Ball(float x, float y, float r, int color) {
        super(x, y, color);
        setR(r);
    }

    @Override
    protected void draw0(Canvas canvas) {
        canvas.drawCircle(mRect.left + mR, mRect.top + mR, mR, mPaint);
    }

    public boolean isInnerVerticalCollision(GameObject obj) {
        return (mRect.left <= obj.mRect.left) || (mRect.right >= obj.mRect.right);
    }

    public boolean isInnerHorizontalCollision(GameObject obj) {
        return (mRect.top <= obj.mRect.top) || (mRect.bottom >= obj.mRect.bottom);
    }

    public int checkOuterCollision(GameObject obj) {
        float b_collision = obj.mRect.bottom - mRect.top;
        float t_collision = mRect.bottom - obj.mRect.top;
        float l_collision = mRect.right - obj.mRect.left;
        float r_collision = obj.mRect.right - mRect.left;

        //top collision
        if (t_collision < b_collision && t_collision < l_collision && t_collision < r_collision )
            return HORIZONTAL_COLLISION;
        //bottom collision
        if (b_collision < t_collision && b_collision < l_collision && b_collision < r_collision)
            return HORIZONTAL_COLLISION;
        //left collision
        if (l_collision < r_collision && l_collision < t_collision && l_collision < b_collision)
            return VERTICAL_COLLISION;
        //right collision
        if (r_collision < l_collision && r_collision < t_collision && r_collision < b_collision )
            return VERTICAL_COLLISION;

        return NONE_COLLISION;
    }
}
