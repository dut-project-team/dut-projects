package com.blogspot.sontx.whitelight.ui.helper;

import android.view.View;

import com.blogspot.sontx.whitelight.R;
import com.blogspot.sontx.whitelight.bean.UserConfig;

/**
 * Copyright by NE 2015.
 * Created by noem on 23/11/2015.
 */
public class DefConfigLayout extends ConfigLayout {
    private UserConfig userConfig;
    private DefConfigViewer viewer;

    public DefConfigLayout(UserConfig userConfig) {
        super(R.layout.layout_light_config_def);
        this.userConfig = userConfig;
    }

    public void loadDefConfig(int defConfigId) {
        viewer.setDefConfig(defConfigId);
    }

    @Override
    protected void onApplyLayout(View view) {
        viewer = new DefConfigViewer(view);
        viewer.setChangeable(false);
        viewer.setDefConfig(userConfig.getLightType() - 1);
    }
}
