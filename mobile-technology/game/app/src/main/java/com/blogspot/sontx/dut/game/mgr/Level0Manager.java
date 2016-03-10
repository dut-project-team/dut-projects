package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Color;

import com.blogspot.sontx.dut.game.obj.Brick;
import com.blogspot.sontx.dut.game.obj.Hole;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 17/1/2016.
 */
public class Level0Manager extends LevelManager {
    @Override
    public int getCurrentLevel() {
        return 0;
    }

    @Override
    protected float getBallSpeed() {
        return 7.0f;
    }

    @Override
    public void init() {
        super.init();

        generateBricks(10, Brick.LEVEL0);

        Hole hole = new Hole(mPlayableBound.left + mPlayableBound.width() / 2.0f - HOLE_RADIUS, mPlayableBound.top, Color.BLACK);
        hole.setMovableBound(mPlayableBound);
        hole.setWidth(HOLE_RADIUS * 2.0f);
        hole.setHeight(HOLE_HEIGHT);
        hole.setSpeedX(5.5f);
        setHole(hole);
    }
}
