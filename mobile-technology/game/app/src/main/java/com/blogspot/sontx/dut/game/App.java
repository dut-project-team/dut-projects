package com.blogspot.sontx.dut.game;

import android.app.Activity;
import android.app.Application;
import android.graphics.Canvas;

import com.blogspot.sontx.dut.game.lib.SoundManager;
import com.blogspot.sontx.dut.game.mgr.StageManager;
import com.blogspot.sontx.dut.game.ui.MainActivity;
import com.blogspot.sontx.dut.game.ui.WindowSurface;

/**
 * Copyright by sontx, www.sontx.in
 * Created by Noem on 16/1/2016.
 */
public final class App extends Application implements WindowSurface.WindowEventListener {
    private static App instance;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private StageManager mStageManager;

    public static App getInstance() {
        return instance;
    }

    public void exit() {
        Activity mainActivity = MainActivity.getInstance();
        if (mainActivity != null)
            mainActivity.finish();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WindowSurface.createInstance(getApplicationContext());
        WindowSurface.getInstance().setWindowEventListener(this);
        instance = this;
    }

    @Override
    public void onDraw(Canvas canvas) {
        mStageManager.draw(canvas);
    }

    @Override
    public void onUpdate() {
        mStageManager.update();
    }

    @Override
    public void onInit() {
        mSurfaceWidth = WindowSurface.getInstance().getWidth();
        mSurfaceHeight = WindowSurface.getInstance().getHeight();
        SoundManager.createInstance(getApplicationContext());
        mStageManager = StageManager.getInstance();
        mStageManager.init();
    }

    @Override
    public void onDestroy() {
        mStageManager.destroy();
        SoundManager.destroyInstance();
    }

    public int getSurfaceWidth() {
        return mSurfaceWidth;
    }

    public int getSurfaceHeight() {
        return mSurfaceHeight;
    }
}
