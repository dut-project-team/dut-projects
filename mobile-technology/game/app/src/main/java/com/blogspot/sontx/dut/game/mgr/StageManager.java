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
    private static StageManager instance = null;
    private SceneManager mSceneManager = null;
    private List<Class> mLevelManagerTypes = new ArrayList<>();
    private boolean mPaused = false;

    public static StageManager getInstance() {
        if (instance == null)
            instance = new StageManager();
        return instance;
    }

    private StageManager() {}

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

    private void displayGameover(final LevelManager currentLevelManager) {
        AlertDialog.Builder builder = getPopupMenu("Gameoverrrrrrrr, you are stupid men :|");
        builder.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLevel(currentLevelManager.getClass());
                closePopupMenu(dialog);
            }
        });
    }

    private AlertDialog.Builder getPopupMenu(String message) {
        AlertDialog.Builder builder = SystemAlert.getBuilder();
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        builder.setNegativeButton("Main menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMainMenu();
                closePopupMenu(dialog);
            }
        });
        return builder;
    }

    private void showPopupMenu(AlertDialog.Builder builder) {
        mPaused = true;
        builder.show();
    }

    private void closePopupMenu(DialogInterface dialog) {
        mPaused = false;
        dialog.dismiss();
    }

    private void startLevel(Class<? extends LevelManager> levelManagerClass) {
        try {
            LevelManager instance = levelManagerClass.newInstance();
            setSceneManager(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayGameWin(final LevelManager currentLevelManager) {
        final Class levelManager = getNextLevelManager(currentLevelManager.getCurrentLevel());
        AlertDialog.Builder builder = getPopupMenu(levelManager != null ? "You win!" : "You are best, WIN WINNNNN!!!");
        if (levelManager != null) {
            builder.setNeutralButton("Next Level", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startLevel(levelManager);
                    closePopupMenu(dialog);
                }
            });
        }
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startLevel(currentLevelManager.getClass());
                closePopupMenu(dialog);
            }
        });
        showPopupMenu(builder);
    }

    public void init() {
        setSceneManager(new MainMenuManager());
        mLevelManagerTypes.add(Level0Manager.class);
        mLevelManagerTypes.add(Level1Manager.class);
        mLevelManagerTypes.add(Level2Manager.class);
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

    public void startGame() {
        setSceneManager(new Level0Manager());
    }

    public void showAbout() {
        setSceneManager(new AboutManager());
    }

    public void showMainMenu() {
        setSceneManager(new MainMenuManager());
    }
}
