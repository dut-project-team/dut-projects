package com.blogspot.sontx.whitelight.net;

/**
 * Copyright by NE 2015.
 * Created by noem on 12/11/2015.
 */
public class RequestRemoveLightRequestPackage extends RequestPackage {
    private byte mLightId;

    protected RequestRemoveLightRequestPackage() {
        super(COMMAND_REMOVE_LIGHT);
    }

    public byte getLightId() {
        return mLightId;
    }

    public void setLightId(byte mLightId) {
        this.mLightId = mLightId;
    }

    @Override
    public byte[] getBytes() {
        byte[] buff = new byte[2];
        buff[0] = mCommand;
        buff[1] = mLightId;
        return buff;
    }
}
