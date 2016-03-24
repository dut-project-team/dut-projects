package com.blogspot.sontx.dut.game.mgr;

import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.mgr.helper.MenuManager;
import com.blogspot.sontx.dut.game.obj.Background;
import com.blogspot.sontx.dut.game.obj.Button;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/03/2016.
 */
public class AboutManager extends MenuManager {

    @Override
    public void init() {
        Background background = new Background();
        background.setBackgroundRectangle(clientRectangle);
        background.setBitmap(R.drawable.about_scene);
        registerObject(background);

        Button backButton = new Button(0, 0);
        putButtonAtMiddle(backButton);
        backButton.setBitmap(R.drawable.about_scene_back);
        float backButtonHeight = backButton.getHeight();
        backButton.setBottom(clientRectangle.bottom - MARGIN_BOTTOM);
        backButton.setTop(backButton.getBottom() - backButtonHeight);
        backButton.setOnClickListener(this);
        registerObject(backButton);
    }

    @Override
    public void onClick(Button button) {
        StageManager.getInstance().showMainMenu();
    }
}
