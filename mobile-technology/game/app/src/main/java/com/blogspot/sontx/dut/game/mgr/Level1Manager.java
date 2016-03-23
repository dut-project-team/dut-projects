package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Color;

import com.blogspot.sontx.dut.game.obj.Brick;
import com.blogspot.sontx.dut.game.obj.Hole;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/03/2016.
 */
public class Level1Manager extends LevelManager {
    @Override
    public int getCurrentLevel() {
        return 1;
    }

    @Override
    protected float getBallSpeed() {
        return 7.5f;
    }

    @Override
    protected int getCountdownMinutes() {
        return 0;
    }

    @Override
    protected int getCountdownSeconds() {
        return 25;
    }

    private void initializeHole() {
        Hole hole = new Hole(mPlayableBound.left + mPlayableBound.width() / 2.0f - HOLE_RADIUS, mPlayableBound.top, Color.BLACK);
        hole.setMovableBound(mPlayableBound);
        hole.setWidth(HOLE_RADIUS * 2.0f);
        hole.setHeight(HOLE_HEIGHT);
        hole.setSpeedX(6.5f);
        setHole(hole);
    }

    @Override
    public void init() {
        super.init();
        generateBricks(20, Brick.LEVEL1);
        initializeHole();
        initializeObjects();
    }
}
