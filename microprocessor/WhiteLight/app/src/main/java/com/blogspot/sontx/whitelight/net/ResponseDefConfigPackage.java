package com.blogspot.sontx.whitelight.net;

import com.blogspot.sontx.whitelight.bean.DefConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class ResponseDefConfigPackage extends ResponsePackage {
    private List<DefConfig> mDefConfigs;

    @Override
    public void init(byte[] data) {
        super.init(data);
        int count = data[1];
        int frameSize = DefConfig.getFrameSize();
        mDefConfigs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            mDefConfigs.add(DefConfig.parse(data, 2 + frameSize * i, frameSize));
        }
    }

    public List<DefConfig> getDefConfigs() {
        return mDefConfigs;
    }
}
