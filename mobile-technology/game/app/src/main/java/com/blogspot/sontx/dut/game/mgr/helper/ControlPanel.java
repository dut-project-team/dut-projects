package com.blogspot.sontx.dut.game.mgr.helper;

import android.graphics.Color;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.obj.GameObject;
import com.blogspot.sontx.dut.game.obj.Text;

import java.util.List;

/**
 * Created by Noem on 19/1/2016.
 */
public final class ControlPanel {
    private Text mLevel;
    private Text mScore;
    private Text mRemainTime;
    private int mCurrentScore = 0;

    public void setLevel(int level) {
        mLevel.setText(String.format("Level: %d", level));
    }

    public void addScore(int score) {
        mScore.setText(String.format("Score: %d", mCurrentScore += score));
    }

    public void register(List<GameObject> container) {
        container.add(mLevel);
        container.add(mScore);
        container.add(mRemainTime);
    }

    public ControlPanel(RectF bound) {
        mLevel = new Text(bound.left, bound.top, Color.RED, "Level: 0");
        mScore = new Text(bound.left, bound.top + 50.0f, Color.BLUE, "Score: 0");
        mRemainTime = new Text(bound.left, bound.top + 100.0f, Color.GREEN, "00:00");
    }
}
