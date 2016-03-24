package com.blogspot.sontx.dut.game.mgr.helper;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.lib.Calc;
import com.blogspot.sontx.dut.game.mgr.SceneManager;
import com.blogspot.sontx.dut.game.obj.Brick;
import com.blogspot.sontx.dut.game.obj.GameObject;

import java.util.List;

/**
 * Copyright by NE 2016.
 * Created by Noem on 17/1/2016.
 */
public final class BrickGenerator {
    private static final float RATIO_WIDTH_HEIGHT = 1.0f;// w:h
    private static final float BRICK_MARGIN = 5.0f;

    private final RectF mBound;
    private final float mBrickWidth;
    private final float mBrickHeight;
    private final boolean[][] mMatrix;
    protected final SceneManager mSceneManager;
    private int countOfBrick = 0;

    private Index2D getAvailablePosition() {
        int i;
        int j;
        do {
            i = Calc.nextInt(0, mMatrix.length - 1);
            j = Calc.nextInt(0, mMatrix[0].length - 1);
        } while (mMatrix[i][j]);
        return new Index2D(i, j);
    }

    private PointF retrieveCoord(Index2D index2d) {
        PointF point = new PointF(
                index2d.j * mBrickWidth + BRICK_MARGIN * (index2d.j + 1) + mBound.left,
                index2d.i * mBrickHeight + BRICK_MARGIN * (index2d.i + 1) + mBound.top);
        mMatrix[index2d.i][index2d.j] = true;
        return point;
    }

    private boolean canGenerateNewBrick() {
        return countOfBrick < mMatrix.length * mMatrix[0].length;
    }

    public void releaseBrick(Brick brick) {
        Index2D index2d = (Index2D) brick.getTag();
        mMatrix[index2d.i][index2d.j] = false;
        countOfBrick--;
        mSceneManager.unregisterObject(brick);
    }

    public Brick generateBrick(int level) {
        if (!canGenerateNewBrick())
            return null;
        Index2D index2d = getAvailablePosition();
        PointF point = retrieveCoord(index2d);
        Brick brick = new Brick(point.x, point.y, level);
        brick.setWidth(mBrickWidth);
        brick.setHeight(mBrickHeight);
        brick.setTag(index2d);
        countOfBrick++;
        mSceneManager.registerObject(brick);
        return brick;
    }

    public BrickGenerator(int cols, RectF bound, SceneManager sceneManager) {
        mBound = bound;
        mBrickWidth = (bound.width() - BRICK_MARGIN * (cols + 1)) / cols;
        mBrickHeight = mBrickWidth / RATIO_WIDTH_HEIGHT;
        int m = (int) ((bound.height() - BRICK_MARGIN) / (mBrickHeight + BRICK_MARGIN));
        mMatrix = new boolean[m][cols];
        mSceneManager = sceneManager;
    }

    private static class Index2D {
        public int i;
        public int j;
        public Index2D(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
