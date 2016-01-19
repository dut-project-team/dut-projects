package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Color;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.obj.Hole;

/**
 * Created by Noem on 17/1/2016.
 */
public class Level0Manager extends PlayingManager {
    @Override
    public int getCurrentLevel() {
        return 0;
    }

    @Override
    public void init() {
        super.init();
        generateBricks(2);
        generateBall();

        Hole hole = new Hole(mPlayableBound.left + mPlayableBound.width() / 2.0f - HOLE_RADIUS, mPlayableBound.top, Color.BLACK);
        hole.setMovableBound(mPlayableBound);
        hole.setWidth(HOLE_RADIUS * 2.0f);
        hole.setHeight(HOLE_HEIGHT);
        hole.setSpeedX(5.5f);
        addHole(hole);
    }
}
