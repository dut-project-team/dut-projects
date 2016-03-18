package com.blogspot.sontx.dut.game.mgr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;

import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.lib.SystemAlert;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 17/1/2016.
 */
public final class StageManager {
    private SceneManager mSceneManager = null;
    private List<Class> mLevelManagerTypes = new ArrayList<>();
    private boolean mPaused = false;

    private void setSceneManager(SceneManager sceneManager) {
        if (mSceneManager != null)
            mSceneManager.destroy();
        mSceneManager = sceneManager;
        mSceneManager.init();
    }

    private Class getNextLevelManager(int currentLevel) {
        int nextLevel = currentLevel + 1;
        return nextLevel < mLevelManagerTypes.size() ? mLevelManagerTypes.get(nextLevel) : null;
    }

    private void displayGameover(final LevelManager playingManager) {
        AlertDialog.Builder builder = SystemAlert.getBuilder();
        builder.setTitle("Gameover");
        builder.show();
    }

    private void displayGameWin(final LevelManager playingManager) {
        mPaused = true;
        final Class levelManager = getNextLevelManager(playingManager.getCurrentLevel());
        AlertDialog.Builder builder = SystemAlert.getBuilder();
        builder.setTitle(R.string.app_name);
        builder.setMessage(levelManager != null ? "You win!" : "You are best, WIN WINNNNN!!!");
        if (levelManager != null) {
            builder.setNegativeButton("Next Level", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LevelManager instance = null;
                    try {
                        instance = (LevelManager) levelManager.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setSceneManager(instance);
                    dialog.dismiss();
                    mPaused = false;
                }
            });
        }
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    LevelManager instance = playingManager.getClass().newInstance();
                    setSceneManager(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                mPaused = false;
            }
        });
        builder.show();
    }

    public void init() {
        setSceneManager(new Level0Manager());
        mLevelManagerTypes.add(Level0Manager.class);
    }

    public void draw(Canvas canvas) {
        mSceneManager.draw(canvas);
    }

    public void update() {
        if (mPaused)
            return;
        if (mSceneManager instanceof LevelManager) {
            LevelManager playingManager = (LevelManager) mSceneManager;
            int state = playingManager.getGameState();
            if ((state & LevelManager.GAME_WIN) == LevelManager.GAME_WIN)
                displayGameWin(playingManager);
            else if ((state & LevelManager.GAME_OVER) == LevelManager.GAME_OVER)
                displayGameover(playingManager);
        }
        mSceneManager.update();
    }

    public void destroy() {
        mSceneManager.destroy();
    }
}
