package com.blogspot.sontx.dut.game.mgr;

import android.graphics.Canvas;

import com.blogspot.sontx.dut.game.App;
import com.blogspot.sontx.dut.game.obj.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noem on 17/1/2016.
 */
public abstract class SceneManager {
    public final int DRAWABLE_WIDTH;
    public final int DRAWABLE_HEIGHT;
    public final int DRAWABLE_X;
    public final int DRAWABLE_Y;
    protected final List<GameObject> mObjects = new ArrayList<>();

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

    public abstract void init();

    public void destroy() {
        for (GameObject obj : mObjects) {
            obj.destroy();
        }
    }

    public SceneManager() {
        DRAWABLE_WIDTH = App.getInstance().getSurfaceWidth();
        DRAWABLE_HEIGHT = App.getInstance().getSurfaceHeight();
        DRAWABLE_X = 0;
        DRAWABLE_Y = 0;
    }
}
