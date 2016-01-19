package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Color;

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

        Hole hole = new Hole(DRAWABLE_X + DRAWABLE_WIDTH / 2.0f - HOLE_RADIUS, DRAWABLE_Y, Color.BLACK);
        hole.setWidth(HOLE_RADIUS * 2.0f);
        hole.setHeight(HOLE_HEIGHT);
        addHole(hole);
    }
}
