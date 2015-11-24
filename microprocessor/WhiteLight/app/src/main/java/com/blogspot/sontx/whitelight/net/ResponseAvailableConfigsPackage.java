package com.blogspot.sontx.whitelight.net;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class ResponseAvailableConfigsPackage extends ResponsePackage {
    private byte[] mPins;
    private byte[] mLightSensors;
    private byte[] mPeopleSensors;

    @Override
    public void init(byte[] data) {
        super.init(data);
        int offset = 1;
        mPins = new byte[data[offset]];
        offset++;
        System.arraycopy(data, offset, mPins, 0, mPins.length);
        offset += mPins.length;

        mLightSensors = new byte[offset];
        offset++;
        System.arraycopy(data, offset, mLightSensors, 0, mLightSensors.length);
        offset += mLightSensors.length;

        mPeopleSensors = new byte[offset];
        offset++;
        System.arraycopy(data, offset, mPeopleSensors, 0, mPeopleSensors.length);
    }

    public byte[] getPins() {
        return mPins;
    }

    public byte[] getLightSensors() {
        return mLightSensors;
    }

    public byte[] getPeopleSensors() {
        return mPeopleSensors;
    }
}
