package com.blogspot.sontx.whitelight.net;

import com.blogspot.sontx.whitelight.bean.UserConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class ResponseUserConfigPackage extends ResponsePackage {
    private List<UserConfig> mUserConfigs;

    @Override
    public void init(byte[] data) {
        super.init(data);
        int count = data[1];
        int frameSize = UserConfig.getFrameSize();
        mUserConfigs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            mUserConfigs.add(UserConfig.parse(data, 2 + frameSize * i, frameSize));
        }
    }

    public List<UserConfig> getUserConfigs() {
        return mUserConfigs;
    }
}
