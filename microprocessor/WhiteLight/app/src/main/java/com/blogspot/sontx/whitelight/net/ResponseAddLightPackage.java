package com.blogspot.sontx.whitelight.net;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class ResponseAddLightPackage extends ResponsePackage {
    private int mNewLightId;

    @Override
    public void init(byte[] data) {
        super.init(data);
        mNewLightId = data[1];
    }

    public int getNewLightId() {
        return mNewLightId;
    }
}
