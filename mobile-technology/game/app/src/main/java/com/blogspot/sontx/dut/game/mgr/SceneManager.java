package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.blogspot.sontx.dut.game.App;
import com.blogspot.sontx.dut.game.obj.Background;
import com.blogspot.sontx.dut.game.obj.GameObject;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Copyright by SONTX 2016. www.sontx.in
 * Created by Noem on 17/1/2016.
 */
public abstract class SceneManager {
    protected final RectF clientRectangle;
    private Background mBackground;
    private final List<GameObject> mObjects = new ArrayList<>();

    public GameObject getObjectByTag(Object tag) {
        for (GameObject obj : mObjects) {
            if (obj.getTag() == null) {
                if (tag == null)
                    return obj;
            } else {
                if (obj.getTag().equals(tag))
                    return obj;
            }
        }
        return null;
    }

    protected abstract int getBackgroundResource();

    protected Iterable<GameObject> getObjects() {
        return mObjects;
    }

    public void registerObject(GameObject object) {
        mObjects.add(object);
    }

    public void unregisterObject(GameObject object) {
        mObjects.remove(object);
    }

    public void draw(Canvas canvas) {
        for (GameObject obj : mObjects) {
            obj.draw(canvas);
        }
    }

    public void update() {
        for (GameObject obj : mObjects) {
            obj.update();
        }
    }

    protected void initializeObjects() {
        for (GameObject obj : mObjects) {
            obj.init();
        }
    }

    private void initializeBackground() {
        mBackground = new Background();
        mBackground.setBackgroundRectangle(clientRectangle);
        mBackground.setBitmap(getBackgroundResource());
        mObjects.add(0, mBackground);
    }

    public void init() {
        initializeBackground();
    }

    public void destroy() {
        for (GameObject obj : mObjects) {
            obj.destroy();
        }
    }

    public SceneManager() {
        clientRectangle = new RectF(
                0.0f,
                0.0f,
                App.getInstance().getSurfaceWidth(),
                App.getInstance().getSurfaceHeight());
    }
}
