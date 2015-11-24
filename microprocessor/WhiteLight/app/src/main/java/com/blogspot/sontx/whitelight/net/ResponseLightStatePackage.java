package com.blogspot.sontx.whitelight.net;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class ResponseLightStatePackage extends ResponsePackage {
    private byte[] mStates;

    @Override
    public void init(byte[] data) {
        super.init(data);
        mStates = new byte[data[1]];
        System.arraycopy(data, 2, mStates, 0, mStates.length);
    }

    public byte[] getStates() {
        return mStates;
    }
}
