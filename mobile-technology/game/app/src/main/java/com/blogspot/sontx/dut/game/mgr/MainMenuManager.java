package com.blogspot.sontx.dut.game.mgr;

import com.blogspot.sontx.dut.game.App;
import com.blogspot.sontx.dut.game.R;
import com.blogspot.sontx.dut.game.mgr.helper.MenuManager;
import com.blogspot.sontx.dut.game.obj.Button;

/**
 * Copyright by sontx, www.sontx.in
 * Created by noem on 23/03/2016.
 */
public class MainMenuManager extends MenuManager {
    @Override
    public void init() {
        Button startButton = new Button(0, 0);
        startButton.setOnClickListener(this);
        startButton.setTag("start");
        startButton.setTop(MARGIN_TOP);
        putButtonAtMiddle(startButton);
        startButton.setBitmap(R.drawable.start_button);
        registerObject(startButton);

        Button aboutButton = new Button(0, 0);
        aboutButton.setOnClickListener(this);
        aboutButton.setTag("about");
        aboutButton.setTop(startButton.getBottom() + MARGIN_EACH_BUTTON);
        putButtonAtMiddle(aboutButton);
        aboutButton.setBitmap(R.drawable.about_button);
        registerObject(aboutButton);

        Button quitButton = new Button(0, 0);
        quitButton.setOnClickListener(this);
        quitButton.setTag("quit");
        quitButton.setTop(aboutButton.getBottom() + MARGIN_EACH_BUTTON);
        putButtonAtMiddle(quitButton);
        quitButton.setBitmap(R.drawable.quit_button);
        registerObject(quitButton);
    }

    @Override
    public void onClick(Button button) {
        if ("start".equals(button.getTag())) {
            StageManager.getInstance().startGame();
        } else if ("about".equals(button.getTag())) {
            StageManager.getInstance().showAbout();
        } else if ("quit".equals(button.getTag())) {
            App.getInstance().exit();
        }
    }
}
