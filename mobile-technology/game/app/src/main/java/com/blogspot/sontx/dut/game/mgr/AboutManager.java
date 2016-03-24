package com.blogspot.sontx.dut.game.mgr;

import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.obj.Button;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/03/2016.
 */
public class AboutManager extends MenuManager {

    @Override
    protected int getBackgroundResource() {
        return R.drawable.about_scene;
    }

    @Override
    public void init() {
        Button backButton = new Button(0, 0);
        putButtonAtMiddle(backButton);
        backButton.setBitmap(R.drawable.about_scene_back);
        float backButtonHeight = backButton.getHeight();
        backButton.setBottom(clientRectangle.bottom - MARGIN_BOTTOM);
        backButton.setTop(backButton.getBottom() - backButtonHeight);
        backButton.setOnClickListener(this);
        registerObject(backButton);

        super.init();
    }

    @Override
    public void onClick(Button button) {
        StageManager.getInstance().showMainMenu();
    }
}
