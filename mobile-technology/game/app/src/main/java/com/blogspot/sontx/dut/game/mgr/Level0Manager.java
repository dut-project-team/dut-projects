package com.blogspot.sontx.dut.game.mgr;

/**
 * Created by Noem on 17/1/2016.
 */
public class Level0Manager extends PlayingManager {
    @Override
    public void init() {
        super.init();
        generateBricks(10);
        generateBall();
    }
}
