package com.blogspot.sontx.dut.game.mgr.helper;

import com.blogspot.sontx.dut.game.mgr.SceneManager;
import com.blogspot.sontx.dut.game.obj.Button;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/03/2016.
 */
public abstract class MenuManager extends SceneManager implements Button.OnClickListener {
    protected static final float MARGIN_LEFT          = 240.0f;
    protected static final float MARGIN_RIGHT         = MARGIN_LEFT;
    protected static final float MARGIN_TOP           = 100.0f;
    protected static final float MARGIN_BOTTOM = 50.0f;
    protected static final float MARGIN_EACH_BUTTON   = 40.0f;

    protected void putButtonAtMiddle(Button button) {
        button.setLeft(clientRectangle.left + MARGIN_RIGHT);
        button.setRight(clientRectangle.right - MARGIN_RIGHT);
    }
}
