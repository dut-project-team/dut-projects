package com.blogspot.sontx.dut.game.mgr.helper;

import android.graphics.Color;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.lib.Timer;
import com.blogspot.sontx.dut.game.mgr.SceneManager;
import com.blogspot.sontx.dut.game.obj.Text;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 19/1/2016.
 */
public final class GameInfoPanel {
    private final Text mLevelObjectText;
    private final Text mScoreObjectText;
    private final Text mTimerObjectText;
    private final Timer mTimer;
    private int mLastRemainSeconds = 0;
    private int mCurrentScore = 0;

    public void setLevel(int level) {
        mLevelObjectText.setText(String.format("Level: %d", level));
    }

    public void addScore(int score) {
        mScoreObjectText.setText(String.format("Score: %d", mCurrentScore += score));
    }

    public void update() {
        mTimer.updateTime();
        if (mLastRemainSeconds != mTimer.getRemainMillis() / 1000) {
            mLastRemainSeconds = mTimer.getRemainMillis() / 1000;
            mTimerObjectText.setText(mTimer.toString());
        }
    }

    public boolean isTimeout() {
        return mTimer.getRemainMillis() <= 0;
    }

    public GameInfoPanel(RectF bound, int countdownMinutes, int countdownSeconds, SceneManager sceneManager) {
        mTimer = new Timer(countdownMinutes, countdownSeconds);
        mLastRemainSeconds = mTimer.getRemainMillis() / 1000;
        mLevelObjectText = new Text(bound.left, bound.top, Color.RED, "Level: 0");
        mScoreObjectText = new Text(bound.left, bound.top + 50.0f, Color.BLUE, "Score: 0");
        mTimerObjectText = new Text(bound.left, bound.top + 100.0f, Color.GREEN, mTimer.toString());
        sceneManager.registerObject(mLevelObjectText);
        sceneManager.registerObject(mScoreObjectText);
        sceneManager.registerObject(mTimerObjectText);
    }
}
