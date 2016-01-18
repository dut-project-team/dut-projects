package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Canvas;

/**
 * Created by Noem on 17/1/2016.
 */
public final class StageManager {
    private SceneManager mSceneManager = null;

    private void setSceneManager(SceneManager sceneManager) {
        if (mSceneManager != null)
            mSceneManager.destroy();
        mSceneManager = sceneManager;
        mSceneManager.init();
    }

    public void init() {
        setSceneManager(new Level0Manager());
    }

    public void draw(Canvas canvas) {
        mSceneManager.draw(canvas);
    }

    public void update() {
        mSceneManager.update();
    }

    public void destroy() {
        mSceneManager.destroy();
    }
}
