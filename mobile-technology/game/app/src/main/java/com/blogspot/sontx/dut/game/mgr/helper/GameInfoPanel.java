package com.blogspot.sontx.dut.game.mgr.helper;

import android.graphics.Color;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.mgr.SceneManager;
import com.blogspot.sontx.dut.game.obj.GameObject;
import com.blogspot.sontx.dut.game.obj.Text;

import java.util.List;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 19/1/2016.
 */
public final class GameInfoPanel {
    private final Text mLevel;
    private final Text mScore;
    private final Text mRemainTime;
    private int mCurrentScore = 0;

    public void setLevel(int level) {
        mLevel.setText(String.format("Level: %d", level));
    }

    public void addScore(int score) {
        mScore.setText(String.format("Score: %d", mCurrentScore += score));
    }

    public GameInfoPanel(RectF bound, SceneManager sceneManager) {
        mLevel = new Text(bound.left, bound.top, Color.RED, "Level: 0");
        mScore = new Text(bound.left, bound.top + 50.0f, Color.BLUE, "Score: 0");
        mRemainTime = new Text(bound.left, bound.top + 100.0f, Color.GREEN, "00:00");
        sceneManager.registerObject(mLevel);
        sceneManager.registerObject(mScore);
        sceneManager.registerObject(mRemainTime);
    }
}
