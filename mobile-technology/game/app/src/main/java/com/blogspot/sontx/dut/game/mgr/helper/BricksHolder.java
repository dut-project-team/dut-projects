package com.blogspot.sontx.dut.game.mgr.helper;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.blogspot.sontx.dut.game.lib.Calc;
import com.blogspot.sontx.dut.game.obj.Brick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noem on 17/1/2016.
 */
public final class BricksHolder {
    private static final float BRICK_SIZE_RATIO = 2.8f;// w:h
    private static final float BRICK_MARGIN = 85.0f;

    private float mBrickWidth;
    private float mBrickHeight;
    private boolean[][] mMatrix;
    private final List<Brick> mBricks = new ArrayList<>();

    public List<Brick> getBricks() {
        return mBricks;
    }

    private Point getAvailablePosition() {
        int i;
        int j;
        do {
            i = Calc.nextInt(0, mMatrix.length - 1);
            j = Calc.nextInt(0, mMatrix[0].length - 1);
        } while (mMatrix[i][j]);
        Log.d("DEBUG", String.format("%d %d", i, j));
        return new Point(i, j);
    }

    private PointF retrieveCoord(Point point) {
        PointF _point = new PointF(
                point.y * mBrickWidth + BRICK_MARGIN * (point.y + 1),
                point.x * mBrickHeight + BRICK_MARGIN * (point.x + 1));
        mMatrix[point.x][point.y] = true;
        return _point;
    }

    public void removeBrick(Brick brick) {
        Point loc = (Point) brick.getTag();
        mBricks.remove(brick);
        mMatrix[loc.x][loc.y] = false;
    }

    public Brick generateBrick(int level) {
        Point loc = getAvailablePosition();
        PointF point = retrieveCoord(loc);
        Brick brick = new Brick(point.x, point.y, level);
        brick.setWidth(mBrickWidth);
        brick.setHeight(mBrickHeight);
        brick.setTag(loc);
        mBricks.add(brick);
        return brick;
    }

    public BricksHolder(int cols, RectF rect) {
        mBrickWidth = (rect.width() - BRICK_MARGIN * (cols + 1)) / cols;
        mBrickHeight = mBrickWidth / BRICK_SIZE_RATIO;
        int m = (int) ((rect.height() - BRICK_MARGIN) / (mBrickHeight + BRICK_MARGIN));
        mMatrix = new boolean[m][cols];
    }
}
